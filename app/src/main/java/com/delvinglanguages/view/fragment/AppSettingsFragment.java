package com.delvinglanguages.view.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;

import com.delvinglanguages.AppSettings;
import com.delvinglanguages.Main;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.RecordManager;
import com.delvinglanguages.net.CredentialsManager;
import com.delvinglanguages.view.utils.LanguageListener;

public class AppSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private CredentialsManager credentialsManager;

    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preferences);

        setUpSummaries();

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
        if (key.equals(AppSettings.APP_LANGUAGE_CODE_KEY)) {
            ListPreference app_name = (ListPreference) findPreference(AppSettings.APP_LANGUAGE_CODE_KEY);
            app_name.setSummary(app_name.getEntry());

            RecordManager.appLanguageChanged(Integer.parseInt(app_name.getValue()));
        } else if (key.equals(AppSettings.PHONKB_VIBRATION_KEY))
            RecordManager.appKBVibrationStateChanged(((SwitchPreference) findPreference(AppSettings.PHONKB_VIBRATION_KEY)).isChecked());

        else if (key.equals(AppSettings.APP_THEME_KEY)) {
            ListPreference app_theme = (ListPreference) findPreference(AppSettings.APP_THEME_KEY);
            app_theme.setSummary(app_theme.getEntry());

            RecordManager.appThemeChanged(Integer.parseInt(app_theme.getValue()));
        }
    }

    private void setUpSummaries()
    {
        ListPreference app_name = (ListPreference) findPreference(AppSettings.APP_LANGUAGE_CODE_KEY);
        app_name.setSummary(app_name.getEntry());

        ListPreference app_theme = (ListPreference) findPreference(AppSettings.APP_THEME_KEY);
        app_theme.setSummary(app_theme.getEntry());
    }

    private Preference.OnPreferenceChangeListener onBackUpChangeListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue)
        {
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
                RecordManager.appOnlineBackUpStateChanged(true);

            } else {
                Main.handler.obtainMessage(LanguageListener.DISABLE_SYNCHRONIZATION).sendToTarget();
                RecordManager.appOnlineBackUpStateChanged(false);
            }
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