package com.delvinglanguages;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.util.Languages;
import com.delvinglanguages.view.activity.BackUpActivity;
import com.delvinglanguages.view.activity.CreateLanguageActivity;
import com.delvinglanguages.view.activity.StartActivity;
import com.delvinglanguages.view.fragment.FragmentManager;
import com.delvinglanguages.view.fragment.LanguageMainFragment;
import com.delvinglanguages.view.utils.LanguageHandler;
import com.delvinglanguages.view.utils.LanguageListener;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        LanguageListener {

    private enum MainState {
        FIRST_LAUNCH, OTHERWISE
    }

    private int currentItemSelected;
    private MainState state;

    private KernelManager dataManager;
    private FragmentManager fragmentManager;

    public static Handler handler;

    private DrawerLayout drawer;

    private LanguageMainFragment languageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        AppSettings.initialize(this);
        AppData.initialize(this);

        //   setTheme(AppSettings.getAppThemeResource());
        setContentView(R.layout.a_main);

        Main.handler = new LanguageHandler(this);
        dataManager = new KernelManager(this);
        dataManager.synchronize();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = new FragmentManager(this, navigationView, R.id.content);

        setUpDrawer();
        updateLanguageList();

        languageFragment = new LanguageMainFragment();

        Language currentLanguage = dataManager.getCurrentLanguage();
        if (currentLanguage == null) {
            if (dataManager.getNumberOfLanguages() == 0) {
                state = MainState.FIRST_LAUNCH;
                startActivityForResult(new Intent(this, StartActivity.class), 0);
                return;
            } else {
                currentLanguage = dataManager.getLanguages().first();
                AppSettings.setCurrentLanguage(currentLanguage.id);
            }
        }

        if (savedInstanceState != null)
            navigateTo((Integer) savedInstanceState.get("currentItemSelected"));
        else
            displayFirstFragment(currentLanguage);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putSerializable("currentItemSelected", currentItemSelected);
    }

    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
   /*     else if (fragmentManager.currentFragment instanceof OnBackPressedListener
                && ((OnBackPressedListener) fragmentManager.currentFragment).onBackPressed()) {
            // do nothing
        } */
        else if (fragmentManager.getBackStackEntryCount() > 0) {

            fragmentManager.popToFirst();

        } else super.onBackPressed();
    }

    public void onDrawerActionBarItemSelected(View v)
    {
        navigateTo(v.getId());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        return navigateTo(item.getItemId());
    }

    private boolean navigateTo(int where)
    {
        Fragment fragment;
        String title;

        switch (where) {
            case R.id.nav_create_language:
                startActivityForResult(new Intent(this, CreateLanguageActivity.class), 0);
                return true;
            case R.id.nav_historial:
                title = getString(R.string.historial);
                //      fragment = new HistorialFragment();
                Snackbar.make(drawer, R.string.msg_not_implemented, Snackbar.LENGTH_SHORT).show();
                return false;//   break;
            case R.id.nav_app_settings:
                fragment = new com.delvinglanguages.view.fragment.AppSettingsFragment();
                title = getString(R.string.settings);
                break;
            case R.id.nav_about:
                fragment = new com.delvinglanguages.view.fragment.AboutFragment();
                title = getString(R.string.about);
                break;
            default:
                AppSettings.setCurrentLanguage(where);
                fragment = languageFragment;
                languageFragment.invalidate();
                title = dataManager.getCurrentLanguage().language_name;

        }
        drawer.closeDrawer(GravityCompat.START);

        if (fragment instanceof LanguageMainFragment) {

            fragmentManager.setNavigationItemId(0, where);

            if (fragmentManager.currentFragment != languageFragment)
                fragmentManager.popToFirst();

            else
                fragmentManager.updateCheckedItem(where, currentItemSelected);

        } else {
            fragmentManager.replaceFragment(fragment, where, true);
            fragmentManager.updateCheckedItem(where, currentItemSelected);
        }

        currentItemSelected = where;

        setTitle(title);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case AppCode.ACTION_CREATE:

                startActivityForResult(new Intent(this, CreateLanguageActivity.class), 0);

                break;
            case AppCode.RESULT_LANGUAGE_CREATED:

                updateLanguageList();

                state = MainState.OTHERWISE;

                Language language = dataManager.getLanguages().last();

                if (fragmentManager.getBackStackEntryCount() == 0) {
                    displayFirstFragment(language);
                    languageFragment.invalidate();
                    setTitle(dataManager.getLanguages().first().language_name);
                } else
                    navigateTo(language.id);

                Snackbar.make(drawer, R.string.msg_language_created, Snackbar.LENGTH_SHORT).show();

                break;
            case AppCode.RESULT_LANGUAGE_CREATED_CANCELED:

                if (state == MainState.FIRST_LAUNCH)
                    startActivityForResult(new Intent(this, StartActivity.class), 0);

                break;

            case AppCode.ACTION_IMPORT:

                startActivityForResult(new Intent(this, BackUpActivity.class).setAction(BackUpActivity.ACTION_IMPORT), 0);

                break;
            case AppCode.RESULT_IMPORT_DONE:

                if (dataManager.getCurrentLanguage() == null)
                    displayFirstFragment(dataManager.getLanguages().first());

                break;
            case AppCode.RESULT_IMPORT_CANCELED:

                if (state == MainState.FIRST_LAUNCH)
                    startActivityForResult(new Intent(this, StartActivity.class), 0);

                break;
            case AppCode.RESULT_SYNC_DONE:

                updateLanguageList();

                Language currentLanguage = dataManager.getCurrentLanguage();
                if (currentLanguage == null)
                    currentLanguage = dataManager.getLanguages().first();

                displayFirstFragment(currentLanguage);
                break;
            case AppCode.START_ABORTED:
                finish();
                break;
        }
    }

    private void displayFirstFragment(Language language)
    {
        AppSettings.setCurrentLanguage(language.id);
        currentItemSelected = language.id;
        fragmentManager.addFragment(languageFragment, language.id);
    }

    private void setUpDrawer()
    {
        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);

        header.findViewById(R.id.nav_create_language).setOnLongClickListener(onDrawerActionBarButtonLongClick);
        header.findViewById(R.id.nav_historial).setOnLongClickListener(onDrawerActionBarButtonLongClick);
        header.findViewById(R.id.nav_app_settings).setOnLongClickListener(onDrawerActionBarButtonLongClick);
        header.findViewById(R.id.nav_about).setOnLongClickListener(onDrawerActionBarButtonLongClick);
    }

    private View.OnLongClickListener onDrawerActionBarButtonLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v)
        {
            int msg = -1;
            switch (v.getId()) {
                case R.id.nav_create_language:
                    msg = R.string.create_language;
                    break;
                case R.id.nav_historial:
                    msg = R.string.historial;
                    break;
                case R.id.nav_app_settings:
                    msg = R.string.settings;
                    break;
                case R.id.nav_about:
                    msg = R.string.about;
            }

            int x = v.getLeft();
            int y = v.getTop() + 2 * v.getHeight();
            Toast toast = Toast.makeText(v.getContext(), msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.START, x, y);
            toast.show();

            return true;
        }
    };

    private void updateLanguageList()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();

        menu.removeGroup(R.id.group_languages);

        Languages languages = dataManager.getLanguages();
        for (Language language : languages) {
            MenuItem mi = menu.add(R.id.group_languages, language.id, 1, language.language_name);
            mi.setCheckable(true);
        }

        navigationView.invalidate();
    }

    @Override
    public void onLanguageRemoved()
    {
        updateLanguageList();

        Language next = dataManager.getLanguages().first();
        if (next == null) {
            state = MainState.FIRST_LAUNCH;
            startActivityForResult(new Intent(this, StartActivity.class), 0);
        } else
            navigateTo(next.id);
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
    public void onSynchronizationStateChanged(boolean enabled)
    {
        if (enabled) {
            dataManager.setState();
            dataManager.synchronize();
        } else {
            dataManager.stopSynchronize();
        }
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

}
