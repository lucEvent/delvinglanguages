package com.delvinglanguages.view.utils;

import android.os.Message;

import java.lang.ref.WeakReference;

public class DelvingListHandler extends android.os.Handler {

    private final WeakReference<DelvingListListener> messenger;

    public DelvingListHandler(DelvingListListener messenger)
    {
        this.messenger = new WeakReference<>(messenger);
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what) {
            case DelvingListListener.LIST_REMOVED:
                messenger.get().onListRemoved();
                break;
            case DelvingListListener.LIST_RECOVERED:
                messenger.get().onListRecovered();
                break;
            case DelvingListListener.LIST_NAME_CHANGED:
                messenger.get().onListNameChanged();
                break;
            case DelvingListListener.LIST_MERGED_AND_REMOVED:
                messenger.get().onListMergeAndRemoved(msg.arg1);
                break;
            case DelvingListListener.SYNC_DATA_CHANGED:
                messenger.get().onSyncDataChanged();
                break;
            case DelvingListListener.ENABLE_SYNCHRONIZATION:
                messenger.get().onSynchronizationStateChanged(true);
                break;
            case DelvingListListener.DISABLE_SYNCHRONIZATION:
                messenger.get().onSynchronizationStateChanged(false);
                break;
            case DelvingListListener.MESSAGE_INT:
                messenger.get().onMessage((int) msg.obj);
                break;
        }
    }

}
