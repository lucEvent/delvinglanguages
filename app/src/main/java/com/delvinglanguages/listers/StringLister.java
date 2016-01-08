package com.delvinglanguages.listers;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;

public class StringLister extends ArrayAdapter<String> {

    private LayoutInflater inflater;

    public StringLister(Context context, String[] values) {
        super(context, R.layout.i_stored_word, values);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public StringLister(Context context, ArrayList<String> values) {
        super(context, R.layout.i_stored_word, values);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.i_stored_word, parent, false);
        }
        ((TextView) view).setText(getItem(position));
        return view;
    }

}
