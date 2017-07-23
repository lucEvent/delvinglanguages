package com.delvinglanguages.view.utils;

public interface LanguageListener extends MessageListener {

    int LANGUAGE_REMOVED = 200;
    int LANGUAGE_RECOVERED = 201;
    int LANGUAGE_NAME_CHANGED = 202;
    int LANGUAGE_MERGED_AND_REMOVED = 203;

    int ENABLE_SYNCHRONIZATION = 210;
    int DISABLE_SYNCHRONIZATION = 211;
    int SYNC_DATA_CHANGED = 212;

    void onSyncDataChanged();

    void onLanguageRemoved();

    void onLanguageRecovered();

    void onLanguageNameChanged();

    void onLanguageMergeAndRemoved(int language_merged_into_id);

    void onSynchronizationStateChanged(boolean enabled);

}
