package com.delvinglanguages.net.utils;

import android.os.Handler;

public abstract class OnlineDictionary {

    protected final Handler handler;

    public OnlineDictionary(Handler handler)
    {
        this.handler = handler;
    }

    public abstract void updateLanguages(int from_language_code, int to_language_code);

    public abstract boolean isTranslationAvailable(int from, int to);

    public abstract boolean search(Search search);

}
