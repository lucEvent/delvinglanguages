package com.delvinglanguages.face.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.face.AppCode;
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
import com.delvinglanguages.face.settings.About;
import com.delvinglanguages.face.settings.HistorialActivity;
import com.delvinglanguages.face.settings.LanguageSettingsActivity;
import com.delvinglanguages.face.settings.SettingsActivity;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.listers.LanguageLister;
import com.delvinglanguages.settings.Settings;

public class Main extends FragmentActivity implements AdapterView.OnItemClickListener {

    private enum Option {
        LANGUAGE, PRACTISE, DICTIONARY, VERBS, PHRASAL_VERBS, WAREHOUSE, BIN, SEARCH, THEMES, PRONUNCIATION
    }

    private static KernelControl kernel;
    private static Settings settings;

    private Language currentLanguage;
    private boolean actualPHMode;
    private Option currentFragment;

    private static DrawerLayout mDrawerLayout;
    private static ListView drawer;
    private OptionsPadManager optionsPadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        settings = new Settings(this);
        kernel = new KernelControl(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer = (ListView) findViewById(R.id.drawer);
        drawer.addHeaderView(getLayoutInflater().inflate(R.layout.v_main_drawer_header, null), null, false);
        drawer.setAdapter(new LanguageLister(this, KernelControl.getLanguages()));
        drawer.setOnItemClickListener(this);

        optionsPadManager = new OptionsPadManager();

        currentLanguage = KernelControl.getCurrentLanguage();

        if (currentLanguage == null) {
            if (KernelControl.getNumLanguages() == 0) {
                //// TODO: 08/01/2016 Crear pantalla de primer inicio
                startActivityForResult(new Intent(this, AddLanguageActivity.class), AppCode.FIRST_LAUNCH);
                return;
            } else {
                currentLanguage = KernelControl.setCurrentLanguage(0);
            }
        }

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
        Settings.setBackgroundTo(mDrawerLayout);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("fragment", currentFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualPHMode = currentLanguage != null && currentLanguage.getSettings(Language.MASK_PH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppCode.FIRST_LAUNCH:
                if (resultCode == RESULT_OK) {
                    currentLanguage = KernelControl.setCurrentLanguage(0);
                    setFragment(Option.LANGUAGE);
                    refreshLanguageList();
                }
                //// TODO: 08/01/2016  si no da OK
                break;
            case AppCode.LANGUAGE_CREATED:
                if (resultCode == RESULT_OK) {
                    currentLanguage = KernelControl.setCurrentLanguage(KernelControl.getNumLanguages() - 1);
                    setFragment(Option.LANGUAGE);
                    refreshLanguageList();
                }
                break;
            case AppCode.STATE_CHANGED:

                switch (resultCode) {
                    case AppCode.LANGUAGE_REMOVED:
                        int nLangs = KernelControl.getNumLanguages();
                        if (nLangs != 0) {
                            currentLanguage = KernelControl.setCurrentLanguage(0);
                            setFragment(Option.LANGUAGE);
                        } else {
                            //// TODO: 08/01/2016 Mostrar pantalla de primer inicio
                        }
                        refreshLanguageList();
                        break;
                    case AppCode.LANGUAGE_RECOVERED:
                        KernelControl.refreshData();
                        refreshLanguageList();
                        break;
                    case AppCode.BACKGROUND_CHANGED:
                        Settings.setBackgroundTo(mDrawerLayout);
                        optionsPadManager.refresh();
                        break;
                }
                break;
            default:
                debug("[Option not implemented] on Main.onActivityResult");
        }
    }

    private void refreshLanguageList() {
        ((LanguageLister) ((HeaderViewListAdapter) drawer.getAdapter())
                .getWrappedAdapter()).clear(KernelControl.getLanguages());
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(drawer)) {
            mDrawerLayout.closeDrawer(drawer);
        } else if (currentFragment != Option.LANGUAGE) {
            setFragment(Option.LANGUAGE);
        } else {
            finish();
        }
    }

    private void setFragment(Option option) {
        Fragment fragment;
        String title;
        switch (option) {
            case LANGUAGE:
                fragment = new LanguageFragment();
                title = currentLanguage.language_delved_name;
                break;
            case PRACTISE:
                fragment = new PractiseFragment();
                title = getString(R.string.title_practising) + " " + currentLanguage.language_delved_name;
                break;
            case DICTIONARY:
                fragment = new DictionaryFragment();
                title = getString(R.string.title_list_selector);
                break;
            case VERBS:
                fragment = new VerbsFragment();
                title = currentLanguage.language_delved_name + "'s Verbs";
                break;
            case PHRASAL_VERBS:
                fragment = new PhrasalsFragment();
                title = getString(R.string.title_phrasals);
                break;
            case WAREHOUSE:
                fragment = new WarehouseFragment();
                title = currentLanguage.language_delved_name + " " + getString(R.string.warehouse);
                break;
            case BIN:
                fragment = new BinFragment();
                title = currentLanguage.language_delved_name + " " + getString(R.string.bin);
                break;
            case SEARCH:
                fragment = new SearchFragment();
                title = currentLanguage.language_delved_name + " " + getString(R.string.search);
                break;
            case PRONUNCIATION:
                fragment = new PronunciationFragment();
                title = currentLanguage.language_delved_name + " " + getString(R.string.pronunciation);
                break;
            case THEMES:
                fragment = new ThemesFragment();
                title = currentLanguage.language_delved_name + " " + getString(R.string.themes);
                break;
            default:
                fragment = new LanguageFragment();
                title = currentLanguage.language_delved_name;
        }
        currentFragment = option;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, fragment);
        ft.commit();
        setTitle(title);
    }

    /**
     * App Drawer Navigation options
     */
    public void onNavigationAction(View view) {
        switch (view.getId()) {
            case R.id.nav_create_language:
                startActivityForResult(new Intent(this, AddLanguageActivity.class), AppCode.LANGUAGE_CREATED);
                break;
            case R.id.nav_history:
                startActivity(new Intent(this, HistorialActivity.class));
                break;
            case R.id.nav_configuration:
                startActivityForResult(new Intent(this, SettingsActivity.class), AppCode.STATE_CHANGED);
                break;
            case R.id.nav_about:
                startActivityForResult(new Intent(this, About.class), 0);
                break;
        }
        mDrawerLayout.closeDrawer(drawer);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < drawer.getHeaderViewsCount()) {
            return;
        }
        mDrawerLayout.closeDrawer(drawer);
        currentLanguage = KernelControl.setCurrentLanguage(position - drawer.getHeaderViewsCount());
        setFragment(Option.LANGUAGE);
    }

    private Dialog dialog;

    /**
     * Options from Pad
     */
    public void onOptionsPadAction(View view) {

        if (!currentLanguage.isLoaded()) {
            showMessage(R.string.languageloading);
            return;
        }
        switch (view.getId()) {
            case R.id.option_practise:
                if (!currentLanguage.hasEntries()) {
                    showMessage(R.string.mssNoWords);
                    return;
                }
                setFragment(Option.PRACTISE);
                break;
            case R.id.option_drawer:
                setFragment(Option.WAREHOUSE);
                break;
            case R.id.option_dictionary:
                if (!currentLanguage.hasEntries()) {
                    showMessage(R.string.mssNoWordsToList);
                    return;
                }
                setFragment(Option.DICTIONARY);
                break;
            case R.id.option_other:
                View d_view = getLayoutInflater().inflate(R.layout.d_other_langoptions, null);
                if (!actualPHMode) {
                    d_view.findViewById(R.id.option_phrasal_verbs).setVisibility(LinearLayout.GONE);
                }
                dialog = new AlertDialog.Builder(this).setView(d_view).show();
                return;
            case R.id.option_themes:
                setFragment(Option.THEMES);
                dialog.dismiss();
                break;
            case R.id.option_verbs:
                setFragment(Option.VERBS);
                dialog.dismiss();
                break;
            case R.id.option_phrasal_verbs:
                setFragment(Option.PHRASAL_VERBS);
                dialog.dismiss();
                break;
            case R.id.option_search:
                setFragment(Option.SEARCH);
                dialog.dismiss();
                break;
            case R.id.option_pronunciation:
                setFragment(Option.PRONUNCIATION);
                dialog.dismiss();
                break;
            case R.id.option_bin:
                if (currentLanguage.removed_words.size() <= 0) {
                    showMessage(R.string.mssNoTrash);
                    dialog.dismiss();
                    return;
                }
                setFragment(Option.BIN);
                dialog.dismiss();
                break;
            case R.id.option_settings:
                startActivityForResult(new Intent(this, LanguageSettingsActivity.class), AppCode.STATE_CHANGED);
                dialog.dismiss();
                break;
        }
        optionsPadManager.hideOptionsPad();
    }

    private void showMessage(int text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private class OptionsPadManager implements View.OnTouchListener {

        private View options;
        private TextView touchpad;
        private RelativeLayout layout;

        private FrameLayout.LayoutParams params;
        private RelativeLayout.LayoutParams touch_params;

        public OptionsPadManager() {

            layout = (RelativeLayout) findViewById(R.id.layout);
            touchpad = (TextView) findViewById(R.id.option_language_main);
            touchpad.setOnTouchListener(this);
            options = findViewById(R.id.options);

            params = (FrameLayout.LayoutParams) layout.getLayoutParams();
            touch_params = (RelativeLayout.LayoutParams) touchpad.getLayoutParams();

            Settings.setBackgroundTo(options);
        }

        public void refresh() {
            Settings.setBackgroundTo(options);
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

        /**
         * ************************* ON TOUCH ******************************
         **/
        private float touchY;
        private boolean goingUp;

        @Override
        public boolean onTouch(View view, MotionEvent event) {

            if (view.getId() != R.id.option_language_main)
                return false;

            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    int bottomMargin = getBottomMargin() - (int) (event.getRawY() - touchY);
                    if (bottomMargin > 0) {
                        bottomMargin = 0;
                    }
                    moveTo(bottomMargin);

                    goingUp = event.getRawY() < touchY;
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

    private static void debug(String text) {
        if (Settings.DEBUG)
            Log.d("##MAIN##", text);
    }

}
