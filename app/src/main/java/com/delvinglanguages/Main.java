package com.delvinglanguages;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.delvinglanguages.view.utils.AppCode;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private enum MainOption {
        MAIN, CREATE_LANGUAGE, HISTORIAL, SETTINGS, ABOUT, PHONETIC
    }

    private enum MainFlag {
        FIRST_LAUNCH
    }

    private MainOption currentFragment, previousFragment;
    private MainFlag flag;

    private Settings app_settings;
    private KernelManager dataManager;

    public static Handler handler;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        Main.handler = this.phandler;
        app_settings = new Settings(this);
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
                Settings.setCurrentLanguage(currentLanguage.id);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("fragment", currentFragment);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

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
                Settings.setCurrentLanguage(item.getItemId());
                setFragment(MainOption.MAIN);
                item.setChecked(true);
                ((NavigationView) findViewById(R.id.nav_view)).setCheckedItem(item.getItemId());
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateLanguageList() {
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

    private Handler phandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AppCode.LANGUAGE_CREATED_OK:

                    Object[] data = (Object[]) msg.obj;
                    Language language = dataManager.createLanguage((int) data[0], (String) data[1], (int) data[2]);

                    Settings.setCurrentLanguage(language.id);

                    setFragment(MainOption.MAIN);
                    updateLanguageList();

                    Snackbar.make(drawer, R.string.msg_language_created, Snackbar.LENGTH_SHORT).show();
                    break;
                case AppCode.LANGUAGE_CREATED_CANCELED:
                    if (flag == MainFlag.FIRST_LAUNCH) {
                        //// TODO: 11/03/2016
                    } else {
                        setFragment(previousFragment);
                    }
                    break;
                case AppCode.LANGUAGE_REMOVED:
                    Languages languages = dataManager.getLanguages();
                    if (languages.isEmpty()) {
                        setFragment(MainOption.CREATE_LANGUAGE);
                    } else {
                        Settings.setCurrentLanguage(languages.get(0).id);
                        setFragment(MainOption.MAIN);
                    }
                    updateLanguageList();
                    break;
                case AppCode.LANGUAGE_RECOVERED:
                    System.out.println("Language recovered receiving and handling it");
                case AppCode.LANGUAGE_NAME_CHANGED:
                    updateLanguageList();
                    break;
                case AppCode.MESSAGE_INT:
                    Snackbar.make(drawer, (int) msg.obj, Snackbar.LENGTH_SHORT).show();

                    break;
            }
        }
    };

    private void setFragment(MainOption option) {
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
                fragment = new com.delvinglanguages.view.fragment.SettingsFragment();
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

        if (currentFragment == null) {
            previousFragment = MainOption.MAIN;
        } else {
            previousFragment = currentFragment;
        }
        currentFragment = option;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment);
        ft.commit();
        setTitle(title);
    }

}
