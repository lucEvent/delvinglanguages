package com.delvinglanguages.face.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.listers.AvailableLanguageLister;
import com.delvinglanguages.settings.Settings;

public class AddLanguageActivity extends Activity implements OnItemSelectedListener {

	private String[] languages;

	private Spinner spinner;
	private EditText input;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.a_select_language, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		languages = getResources().getStringArray(R.array.languages);

		spinner = (Spinner) findViewById(R.id.selector);
		spinner.setAdapter(new AvailableLanguageLister(this, languages));
		spinner.setOnItemSelectedListener(this);
		spinner.setSelection(0);

		input = (EditText) findViewById(R.id.input);
	}

	/** *************** METODOS ONITEMSELECTEDLISTENER *************** **/
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		String in = input.getText().toString();
		boolean update = in.length() == 0;
		if (!update) {
			for (String s : languages) {
				if (s.equals(in)) {
					update = true;
					break;
				}
			}
		}
		if (update) {
			input.setText(languages[pos]);
			input.setSelection(languages[pos].length());
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	public void toggle(View v) {
		((CheckedTextView) v).toggle();
	}

	public void addLanguage(View v) {
		String name = input.getText().toString();
		if (name.isEmpty()) {
			showMessage(R.string.msgnolangname);
			return;
		}
		int code = spinner.getSelectedItemPosition();

		boolean ph = ((CheckedTextView) findViewById(R.id.phrasalsenabled)).isChecked();
		boolean adj = ((CheckedTextView) findViewById(R.id.adjectsenabled)).isChecked();
		boolean spe = ((CheckedTextView) findViewById(R.id.special_chars_enabled)).isChecked();
		int settings = Language.configure(ph, adj, spe);

		KernelControl.addLanguage(code, name, settings);
		finish();
	}

	public void cancel(View v) {
		finish();
	}

	protected void showMessage(int text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

}
