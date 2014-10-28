package com.delvinglanguages.face.activities.practice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.core.Cerebro;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.DReference;
import com.delvinglanguages.core.Test;
import com.delvinglanguages.face.dialog.InputDialog;
import com.delvinglanguages.settings.Configuraciones;

public class TestActivity5 extends Activity implements TextWatcher,
		OnClickListener {
	// Write Activity

	private DReference refActual;
	private Cerebro cerebro;
	private Test test;

	private TextView palabra;
	private EditText input;
	private ProgressBar progress;
	private TextView labels[];
	private ImageButton next;

	private String answer;
	private int succesCounter, posicionPalabra;
	private Handler mHandler = new Handler();

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_write);

		RelativeLayout background = (RelativeLayout) findViewById(R.id.background);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		ImageButton help = (ImageButton) findViewById(R.id.aww_help);
		ImageButton swap = (ImageButton) findViewById(R.id.aww_swap);
		next = (ImageButton) findViewById(R.id.aww_new);
		help.setClickable(false);
		swap.setClickable(false);
		help.setEnabled(false);
		swap.setEnabled(false);
		next.setOnClickListener(this);

		palabra = (TextView) findViewById(R.id.aww_word);
		input = (EditText) findViewById(R.id.aww_input);
		progress = (ProgressBar) findViewById(R.id.aww_progress);

		progress.getProgressDrawable().setColorFilter(0xFF33CC00,
				PorterDuff.Mode.MULTIPLY);

		input.addTextChangedListener(this);

		test = ControlCore.testActual;
		test.state = Test.STAT_WRITE;
		cerebro = new Cerebro(test.references);

		labels = new TextView[Configuraciones.NUM_TYPES];
		labels[0] = (TextView) findViewById(R.id.aww_noun);
		labels[1] = (TextView) findViewById(R.id.aww_verb);
		labels[2] = (TextView) findViewById(R.id.aww_adj);
		labels[3] = (TextView) findViewById(R.id.aww_adv);
		labels[4] = (TextView) findViewById(R.id.aww_phrasal);
		labels[5] = (TextView) findViewById(R.id.aww_expression);
		labels[6] = (TextView) findViewById(R.id.aww_other);

		succesCounter = 0;
		for (int i = 0; i < test.passed.length; i++) {
			if (test.passed[i]) {
				succesCounter++;
			}
		}
		siguientePalabra();
	}

	private void siguientePalabra() {
		posicionPalabra = cerebro.nextPosition(test.passed);
		refActual = test.references.get(posicionPalabra);

		int type = refActual.type;
		for (int i = 0; i < Configuraciones.NUM_TYPES; ++i) {
			if ((type & (1 << i)) != 0) {
				labels[i].setBackgroundColor(Configuraciones.type_colors[i]);
			} else {
				labels[i].setBackgroundColor(0xFFCCCCCC);
			}
		}
		progress.setMax(refActual.item.length());
		progress.setProgress(0);
		answer = "";
		input.setText(answer);
		palabra.setText(refActual.getTranslation().toUpperCase());
	}

	/** **************** TEXTWATCHER ******************* **/
	
	@Override
	public void afterTextChanged(Editable s) {

		answer = input.getText().toString();
		if (refActual.item.toLowerCase()
				.startsWith(answer.toLowerCase())) {
			test.statistics.get(posicionPalabra).aciertos_write++;

			progress.setProgress(answer.length());
			progress.getProgressDrawable().setColorFilter(0xFF33CC00,
					PorterDuff.Mode.MULTIPLY);

			if (refActual.item.toLowerCase()
					.equals(answer.toLowerCase())) {
				succesCounter++;
				if (succesCounter == test.passed.length) {
					test.nextStat();
					startActivity(new Intent(this, TestActivity6.class));
					finish();
					return;
				}
				test.passed[posicionPalabra] = true;

				new Thread(new Runnable() {
					public void run() {

						try {
							Thread.sleep(1200);
						} catch (InterruptedException e) {
							e.printStackTrace();
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
		} else {
			test.statistics.get(posicionPalabra).fallos_write++;
			progress.setProgress(refActual.item.length());
			progress.getProgressDrawable().setColorFilter(0xFFFF0000,
					PorterDuff.Mode.SRC_IN);
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void onClick(View v) {
		siguientePalabra();
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
