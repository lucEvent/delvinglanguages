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
import com.delvinglanguages.kernel.IDDelved;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.settings.Settings;

public class AddLanguageActivity extends Activity implements OnItemSelectedListener {

	protected Spinner selector;

	protected EditText input;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.a_select_language, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		selector = (Spinner) findViewById(R.id.selector);
		selector.setOnItemSelectedListener(this);
		selector.setSelection(0);

		input = (EditText) findViewById(R.id.input);
	}

	/** *************** METODOS ONITEMSELECTEDLISTENER *************** **/
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		String[] idiomas = getResources().getStringArray(R.array.paises);
		if (pos != 0) {
			input.setText(idiomas[pos]);
			input.setSelection(idiomas[pos].length());
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
		boolean ph = ((CheckedTextView) findViewById(R.id.phrasalsenabled)).isChecked();
		boolean adj = ((CheckedTextView) findViewById(R.id.adjectsenabled)).isChecked();
		boolean spe = ((CheckedTextView) findViewById(R.id.special_chars_enabled)).isChecked();
		int settings = IDDelved.configure(ph, adj, spe);

		KernelControl.addLanguage(name, settings);
		finish();
	}

	public void cancel(View v) {
		finish();
	}

	protected void showMessage(int text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
}