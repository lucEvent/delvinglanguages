package com.delvinglanguages.face.activity.test;

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
import com.delvinglanguages.kernel.game.CompleteGame;
import com.delvinglanguages.face.activity.practice.CompleteActivity;
import com.delvinglanguages.face.dialog.InputDialog;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.test.TestKernelControl;

public class Test_5_CompleteActivity extends CompleteActivity {

	private Test test;
	private TestKernelControl kernel;

	private int succesCounter, posicionPalabra;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.test_title_4);

		kernel = new TestKernelControl(this);

		test = TestKernelControl.runningTest;
		test.state = Test.PHASE_COMPLETE;
		gamecontroller = new CompleteGame(test.getReferences());

		succesCounter = 0;
		for (int i = 0; i < test.references.size(); i++) {
			if (test.references.get(i).passed) {
				succesCounter++;
			}
		}

		siguientePregunta();
	}

	private void siguientePregunta() {
		posicionPalabra = gamecontroller.nextPosition(test.references);
		super.siguientePregunta(test.references.get(posicionPalabra).reference);
	}

	@Override
	public void onClick(View v) {
		int tecla = (Integer) v.getTag();
		if (teclas[tecla].position == position) {
			flashcolor(0xFF00FF00);

			String toappend = teclas[tecla].string;
			descubierta.replace(cursor, cursor + (toappend.length() << 1), toappend);

			cursor += toappend.length();
			position++;

			hidden.setText(descubierta);

			// Si se ha completado la palabra
			if (cursor == palabraUpp.length()) {
				pronounce.setText("[" + refActual.pronunciation + "]");
				succesCounter++;
				if (succesCounter == test.references.size()) {
					test.nextStat();
					startActivity(new Intent(this, Test_6_WriteActivity.class));
					finish();
					return;
				}
				test.references.get(posicionPalabra).passed = true;
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
						handler.post(new Runnable() {
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
			test.references.get(posicionPalabra).fallos_complete++;
			for (int i = 0; i < letras.length; i++) {
				letras[i].setClickable(false);
				letras[i].setEnabled(false);
			}
			new Thread(new Runnable() {
				public void run() {

					try {
						Thread.sleep(1200);
					} catch (InterruptedException e) {
					}
					handler.post(new Runnable() {
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