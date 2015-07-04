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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.debug.Debug;
import com.delvinglanguages.face.fragment.BinFragment;
import com.delvinglanguages.face.fragment.DictionaryFragment;
import com.delvinglanguages.face.fragment.LanguageFragment;
import com.delvinglanguages.face.fragment.PhrasalsFragment;
import com.delvinglanguages.face.fragment.PractiseFragment;
import com.delvinglanguages.face.fragment.PronunciationFragment;
import com.delvinglanguages.face.fragment.SearchFragment;
import com.delvinglanguages.face.fragment.ThemesFragment;
import com.delvinglanguages.face.fragment.VerbsFragment;
import com.delvinglanguages.face.fragment.WarehouseFragment;
import com.delvinglanguages.face.settings.LanguageSettingsActivity;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.settings.Settings;

public class LanguageActivity extends Activity {

	private static final int REQUEST_REMOVE = 0;

	private static enum Option {
		LANGUAGE, PRACTISE, DICTIONARY, VERBS, PHRASAL_VERBS, WAREHOUSE, BIN, SEARCH, THEMES, PRONUNCIATION
	};

	private Language idioma;

	private boolean actualPHMode;

	private Option currentFragment;

	private OptionsPadManager optionsPadManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_language_main);
		Settings.setBackgroundTo(findViewById(R.id.options));

		optionsPadManager = new OptionsPadManager();

		idioma = KernelControl.getCurrentLanguage();
		//
		// Mejorar el rendimiento usando attach and dettach fragment????
		//

		if (savedInstanceState != null) {
			setFragment((Option) savedInstanceState.get("fragment"));
			optionsPadManager.hideOptionsPad();
		} else {
			currentFragment = Option.LANGUAGE;
			getFragmentManager().beginTransaction().add(R.id.fragment, new LanguageFragment()).commit();
		}

		setTitle(idioma.getName());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("fragment", currentFragment);
	}

	@Override
	protected void onResume() {
		super.onResume();
		actualPHMode = idioma.getSettings(Language.MASK_PH);
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
			if (resultCode == RESULT_OK) {
				finish();
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_langsettings:
			startActivityForResult(new Intent(this, LanguageSettingsActivity.class), REQUEST_REMOVE);
			return true;
		}
		return false;
	}

	private void setFragment(Option option) {
		Fragment fragment = null;
		String title = null;
		switch (option) {
		case LANGUAGE:
			fragment = new LanguageFragment();
			title = idioma.getName();
			break;
		case PRACTISE:
			fragment = new PractiseFragment();
			title = getString(R.string.title_practising) + " " + idioma.getName();
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
		case PRONUNCIATION:
			fragment = new PronunciationFragment();
			title = idioma.getName() + " " + getString(R.string.pronunciation);
			break;
		case THEMES:
			fragment = new ThemesFragment();
			title = idioma.getName() + " " + getString(R.string.themes);
			break;
		default:
			fragment = new LanguageFragment();
			title = idioma.getName();
		}
		currentFragment = option;
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.fragment, fragment);
		ft.commit();
		setTitle(title);
	}

	public void jumptoPractise(View v) {
		if (!idioma.isLoaded()) {
			showMessage(R.string.languageloading);
			return;
		}
		if (!idioma.hasEntries()) {
			showMessage(R.string.mssNoWords);
			return;
		}
		setFragment(Option.PRACTISE);
		optionsPadManager.hideOptionsPad();
	}

	public void jumptoDictionary(View v) {
		if (!idioma.isLoaded()) {
			showMessage(R.string.languageloading);
			return;
		}
		if (!idioma.hasEntries()) {
			showMessage(R.string.mssNoWordsToList);
			return;
		}
		setFragment(Option.DICTIONARY);
		optionsPadManager.hideOptionsPad();
	}

	public void jumptoLanguageMain(View v) {
		setFragment(Option.LANGUAGE);
	}

	private Dialog dialog;

	public void jumptoOther(View v) {
		View view = getLayoutInflater().inflate(R.layout.d_other_langoptions, null);
		if (!actualPHMode) {
			((Button) view.findViewById(R.id.phrasal_verbs)).setVisibility(View.GONE);
		}
		dialog = new AlertDialog.Builder(this).setView(view).show();
		return;
	}

	public void jumptoWarehouse(View v) {
		if (!idioma.isLoaded()) {
			showMessage(R.string.languageloading);
			return;
		}
		setFragment(Option.WAREHOUSE);
		optionsPadManager.hideOptionsPad();
	}

	public void jumptoVerbs(View v) {
		if (!idioma.isLoaded()) {
			showMessage(R.string.languageloading);
			return;
		}
		setFragment(Option.VERBS);
		dialog.dismiss();
		optionsPadManager.hideOptionsPad();
	}

	public void jumptoPhrasalVerbs(View v) {
		if (!idioma.isLoaded()) {
			showMessage(R.string.languageloading);
			return;
		}
		setFragment(Option.PHRASAL_VERBS);
		dialog.dismiss();
		optionsPadManager.hideOptionsPad();
	}

	public void jumptoBin(View v) {
		if (!idioma.isLoaded()) {
			showMessage(R.string.languageloading);
			return;
		}
		if (idioma.getRemovedWords().size() <= 0) {
			showMessage(R.string.mssNoTrash);
			dialog.dismiss();
			return;
		}
		setFragment(Option.BIN);
		dialog.dismiss();
		optionsPadManager.hideOptionsPad();
	}

	public void jumptoSearch(View v) {
		setFragment(Option.SEARCH);
		dialog.dismiss();
		optionsPadManager.hideOptionsPad();
	}

	public void jumptoPronunciation(View v) {
		setFragment(Option.PRONUNCIATION);
		dialog.dismiss();
		optionsPadManager.hideOptionsPad();
	}

	public void jumptoThemes(View v) {
		if (!idioma.isLoaded()) {
			showMessage(R.string.languageloading);
			return;
		}
		setFragment(Option.THEMES);
		dialog.dismiss();
		optionsPadManager.hideOptionsPad();
	}

	public void jumptoDebug(View v) {
		if (!idioma.isLoaded()) {
			showMessage(R.string.languageloading);
			return;
		}
		dialog.dismiss();
		optionsPadManager.hideOptionsPad();
		startActivity(new Intent(this, Debug.class));
	}

	private void showMessage(int text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	private void debug(String text) {
		if (Settings.DEBUG)
			android.util.Log.d("##LanguageActivity##", text);
	}

	private class OptionsPadManager implements OnTouchListener {

		private View options;
		private TextView touchpad;
		private RelativeLayout layout;

		private FrameLayout.LayoutParams params;
		private RelativeLayout.LayoutParams touch_params;

		public OptionsPadManager() {

			layout = (RelativeLayout) findViewById(R.id.layout);
			touchpad = (TextView) findViewById(R.id.touchpad);
			touchpad.setOnTouchListener(this);
			options = findViewById(R.id.options);

			params = (FrameLayout.LayoutParams) layout.getLayoutParams();
			touch_params = (RelativeLayout.LayoutParams) touchpad.getLayoutParams();
		}

		public int getBottomMargin() {
			return params.bottomMargin;
		}

		public void moveTo(int bottomMargin) {
			params.bottomMargin = bottomMargin;
			layout.setLayoutParams(params);
		}

		public void showOptionsPad() {
			moveTo(0);
			touchpad.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_down_float);

			touch_params.height = (int) (getResources().getDisplayMetrics().density * 65 + 0.5f);
			touchpad.setLayoutParams(touch_params);
		}

		public void hideOptionsPad() {
			touchpad.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_up_float);
			moveTo(-options.getHeight());

			touch_params.height = (int) (getResources().getDisplayMetrics().density * 15 + 0.5f);
			touchpad.setLayoutParams(touch_params);
		}

		/** ************************** ON TOUCH ****************************** **/
		private float touchY;
		private boolean goingUp;

		@Override
		public boolean onTouch(View view, MotionEvent event) {

			if (view.getId() != R.id.touchpad)
				return false;

			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				int bottomMargin = getBottomMargin() - (int) (event.getRawY() - touchY);
				if (bottomMargin > 0) {
					bottomMargin = 0;
				}
				moveTo(bottomMargin);

				goingUp = event.getRawY() < touchY ? true : false;
				break;
			case MotionEvent.ACTION_UP:
				if (goingUp) {
					showOptionsPad();
				} else {
					hideOptionsPad();
				}
				break;
			case MotionEvent.ACTION_DOWN:
				break;
			}
			touchY = event.getRawY();
			return true;
		}

	}

}
