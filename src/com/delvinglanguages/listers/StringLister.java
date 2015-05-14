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

	private String[] values;
	private LayoutInflater inflater;

	public StringLister(Context context, String[] values) {
		super(context, R.layout.i_language, values);
		setFields(context, values);
	}

	public StringLister(Context context, ArrayList<String> values) {
		super(context, R.layout.i_language, values);
		setFields(context, values.toArray(new String[values.size()]));
	}

	private void setFields(Context context, String[] values) {
		this.values = values;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.i_language, parent, false);
		}
		((TextView) view.findViewById(R.id.name)).setText(values[position]);
		return view;
	}

}
