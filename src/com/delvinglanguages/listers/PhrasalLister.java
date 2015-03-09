package com.delvinglanguages.listers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;

public class PhrasalLister extends ArrayAdapter<String> {

	public final static Integer STAT_NORMAL = 0;
	public final static Integer STAT_PRESSED = 1;
	public final static Integer STAT_MARKED = 2;

	private String[] values;
	private Integer[] marks;
	private LayoutInflater inflater;

	public PhrasalLister(Context context, String[] values, Integer[] marks) {
		super(context, R.layout.i_stored_word, values);
		this.values = values;
		this.marks = marks;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.i_stored_word, parent, false);
		}
		TextView word = (TextView) view.findViewById(R.id.stored_word);

		word.setText(values[position]);
		if (marks[position] == STAT_PRESSED) {
			view.setBackgroundResource(R.drawable.button_bg_pressed);
		} else if (marks[position] == STAT_MARKED) {
			view.setBackgroundResource(R.drawable.button_bg_marked);
		}
		return view;
	}
}
