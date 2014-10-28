package com.delvinglanguages.face.activity.add;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.Palabra;
import com.delvinglanguages.face.activity.add.AddWordActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

public class AddWordFromModifyActivity extends AddWordActivity {

	private Palabra palabra;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_add_word_from_modify);

		word.removeTextChangedListener(this);
		((Button) findViewById(R.id.addmore)).setVisibility(View.GONE);

		
		int id = getIntent().getExtras().getInt(SEND_WORD);
		palabra = idioma.getPalabra(id);

		String name = palabra.getName();
		word.setText(name);
		word.setSelection(name.length());

		{ // Debug.edit for svenska
			if (palabra.isNoun() || palabra.isAdjective()) {
				String auxs = name + " [" + name + "en, " + name + "]";
				word.setText(auxs);
				word.setSelection(auxs.length() - 1);
			}
		} // end debug.edit

		tranlation.setText(palabra.getTranslation());
		pronuntiation.setText(palabra.getPronunciation());
		setType(palabra.getType());
		setTitle(R.string.modifyingword);

	}

	@Override
	protected void saveWord(String nombre, String trans, String pron, int type) {
		ControlCore.updatePalabra(palabra, nombre, trans, pron, type);
		Intent resultIntent = new Intent();
		resultIntent.putExtra(EDITED, nombre);
		setResult(Activity.RESULT_OK, resultIntent);
		showMessage(R.string.msswordadded);
	}

}
