package com.delvinglanguages.face.settings;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.delvinglanguages.R;
import com.delvinglanguages.settings.Configuraciones;

public class FoneticsActivity extends Activity {

	private RelativeLayout background;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_fonetics);

		background = (RelativeLayout) findViewById(R.id.af_bg);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

	}

}
