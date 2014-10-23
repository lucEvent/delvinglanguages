package com.delvinglanguages.listers;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
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
	private Context context;

	public PhrasalLister(Context context, String[] values, Integer[] marks) {
		super(context, R.layout.i_stored_word, values);
		this.context = context;
		this.values = values;
		this.marks = marks;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View viewres = inflater.inflate(R.layout.i_stored_word, parent, false);
		TextView word = (TextView) viewres.findViewById(R.id.stored_word);

		word.setText(values[position]);
		if (marks[position] == STAT_PRESSED) {
			Log.d("##ListOfPhrasals##", "##Asignando PRESSED a: "
					+ values[position]);
			viewres.setBackgroundResource(R.drawable.pressed_button_background);
		} else if (marks[position] == STAT_MARKED) {
			Log.d("##ListOfPhrasals##", "##Asignando MARKED a: "
					+ values[position]);
			viewres.setBackgroundResource(R.drawable.marked_button_background);
		}
		return viewres;
	}
}
