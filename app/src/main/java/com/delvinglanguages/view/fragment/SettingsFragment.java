package com.delvinglanguages.view.fragment;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.delvinglanguages.R;
import com.delvinglanguages.Settings;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    /*
        R.string.pref_app_language
        R.string.pref_phonetic_keyboard_vibration
        R.string.pref_app_theme
     */
    private String[] languages;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preferences);

        languages = getResources().getStringArray(R.array.languages);
        setUpPreferenceSummaries();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getResources().getString(R.string.pref_app_language_key))) {

            int position = Integer.parseInt(sharedPreferences.getString(key, "0"));

            Preference app_name = findPreference(key);
            app_name.setSummary(languages[position]);
        }

    }

    private void setUpPreferenceSummaries() {
        Resources resources = getResources();

        Preference app_name = findPreference(resources.getString(R.string.pref_app_language_key));
        app_name.setSummary(languages[Settings.getAppLanguageCode()]);
    }

}