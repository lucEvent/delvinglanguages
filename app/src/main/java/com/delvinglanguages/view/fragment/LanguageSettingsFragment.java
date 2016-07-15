package com.delvinglanguages.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.LanguageManager;
import com.delvinglanguages.view.utils.LanguageListener;

public class LanguageSettingsFragment extends android.preference.PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    /*
        R.string.pref_language_code_key
        R.string.pref_language_name_key
        R.string.pref_language_phrasalverbs_key
        R.string.pref_language_merge_key
        R.string.pref_language_remove_key
        R.string.pref_language_reset_statistics_key
    */

    private static final int PREF_LANGUAGE_CODE = 0x1;
    private static final int PREF_LANGUAGE_NAME = 0x2;

    private String[] languages;
    private LanguageManager dataManager;
    private Language language;

    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        dataManager = new LanguageManager(getActivity());
        languages = getResources().getStringArray(R.array.languages);
        language = dataManager.getCurrentLanguage();

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        editor.putString(getString(R.string.pref_language_code_key), Integer.toString(language.CODE));
        editor.putString(getString(R.string.pref_language_name_key), language.language_name);
        editor.putBoolean(getString(R.string.pref_language_phrasalverbs_key), language.getSetting(Language.MASK_PHRASAL_VERBS));
        editor.apply();

        addPreferencesFromResource(R.xml.language_preferences);

        Preference pref_remove = getPreferenceManager().findPreference(getString(R.string.pref_language_remove_key));
        pref_remove.setOnPreferenceClickListener(onRemoveLanguageAction);
        Preference pref_reset_statistics = getPreferenceManager().findPreference(getString(R.string.pref_language_reset_statistics_key));
        pref_reset_statistics.setOnPreferenceClickListener(onResetStatisticsAction);

        setUpSummaries(0xFF);
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
        Resources resources = getResources();

        if (key.equals(resources.getString(R.string.pref_language_code_key))) {

            int code = Integer.parseInt(sharedPreferences.getString(key, "0"));
            dataManager.updateLanguage(code, language.language_name);
            setUpSummaries(PREF_LANGUAGE_CODE);

        } else if (key.equals(resources.getString(R.string.pref_language_name_key))) {

            String language_name = sharedPreferences.getString(key, "").trim();
            if (language_name.length() > 0) {
                dataManager.updateLanguage(language.CODE, language_name);
                setUpSummaries(PREF_LANGUAGE_NAME);
                getActivity().setResult(LanguageListener.LANGUAGE_NAME_CHANGED);
                getActivity().setTitle(language_name);
            }

        } else if (key.equals(resources.getString(R.string.pref_language_phrasalverbs_key))) {

            boolean phrasals = sharedPreferences.getBoolean(key, true);
            dataManager.updateLanguageSettings(phrasals, Language.MASK_PHRASAL_VERBS);
        }
    }

    private void setUpSummaries(int settings)
    {
        Resources resources = getResources();

        if ((PREF_LANGUAGE_CODE & settings) != 0) {

            findPreference(resources.getString(R.string.pref_language_code_key)).setSummary(languages[language.CODE]);

        }
        if ((PREF_LANGUAGE_NAME & settings) != 0) {

            findPreference(resources.getString(R.string.pref_language_name_key)).setSummary(language.language_name);

        }
    }

    private Preference.OnPreferenceClickListener onRemoveLanguageAction = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference)
        {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.msg_confirm_to_delete_xxx)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            onRemoveLanguageConfirmed();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
            return true;
        }
    };

    private void onRemoveLanguageConfirmed()
    {
        dataManager.deleteLanguage();
        getActivity().setResult(LanguageListener.LANGUAGE_REMOVED);
        getActivity().finish();
    }

    private Preference.OnPreferenceClickListener onResetStatisticsAction = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference)
        {
            new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.msg_confirm_to_reset_statistics, language.language_name))
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            onResetStatisticsConfirmed();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
            return true;
        }
    };

    private void onResetStatisticsConfirmed()
    {
        dataManager.resetStatistics();
    }

}
