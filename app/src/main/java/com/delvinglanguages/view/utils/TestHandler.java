package com.delvinglanguages.view.utils;

import android.os.Message;

import java.lang.ref.WeakReference;

public class TestHandler extends android.os.Handler {

    private final WeakReference<TestListener> messager;

    public TestHandler(TestListener messager)
    {
        this.messager = new WeakReference<>(messager);
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what) {
            case TestListener.TEST_START:
                messager.get().onTestStart();
                break;
            case TestListener.TEST_DELETED:
                messager.get().onTestDeleted();
                break;
            case TestListener.TEST_ROUND_PASSED:
                messager.get().onTestRoundPassed();
                break;
            case TestListener.TEST_ROUND_SKIPPED:
                messager.get().onTestRoundSkipped();
                break;
        }

    }

}

