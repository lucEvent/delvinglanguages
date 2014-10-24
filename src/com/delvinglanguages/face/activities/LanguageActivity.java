package com.delvinglanguages.face.activities;

import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
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
import com.delvinglanguages.listers.OptionLister;
import com.delvinglanguages.settings.Configuraciones;

public class LanguageActivity extends FragmentActivity implements
		OnClickListener {

	private static final String DEBUG = "##LanguageActivity##";

	private IDDelved idioma;

	// private String actualLangName;

	private ViewAdapter sectionAdapter;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_pager);

		ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
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

		// Opciones Menu
		delv = (Button) findViewById(R.id.delv);
		delv.setOnClickListener(this);
		practise = (Button) findViewById(R.id.practise);
		practise.setOnClickListener(this);
		dictionary = (Button) findViewById(R.id.dictionary);
		dictionary.setOnClickListener(this);
		warehouse = (Button) findViewById(R.id.warehouse);
		warehouse.setOnClickListener(this);
		other = (Button) findViewById(R.id.other);
		other.setOnClickListener(this);
		
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

	private Button delv, practise, dictionary, warehouse, other;

	@Override
	public void onClick(View v) {
		if (v == delv) {

		} else if (v == practise) {
			if (!idioma.hasEntries()) {
				showMessage(R.string.mssNoWords);
				return;
			}

		} else if (v == dictionary) {
			if (!idioma.hasEntries()) {
				showMessage(R.string.mssNoWordsToList);
				return;
			}

		} else if (v == warehouse) {

		} else if (v == other) {
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
	}

	private void showMessage(int text) {
		Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
		toast.show();
	}

}
