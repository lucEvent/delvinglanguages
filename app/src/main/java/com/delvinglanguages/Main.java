package com.delvinglanguages;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.SubMenu;

import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.util.Languages;
import com.delvinglanguages.view.fragment.LanguageMainFragment;
import com.delvinglanguages.view.utils.LanguageListener;
import com.delvinglanguages.view.utils.LanguageHandler;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        LanguageListener {

    private enum MainOption {
        MAIN, CREATE_LANGUAGE, HISTORIAL, SETTINGS, ABOUT, PHONETIC
    }

    private enum MainFlag {
        FIRST_LAUNCH
    }

    private MainOption currentFragment, previousFragment;
    private MainFlag flag;

    private KernelManager dataManager;

    public static Handler handler;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        AppSettings.initialize(this);

        //   setTheme(AppSettings.getAppThemeResource());
        setContentView(R.layout.a_main);

        Main.handler = new LanguageHandler(this);
        dataManager = new KernelManager(this);
        previousFragment = MainOption.MAIN;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateLanguageList();

        Language currentLanguage = dataManager.getCurrentLanguage();
        if (currentLanguage == null) {
            if (dataManager.getNumLanguages() == 0) {
                //// TODO: 08/01/2016 Crear pantalla de primer inicio
                flag = MainFlag.FIRST_LAUNCH;
                setFragment(MainOption.CREATE_LANGUAGE);
                return;
            } else {
                currentLanguage = dataManager.getLanguages().get(0);
                AppSettings.setCurrentLanguage(currentLanguage.id);
            }
        }

        // TODO: 11/03/2016 Mejorar el rendimiento usando attach and dettach fragment????
        if (savedInstanceState != null) {
            setFragment((MainOption) savedInstanceState.get("fragment"));
        } else {
            currentFragment = MainOption.MAIN;
            getFragmentManager().beginTransaction().add(R.id.content, new LanguageMainFragment()).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putSerializable("fragment", currentFragment);
    }

    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.nav_create_language:
                setFragment(MainOption.CREATE_LANGUAGE);
                break;
            case R.id.nav_historial:
                setFragment(MainOption.HISTORIAL);
                break;
            case R.id.nav_app_settings:
                setFragment(MainOption.SETTINGS);
                break;
            case R.id.nav_about:
                setFragment(MainOption.ABOUT);
                break;
            default:
                AppSettings.setCurrentLanguage(item.getItemId());
                setFragment(MainOption.MAIN);
                item.setChecked(true);
                ((NavigationView) findViewById(R.id.nav_view)).setCheckedItem(item.getItemId());
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateLanguageList()
    {
        Languages languages = dataManager.getLanguages();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        SubMenu menu = navigationView.getMenu().findItem(R.id.nav_header_languages).getSubMenu();
        menu.clear();

        for (int i = 0; i < languages.size(); i++) {
            Language l = languages.get(i);
            MenuItem mi = menu.add(0, l.id, i, l.language_name);
            mi.setCheckable(true);
        }
        navigationView.invalidate();
    }

    @Override
    public void onLanguageCreatedOK(Object[] data)
    {
        Language language = dataManager.createLanguage((int) data[0], (String) data[1], (int) data[2]);

        AppSettings.setCurrentLanguage(language.id);

        setFragment(MainOption.MAIN);
        updateLanguageList();

        Snackbar.make(drawer, R.string.msg_language_created, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onLanguageCreatedCanceled()
    {
        if (flag == MainFlag.FIRST_LAUNCH) {
            //// TODO: 11/03/2016
        } else {
            setFragment(previousFragment);
        }
    }

    @Override
    public void onLanguageRemoved()
    {
        Languages languages = dataManager.getLanguages();
        if (languages.isEmpty()) {
            setFragment(MainOption.CREATE_LANGUAGE);
        } else {
            AppSettings.setCurrentLanguage(languages.get(0).id);
            setFragment(MainOption.MAIN);
        }
        updateLanguageList();
    }

    @Override
    public void onLanguageRecovered()
    {
        updateLanguageList();
    }

    @Override
    public void onLanguageNameChanged()
    {
        updateLanguageList();
    }

    @Override
    public void onMessage(int res_id)
    {
        Snackbar.make(drawer, res_id, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onMessage(String msg)
    {
        Snackbar.make(drawer, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onError()
    {
        AppSettings.printerror("Error in MAIN through handler", null);
    }

    private void setFragment(MainOption option)
    {
        Fragment fragment = null;
        String title = "Fragment not implemented";

        switch (option) {
            case MAIN:
                fragment = new LanguageMainFragment();
                title = dataManager.getCurrentLanguage().language_name;
                break;
            case CREATE_LANGUAGE:
                fragment = new com.delvinglanguages.view.fragment.CreateLanguageFragment();
                title = getString(R.string.create_language);
                break;
            case HISTORIAL:
                title = getString(R.string.historial);
                //      fragment = new HistorialFragment();
                Snackbar.make(drawer, R.string.msg_not_implemented, Snackbar.LENGTH_SHORT).show();
                return;//                break;
            case SETTINGS:
                fragment = new com.delvinglanguages.view.fragment.AppSettingsFragment();
                title = getString(R.string.settings);
                break;
            case ABOUT:
                fragment = new com.delvinglanguages.view.fragment.AboutFragment();
                title = getString(R.string.about);
                break;
            case PHONETIC:
                // title = getString(R.string.historial);
                // fragment = new HistorialFragment();
                Snackbar.make(drawer, R.string.msg_not_implemented, Snackbar.LENGTH_SHORT).show();
                return;//                break;
        }

        if (currentFragment == null)
            previousFragment = MainOption.MAIN;
        else
            previousFragment = currentFragment;

        currentFragment = option;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment);
        ft.commit();
        setTitle(title);
    }

}
