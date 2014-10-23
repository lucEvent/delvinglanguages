package com.delvinglanguages.debug;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.debug.Communicator.Channel;
import com.delvinglanguages.settings.Configuraciones;

public class DebugfromPC extends Activity implements OnClickListener, Channel {

	private static final String DEBUG = "##DebugfromPC##";
	// ///////////
	// //////////
	private RelativeLayout background;
	private EditText word, tranlation, pronuntiation;
	private Button san, rep;
	private Button[] types;
	private String[] replacements;
	
	private Communicator comm;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_debug);

		background = (RelativeLayout) findViewById(R.id.d_bg);
		background.setBackgroundColor(Color.GREEN);
		
		san = (Button) findViewById(R.id.d_san);
		san.setOnClickListener(this);

		rep = (Button) findViewById(R.id.d_replace);
		rep.setOnClickListener(this);

		word = (EditText) findViewById(R.id.d_word);
		tranlation = (EditText) findViewById(R.id.d_trans);
		pronuntiation = (EditText) findViewById(R.id.d_pron);

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
		
		comm = new Communicator(Communicator.APP, this);
	}

	@Override
	public void onClick(View v) {
		if (v == rep) {
			word.setText("");
			tranlation.setText("");
			pronuntiation.setText("");
			setType(0);
			return;
		}
		if (v != san) {
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
		Log.d("##DEBUG##", "Añadiendo:" + nombre);
		ControlCore.addPalabra(nombre, trans, pron, type);
		Log.d("##DEBUG##", "Añadida");

		// Borrando campos
		word.setText("");
		tranlation.setText("");
		pronuntiation.setText("");
		setType(0);
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
	public void notificate(int code, String message) {
		switch (code) {
		case Channel.SEND:
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
			Log.d(DEBUG, "Send: " + message);
			break;
		case Channel.RECEIVED:
			String[] frags = message.split(".");
			if (frags.length < 4) {
				Log.d(DEBUG,
						"Error: No se han encontrado suficientes elementos");
				return;
			}
			Toast.makeText(this, "Received", Toast.LENGTH_SHORT).show();
			word.setText(frags[0]);
			tranlation.setText(frags[1]);
			pronuntiation.setText(frags[2]);
			setType(Integer.parseInt(frags[4]));
			break;
		case Channel.ERROR:
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
			Log.d(DEBUG, "Error: " + message);
			break;
		default:
			break;
		}

	}

}
