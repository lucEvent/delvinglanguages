package com.delvinglanguages.face.activity;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.face.settings.SettingsActivity;
import com.delvinglanguages.listers.LanguageLister;
import com.delvinglanguages.settings.Configuraciones;

public class Main extends ListActivity implements OnClickListener {

	private static final String DEBUG = "##Main##";

	private ControlCore core;
	private Configuraciones settings;

	private LinearLayout addLang, changeSett;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		core = new ControlCore(this);
		settings = new Configuraciones();

		setContentView(R.layout.a_main);

		addLang = (LinearLayout) findViewById(R.id.addlanguage);
		changeSett = (LinearLayout) findViewById(R.id.settings);

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();

		int type_bg = Configuraciones.backgroundType();
		FrameLayout background = (FrameLayout) findViewById(R.id.background);
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}
		ListView list = getListView();
		RelativeLayout fsteps = (RelativeLayout) findViewById(R.id.mainfirststeps);

		View toinvis, tovisib = null;

		if (ControlCore.getCount() == 0) {
			addLang.setOnClickListener(this);
			changeSett.setOnClickListener(this);
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

	/** *************** METODOS DE MENÚ *************** **/

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

	@Override
	public void onClick(View v) {
		if (v == addLang) {
			startActivity(new Intent(this, AddLanguageActivity.class));
		} else if (v == changeSett) {
			startActivity(new Intent(this, SettingsActivity.class));
		}
	}

}
