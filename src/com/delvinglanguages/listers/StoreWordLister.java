package com.delvinglanguages.listers;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.Nota;

public class StoreWordLister extends ArrayAdapter<Nota> {
	
	private ArrayList<Nota> values;
	public Context context;

	public StoreWordLister(Context context, ArrayList<Nota> values) {
		super(context, R.layout.i_stored_word, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View viewres = inflater.inflate(R.layout.i_stored_word, parent, false);

		TextView word = (TextView) viewres.findViewById(R.id.stored_word);
	
		word.setText(values.get(position).get());
		return viewres;
	}

}
