package com.delvinglanguages.face.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.delvinglanguages.R;
import com.delvinglanguages.settings.Settings;

public class FoneticsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.a_fonetics, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

	}

}
