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

		public TranslationLister(Context context, ArrayList<String> values) {
			super(context, R.layout.i_option, values);
			this.values = values;
		}

		public TranslationLister(Context context, String[] Svalues) {
			super(context, R.layout.i_option, Svalues);		
			values = new ArrayList<String>();
			for (int i = 0; i < Svalues.length; ++i) {
				values.add(Svalues[i]);
			}
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View viewres = inflater.inflate(R.layout.i_translation, parent, false);

			TextView option = (TextView) viewres.findViewById(R.id.it_trans);
			option.setText(values.get(position));

			return viewres;
		}

}
