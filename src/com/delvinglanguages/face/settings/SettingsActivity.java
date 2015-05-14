package com.delvinglanguages.face.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;

import com.delvinglanguages.R;
import com.delvinglanguages.data.BackUp;
import com.delvinglanguages.settings.Settings;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_app_settings);
	}

	@Override
	public void onResume() {
		super.onResume();

		Settings.setBackgroundTo(findViewById(android.R.id.content));

		CheckedTextView dm = (CheckedTextView) findViewById(R.id.doublemode);
		CheckedTextView vkb = (CheckedTextView) findViewById(R.id.vibratekb_state);

		dm.setChecked(Settings.doubleMode());
		vkb.setChecked(Settings.vibration());
	}

	public void changeNativeLanguage(View v) {
		startActivity(new Intent(this, SelectLanguage.class));

	}

	public void double_mode_state(View v) {
		((CheckedTextView) v).toggle();
		Settings.toggleDoubleMode();
	}

	public void vibratekb_state(View v) {
		((CheckedTextView) v).toggle();
		Settings.toggleVibration();

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

	public void createBackup(View v) {
		new BackUp().createBackUp(this);
	}

	public void recoverBackup(View v) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				new BackUp().recoverBackUp(SettingsActivity.this);
			}
		}).start();
	}

}