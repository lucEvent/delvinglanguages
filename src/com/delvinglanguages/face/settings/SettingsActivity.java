package com.delvinglanguages.face.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ScrollView;

import com.delvinglanguages.R;
import com.delvinglanguages.settings.Configuraciones;

public class SettingsActivity extends Activity implements OnClickListener {

	private CheckedTextView doublemode, vibratekb;
	private Button natlanguage, fonetics, background, historial, about;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_app_settings);

		natlanguage = (Button) findViewById(R.id.nativelanguage);
		doublemode = (CheckedTextView) findViewById(R.id.doublemode);
		vibratekb = (CheckedTextView) findViewById(R.id.vibrate_phon_kb);
		fonetics = (Button) findViewById(R.id.fonetics);
		background = (Button) findViewById(R.id.sel_background);
		historial = (Button) findViewById(R.id.historial);
		about = (Button) findViewById(R.id.about);

		natlanguage.setOnClickListener(this);
		doublemode.setOnClickListener(this);
		vibratekb.setOnClickListener(this);
		fonetics.setOnClickListener(this);
		background.setOnClickListener(this);
		historial.setOnClickListener(this);
		about.setOnClickListener(this);

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {
		super.onResume();

		int type_bg = Configuraciones.backgroundType();
		ScrollView background = (ScrollView) findViewById(R.id.background);
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		doublemode.setChecked(Configuraciones.doubleMode());
		vibratekb.setChecked(Configuraciones.vibration());
	}

	@Override
	public void onClick(View v) {
		if (v == natlanguage) {
			startActivity(new Intent(this, SelectLanguage.class));
		} else if (v == doublemode) {
			doublemode.toggle();
			Configuraciones.toggleDoubleMode();
		} else if (v == vibratekb) {
			vibratekb.toggle();
			Configuraciones.toggleVibration();
		} else if (v == fonetics) {
			startActivity(new Intent(this, FoneticsActivity.class));
		} else if (v == background) {
			startActivity(new Intent(this, SelectBackground.class));
		} else if (v == historial) {
			startActivity(new Intent(this, Historial.class));
		} else if (v == about) {
			startActivity(new Intent(this, About.class));
		}
	}
}