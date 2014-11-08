package com.delvinglanguages.face.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.debug.Debug;
import com.delvinglanguages.face.fragment.BinFragment;
import com.delvinglanguages.face.fragment.DictionaryFragment;
import com.delvinglanguages.face.fragment.LanguageFragment;
import com.delvinglanguages.face.fragment.PhrasalsFragment;
import com.delvinglanguages.face.fragment.PractiseFragment;
import com.delvinglanguages.face.fragment.SearchFragment;
import com.delvinglanguages.face.fragment.VerbsFragment;
import com.delvinglanguages.face.fragment.WarehouseFragment;
import com.delvinglanguages.face.settings.LanguageSettingsActivity;
import com.delvinglanguages.settings.Configuraciones;

public class LanguageActivity extends Activity {

	private static final String DEBUG = "##LanguageActivity##";

	private static final int REQUEST_REMOVE = 0;

	private static final int LANGUAGE = 0;
	private static final int PRACTISE = 1;
	private static final int DICTIONARY = 2;
	private static final int VERBS = 3;
	private static final int PHRASAL_VERBS = 4;
	private static final int WAREHOUSE = 5;
	private static final int BIN = 6;
	private static final int SEARCH = 7;

	private IDDelved idioma;

	private boolean actualPHMode;

	private View options, show_options;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_language_main);

		options = findViewById(R.id.langoptions);
		show_options = findViewById(R.id.open_opts);

		FrameLayout background = (FrameLayout) findViewById(R.id.background);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
			options.setBackgroundDrawable(Configuraciones.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
			options.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		idioma = ControlCore.getIdiomaActual(this);
		ControlCore.loadLanguage(true);

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.fragment, new LanguageFragment());
		ft.commit();
		setTitle(idioma.getName());
	}

	@Override
	protected void onResume() {
		super.onResume();
		actualPHMode = idioma.getSettings(IDDelved.MASK_PH);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.language, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_REMOVE) {
			if (resultCode == Activity.RESULT_OK) {
				finish();
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_langsettings:
			startActivityForResult(new Intent(this,
					LanguageSettingsActivity.class), REQUEST_REMOVE);
			return true;
		}
		return false;
	}

	private void setFragment(int position) {
		Fragment fragment = null;
		String title = null;
		switch (position) {
		case LANGUAGE:
			fragment = new LanguageFragment();
			title = idioma.getName();
			break;
		case PRACTISE:
			fragment = new PractiseFragment();
			title = getString(R.string.title_practising) + " "
					+ idioma.getName();
			break;
		case DICTIONARY:
			fragment = new DictionaryFragment();
			title = getString(R.string.title_list_selector);
			break;
		case VERBS:
			fragment = new VerbsFragment();
			title = idioma.getName() + "'s Verbs";
			break;
		case PHRASAL_VERBS:
			fragment = new PhrasalsFragment();
			title = getString(R.string.title_phrasals);
			break;
		case WAREHOUSE:
			fragment = new WarehouseFragment();
			title = idioma.getName() + " " + getString(R.string.warehouse);
			break;
		case BIN:
			fragment = new BinFragment();
			title = idioma.getName() + " " + getString(R.string.bin);
			break;
		case SEARCH:
			fragment = new SearchFragment();
			title = idioma.getName() + " " + getString(R.string.search);
			break;
		default:
			fragment = new LanguageFragment();
			title = idioma.getName();
		}

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.fragment, fragment);
		ft.commit();
		setTitle(title);
	}

	public void jumptoPractise(View v) {
		if (!idioma.hasEntries()) {
			showMessage(R.string.mssNoWords);
			return;
		}
		setFragment(PRACTISE);
		hideOptionsMenu(null);
	}

	public void jumptoDictionary(View v) {
		if (!idioma.hasEntries()) {
			showMessage(R.string.mssNoWordsToList);
			return;
		}
		setFragment(DICTIONARY);
		hideOptionsMenu(null);
	}

	public void jumptoLanguageMain(View v) {
		setFragment(LANGUAGE);
	}

	private Dialog dialog;

	public void jumptoOther(View v) {
		View view = getLayoutInflater().inflate(R.layout.d_other_langoptions,
				null);

		if (!actualPHMode) {
			((Button) view.findViewById(R.id.phrasal_verbs))
					.setVisibility(View.GONE);
		}
		dialog = new AlertDialog.Builder(this).setView(view).show();
		return;
	}

	public void jumptoWarehouse(View v) {
		setFragment(WAREHOUSE);
		hideOptionsMenu(null);
	}

	public void jumptoVerbs(View v) {
		setFragment(VERBS);
		dialog.dismiss();
		hideOptionsMenu(null);
	}

	public void jumptoPhrasalVerbs(View v) {
		setFragment(PHRASAL_VERBS);
		dialog.dismiss();
		hideOptionsMenu(null);
	}

	public void jumptoBin(View v) {
		if (idioma.getPapelera().size() <= 0) {
			showMessage(R.string.mssNoTrash);
			dialog.dismiss();
			return;
		}
		setFragment(BIN);
		dialog.dismiss();
		hideOptionsMenu(null);
	}

	public void jumptoSearch(View v) {
		setFragment(SEARCH);
		dialog.dismiss();
		hideOptionsMenu(null);
	}

	public void jumptoDebug(View v) {
		dialog.dismiss();
		hideOptionsMenu(null);
		startActivity(new Intent(this, Debug.class));
	}

	private void showMessage(int text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	public void showOptionsMenu(View v) {
		show_options.setVisibility(View.GONE);
		options.setVisibility(View.VISIBLE);
	}

	public void hideOptionsMenu(View v) {
		options.setVisibility(View.GONE);
		show_options.setVisibility(View.VISIBLE);
	}

}
