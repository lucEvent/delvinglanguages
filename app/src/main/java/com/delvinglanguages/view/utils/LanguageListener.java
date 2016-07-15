package com.delvinglanguages.view.utils;

public interface LanguageListener extends MessageListener {

    int LANGUAGE_CREATED_OK = 200;
    int LANGUAGE_CREATED_CANCELED = 201;
    int LANGUAGE_REMOVED = 202;
    int LANGUAGE_RECOVERED = 203;
    int LANGUAGE_NAME_CHANGED = 204;

    void onLanguageCreatedOK(Object[] data);

    void onLanguageCreatedCanceled();

    void onLanguageRemoved();

    void onLanguageRecovered();

    void onLanguageNameChanged();

}
