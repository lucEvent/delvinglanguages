package com.delvinglanguages.face.activities;

import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.debug.Debug;
import com.delvinglanguages.debug.Inserter;
import com.delvinglanguages.face.langoptions.BinFragment;
import com.delvinglanguages.face.langoptions.LanguageFragment;
import com.delvinglanguages.face.langoptions.DictionaryFragment;
import com.delvinglanguages.face.langoptions.PhrasalsFragment;
import com.delvinglanguages.face.langoptions.PractiseFragment;
import com.delvinglanguages.face.langoptions.VerbsFragment;
import com.delvinglanguages.face.langoptions.WarehouseFragment;
import com.delvinglanguages.face.settings.LanguageSettingsFragment;
import com.delvinglanguages.listers.OptionLister;
import com.delvinglanguages.settings.Configuraciones;

public class LanguageActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private static final String DEBUG = "##LanguageActivity##";

	private IDDelved idioma;

	private String actualLangName;

	private TabViewAdapter sectionsAdapter;
	private ViewPager viewPager;
	private ActionBar actionBar;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_pager);

		idioma = ControlCore.getIdiomaActual(this);
		ControlCore.loadLanguage(true);

		viewPager = (ViewPager) findViewById(R.id.pager);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			viewPager.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			viewPager.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);

		sectionsAdapter = new TabViewAdapter(getSupportFragmentManager());

		viewPager.setAdapter(sectionsAdapter);

		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		Locale l = Locale.getDefault();
		String opts = getString(R.string.options).toUpperCase(l);
		String sett = getString(R.string.settings).toUpperCase(l);

		// Añadiendo los 3 TABS
		actionBar.addTab(actionBar.newTab().setText(opts).setTabListener(this));
		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.ic_launcher)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(sett).setTabListener(this));

		mainView = 0;
		update = false;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fTrans) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fTrans) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fTrans) {
	}

	public class TabViewAdapter extends FragmentStatePagerAdapter {

		public TabViewAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Log.d(DEBUG, "gettingItem " + position);
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new OptionsFragment();
				break;
			case 1:
				String title = null;
				switch (mainView) {
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
					title = idioma.getName() + " "
							+ getString(R.string.warehouse);
					break;
				case 6: // Bin
					fragment = new BinFragment();
					title = idioma.getName() + " " + getString(R.string.bin);
					break;
				}
				break;
			case 2:
				fragment = new LanguageSettingsFragment();
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

		public int getItemPosition(Object item) {
			if (update) {
				update = false;
				return POSITION_UNCHANGED;
			} else {
				return POSITION_NONE;
			}
		}

	}

	private int mainView;
	private boolean update;

	private void changeTab(int position) {
		mainView = position;
		viewPager.setCurrentItem(1);
		update = true;
		sectionsAdapter.notifyDataSetChanged();
	}

	public static class OptionsFragment extends ListFragment {

		private IDDelved idioma;
		private boolean actualPHMode;
		private String[] indexTitles, indexTitlesAdv;

		@SuppressWarnings("deprecation")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			idioma = ControlCore.getIdiomaActual(getActivity());
			indexTitles = getResources().getStringArray(R.array.lang_opt);
			indexTitles[0] = idioma.getName();
			indexTitlesAdv = getResources()
					.getStringArray(R.array.lang_opt_ext);
			indexTitlesAdv[0] = idioma.getName();

			View view = inflater.inflate(R.layout.a_simple_list, container,
					false);

			FrameLayout background = (FrameLayout) view
					.findViewById(R.id.background);
			int type_bg = Configuraciones.backgroundType();
			if (type_bg == Configuraciones.BG_IMAGE_ON) {
				background.setBackgroundDrawable(Configuraciones
						.getBackgroundImage());
			} else if (type_bg == Configuraciones.BG_COLOR_ON) {
				background.setBackgroundColor(Configuraciones
						.getBackgroundColor());
			}

			actualPHMode = idioma.getSettings(IDDelved.MASK_PH);
			String[] options = actualPHMode ? indexTitlesAdv : indexTitles;

			setListAdapter(new OptionLister(getActivity(), options));

			return view;
		}

		public void onListItemClick(ListView l, View v, int position, long id) {
			super.onListItemClick(l, v, position, id);

			if (!idioma.getSettings(IDDelved.MASK_PH)) {
				if (position > 3)
					position++;
			}
			switch (position) {
			case 1: // Pratise
				if (!idioma.hasEntries()) {
					showMessage(R.string.mssNoWords);
					return;
				}
				break;
			case 2: // Dictionary
				if (!idioma.hasEntries()) {
					showMessage(R.string.mssNoWordsToList);
					return;
				}
				break;
			case 6: // Bin
				if (idioma.getPapelera().size() <= 0) {
					showMessage(R.string.mssNoTrash);
					return;
				}
				break;
			case 7: // Debug
				startActivity(new Intent(getActivity(), Debug.class));
				return;
			}

			LanguageActivity act = (LanguageActivity) getActivity();
			act.changeTab(position);
		}

		private void showMessage(int text) {
			Toast toast = Toast
					.makeText(getActivity(), text, Toast.LENGTH_LONG);
			toast.show();
		}

	}

}
