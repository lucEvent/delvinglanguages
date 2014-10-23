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
	private Context context;

	public TestLister(Context context, ArrayList<Test> values) {
		super(context, android.R.layout.simple_list_item_1, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		TextView viewres = (TextView) inflater.inflate(
				android.R.layout.simple_list_item_1, parent, false);

		viewres.setText(values.get(position).name);
		return viewres;
	}

}
