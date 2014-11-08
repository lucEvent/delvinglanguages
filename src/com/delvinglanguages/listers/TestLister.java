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
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		TextView view = (TextView) inflater.inflate(
				android.R.layout.simple_list_item_1, parent, false);

		view.setText(values.get(position).name);
		return view;
	}

}
