package com.delvinglanguages.face.activity.test;

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
import com.delvinglanguages.core.game.WriteGame;
import com.delvinglanguages.face.activity.practice.WriteWordsActivity;
import com.delvinglanguages.face.dialog.InputDialog;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.test.TestKernelControl;
import com.delvinglanguages.kernel.test.TestReferenceState;
import com.delvinglanguages.settings.Configuraciones;

public class Test_6_WriteActivity extends WriteWordsActivity {

	private Test test;
	private TestKernelControl kernel;

	private int succesCounter, posicionPalabra;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		test = TestKernelControl.runningTest;
		test.state = Test.PHASE_WRITE;
		gamecontroller = new WriteGame(test.getReferences());

		succesCounter = 0;
		for (int i = 0; i < test.references.size(); i++) {
			if (test.references.get(i).passed) {
				succesCounter++;
			}
		}
		super.onCreate(savedInstanceState);

		kernel = new TestKernelControl(this);

		help.setEnabled(false);
		swap.setEnabled(false);
	}

	@Override
	protected void siguientePalabra() {
		posicionPalabra = gamecontroller.nextPosition(test.references);
		refActual = test.references.get(posicionPalabra).reference;

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
				if (succesCounter == test.references.size()) {
					// Modificar prioridades segun resultados
					for (TestReferenceState refstate : test.references) {
						if (refstate.fallos_match + refstate.fallos_complete
								+ refstate.fallos_write == 0) {
							ControlCore.ejercicio(refstate.reference, 1);
						}
					}
					//
					test.nextStat();
					startActivity(new Intent(this, Test_7_ResultActivity.class));
					finish();
					return;
				}
				test.references.get(posicionPalabra).passed = true;

				new Thread(new Runnable() {
					public void run() {

						try {
							Thread.sleep(1200);
						} catch (InterruptedException e) {
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
			test.references.get(posicionPalabra).fallos_write++;
			progress.setProgress(refActual.name.length());
			progress.getProgressDrawable().setColorFilter(0xFFFF0000,
					PorterDuff.Mode.SRC_IN);
		}
	}

	/** *************** METODOS DE MEN� *************** **/
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
			builder.setPositiveButton(R.string.confirm,
					new DialogInterface.OnClickListener() {
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
