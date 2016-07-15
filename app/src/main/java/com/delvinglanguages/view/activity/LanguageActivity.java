package com.delvinglanguages.view.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.LanguageManager;
import com.delvinglanguages.view.fragment.DrawerFragment;
import com.delvinglanguages.view.fragment.LanguageSettingsFragment;
import com.delvinglanguages.view.fragment.PractiseFragment;
import com.delvinglanguages.view.fragment.PronunciationFragment;
import com.delvinglanguages.view.fragment.RecycleBinFragment;
import com.delvinglanguages.view.fragment.ThemesFragment;
import com.delvinglanguages.AppCode;

public class LanguageActivity extends AppCompatActivity {

    public enum Option {
        PRACTISE, VERBS, PHRASAL_VERBS, DRAWER, RECYCLE_BIN, THEMES, PRONUNCIATION, SETTINGS
    }

    private Option currentOption;

    public static Handler handler;

    private LanguageManager dataManager;

    private Language currentLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_language);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataManager = new LanguageManager(this);
        currentLanguage = dataManager.getCurrentLanguage();

        currentOption = (Option) getIntent().getExtras().get(AppCode.FRAGMENT);
        setFragment(currentOption);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.delete_all:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.msg_confirm_to_clear_all_items)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                clearRecycleBin();
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .create().show();
                break;
        }
        return true;
    }

    private void setFragment(Option option)
    {
        Fragment fragment = null;
        String title = "Fragment not implemented";
        switch (option) {
            case PRACTISE:
                fragment = new PractiseFragment();
                title = getString(R.string.title_practise_fragment, currentLanguage.language_name);
                break;
            case DRAWER:
                fragment = new DrawerFragment();
                title = getString(R.string.title_drawer_fragment, currentLanguage.language_name);
                break;
            case THEMES:
                fragment = new ThemesFragment();
                title = getString(R.string.title_themes_fragment, currentLanguage.language_name);
                break;
            case PHRASAL_VERBS:
                //fragment = new PhrasalsFragment();
                //title = getString(R.string.title_phrasals);
                break;
            case VERBS:
                //fragment = new VerbsFragment();
                //title = currentLanguage.language_delved_name + "'s Verbs";
                break;
            case PRONUNCIATION:
                fragment = new PronunciationFragment();
                title = getString(R.string.title_pronunciation_fragment, currentLanguage.language_name);
                break;
            case RECYCLE_BIN:
                fragment = new RecycleBinFragment();
                title = getString(R.string.title_recyclebin_fragment, currentLanguage.language_name);
                break;
            case SETTINGS:
                fragment = new LanguageSettingsFragment();
                title = getString(R.string.title_language_settings_fragment, currentLanguage.language_name);
                break;
        }

        currentOption = option;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment);
        ft.commit();
        setTitle(title);
    }

    private void clearRecycleBin()
    {
        dataManager.deleteAllRemovedReferences();
        finish();
    }

}
