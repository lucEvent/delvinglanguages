package com.delvinglanguages.face.activities.practics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.core.Cerebro;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.Test;
import com.delvinglanguages.core.Cerebro.QuestionModel;
import com.delvinglanguages.face.adapters.InputDialog;
import com.delvinglanguages.settings.Configuraciones;

public class TestActivity3 extends Activity implements OnClickListener {
	
	private static final String DEBUG = "##TestAct3##";

	// Match Activity
	private final int NUM_RESP = 6;

	private LinearLayout background;

	// Elementos del core
	private Cerebro calculador;
	private Test test;

	// Elementos de la activity
	private QuestionModel pActual;

	// Elementos graficos
	private TextView palabra, pregunta;
	private Button resps[];

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Iniciamos elementos del core
		test = ControlCore.testActual;
		test.state = Test.STAT_MATCH;
		calculador = new Cerebro(test.references);
		// Iniciamos elementos de la activity
		setContentView(R.layout.a_match);

		// Configuramos background
		background = (LinearLayout) findViewById(R.id.apreg_bg);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

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

	/** *************** METODOS ONCLICK **************************** **/
	@Override
	public void onClick(View v) {
		Boolean acierto = (Boolean) v.getTag();
		if (acierto == true) {
			test.statistics.get(positionActual).aciertos_match++;
			int count = 0;
			for (int i = 0; i < test.passed.length; i++) {
				if (pActual.reference == test.references.get(i)) {
					test.passed[i] = true;
				}
				if (test.passed[i]) {
					count++;
				}
			}
			for (int i = 0; i < NUM_RESP; i++) {
				if (pActual.correct[i]) {
					resps[i].getBackground().setColorFilter(0xFF33CC00,
							PorterDuff.Mode.MULTIPLY);
				}
				resps[i].setClickable(false);
			}

			if (count == test.passed.length) {
				siguientePantalla(v);
			} else {
				acierto();
			}
		} else {
			test.statistics.get(positionActual).fallos_match++;
			v.getBackground().setColorFilter(0xFFFF0000,
					PorterDuff.Mode.MULTIPLY);
			for (int i = 0; i < NUM_RESP; i++) {
				if (pActual.correct[i]) {
					resps[i].getBackground().setColorFilter(0xFF33CC00,
							PorterDuff.Mode.MULTIPLY);
				}
				resps[i].setClickable(false);
			}
			acierto();
		}
	}

	/** ****************** METODOS PRIVADOS ************************* **/
	private Handler mHandler = new Handler();

	private void acierto() {
		new Thread(new Runnable() {

			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Log.d(DEBUG, "ERROR EN EL SLEEP");
				}
				mHandler.post(new Runnable() {
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
		pActual = calculador.nextWord(test.passed, NUM_RESP);
		positionActual = test.references.indexOf(pActual.reference);
		palabra.setText(pActual.reference.item);
		pregunta.setText(pActual.reference.getPronunciation());

		for (int i = 0; i < NUM_RESP; i++) {
			resps[i].getBackground().setColorFilter(0xFFFFFFFF,
					PorterDuff.Mode.MULTIPLY);
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
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						test.nextStat();
						startActivity(new Intent(getApplicationContext(),
								TestActivity4.class));
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

				ControlCore.saveTestActual();
				showMessage(R.string.testsaved);

			} else {

				new InputDialog(this).create().show();

			}
			return true;
		case R.id.menu_test_remove:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.title_removingtest);
			builder.setMessage(R.string.removetestquestion);
			builder.setPositiveButton(R.string.confirm,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							ControlCore.removeTestActual();
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
