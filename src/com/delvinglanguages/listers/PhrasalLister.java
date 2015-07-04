package com.delvinglanguages.listers;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.phrasals.PVLink;

public class PhrasalLister extends ArrayAdapter<PVLink> {

	public final static Integer STAT_NORMAL = 0;
	public final static Integer STAT_PRESSED = 1;
	public final static Integer STAT_MARKED = 2;

	private Integer[] marks;
	private LayoutInflater inflater;

	public PhrasalLister(Context context, ArrayList<PVLink> values, Integer[] marks) {
		super(context, R.layout.i_stored_word, values);
		this.marks = marks;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.i_stored_word, parent, false);
		}
		TextView word = (TextView) view.findViewById(R.id.stored_word);

		word.setText(getItem(position).name);
		if (marks[position] == STAT_PRESSED) {
			view.setBackgroundColor(0xFF33AA33);
		} else if (marks[position] == STAT_MARKED) {
			view.setBackgroundColor(0xFF33AA33);
		} else {
			view.setBackgroundColor(0x0000);
		}
		return view;
	}
}
