package com.delvinglanguages.face.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.debug.Debug;
import com.delvinglanguages.face.langoptions.BinFragment;
import com.delvinglanguages.face.langoptions.DictionaryFragment;
import com.delvinglanguages.face.langoptions.LanguageFragment;
import com.delvinglanguages.face.langoptions.PhrasalsFragment;
import com.delvinglanguages.face.langoptions.PractiseFragment;
import com.delvinglanguages.face.langoptions.VerbsFragment;
import com.delvinglanguages.face.langoptions.WarehouseFragment;
import com.delvinglanguages.face.settings.LanguageSettingsActivity;
import com.delvinglanguages.settings.Configuraciones;

public class LanguageActivity extends FragmentActivity {

	private static final String DEBUG = "##LanguageActivity##";

	private static final int LANGUAGE = 0;
	private static final int PRACTISE = 1;
	private static final int DICTIONARY = 2;
	private static final int VERBS = 3;
	private static final int PHRASAL_VERBS = 4;
	private static final int WAREHOUSE = 5;
	private static final int BIN = 6;
	private static final int _DEBUG_ = 7;

	private IDDelved idioma;

	private ViewAdapter sectionAdapter;
	private ViewPager viewPager;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_pager);

		viewPager = (ViewPager) findViewById(R.id.pager);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			viewPager.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			viewPager.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		idioma = ControlCore.getIdiomaActual(this);
		ControlCore.loadLanguage(true);

		sectionAdapter = new ViewAdapter(getSupportFragmentManager());
		viewPager.setAdapter(sectionAdapter);

		indexTitles = getResources().getStringArray(R.array.lang_opt);
		indexTitles[0] = idioma.getName();
		indexTitlesAdv = getResources().getStringArray(R.array.lang_opt_ext);
		indexTitlesAdv[0] = idioma.getName();

		actualPHMode = idioma.getSettings(IDDelved.MASK_PH);
		String[] options = actualPHMode ? indexTitlesAdv : indexTitles;
	}

	private boolean actualPHMode;
	private String[] indexTitles, indexTitlesAdv;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.language, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_langsettings:
			startActivity(new Intent(this, LanguageSettingsActivity.class));
			return true;
		}
		return false;
	}

	public class ViewAdapter extends FragmentStatePagerAdapter {

		public ViewAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			String title = null;
			switch (position) {
			case 0: // Language
				fragment = new LanguageFragment();
				title = idioma.getName();
				break;
			case 1: // Pratise
				fragment = new PractiseFragment();
				title = getString(R.string.title_practising) + " "
						+ idioma.getName();
				break;
			case 2: // Dictionary
				fragment = new DictionaryFragment();
				title = getString(R.string.title_list_selector);
				break;
			case 3: // Verbs
				fragment = new VerbsFragment();
				title = idioma.getName() + "'s Verbs";
				break;
			case 4: // Phrasal verbs
				fragment = new PhrasalsFragment();
				title = getString(R.string.title_phrasals);
				break;
			case 5: // Warehouse
				fragment = new WarehouseFragment();
				title = idioma.getName() + " " + getString(R.string.warehouse);
				break;
			case 6: // Bin
				fragment = new BinFragment();
				title = idioma.getName() + " " + getString(R.string.bin);
				break;
			default:
				fragment = new LanguageFragment();
				title = idioma.getName();
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}

	}

	public void jumptoPractise(View v) {
		if (!idioma.hasEntries()) {
			showMessage(R.string.mssNoWords);
			return;
		}
		viewPager.setCurrentItem(PRACTISE);
	}

	public void jumptoDictionary(View v) {
		if (!idioma.hasEntries()) {
			showMessage(R.string.mssNoWordsToList);
			return;
		}
		viewPager.setCurrentItem(DICTIONARY);
	}

	public void jumptoLanguageMain(View v) {
		viewPager.setCurrentItem(LANGUAGE);
	}

	public void jumptoOther(View v) {
		// Mostrar otras opciones: Bin, debug
		// Bin
		if (idioma.getPapelera().size() <= 0) {
			showMessage(R.string.mssNoTrash);
			return;
		}
		// Debug
		startActivity(new Intent(this, Debug.class));
		return;
	}

	public void jumptoWarehouse(View v) {
		viewPager.setCurrentItem(WAREHOUSE);

	}

	private void showMessage(int text) {
		Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
		toast.show();
	}

}
