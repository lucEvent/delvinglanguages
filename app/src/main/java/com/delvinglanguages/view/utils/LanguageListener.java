package com.delvinglanguages.view.utils;

public interface LanguageListener extends MessageListener {

    int LANGUAGE_REMOVED = 200;
    int LANGUAGE_RECOVERED = 201;
    int LANGUAGE_NAME_CHANGED = 202;

    int ENABLE_SYNCHRONIZATION = 210;
    int DISABLE_SYNCHRONIZATION = 211;

    void onLanguageRemoved();

    void onLanguageRecovered();

    void onLanguageNameChanged();

    void onSynchronizationStateChanged(boolean enabled);

}
