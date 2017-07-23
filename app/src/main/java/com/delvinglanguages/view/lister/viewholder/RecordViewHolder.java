package com.delvinglanguages.view.lister.viewholder;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.delvinglanguages.AppData;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.LanguageCode;
import com.delvinglanguages.kernel.LanguageManager;
import com.delvinglanguages.kernel.record.AppSettingsRecord;
import com.delvinglanguages.kernel.record.LanguageRecord;
import com.delvinglanguages.kernel.record.LanguageSettingsRecord;
import com.delvinglanguages.kernel.record.Record;

public class RecordViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private ImageView main_icon, secondary_icon;
    private TextView title, description;

    public RecordViewHolder(View v)
    {
        super(v);
        view = v;
        main_icon = (ImageView) view.findViewById(R.id.main_icon);
        secondary_icon = (ImageView) view.findViewById(R.id.secondary_icon);
        title = (TextView) view.findViewById(R.id.title);
        description = (TextView) view.findViewById(R.id.description);
    }

    public static void populateViewHolder(RecordViewHolder holder, Record record)
    {
        Resources res = holder.view.getResources();
        int main_icon = -1, secondary_icon = -1;
        String title = "........", description = "........";

        if (record instanceof LanguageRecord) {
            LanguageRecord lr = (LanguageRecord) record;

            String list_name = LanguageManager.getLanguageName(lr.language_id);
            title = res.getString(R.string.h_in, (list_name != null ? list_name : res.getString(R.string.h_deleted_list)));

            switch (record.type) {
                case Record.DRAWERWORD_ADDED:
                    main_icon = R.drawable.ic_drawer;
                    secondary_icon = R.drawable.ic_add;
                    description = res.getString(R.string.h_drawer_added, lr.getNumber());
                    break;
                case Record.DRAWERWORD_DELETED:
                    main_icon = R.drawable.ic_drawer;
                    secondary_icon = R.drawable.ic_delete;
                    description = res.getString(R.string.h_drawer_deleted, lr.getNumber());
                    break;
                case Record.REFERENCE_CREATED:
                    main_icon = R.drawable.ic_dictionary;
                    secondary_icon = R.drawable.ic_add;
                    description = res.getString(R.string.h_reference_added, lr.getNumber());
                    break;
                case Record.REFERENCE_MODIFIED:
                    main_icon = R.drawable.ic_dictionary;
                    secondary_icon = R.drawable.ic_edit;
                    description = res.getString(R.string.h_reference_modified, lr.getNumber());
                    break;
                case Record.REFERENCE_REMOVED:
                    main_icon = R.drawable.ic_dictionary;
                    secondary_icon = R.drawable.ic_delete;
                    description = res.getString(R.string.h_reference_removed, lr.getNumber());
                    break;
                case Record.REFERENCE_RECOVERED:
                    main_icon = R.drawable.ic_dictionary;
                    secondary_icon = R.drawable.ic_undo;
                    description = res.getString(R.string.h_reference_recovered, lr.getNumber());
                    break;
                case Record.THEME_CREATED:
                    main_icon = R.drawable.ic_theme;
                    secondary_icon = R.drawable.ic_add;
                    description = res.getString(R.string.h_theme_added, lr.getNumber());
                    break;
                case Record.THEME_MODIFIED:
                    main_icon = R.drawable.ic_theme;
                    secondary_icon = R.drawable.ic_edit;
                    description = res.getString(R.string.h_theme_modified, lr.getNumber());
                    break;
                case Record.THEME_REMOVED:
                    main_icon = R.drawable.ic_theme;
                    secondary_icon = R.drawable.ic_delete;
                    description = res.getString(R.string.h_theme_removed, lr.getNumber());
                    break;
                case Record.THEME_RECOVERED:
                    main_icon = R.drawable.ic_theme;
                    secondary_icon = R.drawable.ic_undo;
                    description = res.getString(R.string.h_theme_recovered, lr.getNumber());
                    break;
                case Record.TEST_CREATED:
                    main_icon = R.drawable.ic_test;
                    secondary_icon = R.drawable.ic_add;
                    description = res.getString(R.string.h_test_added, lr.getNumber());
                    break;
                case Record.TEST_DONE:
                    main_icon = R.drawable.ic_test;
                    secondary_icon = R.drawable.ic_play;
                    description = res.getString(R.string.h_test_done, lr.getNumber());
                    break;
                case Record.TEST_REMOVED:
                    main_icon = R.drawable.ic_test;
                    secondary_icon = R.drawable.ic_delete;
                    description = res.getString(R.string.h_test_removed, lr.getNumber());
                    break;
                case Record.TEST_RECOVERED:
                    main_icon = R.drawable.ic_test;
                    secondary_icon = R.drawable.ic_undo;
                    description = res.getString(R.string.h_test_recovered, lr.getNumber());
                    break;
                case Record.PRACTISED_MATCH:
                    main_icon = R.drawable.ic_match;
                    secondary_icon = R.drawable.ic_ok;
                    description = res.getString(R.string.h_practised_match, lr.getNumber());
                    break;
                case Record.PRACTISED_COMPLETE:
                    main_icon = R.drawable.ic_complete;
                    secondary_icon = R.drawable.ic_ok;
                    description = res.getString(R.string.h_practised_complete, lr.getNumber());
                    break;
                case Record.PRACTISED_WRITE:
                    main_icon = R.drawable.ic_write;
                    secondary_icon = R.drawable.ic_ok;
                    description = res.getString(R.string.h_practised_write, lr.getNumber());
                    break;
                case Record.PRACTISED_LISTENING:
                    main_icon = R.drawable.ic_listening;
                    secondary_icon = R.drawable.ic_ok;
                    description = res.getString(R.string.h_practised_listening, lr.getNumber());
                    break;
            }

        } else if (record instanceof LanguageSettingsRecord) {
            LanguageSettingsRecord sr = (LanguageSettingsRecord) record;

            int flag = LanguageCode.getFlagResId(sr.language_code);
            String list_name = LanguageManager.getLanguageName(sr.language_id);
            if (list_name == null)
                list_name = res.getString(R.string.h_deleted_list);

            switch (record.type) {
                // Language Settings
                case Record.LANGUAGE_CREATED:
                    main_icon = flag;
                    secondary_icon = R.drawable.ic_add;
                    title = res.getString(R.string.h_list_created);
                    description = list_name;
                    break;
                case Record.LANGUAGE_REMOVED:
                    main_icon = flag;
                    secondary_icon = R.drawable.ic_delete;
                    title = res.getString(R.string.h_list_deleted);
                    description = (String) sr.newValue;
                    break;
                case Record.LANGUAGE_INTEGRATED:
                    main_icon = flag;
                    secondary_icon = R.drawable.ic_play;
                    title = res.getString(R.string.h_list_integrated);
                    description = sr.oldValue + res.getString(R.string.h_list_integrated_in) + sr.newValue;
                    break;
                case Record.LANGUAGE_CODE_CHANGED:
                    main_icon = flag;
                    secondary_icon = R.drawable.ic_configuration;
                    title = list_name;
                    description = res.getString(R.string.h_list_code_changed, AppData.getLanguageName((Integer) sr.newValue));
                    break;
                case Record.LANGUAGE_NAME_CHANGED:
                    main_icon = flag;
                    secondary_icon = R.drawable.ic_configuration;
                    title = res.getString(R.string.h_list_name_changed, sr.oldValue, sr.newValue);
                    description = "";
                    break;
                case Record.LANGUAGE_PHRASAL_STATE_CHANGED:
                    main_icon = flag;
                    secondary_icon = R.drawable.ic_configuration;
                    title = list_name;
                    description = res.getString(R.string.phrasalverbs) + " " + ((Boolean) sr.newValue ? res.getString(R.string.enabled) : res.getString(R.string.disabled));
                    break;
                case Record.LANGUAGE_STATISTICS_CLEARED:
                    main_icon = flag;
                    secondary_icon = R.drawable.ic_configuration;
                    title = list_name;
                    description = res.getString(R.string.h_list_stats_cleared);
                    break;
                case Record.RECYCLE_BIN_CLEARED:
                    main_icon = R.drawable.ic_delete_all;
                    secondary_icon = R.drawable.ic_ok;
                    title = list_name;
                    description = res.getString(R.string.h_recycle_bin_cleared);
                    break;
            }

        } else if (record instanceof AppSettingsRecord) {
            AppSettingsRecord asr = (AppSettingsRecord) record;

            main_icon = R.drawable.ic_configuration;
            secondary_icon = android.R.drawable.presence_invisible;

            switch (record.type) {
                case Record.APPSET_LANGUAGE_CHANGED:
                    title = res.getString(R.string.settings);
                    description = res.getString(R.string.h_app_lang_changed, AppData.getLanguageName((Integer) asr.value));
                    break;
                case Record.APPSET_KBVIBRATION_STATE_CHANGED:
                    title = res.getString(R.string.settings);
                    description = res.getString(R.string.pref_phonetic_keyboard_vibration) + " " + ((Boolean) asr.value ? res.getString(R.string.enabled) : res.getString(R.string.disabled));
                    break;
                case Record.APPSET_THEME_CHANGED:
                    title = res.getString(R.string.settings);
                    description = res.getString(R.string.h_app_theme_changed, res.getStringArray(R.array.themes)[(Integer) asr.value]);
                    break;
                case Record.APPSET_ONLINE_BACKUP_STATE_CHANGED:
                    title = res.getString(R.string.settings);
                    description = res.getString(R.string.pref_backup_n_synchronization) + " " + ((Boolean) asr.value ? res.getString(R.string.enabled) : res.getString(R.string.disabled));
                    break;
                case Record.APPSET_IMPORT:
                    title = res.getString(R.string.pref_import_data);
                    description = res.getString(R.string.h_import, (Integer) asr.value);
                    break;
                case Record.APPSET_EXPORT:
                    title = res.getString(R.string.pref_export_data);
                    description = res.getString(R.string.h_export, (Integer) asr.value);
                    break;
            }
        }

        holder.main_icon.setImageResource(main_icon);
        holder.secondary_icon.setImageResource(secondary_icon);
        holder.title.setText(title);
        holder.description.setText(description);
        holder.view.setTag(record);
    }

}