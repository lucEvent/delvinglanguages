package com.delvinglanguages.listers;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.core.Test;

public class TestLister extends ArrayAdapter<Test> {

	private ArrayList<Test> values;
	private LayoutInflater inflater;

	public TestLister(Context context, ArrayList<Test> values) {
		super(context, android.R.layout.simple_list_item_1, values);
		this.values = values;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = (TextView) inflater.inflate(
					android.R.layout.simple_list_item_1, parent, false);
		}
		((TextView) view).setText(values.get(position).name);
		return view;
	}

}
