package com.delvinglanguages.face.activity;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.face.settings.SettingsActivity;
import com.delvinglanguages.listers.LanguageLister;
import com.delvinglanguages.settings.Configuraciones;

public class Main extends ListActivity {

	private static final String DEBUG = "##Main##";

	private static ControlCore core;
	private static Configuraciones settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_main);

		core = new ControlCore(this);
		settings = new Configuraciones();

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		int type_bg = Configuraciones.backgroundType();
		View background = findViewById(R.id.background);
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}
		ListView list = getListView();
		View fsteps = findViewById(R.id.firststeps);
		View toinvis, tovisib = null;

		if (ControlCore.getCount() == 0) {
			toinvis = list;
			tovisib = fsteps;
		} else {
			toinvis = fsteps;
			tovisib = list;
			setListAdapter(new LanguageLister(this, getLanguageStrings()));
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
		ControlCore.setIdiomaActual(position);
		startActivity(new Intent(this, LanguageActivity.class));
	}

	private String[] getLanguageStrings() {
		ArrayList<IDDelved> langs = ControlCore.getIdiomas();
		String[] values = new String[langs.size()];
		for (int i = 0; i < langs.size(); ++i) {
			values[i] = langs.get(i).getName();
		}
		return values;
	}

	public void addLanguage(View v) {
		startActivity(new Intent(this, AddLanguageActivity.class));
	}

	public void changeSettings(View v) {
		startActivity(new Intent(this, SettingsActivity.class));
	}


}
