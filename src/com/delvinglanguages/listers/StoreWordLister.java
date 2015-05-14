package com.delvinglanguages.listers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.Nota;
import com.delvinglanguages.kernel.set.Notas;

public class StoreWordLister extends ArrayAdapter<Nota> {

	private Notas values;
	private LayoutInflater inflater;

	public StoreWordLister(Context context, Notas values) {
		super(context, R.layout.i_stored_word, values);
		this.values = values;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.i_stored_word, parent, false);
		}

		TextView word = (TextView) view.findViewById(R.id.stored_word);

		word.setText(values.get(position).get());
		return view;
	}

}
