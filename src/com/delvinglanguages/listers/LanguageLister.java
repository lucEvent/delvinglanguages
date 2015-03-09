package com.delvinglanguages.listers;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.IDDelved;

public class LanguageLister extends ArrayAdapter<IDDelved> {

	private ArrayList<IDDelved> values;
	private LayoutInflater inflater;

	public LanguageLister(Context context, ArrayList<IDDelved> values) {
		super(context, R.layout.i_language, values);
		this.values = values;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.i_language, parent, false);
		}
		((TextView) view.findViewById(R.id.name)).setText(values.get(position)
				.getName());
		return view;
	}

}
