package com.delvinglanguages.view.utils;

import android.os.Message;

import com.delvinglanguages.kernel.Inflexion;

import java.lang.ref.WeakReference;

public class ReferenceHandler extends android.os.Handler {

    private final WeakReference<ReferenceListener> messager;

    public ReferenceHandler(ReferenceListener messager)
    {
        this.messager = new WeakReference<>(messager);
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what) {
            case ReferenceListener.INFLEXION_ADDED:
                messager.get().onInflexionAdded((Inflexion) msg.obj);
                break;
            case ReferenceListener.MESSAGE_INT:
                messager.get().onMessage((int) msg.obj);
                break;
        }
    }

}
