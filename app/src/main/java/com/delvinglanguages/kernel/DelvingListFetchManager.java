package com.delvinglanguages.kernel;

import android.content.Context;
import android.os.Handler;

import com.delvinglanguages.view.lister.viewholder.MainTypesViewHolder;
import com.delvinglanguages.view.utils.DataListener;

public class DelvingListFetchManager extends DelvingListManager {

    private Handler handler;

    public DelvingListFetchManager(Context context, Handler handler)
    {
        super(context);
        this.handler = handler;
    }

    public void fetchListContents(final DelvingList list)
    {
        if (!list.isLoaded()) {

            new Thread(new Runnable() {
                @Override
                public void run()
                {

                    loadContentOf(list);

                }
            }).start();
        }
    }

    public void fetchListContentNumbers(final DelvingList list)
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {

                loadContentOf(list);

                while (!list.isDictionaryCreated()) ;

                MainTypesViewHolder.Data data = new MainTypesViewHolder.Data();
                data.list = list;
                handler.obtainMessage(DataListener.MAIN_DATA_COUNTERS, data).sendToTarget();
            }
        }).start();
    }

}
