package com.delvinglanguages.view.lister;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DrawerReference;
import com.delvinglanguages.kernel.util.DrawerReferences;

public class DrawerLister extends ArrayAdapter<DrawerReference> {

    private LayoutInflater inflater;

    public DrawerLister(Context context, DrawerReferences values) {
        super(context, R.layout.i_drawer_word, values);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.i_drawer_word, parent, false);
        }

        ((TextView) view).setText(getItem(position).name);
        return view;
    }

}
