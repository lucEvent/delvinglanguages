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

	public OptionLister(Context context,String[] values) {
		super(context, R.layout.i_option, values);
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View viewres = inflater.inflate(R.layout.i_option, parent, false);

		TextView option = (TextView) viewres.findViewById(R.id.io_option);
		option.setText(values[position]);

		return viewres;
	}
	
}
