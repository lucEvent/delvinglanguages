package com.delvinglanguages.listers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.communications.NetWork;

public class LanguageLister extends ArrayAdapter<String> implements NetWork {

	private String[] values;

	private Drawable[] flags;
	private LayoutInflater inflater;

	public LanguageLister(Context context, String[] values) {
		super(context, R.layout.i_language, values);
		this.values = values;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.i_language, parent, false);
		}
		TextView idioma = (TextView) view.findViewById(R.id.li_name);

		idioma.setText(values[position]);
		return view;
	}

	@Override
	public void datagram(int code, String message, Object packet) {

	}

}
