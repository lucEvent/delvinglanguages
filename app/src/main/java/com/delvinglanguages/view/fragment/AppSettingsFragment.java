package com.delvinglanguages.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.delvinglanguages.AppSettings;
import com.delvinglanguages.R;

public class AppSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int PREF_MASK_APP_LANGUAGE_NAME = 0x01;
    private static final int PREF_MASK_APP_THEME = 0x02;

    private String[] themes;
    private String[] languages;

    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preferences);

        languages = getResources().getStringArray(R.array.languages);
        themes = getResources().getStringArray(R.array.themes);
        setUpPreferenceSummaries(0xff);
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

}