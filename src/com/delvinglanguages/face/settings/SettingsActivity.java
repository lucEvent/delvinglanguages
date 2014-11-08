package com.delvinglanguages.face.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;

import com.delvinglanguages.R;
import com.delvinglanguages.settings.Configuraciones;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_app_settings);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {
		super.onResume();

		int type_bg = Configuraciones.backgroundType();
		View background = findViewById(R.id.background);
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		CheckedTextView dm = (CheckedTextView) findViewById(R.id.doublemode);
		CheckedTextView vkb = (CheckedTextView) findViewById(R.id.vibratekb_state);

		dm.setChecked(Configuraciones.doubleMode());
		vkb.setChecked(Configuraciones.vibration());
	}

	public void changeNativeLanguage(View v) {
		startActivity(new Intent(this, SelectLanguage.class));

	}

	public void double_mode_state(View v) {
		((CheckedTextView) v).toggle();
		Configuraciones.toggleDoubleMode();
	}

	public void vibratekb_state(View v) {
		((CheckedTextView) v).toggle();
		Configuraciones.toggleVibration();

	}

	public void fonetics(View v) {
		startActivity(new Intent(this, FoneticsActivity.class));
	}

	public void background(View v) {
		startActivity(new Intent(this, SelectBackground.class));
	}

	public void historial(View v) {
		startActivity(new Intent(this, HistorialActivity.class));
	}

	public void about(View v) {
		startActivity(new Intent(this, About.class));
	}

}