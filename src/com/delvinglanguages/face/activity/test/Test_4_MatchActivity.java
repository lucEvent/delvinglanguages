package com.delvinglanguages.face.activity.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.face.dialog.InputDialog;
import com.delvinglanguages.kernel.game.MatchGame;
import com.delvinglanguages.kernel.game.MatchGame.QuestionModel;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.test.TestKernelControl;
import com.delvinglanguages.kernel.test.TestReferenceState;
import com.delvinglanguages.settings.Settings;

public class Test_4_MatchActivity extends Activity implements OnClickListener {

	private final int NUM_RESP = 6;

	private TestKernelControl kernel;
	private MatchGame gamecontroller;
	private Test test;

	private QuestionModel pActual;

	private TextView palabra, pregunta;
	private Button resps[];
	private Handler handler;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.a_match, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		kernel = new TestKernelControl(this);
		test = TestKernelControl.runningTest;
		test.state = Test.PHASE_MATCH;
		gamecontroller = new MatchGame(test.getReferences());
		handler = new Handler();

		// Iniciamos elementos graficos
		palabra = (TextView) findViewById(R.id.palabra);
		pregunta = (TextView) findViewById(R.id.quesignifica);

		resps = new Button[NUM_RESP];
		resps[0] = (Button) findViewById(R.id.resp1);
		resps[1] = (Button) findViewById(R.id.resp2);
		resps[2] = (Button) findViewById(R.id.resp3);
		resps[3] = (Button) findViewById(R.id.resp4);
		resps[4] = (Button) findViewById(R.id.resp5);
		resps[5] = (Button) findViewById(R.id.resp6);

		for (int i = 0; i < NUM_RESP; i++) {
			resps[i].setOnClickListener(this);
		}

		siguientePalabra();
	}

	@Override
	public void onClick(View v) {
		Boolean acierto = (Boolean) v.getTag();
		if (acierto == true) {
			int count = 0;
			for (int i = 0; i < test.references.size(); i++) {
				TestReferenceState refstate = test.references.get(i);
				if (pActual.reference == refstate.reference) {
					refstate.passed = true;
				}
				if (refstate.passed) {
					count++;
				}
			}
			for (int i = 0; i < NUM_RESP; i++) {
				if (pActual.correct[i]) {
					resps[i].getBackground().setColorFilter(0xFF33CC00, PorterDuff.Mode.MULTIPLY);
				}
				resps[i].setClickable(false);
			}

			if (count == test.references.size()) {
				siguientePantalla(v);
			} else {
				acierto();
			}
		} else {
			test.references.get(positionActual).fallos_match++;
			v.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
			for (int i = 0; i < NUM_RESP; i++) {
				if (pActual.correct[i]) {
					resps[i].getBackground().setColorFilter(0xFF33CC00, PorterDuff.Mode.MULTIPLY);
				}
				resps[i].setClickable(false);
			}
			acierto();
		}
	}

	private void acierto() {
		new Thread(new Runnable() {

			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				handler.post(new Runnable() {
					@Override
					public void run() {
						siguientePalabra();
					}
				});
			}
		}).start();
	}

	private int positionActual;

	private void siguientePalabra() {
		pActual = gamecontroller.nextQuestion(test.references, NUM_RESP);
		positionActual = test.indexOf(pActual.reference);
		palabra.setText(pActual.reference.getName());
		pregunta.setText(pActual.reference.getPronunciation());

		for (int i = 0; i < NUM_RESP; i++) {
			resps[i].getBackground().setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
			resps[i].setText(pActual.answers[i]);
			resps[i].setClickable(true);
			resps[i].setTag(pActual.correct[i]);
		}
	}

	private void siguientePantalla(View v) {

		new Thread(new Runnable() {

			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.post(new Runnable() {
					@Override
					public void run() {
						test.nextStat();
						startActivity(new Intent(getApplicationContext(), Test_5_CompleteActivity.class));
						finish();
					}
				});
			}
		}).start();
	}

	/** *************** METODOS DE MENÚ *************** **/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.test, menu);
		if (!test.isSaved()) {
			menu.findItem(R.id.menu_test_remove).setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_test_save:
			if (test.isSaved()) {

				kernel.saveTest(test);
				showMessage(R.string.testsaved);

			} else {

				new InputDialog(this).create().show();

			}
			return true;
		case R.id.menu_test_remove:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.title_removingtest);
			builder.setMessage(R.string.removetestquestion);
			builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					kernel.removeTest(test);
					showMessage(R.string.testremoved);
					finish();
				}
			});
			builder.setNegativeButton(R.string.cancel, null);
			builder.create().show();

			return true;
		}
		return false;
	}

	private void showMessage(int text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

}
