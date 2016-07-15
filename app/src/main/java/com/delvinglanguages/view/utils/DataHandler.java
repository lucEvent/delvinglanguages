package com.delvinglanguages.view.utils;

import android.os.Message;

import java.lang.ref.WeakReference;

public class DataHandler extends android.os.Handler {

    private final WeakReference<DataListener> messager;

    public DataHandler(DataListener messager)
    {
        this.messager = new WeakReference<>(messager);
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what) {
            case DataListener.MAIN_DATA_COUNTERS:
                messager.get().onMainDataCounters(msg.obj);
                break;
        }

    }

}
