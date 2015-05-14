package com.delvinglanguages.face.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.face.dialog.IntegrateManager;
import com.delvinglanguages.kernel.IDDelved;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.settings.Settings;

public class LanguageSettingsActivity extends Activity {

	private static final String DEBUG = "##LanguageSettingsActivity##";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.a_language_settings, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		CheckedTextView ph = (CheckedTextView) findViewById(R.id.phrasal_state);
		CheckedTextView ad = (CheckedTextView) findViewById(R.id.adjective_state);
		CheckedTextView sp = (CheckedTextView) findViewById(R.id.special_chars_state);

		IDDelved idioma = KernelControl.getCurrentLanguage();
		ph.setChecked(idioma.getSettings(IDDelved.MASK_PH));
		ad.setChecked(idioma.getSettings(IDDelved.MASK_ADJ));
		sp.setChecked(idioma.getSettings(IDDelved.MASK_ESP_CHARS));
	}

	public void changeLanguageName(View v) {
		String name = LanguageKernelControl.getLanguageName();

		View view = LayoutInflater.from(this).inflate(R.layout.i_input, null);
		final EditText input = (EditText) view.findViewById(R.id.input_dialog);

		input.setText(name);
		input.setSelection(name.length());

		new AlertDialog.Builder(this).setTitle(R.string.renaminglanguage).setView(view)
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface d, int id) {
						String s = input.getText().toString();
						if (s.length() == 0) {
							showMessage(R.string.nonamelang);
						} else {
							KernelControl.renameLanguage(s);
							showMessage(R.string.renamedsuccessfully);
						}
					}
				}).setNegativeButton(R.string.cancel, null).create().show();

		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(input, InputMethodManager.SHOW_FORCED);
	}

	public void togglePhrasalState(View v) {
		CheckedTextView ctv = (CheckedTextView) v;
		ctv.toggle();
		KernelControl.setLanguageSettings(ctv.isChecked(), IDDelved.MASK_PH);
	}

	public void toggleAdjectiveState(View v) {
		CheckedTextView ctv = (CheckedTextView) v;
		ctv.toggle();
		KernelControl.setLanguageSettings(ctv.isChecked(), IDDelved.MASK_ADJ);
	}

	public void toggleSpecialCharacterState(View v) {
		CheckedTextView ctv = (CheckedTextView) v;
		ctv.toggle();
		KernelControl.setLanguageSettings(ctv.isChecked(), IDDelved.MASK_ESP_CHARS);
	}

	public void integrate(View v) {
		IntegrateManager im = new IntegrateManager(this);
		im.start();
	}

	public void remove(View v) {
		String temp = getString(R.string.title_removing) + " " + LanguageKernelControl.getLanguageName();

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(temp);
		builder.setMessage(R.string.removeidiomquestion);
		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				removeLanguage();
			}
		});
		builder.setNegativeButton(R.string.cancel, null);
		builder.create().show();
	}

	public void clearStatistics(View v) {
		KernelControl.clearStatistics();
		showMessage(R.string.mssclearstats);
	}

	private void removeLanguage() {
		String mssg = LanguageKernelControl.getLanguageName() + " " + getResources().getString(R.string._removed);
		KernelControl.deleteLanguage();
		setResult(Activity.RESULT_OK, null);
		showMessage(mssg);
		finish();
	}

	private void showMessage(int text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	private void showMessage(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

}
