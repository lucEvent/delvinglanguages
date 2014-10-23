package com.delvinglanguages.face.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.delvinglanguages.R;
import com.delvinglanguages.settings.Configuraciones;

public class SelectLanguage extends Activity implements OnItemSelectedListener,
		OnClickListener {

	private RelativeLayout background;
	
	private Spinner selector;
	private EditText input;
	private Button aceptar;
	private Button cancelar;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_select_language);

		background = (RelativeLayout) findViewById(R.id.alang_bg);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}
		
		selector = (Spinner) findViewById(R.id.aia_selector);
		selector.setOnItemSelectedListener(this);
		selector.setSelection(0);

		input = (EditText) findViewById(R.id.aia_input);
		aceptar = (Button) findViewById(R.id.aia_accept);
		cancelar = (Button) findViewById(R.id.aia_cancel);
		aceptar.setOnClickListener(this);
		cancelar.setOnClickListener(this);
		
		String idiomanativo = Configuraciones.IdiomaNativo;
		input.setText(idiomanativo);
		input.setSelection(idiomanativo.length());
	}

	/** *************** METODOS ONITEMSELECTEDLISTENER *************** **/
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		String[] idiomas = getResources().getStringArray(R.array.paises);
		if (pos != 0) {
			input.setText(idiomas[pos]);
			input.setSelection(idiomas[pos].length());
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	/** *************** METODOS ONCLICKLISTENER *************** **/
	@Override
	public void onClick(View v) {
		if (v == aceptar) {
			Configuraciones.setIdiomaNativo(input.getText().toString());
		}
		finish();
	}
}
