package com.delvinglanguages.face.settings;

import android.os.Bundle;
import android.view.View;

import com.delvinglanguages.R;
import com.delvinglanguages.face.activity.AddLanguageActivity;
import com.delvinglanguages.settings.Settings;

public class SelectLanguage extends AddLanguageActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String idiomanativo = Settings.IdiomaNativo;
		input.setText(idiomanativo);
		input.setSelection(idiomanativo.length());

		(findViewById(R.id.parameters)).setVisibility(View.GONE);

	}

	public void addLanguage(View v) {
		String name = input.getText().toString();
		if (name.isEmpty()) {
			showMessage(R.string.msgnolangname);
			return;
		}
		Settings.setIdiomaNativo(name);
		finish();
	}

}
