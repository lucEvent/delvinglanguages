package com.delvinglanguages.view.utils;

import android.os.Message;

import java.lang.ref.WeakReference;

public class LanguageHandler extends android.os.Handler {

    private final WeakReference<LanguageListener> messager;

    public LanguageHandler(LanguageListener messager)
    {
        this.messager = new WeakReference<>(messager);
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what) {
            case LanguageListener.LANGUAGE_REMOVED:
                messager.get().onLanguageRemoved();
                break;
            case LanguageListener.LANGUAGE_RECOVERED:
                messager.get().onLanguageRecovered();
                break;
            case LanguageListener.LANGUAGE_NAME_CHANGED:
                messager.get().onLanguageNameChanged();
                break;
            case LanguageListener.LANGUAGE_MERGED_AND_REMOVED:
                messager.get().onLanguageMergeAndRemoved(msg.arg1);
                break;
            case LanguageListener.SYNC_DATA_CHANGED:
                messager.get().onSyncDataChanged();
                break;
            case LanguageListener.ENABLE_SYNCHRONIZATION:
                messager.get().onSynchronizationStateChanged(true);
                break;
            case LanguageListener.DISABLE_SYNCHRONIZATION:
                messager.get().onSynchronizationStateChanged(false);
                break;
            case LanguageListener.MESSAGE_INT:
                messager.get().onMessage((int) msg.obj);
                break;
        }
    }

}
