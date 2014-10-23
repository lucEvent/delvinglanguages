package com.delvinglanguages.face.settings;

import com.delvinglanguages.R;
import com.delvinglanguages.settings.Configuraciones;

import android.os.Bundle;
import android.app.Activity;
import android.widget.LinearLayout;

public class About extends Activity {

	private LinearLayout background;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_about);
		
		background = (LinearLayout) findViewById(R.id.aabout_bg);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}
	}


}
