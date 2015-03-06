package com.delvinglanguages.face.activity.practice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.Test;
import com.delvinglanguages.core.Test.State;
import com.delvinglanguages.core.game.WriteGame;
import com.delvinglanguages.face.dialog.InputDialog;
import com.delvinglanguages.settings.Configuraciones;

public class TestActivityWrite extends WriteWordsActivity {

	private Test test;

	private int succesCounter, posicionPalabra;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		test = ControlCore.testActual;
		test.state = Test.STAT_WRITE;
		gamecontroller = new WriteGame(test.references);

		succesCounter = 0;
		for (int i = 0; i < test.passed.length; i++) {
			if (test.passed[i]) {
				succesCounter++;
			}
		}
		super.onCreate(savedInstanceState);

		help.setEnabled(false);
		swap.setEnabled(false);
	}

	@Override
	protected void siguientePalabra() {
		posicionPalabra = gamecontroller.nextPosition(test.passed);
		refActual = test.references.get(posicionPalabra);

		int type = refActual.type;
		for (int i = 0; i < Configuraciones.NUM_TYPES; ++i) {
			if ((type & (1 << i)) != 0) {
				labels[i].setBackgroundColor(Configuraciones.type_colors[i]);
			} else {
				labels[i].setBackgroundColor(0xFFCCCCCC);
			}
		}
		progress.setMax(refActual.name.length());
		progress.setProgress(0);
		input.setText("");
		palabra.setText(refActual.getTranslation().toUpperCase());
	}

	/** **************** TEXTWATCHER ******************* **/

	@Override
	public void afterTextChanged(Editable s) {
		String answer = input.getText().toString();
		if (refActual.name.toLowerCase().startsWith(answer.toLowerCase())) {

			progress.setProgress(answer.length());
			progress.getProgressDrawable().setColorFilter(0xFF33CC00,
					PorterDuff.Mode.MULTIPLY);
			if (answer.length() < refActual.name.length()) {
				fullfill();
			} else if (refActual.name.toLowerCase()
					.equals(answer.toLowerCase())) {
				succesCounter++;
				if (succesCounter == test.passed.length) {
					// Modificar prioridades segun resultados
					for (int i = 0; i < test.passed.length; i++) {
						State e = test.statistics.get(i);
						if (e.fallos_match + e.fallos_complete + e.fallos_write == 0) {
							ControlCore.ejercicio(test.references.get(i), 1);
						}
					}
					//
					test.nextStat();
					startActivity(new Intent(this, TestActivityResult.class));
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
			progress.setProgress(refActual.name.length());
			progress.getProgressDrawable().setColorFilter(0xFFFF0000,
					PorterDuff.Mode.SRC_IN);
		}
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
