package com.delvinglanguages.listers;

import com.delvinglanguages.kernel.set.Tests;
import com.delvinglanguages.kernel.test.Test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TestLister extends ArrayAdapter<Test> {

    private LayoutInflater inflater;

    public TestLister(Context context, Tests values) {
        super(context, android.R.layout.simple_list_item_1, values);
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        ((TextView) view).setText(getItem(position).name);
        return view;
    }

}
