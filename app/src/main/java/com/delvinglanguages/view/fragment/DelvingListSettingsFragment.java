package com.delvinglanguages.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DelvingList;
import com.delvinglanguages.kernel.DelvingListManager;
import com.delvinglanguages.kernel.LanguageCode;
import com.delvinglanguages.view.activity.DelvingListMergerActivity;
import com.delvinglanguages.view.utils.DelvingListListener;

public class DelvingListSettingsFragment extends android.preference.PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int PREF_FROM_CODE = 0x1;
    private static final int PREF_TO_CODE = 0x2;
    private static final int PREF_NAME = 0x4;

    private String key_from_code, key_to_code, key_name, key_phrasal;

    private DelvingListManager dataManager;
    private DelvingList delvingList;

    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        dataManager = new DelvingListManager(getActivity());
        delvingList = dataManager.getCurrentList();

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        editor.putString((key_from_code = getString(R.string.pref_list_language_from_code_key)), Integer.toString(delvingList.from_code));
        editor.putString((key_to_code = getString(R.string.pref_list_language_to_code_key)), Integer.toString(delvingList.to_code));
        editor.putString((key_name = getString(R.string.pref_list_name_key)), delvingList.name);
        editor.putBoolean((key_phrasal = getString(R.string.pref_list_phrasal_key)), delvingList.arePhrasalVerbsEnabled());
        editor.apply();

        addPreferencesFromResource(R.xml.delving_list_preferences);

        PreferenceManager pm = getPreferenceManager();
        pm.findPreference(getString(R.string.pref_list_remove_key)).setOnPreferenceClickListener(onRemoveListAction);
        pm.findPreference(getString(R.string.pref_list_reset_statistics_key)).setOnPreferenceClickListener(onResetStatisticsAction);
        pm.findPreference(getString(R.string.pref_list_merge_key)).setOnPreferenceClickListener(onMergeAction);

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
        if (key.equals(key_from_code)) {

            int code = Integer.parseInt(sharedPreferences.getString(key, "0"));
            dataManager.updateListLanguageFromCode(code);
            setUpSummaries(PREF_FROM_CODE);

        } else if (key.equals(key_to_code)) {

            int code = Integer.parseInt(sharedPreferences.getString(key, "0"));
            dataManager.updateListLanguageToCode(code);
            setUpSummaries(PREF_TO_CODE);

        } else if (key.equals(key_name)) {

            String list_name = sharedPreferences.getString(key, "").trim();
            if (list_name.length() > 0) {
                dataManager.updateListName(list_name);
                setUpSummaries(PREF_NAME);
                getActivity().setResult(DelvingListListener.LIST_NAME_CHANGED);
                getActivity().setTitle(list_name);
            }

        } else if (key.equals(key_phrasal)) {

            boolean state = sharedPreferences.getBoolean(key, true);
            dataManager.phrasalVerbsStateChanged(state);
        }
    }

    private void setUpSummaries(int settings)
    {
        if ((PREF_FROM_CODE & settings) != 0) {

            ListPreference pref = (ListPreference) findPreference(key_from_code);
            pref.setSummary(pref.getEntry());
            pref.setIcon(LanguageCode.getFlagResId(Integer.parseInt(pref.getValue())));

        }
        if ((PREF_TO_CODE & settings) != 0) {

            ListPreference pref = (ListPreference) findPreference(key_to_code);
            pref.setSummary(pref.getEntry());
            pref.setIcon(LanguageCode.getFlagResId(Integer.parseInt(pref.getValue())));

        }
        if ((PREF_NAME & settings) != 0) {

            findPreference(key_name).setSummary(delvingList.name);

        }
    }

    private Preference.OnPreferenceClickListener onRemoveListAction = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference)
        {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.msg_confirm_to_remove)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            onRemoveListConfirmed();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
            return true;
        }
    };

    private void onRemoveListConfirmed()
    {
        dataManager.deleteCurrentList();
        getActivity().setResult(DelvingListListener.LIST_REMOVED);
        getActivity().finish();
    }

    private Preference.OnPreferenceClickListener onResetStatisticsAction = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference)
        {
            new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.msg_confirm_to_reset_statistics))
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

    private Preference.OnPreferenceClickListener onMergeAction = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference)
        {
            if (dataManager.getDelvingLists().size() == 1) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.msg_no_language_to_merge_with))
                        .setPositiveButton(R.string.ok, null)
                        .show();
            } else {
                Activity context = getActivity();
                context.startActivityForResult(new Intent(context, DelvingListMergerActivity.class), 0);
            }
            return true;
        }
    };

}
