package com.delvinglanguages.listers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;

public class OptionLister extends ArrayAdapter<String> {

	private String[] values;
	private LayoutInflater inflater;

	public OptionLister(Context context, String[] values) {
		super(context, R.layout.i_option, values);
		this.values = values;
		this.inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.i_option, parent, false);
		}

		TextView option = (TextView) view.findViewById(R.id.option);
		option.setText(values[position]);

		return view;
	}

}
