package com.delvinglanguages.view.lister;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;

import java.io.IOException;
import java.io.InputStream;

public class AvailableLanguageLister extends ArrayAdapter<String> {

    private Drawable[] flags;
    private LayoutInflater inflater;

    public AvailableLanguageLister(Context context, String[] values) {
        super(context, R.layout.i_image_list, values);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        AssetManager amanager = context.getAssets();
        flags = new Drawable[values.length];
        for (int i = 0; i < values.length; i++) {
            try {
                InputStream reader = amanager.open(values[i] + ".png");
                flags[i] = Drawable.createFromStream(reader, null);
                flags[i].setBounds(0, 0, 10, 10);
                reader.close();
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.i_image_list, parent, false);
        }

        ((TextView) view.findViewById(R.id.title)).setText(getItem(position));
        view.findViewById(R.id.image).setBackground(flags[position]);

        return view;
    }

}
