package com.delvinglanguages.kernel.manager;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.delvinglanguages.view.dialog.NotifierDialog;

import java.util.Locale;

public class PronunciationManager implements TextToSpeech.OnInitListener {

    private Locale locale;

    private TextToSpeech speechEngine;

    private NotifierDialog notifier;

    private boolean askToUser;

    /**
     * This variable indicates if onInit method has already been called by the system
     */
    private boolean initialized;

    /**
     * This variable stores the last string that tried to be pronounced before the Engine is initialized
     */
    private String advanced;

    public PronunciationManager(Context context, Locale locale, boolean askToUser)
    {
        this.locale = locale;
        this.speechEngine = new TextToSpeech(context, this);
        if (askToUser)
            this.notifier = new NotifierDialog(context);
        this.initialized = false;
        this.askToUser = askToUser;
    }

    @Override
    public void onInit(int status)
    {
        if (status == TextToSpeech.SUCCESS)
            speechEngine.setLanguage(locale);

        initialized = true;
        if (advanced != null) {
            pronounce(advanced);
            advanced = null;
        }
    }

    public void pronounce(String text)
    {
        if (!initialized) {
            advanced = text;
            return;
        }

        int availability = speechEngine.isLanguageAvailable(locale);
        if (availability == TextToSpeech.LANG_MISSING_DATA || availability == TextToSpeech.LANG_NOT_SUPPORTED) {

            if (askToUser)
                notifier.notify_languageTTSnotAvailable(locale.getDisplayLanguage());

        } else
            speechEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "");
    }

    public boolean isLanguageAvailable()
    {
        int availability = speechEngine.isLanguageAvailable(locale);
        return !(availability == TextToSpeech.LANG_MISSING_DATA || availability == TextToSpeech.LANG_NOT_SUPPORTED);
    }

    public void destroy()
    {
        if (speechEngine != null) {
            speechEngine.stop();
            speechEngine.shutdown();
        }
    }

}
