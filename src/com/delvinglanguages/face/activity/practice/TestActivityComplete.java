package com.delvinglanguages.face.activity.practice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.core.Cerebro;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.Test;
import com.delvinglanguages.face.dialog.InputDialog;

public class TestActivityComplete extends CompleteActivity {

	private static final String DEBUG = "##TestActivityComplete##";

	private Test test;

	private int succesCounter, posicionPalabra;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		test = ControlCore.testActual;
		test.state = Test.STAT_COMPLETE;
		cerebro = new Cerebro(test.references);

		succesCounter = 0;
		for (int i = 0; i < test.passed.length; i++) {
			if (test.passed[i]) {
				succesCounter++;
			}
		}

		siguientePregunta();
	}

	private void siguientePregunta() {
		posicionPalabra = cerebro.nextPosition(test.passed);
		super.siguientePregunta(test.references.get(posicionPalabra));
	}

	@Override
	public void onClick(View v) {
		int tecla = (Integer) v.getTag();
		if (teclas[tecla].position == position) {
			flashcolor(0xFF00FF00);

			String toappend = teclas[tecla].string;
			descubierta.replace(cursor, cursor + (toappend.length() << 1),
					toappend);

			cursor += toappend.length();
			position++;

			hidden.setText(descubierta);

			// Si se ha completado la palabra
			if (cursor == palabraUpp.length()) {
				pronounce.setText("[" + refActual.getPronunciation() + "]");
				succesCounter++;
				test.statistics.get(posicionPalabra).aciertos_complete++;
				if (succesCounter == test.passed.length) {
					test.nextStat();
					startActivity(new Intent(this, TestActivityWrite.class));
					finish();
					return;
				}
				test.passed[posicionPalabra] = true;
				for (int i = 0; i < letras.length; i++) {
					letras[i].setClickable(false);
					letras[i].setEnabled(false);
				}
				new Thread(new Runnable() {
					public void run() {

						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								siguientePregunta();
							}
						});
					}
				}).start();
			} else if (teclas[tecla].visibleUntil <= position) {
				if (teclas[tecla].replaceBy != null) {
					Button b = (Button) v;
					teclas[tecla] = teclas[tecla].replaceBy;
					b.setText("" + teclas[tecla].letter);
				}
			}
		} else {
			flashcolor(0xFFFF0000);
			test.statistics.get(posicionPalabra).fallos_complete++;
			for (int i = 0; i < letras.length; i++) {
				letras[i].setClickable(false);
				letras[i].setEnabled(false);
			}
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
							siguientePregunta();
						}
					});
				}
			}).start();
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