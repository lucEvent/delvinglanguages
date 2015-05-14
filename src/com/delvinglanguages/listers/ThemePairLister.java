package com.delvinglanguages.listers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.set.ThemePairs;
import com.delvinglanguages.kernel.theme.ThemePair;

public class ThemePairLister extends ArrayAdapter<ThemePair> {

	private ThemePairs values;
	private LayoutInflater inflater;

	public ThemePairLister(Context context, ThemePairs values) {
		super(context, R.layout.i_theme_pair, values);
		this.values = values;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.i_theme_pair, parent, false);
		}
		ThemePair pair = values.get(position);
		((TextView) view.findViewById(R.id.pair_1)).setText(pair.inDelved);
		((TextView) view.findViewById(R.id.pair_2)).setText(pair.inNative);
		return view;
	}

}
