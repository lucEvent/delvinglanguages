package com.delvinglanguages.listers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.set.Themes;
import com.delvinglanguages.kernel.theme.Theme;

public class ThemeLister extends ArrayAdapter<Theme> {

    private LayoutInflater inflater;

    public ThemeLister(Context context, Themes values) {
        super(context, R.layout.i_stored_word, values);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.i_stored_word, parent, false);
        }
        ((TextView) view).setText(getItem(position).getName());
        return view;
    }

}
