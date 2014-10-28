package com.delvinglanguages.face.activity.add;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.core.Palabra;
import com.delvinglanguages.face.listeners.FoneticsKeyboard;
import com.delvinglanguages.face.listeners.SpecialKeysBar;
import com.delvinglanguages.settings.Configuraciones;

public class AddWordActivity extends Activity implements TextWatcher,
		OnFocusChangeListener {

	public static final String SEND_NAME = "sendName";
	public static final String SEND_ID = "sendID";
	public static final String SEND_TRANSLATION = "sendTranslation";
	public static final String SEND_PRONUNTIATION = "sendPronuntiation";
	public static final String SEND_TYPE = "sendType";
	public static final String SEND_WORD = "sendWord";
	public static final String EDITED = "edited";

	protected static final int NOUN = 0;
	protected static final int VERB = 1;
	protected static final int ADJECTIVE = 2;
	protected static final int ADVERB = 3;
	protected static final int PHRASAL = 4;
	protected static final int EXPRESSION = 5;
	protected static final int OTHER = 6;

	protected IDDelved idioma;

	protected EditText word, tranlation, pronuntiation;

	protected Button[] types;

	protected Button remove;

	protected FoneticsKeyboard fonetickb;

	protected SpecialKeysBar specialKeys;

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
		word.addTextChangedListener(this);
		word.setOnFocusChangeListener(this);
		tranlation = (EditText) findViewById(R.id.translation);
		tranlation.setOnFocusChangeListener(this);
		pronuntiation = (EditText) findViewById(R.id.pronunciation);
		pronuntiation.setOnFocusChangeListener(this);

		types = new Button[Configuraciones.NUM_TYPES];
		types[NOUN] = (Button) findViewById(R.id.sel_noun);
		types[VERB] = (Button) findViewById(R.id.sel_verb);
		types[ADJECTIVE] = (Button) findViewById(R.id.sel_adj);
		types[ADVERB] = (Button) findViewById(R.id.sel_adv);
		types[PHRASAL] = (Button) findViewById(R.id.sel_phrasal);
		types[EXPRESSION] = (Button) findViewById(R.id.sel_expression);
		types[OTHER] = (Button) findViewById(R.id.sel_other);
		for (int i = 0; i < types.length; i++) {
			types[i].setSelected(false);
		}

		specialKeys = new SpecialKeysBar(this, null, null);

		fonetickb = new FoneticsKeyboard(this, R.id.ap_keyboard, pronuntiation,
				idioma.CODE);

		setTitle(R.string.addingnewword);
	}

	@Override
	public void onBackPressed() {
		if (fonetickb.isCustomKeyboardVisible())
			fonetickb.hideCustomKeyboard();
		else
			finish();
	}

	protected void get_and_set_Name(Bundle bundle) {
		String name = bundle.getString(SEND_NAME);
		if (name != null) {
			word.setText(name);
			word.setSelection(name.length());
		}
	}

	protected void get_and_set_Translation(Bundle bundle) {
		String value = bundle.getString(SEND_TRANSLATION);
		if (value != null) {
			tranlation.setText(value);
		}
	}

	protected void get_and_set_Pronuntiation(Bundle bundle) {
		String value = bundle.getString(SEND_PRONUNTIATION);
		if (value != null) {
			pronuntiation.setText(value);
		}
	}

	protected void get_and_set_Type(Bundle bundle) {
		Integer value = bundle.getInt(SEND_TYPE);
		if (value != null) {
			setType(value);
		}
	}

	public void changeState(View v) {
		v.setSelected(!v.isSelected());
	}

	public void cancel(View v) {
		finish();
	}

	public void secondOption(View v) {
		addWord();
		word.setText("");
		tranlation.setText("");
		pronuntiation.setText("");
		setType(0);
		word.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
		}
	}

	public void addWord(View v) {
		addWord();
		finish();
	}

	protected void addWord() {
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
		saveWord(nombre, trans, pron, type);
	}

	protected int getType() {
		int type = 0;
		for (int i = 0; i < Configuraciones.NUM_TYPES; i++) {
			if (types[i].isSelected()) {
				type += (1 << i);
			}
		}
		return type;
	}

	protected void setType(int type) {
		for (int i = 0; i < Configuraciones.NUM_TYPES; ++i) {
			if ((type & (1 << i)) != 0) {
				types[i].setSelected(true);
			} else if (types[i].isSelected()) {
				types[i].setSelected(false);
			}
		}
	}

	protected void showMessage(int text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	protected void saveWord(String nombre, String trans, String pron, int type) {
		if (autocomplete) {
			ControlCore.updatePalabra(modifiedWord, nombre, trans, pron, type);
			showMessage(R.string.msswordmodified);
			return;
		}
		ControlCore.addPalabra(nombre, trans, pron, type);
		showMessage(R.string.msswordadded);
	}

	public void specialKeyAction(View v) {
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
		if (t.isEmpty()) {
			t = (String) v.getTag();
		} else {
			t = t + ((Button) v).getText();
		}
		foc.setText(t);
		foc.setSelection(t.length());
	}

	// ********** LISTENERS *************************************************
	@Override
	public void onFocusChange(View view, boolean hasfocus) {
		if (hasfocus) {
			EditText ed = (EditText) view;
			ed.setSelection(ed.getText().length());
		}
	}

	/** ** METODOS TEXT WATCHER ** **/
	protected boolean autocomplete = false;
	protected Palabra modifiedWord = null;
	private Editable savedTranlation = null;
	private Editable savedPronuntiation = null;
	private int savedType = 0;

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (autocomplete) {
			if (remove != null) {
				remove.setEnabled(true);
			}
			autocomplete = false;
			pronuntiation.setText(savedPronuntiation);
			tranlation.setText(savedTranlation);
			setType(savedType);
		}
		modifiedWord = null;
		if (s.length() > 0) {
			modifiedWord = idioma.getPalabra(s.toString());
		}
		if (modifiedWord != null) {
			if (remove != null) {
				remove.setEnabled(false);
			}
			autocomplete = true;

			savedPronuntiation = pronuntiation.getText();
			savedTranlation = tranlation.getText();
			savedType = getType();

			pronuntiation.setText(modifiedWord.getPronunciation());
			tranlation.setText(modifiedWord.getTranslation());
			setType(modifiedWord.getType());
		}
	}

}
