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

	public LanguageLister(Context context, String[] values) {
		super(context, R.layout.i_language, values);
		this.values = values;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View viewres = inflater.inflate(R.layout.i_language, parent, false);

		TextView idioma = (TextView) viewres.findViewById(R.id.li_name);

		idioma.setText(values[position]);
		return viewres;
	}

	@Override
	public void datagram(int code, String message, Object packet) {
		// TODO Auto-generated method stub
		
	}
}
