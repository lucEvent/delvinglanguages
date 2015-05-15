package com.delvinglanguages.face.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.delvinglanguages.R;
import com.delvinglanguages.face.settings.SettingsActivity;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.listers.LanguageLister;
import com.delvinglanguages.settings.Settings;

public class Main extends ListActivity {

	private static final String DEBUG = "##Main##";

	private static KernelControl kernel;
	private static Settings settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_main);

		settings = new Settings();
		kernel = new KernelControl(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		Settings.setBackgroundTo(findViewById(android.R.id.content));

		ListView list = getListView();
		View fsteps = findViewById(R.id.firststeps);
		View toinvis, tovisib = null;

		if (KernelControl.getNumLanguages() == 0) {
			toinvis = list;
			tovisib = fsteps;
		} else {
			toinvis = fsteps;
			tovisib = list;
			setListAdapter(new LanguageLister(this, KernelControl.getLanguages()));
		}
		toinvis.setVisibility(View.INVISIBLE);
		tovisib.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_addidiom:
			startActivity(new Intent(this, AddLanguageActivity.class));
			return true;
		case R.id.menu_appsettings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		return false;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		KernelControl.setCurrentLanguage(position);
		startActivity(new Intent(this, LanguageActivity.class));
	}

	public void addLanguage(View v) {
		startActivity(new Intent(this, AddLanguageActivity.class));
	}

	public void changeSettings(View v) {
		startActivity(new Intent(this, SettingsActivity.class));
	}

}
