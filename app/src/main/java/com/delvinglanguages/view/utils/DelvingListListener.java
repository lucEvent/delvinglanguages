package com.delvinglanguages.view.utils;

public interface DelvingListListener extends MessageListener {

    int LIST_REMOVED = 200;
    int LIST_RECOVERED = 201;
    int LIST_NAME_CHANGED = 202;
    int LIST_MERGED_AND_REMOVED = 203;

    int ENABLE_SYNCHRONIZATION = 210;
    int DISABLE_SYNCHRONIZATION = 211;
    int SYNC_DATA_CHANGED = 212;

    void onSyncDataChanged();

    void onListRemoved();

    void onListRecovered();

    void onListNameChanged();

    void onListMergeAndRemoved(int list_merged_into_id);

    void onSynchronizationStateChanged(boolean enabled);

}
