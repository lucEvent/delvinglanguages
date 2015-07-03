package com.delvinglanguages.listers;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.delvinglanguages.R;

public class AvailableLanguageLister extends ArrayAdapter<Object> {

	private String[] values;
	private Drawable[] flags;
	private LayoutInflater inflater;

	public AvailableLanguageLister(Context context, String[] values) {
		super(context, R.layout.i_image_list, values);
		this.values = values;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		AssetManager amanager = context.getAssets();
		flags = new Drawable[values.length];
		for (int i = 0; i < values.length; i++) {
			try {
				InputStream reader = amanager.open(values[i] + ".png");
				flags[i] = Drawable.createFromStream(reader, null);
				reader.close();
			} catch (IOException ex) {

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

		((TextView) view.findViewById(R.id.title)).setText(values[position]);
		((ImageView) view.findViewById(R.id.image)).setBackground(flags[position]);

		return view;
	}

}
