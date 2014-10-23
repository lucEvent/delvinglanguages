package com.delvinglanguages.face.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.core.Nota;
import com.delvinglanguages.core.Palabra;
import com.delvinglanguages.face.listeners.FoneticsKeyboard;
import com.delvinglanguages.face.listeners.SpecialKeysBar;
import com.delvinglanguages.settings.Configuraciones;

public class AddWordActivity extends Activity implements OnClickListener,
		TextWatcher, OnFocusChangeListener {

	private static final int NOUN = 0;
	private static final int VERB = 1;
	private static final int ADJECTIVE = 2;
	private static final int ADVERB = 3;
	private static final int PHRASAL = 4;
	private static final int EXPRESSION = 5;
	private static final int OTHER = 6;

	public static final int FROM_DEFAULT = 1;
	public static final int FROM_WAREHOUSE = 2;
	public static final int FROM_MODIFY = 3;
	public static final int FROM_PHRASAL = 4;
	public static final int FROM_VERB = 5;
	public static final int FROM_OTHER = 6;

	public static final String EDITED = "edited";

	private IDDelved idioma;

	private EditText word, tranlation, pronuntiation;

	private Button cancel, addmore, add, remove;

	private Button[] types;

	private FoneticsKeyboard fonetickb;

	private SpecialKeysBar specialKeys;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_add_word);

		RelativeLayout background = (RelativeLayout) findViewById(R.id.background);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		idioma = ControlCore.getIdiomaActual(this);

		word = (EditText) findViewById(R.id.word);
		tranlation = (EditText) findViewById(R.id.translation);
		pronuntiation = (EditText) findViewById(R.id.pronunciation);

		cancel = (Button) findViewById(R.id.cancel);
		addmore = (Button) findViewById(R.id.addmore);
		add = (Button) findViewById(R.id.add);

		cancel.setOnClickListener(this);
		add.setOnClickListener(this);

		types = new Button[Configuraciones.NUM_TYPES];
		types[NOUN] = (Button) findViewById(R.id.sel_noun);
		types[VERB] = (Button) findViewById(R.id.sel_verb);
		types[ADJECTIVE] = (Button) findViewById(R.id.sel_adj);
		types[ADVERB] = (Button) findViewById(R.id.sel_adv);
		types[PHRASAL] = (Button) findViewById(R.id.sel_phrasal);
		types[EXPRESSION] = (Button) findViewById(R.id.sel_expression);
		types[OTHER] = (Button) findViewById(R.id.sel_other);
		for (int i = 0; i < types.length; i++) {
			types[i].setOnClickListener(this);
			types[i].setSelected(false);
		}

		specialKeys = new SpecialKeysBar(this, null, new SpecialListener());

		fonetickb = new FoneticsKeyboard(this, R.id.ap_keyboard, pronuntiation,
				idioma.CODE);
		configureState();
	}

	@Override
	public void onBackPressed() {
		if (fonetickb.isCustomKeyboardVisible())
			fonetickb.hideCustomKeyboard();
		else
			finish();
	}

	private Palabra pmod;

	private void configureState() {
		int from = getIntent().getExtras().getInt("from");
		switch (from) {
		case FROM_VERB:
			types[VERB].setSelected(true);
		case FROM_DEFAULT: // Normal add
			word.addTextChangedListener(this);
			addmore.setOnClickListener(this);
			setTitle(R.string.addingnewword);
			break;
		case FROM_MODIFY: // Modify
			addmore.setEnabled(false);
			int id = getIntent().getExtras().getInt(ControlCore.sendPalabra);
			pmod = idioma.getPalabra(id); // necesario
			String name = pmod.getName();
			word.setText(name);
			word.setSelection(name.length());

			{ // Debug.edit
				if (pmod.isNoun() || pmod.isAdjective()) {
					String auxs = name + " [" + name + "en, " + name + "]";
					word.setText(auxs);
					word.setSelection(auxs.length() - 1);
				}
			} // end debug.edit

			tranlation.setText(pmod.getTranslation());
			pronuntiation.setText(pmod.getPronunciation());
			setType(pmod.getType());
			setTitle(R.string.modifyingword);
			break;
		case FROM_WAREHOUSE: // Add from store
			word.addTextChangedListener(this);
			remove = addmore;
			addmore = null;
			remove.setText(R.string.remove);
			remove.setOnClickListener(this);
			Nota nota = ControlCore.notaToModify;
			word.setText(nota.get());
			tranlation.requestFocus();
			setTitle(R.string.addingnewword);
			break;
		case FROM_PHRASAL:
			word.addTextChangedListener(this);
			String ph = getIntent().getExtras().getString("phrasal");
			word.setText(ph);
			word.setSelection(ph.length());
			addmore.setEnabled(false);
			for (int i = 0; i < types.length; i++) {
				types[i].setOnClickListener(null);
				types[i].setClickable(false);
			}
			types[4].setSelected(true);
			setTitle(R.string.addingnewword);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == cancel) {
			//
			finish();
			return;
		} else if (v == remove) {
			ControlCore.removeFromStore();
			finish();
			return;
		} else if (v != add && v != addmore) {
			v.setSelected(!v.isSelected());
			return;
		}
		String nombre = word.getText().toString();
		if (nombre.length() == 0) {
			showMessage(R.string.noword);
			return;
		}
		String trans = tranlation.getText().toString();
		if (trans.length() == 0) {
			showMessage(R.string.notrans);
			return;
		}
		String pron = pronuntiation.getText().toString();
		if (pron.length() == 0) {
			showMessage(R.string.nopron);
			return;
		}
		int type = getType();
		if (type == 0) {
			showMessage(R.string.notype);
			return;
		}

		if (v == addmore) {
			addPalabra(nombre, trans, pron, type);
			word.setText("");
			tranlation.setText("");
			pronuntiation.setText("");
			setType(0);
			word.requestFocus();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null) {
				imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
			}
		} else if (v == add) {
			addPalabra(nombre, trans, pron, type);
			finish();
		}
	}

	private void addPalabra(String nombre, String trans, String pron, int type) {
		int from = getIntent().getExtras().getInt("from");
		if (autocomplete) {
			if (from == FROM_WAREHOUSE) {
				ControlCore.removeFromStore();
			}
			ControlCore.updatePalabra(modifyingWord, nombre, trans, pron, type);
			showMessage(R.string.msswordmodified);
			return;
		}
		switch (from) {
		case FROM_DEFAULT: // Normal add
		case FROM_PHRASAL:
			ControlCore.addPalabra(nombre, trans, pron, type);
			break;
		case FROM_MODIFY: // Modify
			ControlCore.updatePalabra(pmod, nombre, trans, pron, type);
			Intent resultIntent = new Intent();
			resultIntent.putExtra(EDITED, nombre);
			setResult(Activity.RESULT_OK, resultIntent);
			break;
		case FROM_WAREHOUSE: // Add from store
			ControlCore.addWordFromStore(nombre, trans, pron, type);
		}
		showMessage(R.string.msswordadded);
	}

	private void showMessage(int text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	private int getType() {
		int type = 0;
		for (int i = 0; i < Configuraciones.NUM_TYPES; i++) {
			if (types[i].isSelected()) {
				type += (1 << i);
			}
		}
		return type;
	}

	private void setType(int type) {
		for (int i = 0; i < Configuraciones.NUM_TYPES; ++i) {
			if ((type & (1 << i)) != 0) {
				types[i].setSelected(true);
			} else if (types[i].isSelected()) {
				types[i].setSelected(false);
			}
		}
	}

	/** ** METODOS TEXTWATCHER ** **/
	private boolean autocomplete = false;
	private Palabra modifyingWord;
	private String savedTranlation;
	private String savedPronuntiation = null;
	private int savedType;

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		if (autocomplete) {
			if (remove != null) {
				remove.setEnabled(true);
			}
			autocomplete = false;
			pronuntiation.setText(savedPronuntiation);
			tranlation.setText(savedTranlation);
			setType(savedType);
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		modifyingWord = null;
		if (s.length() > 0) {
			modifyingWord = idioma.getPalabra(s.toString()); // Necesario
		}
		if (modifyingWord != null) {
			if (remove != null) {
				remove.setEnabled(false);
			}
			autocomplete = true;

			savedPronuntiation = pronuntiation.getText().toString();
			savedTranlation = tranlation.getText().toString();
			savedType = getType();

			pronuntiation.setText(modifyingWord.getPronunciation());
			tranlation.setText(modifyingWord.getTranslation());
			setType(modifyingWord.getType());
		}
	}

	@Override
	public void onFocusChange(View view, boolean hasfocus) {
		if (hasfocus) {
			EditText ed = (EditText) view;
			ed.setSelection(ed.getText().length());
		}
	}

	private class SpecialListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			EditText foc;
			if (word.isFocused()) {
				foc = word;
			} else if (tranlation.isFocused()) {
				foc = tranlation;
			} else if (pronuntiation.isFocused()) {
				foc = pronuntiation;
			} else {
				return;
			}
			String t = foc.getText().toString();
			if (t.length() == 0) {
				t = t + v.getTag();
			} else {
				Button b = (Button) v;
				t = t + b.getText();
			}
			foc.setText(t);
			foc.setSelection(t.length());
		}

	}
}
