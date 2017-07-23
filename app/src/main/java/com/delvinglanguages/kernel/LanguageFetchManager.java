package com.delvinglanguages.kernel;

import android.content.Context;
import android.os.Handler;

import com.delvinglanguages.view.lister.viewholder.MainTypesViewHolder;
import com.delvinglanguages.view.utils.DataListener;

public class LanguageFetchManager extends LanguageManager {

    private Handler handler;

    public LanguageFetchManager(Context context, Handler handler)
    {
        super(context);
        this.handler = handler;
    }

    public void fetchLanguageContents(final Language language)
    {
        if (!language.isLoaded()) {

            new Thread(new Runnable() {
                @Override
                public void run()
                {

                    loadContentOf(language);

                }
            }).start();
        }
    }

    public void fetchLanguageContentNumbers(final Language language)
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {

                loadContentOf(language);

                while (!language.isDictionaryCreated()) ;

                MainTypesViewHolder.Data data = new MainTypesViewHolder.Data();
                data.language = language;
                handler.obtainMessage(DataListener.MAIN_DATA_COUNTERS, data).sendToTarget();
            }
        }).start();
    }

}
