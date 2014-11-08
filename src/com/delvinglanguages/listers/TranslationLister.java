package com.delvinglanguages.listers;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;

public class TranslationLister extends ArrayAdapter<String> {

	private ArrayList<String> values;
	private LayoutInflater inflater;

	public TranslationLister(Context context, ArrayList<String> values) {
		super(context, R.layout.i_option, values);
		this.values = values;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public TranslationLister(Context context, String[] Svalues) {
		super(context, R.layout.i_option, Svalues);
		values = new ArrayList<String>();
		for (int i = 0; i < Svalues.length; ++i) {
			values.add(Svalues[i]);
		}
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.i_translation, parent, false);
		}

		TextView option = (TextView) view.findViewById(R.id.translation);
		option.setText(values.get(position));

		return view;
	}

}
