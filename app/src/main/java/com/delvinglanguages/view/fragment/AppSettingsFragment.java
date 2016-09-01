package com.delvinglanguages.view.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;

import com.delvinglanguages.AppSettings;
import com.delvinglanguages.Main;
import com.delvinglanguages.R;
import com.delvinglanguages.net.CredentialsManager;
import com.delvinglanguages.view.utils.LanguageListener;

public class AppSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int PREF_MASK_APP_LANGUAGE_NAME = 0x01;
    private static final int PREF_MASK_APP_THEME = 0x02;

    private String[] themes;
    private String[] languages;

    private CredentialsManager credentialsManager;

    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preferences);

        languages = getResources().getStringArray(R.array.languages);
        themes = getResources().getStringArray(R.array.themes);
        setUpPreferenceSummaries(0xff);

        findPreference(AppSettings.ONLINE_BACKUP)
                .setOnPreferenceChangeListener(onBackUpChangeListener);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {

        if (key.equals(AppSettings.APP_LANGUAGE_CODE_KEY))
            setUpPreferenceSummaries(PREF_MASK_APP_LANGUAGE_NAME);

        else if (key.equals(AppSettings.APP_THEME_KEY))
            setUpPreferenceSummaries(PREF_MASK_APP_THEME);
    }

    private void setUpPreferenceSummaries(int mask)
    {
        if ((mask & PREF_MASK_APP_LANGUAGE_NAME) != 0) {
            Preference app_name = findPreference(AppSettings.APP_LANGUAGE_CODE_KEY);
            app_name.setSummary(languages[AppSettings.getAppLanguageCode()]);
        }
        if ((mask & PREF_MASK_APP_THEME) != 0) {
            Preference app_theme = findPreference(AppSettings.APP_THEME_KEY);
            app_theme.setSummary(themes[AppSettings.getAppThemeCode()]);
        }
    }

    private Preference.OnPreferenceChangeListener onBackUpChangeListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue)
        {
            System.out.println("onPreferenceChange!! with:" + ((boolean) newValue));
            if ((boolean) newValue) {
                if (credentialsManager == null)
                    credentialsManager = new CredentialsManager(getActivity());

                if (!credentialsManager.hasPermissions()) {

                    credentialsManager.askPermissions(AppSettingsFragment.this);

                    return false;

                } else if (!credentialsManager.hasCredentials()) {
                    credentialsManager.askCredentials(AppSettingsFragment.this);

                    return false;
                }
                Main.handler.obtainMessage(LanguageListener.ENABLE_SYNCHRONIZATION).sendToTarget();

            } else
                Main.handler.obtainMessage(LanguageListener.DISABLE_SYNCHRONIZATION).sendToTarget();

            return true;
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        credentialsManager.onActivityResult(requestCode, resultCode, data);

        if (credentialsManager.hasCredentials()) {
            ((SwitchPreference) findPreference(AppSettings.ONLINE_BACKUP)).setChecked(true);
            Main.handler.obtainMessage(LanguageListener.ENABLE_SYNCHRONIZATION).sendToTarget();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        credentialsManager.onRequestPermissionsResult(requestCode, grantResults);

        if (credentialsManager.hasPermissions())
            if (credentialsManager.hasCredentials()) {
                ((SwitchPreference) findPreference(AppSettings.ONLINE_BACKUP)).setChecked(true);
                Main.handler.obtainMessage(LanguageListener.ENABLE_SYNCHRONIZATION).sendToTarget();
            } else
                credentialsManager.askCredentials(this);
    }

}