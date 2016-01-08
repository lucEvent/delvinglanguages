package com.delvinglanguages.face.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.debug.Debug;
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
import com.delvinglanguages.listers.DrawerMainLister;
import com.delvinglanguages.settings.Settings;

public class Main extends FragmentActivity {

    private enum Option {
        LANGUAGE, PRACTISE, DICTIONARY, VERBS, PHRASAL_VERBS, WAREHOUSE, BIN, SEARCH, THEMES, PRONUNCIATION
    }

    private static KernelControl kernel;
    private static Settings settings;

    private Language currentLanguage;
    private boolean actualPHMode;
    private Option currentFragment;

    private static DrawerLayout mDrawerLayout;
    private static ListView drawerlist;
    private OptionsPadManager optionsPadManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        settings = new Settings(this);
        kernel = new KernelControl(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerlist = (ListView) findViewById(R.id.drawer);
        drawerlist.setAdapter(new DrawerMainLister(this, KernelControl.getLanguages()));
        drawerlist.setOnItemClickListener(null);

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

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("fragment", currentFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Settings.setBackgroundTo(findViewById(android.R.id.content));
        actualPHMode = currentLanguage.getSettings(Language.MASK_PH);
        ((DrawerMainLister) drawerlist.getAdapter()).clear(KernelControl.getLanguages());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppCode.FIRST_LAUNCH) {
            if (resultCode == RESULT_OK) {
                currentLanguage = KernelControl.setCurrentLanguage(0);
                setFragment(Option.LANGUAGE);
            }
            //// TODO: 08/01/2016  si no da OK
        }
        if (requestCode == AppCode.LANGUAGE_CREATED) {
            if (resultCode == RESULT_OK) {
                currentLanguage = KernelControl.setCurrentLanguage(KernelControl.getNumLanguages() - 1);
                setFragment(Option.LANGUAGE);
            }
        }
        if (requestCode == AppCode.STATE_CHANGED) {
            if (resultCode == AppCode.LANGUAGE_REMOVED) {
                int nLangs = KernelControl.getNumLanguages();
                if (nLangs != 0) {
                    currentLanguage = KernelControl.setCurrentLanguage(0);
                    setFragment(Option.LANGUAGE);
                } else {
                    //// TODO: 08/01/2016 Mostrar pantalla de primer inicio 
                }
            } else if (resultCode == AppCode.LANGUAGE_RECOVERED) {
                KernelControl.refreshData();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(drawerlist)) {
            mDrawerLayout.closeDrawer(drawerlist);
        } else if (currentFragment != Option.LANGUAGE) {
            setFragment(Option.LANGUAGE);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivityForResult(new Intent(this, LanguageSettingsActivity.class), AppCode.STATE_CHANGED);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
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
     * Options from Drawer
     */
    public void onCreateLanguageAction(View view) {
        mDrawerLayout.closeDrawer(drawerlist);
        startActivityForResult(new Intent(this, AddLanguageActivity.class), AppCode.LANGUAGE_CREATED);
    }

    public void onHistoryAction(View view) {
        mDrawerLayout.closeDrawer(drawerlist);
        startActivity(new Intent(this, HistorialActivity.class));
    }

    public void onConfigurationAction(View view) {
        mDrawerLayout.closeDrawer(drawerlist);
        startActivityForResult(new Intent(this, SettingsActivity.class), AppCode.STATE_CHANGED);
    }

    public void onAboutAction(View view) {
        mDrawerLayout.closeDrawer(drawerlist);
        startActivityForResult(new Intent(this, About.class), 0);
    }

    public void onLanguageSelectedAction(View view) {
        mDrawerLayout.closeDrawer(drawerlist);
        currentLanguage = (Language) view.getTag();
        KernelControl.setCurrentLanguage(currentLanguage);
        setFragment(Option.LANGUAGE);
    }

    /**
     * Options from Pad
     */
    public void jumptoPractise(View v) {
        if (!currentLanguage.isLoaded()) {
            showMessage(R.string.languageloading);
            return;
        }
        if (!currentLanguage.hasEntries()) {
            showMessage(R.string.mssNoWords);
            return;
        }
        setFragment(Option.PRACTISE);
        optionsPadManager.hideOptionsPad();
    }

    public void jumptoDictionary(View v) {
        if (!currentLanguage.isLoaded()) {
            showMessage(R.string.languageloading);
            return;
        }
        if (!currentLanguage.hasEntries()) {
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
            view.findViewById(R.id.phrasal_verbs).setVisibility(View.GONE);
        }
        dialog = new AlertDialog.Builder(this).setView(view).show();
    }

    public void jumptoWarehouse(View v) {
        if (!currentLanguage.isLoaded()) {
            showMessage(R.string.languageloading);
            return;
        }
        setFragment(Option.WAREHOUSE);
        optionsPadManager.hideOptionsPad();
    }

    public void jumptoVerbs(View v) {
        if (!currentLanguage.isLoaded()) {
            showMessage(R.string.languageloading);
            return;
        }
        setFragment(Option.VERBS);
        dialog.dismiss();
        optionsPadManager.hideOptionsPad();
    }

    public void jumptoPhrasalVerbs(View v) {
        if (!currentLanguage.isLoaded()) {
            showMessage(R.string.languageloading);
            return;
        }
        setFragment(Option.PHRASAL_VERBS);
        dialog.dismiss();
        optionsPadManager.hideOptionsPad();
    }

    public void jumptoBin(View v) {
        if (!currentLanguage.isLoaded()) {
            showMessage(R.string.languageloading);
            return;
        }
        if (currentLanguage.removed_words.size() <= 0) {
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
        if (!currentLanguage.isLoaded()) {
            showMessage(R.string.languageloading);
            return;
        }
        setFragment(Option.THEMES);
        dialog.dismiss();
        optionsPadManager.hideOptionsPad();
    }

    public void jumptoDebug(View v) {
        if (!currentLanguage.isLoaded()) {
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

    /*

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

*/

    private class OptionsPadManager implements View.OnTouchListener {

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

            if (view.getId() != R.id.touchpad)
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
            android.util.Log.d("##MAIN##", text);
    }

}
