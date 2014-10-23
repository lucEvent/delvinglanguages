package com.delvinglanguages.debug;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.face.listeners.FoneticsKeyboard;
import com.delvinglanguages.settings.Configuraciones;

public class Debug extends Activity implements OnClickListener,
		OnFocusChangeListener {

	// ///////////
	// //////////
	private RelativeLayout background;
	private EditText word, tranlation, pronuntiation;
	private Button san,rep;
	private Button[] types;
	private FoneticsKeyboard fonetickb;
	private String[] replacements;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_debug);

		background = (RelativeLayout) findViewById(R.id.d_bg);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		san = (Button) findViewById(R.id.d_san);
		san.setOnClickListener(this);
		
		rep = (Button) findViewById(R.id.d_replace);
		rep.setOnClickListener(this);
		rep.setText("Remove Store");

//		TxtReader txt = new TxtReader();
//		replacements = txt.getOntheShadowofTheMountain();
		indexREPS = 0;

		word = (EditText) findViewById(R.id.d_word);
		tranlation = (EditText) findViewById(R.id.d_trans);
		pronuntiation = (EditText) findViewById(R.id.d_pron);

		word.setOnFocusChangeListener(this);
		tranlation.setOnFocusChangeListener(this);

		types = new Button[Configuraciones.NUM_TYPES];
		types[0] = (Button) findViewById(R.id.d_noun);
		types[1] = (Button) findViewById(R.id.d_verb);
		types[2] = (Button) findViewById(R.id.d_adj);
		types[3] = (Button) findViewById(R.id.d_adv);
		types[4] = (Button) findViewById(R.id.d_phrasal);
		types[5] = (Button) findViewById(R.id.d_expression);
		types[6] = (Button) findViewById(R.id.d_other);
		for (int i = 0; i < types.length; i++) {
			types[i].setOnClickListener(this);
			types[i].setSelected(false);
		}

		fonetickb = new FoneticsKeyboard(this, R.id.d_keyboard, pronuntiation, ControlCore.getIdiomaActual(this).CODE);

		setNext();

	}

	@Override
	protected void onResume() {
		super.onResume();

		word.setText(replacements[indexREPS - 2]);
		tranlation.setText(replacements[indexREPS - 1]);

	}

	@Override
	public void onBackPressed() {
		if (fonetickb.isCustomKeyboardVisible())
			fonetickb.hideCustomKeyboard();
		else
			finish();
	}

	private void setNext() {
		if (indexREPS >= replacements.length) {
			finish();
			return;
		}

		String pal = replacements[indexREPS];
		Toast.makeText(this, pal, Toast.LENGTH_SHORT).show();
		word.setText(pal);
		indexREPS++;
		
		String trans = replacements[indexREPS];
		Toast.makeText(this, trans, Toast.LENGTH_SHORT).show();
		tranlation.setText(trans);
		indexREPS++;

		pronuntiation.setText("");
		pronuntiation.requestFocus();
		setType(0);

	}

	private int indexREPS;

	@Override
	public void onClick(View v) {
		if (v == rep) {
			return;
		}
		if (v != san) {
			v.setSelected(!v.isSelected());
			return;
		}

		// Salvar palabra actual (si se ha modificado)
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
		Log.d("##DEBUG##", "Añadiendo");
		ControlCore.addPalabra(nombre, trans, pron, type);
		Log.d("##DEBUG##", "Añadida");
		
		// Siguiente palabra
		setNext();
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

	private void showMessage(int text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onFocusChange(View view, boolean hasfocus) {
		if (hasfocus) {
			EditText ed = (EditText) view;
			ed.setSelection(ed.getText().length());
		}
	}

}
