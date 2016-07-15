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
            case LanguageListener.LANGUAGE_CREATED_OK:
                messager.get().onLanguageCreatedOK((Object[]) msg.obj);
                break;
            case LanguageListener.LANGUAGE_CREATED_CANCELED:
                messager.get().onLanguageCreatedCanceled();
                break;
            case LanguageListener.LANGUAGE_REMOVED:
                messager.get().onLanguageRemoved();
                break;
            case LanguageListener.LANGUAGE_RECOVERED:
                messager.get().onLanguageRecovered();
            case LanguageListener.LANGUAGE_NAME_CHANGED:
                messager.get().onLanguageNameChanged();
                break;
            case LanguageListener.MESSAGE_INT:
                messager.get().onMessage((int) msg.obj);
                break;
        }
    }

}
