package com.delvinglanguages.view.utils;

import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.delvinglanguages.R;

public class NoContentViewHelper {

    private View view;
    private int message;

    public NoContentViewHelper(View view, int message)
    {
        this.view = view;
        this.message = message;
    }

    public void setMessage(int message)
    {
        this.message = message;
        if (!(view instanceof ViewStub))
            ((TextView) view.findViewById(R.id.message)).setText(message);
    }

    public void displayMessage()
    {
        if (view instanceof ViewStub) {
            view = ((ViewStub) view).inflate();
            ((TextView) view.findViewById(R.id.message)).setText(message);
        } else
            view.setVisibility(View.VISIBLE);
    }

    public void hide()
    {
        if (!(view instanceof ViewStub))
            view.setVisibility(View.GONE);
    }
}
