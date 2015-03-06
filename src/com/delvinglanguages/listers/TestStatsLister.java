package com.delvinglanguages.listers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.Test;
import com.delvinglanguages.core.Test.State;

public class TestStatsLister extends ArrayAdapter<State> {

	private Test test;
	private LayoutInflater inflater;

	public TestStatsLister(Context context) {
		super(context, R.layout.i_test_stats, ControlCore.testActual.statistics);
		this.test = ControlCore.testActual;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.i_test_stats, parent, false);
		}

		TextView word = (TextView) view.findViewById(R.id.word);
		TextView pmat = (TextView) view.findViewById(R.id.match);
		TextView pcom = (TextView) view.findViewById(R.id.complete);
		TextView pwri = (TextView) view.findViewById(R.id.write);
		TextView ptot = (TextView) view.findViewById(R.id.total);

		word.setText(test.references.get(position).name);
		State e = test.statistics.get(position);
		int tot = e.fallos_match + e.fallos_complete + e.fallos_write;

		pmat.setText(e.fallos_match + " error(s)");
		pcom.setText(e.fallos_complete + " error(s)");
		pwri.setText(e.fallos_write + " error(s)");
		ptot.setText(tot + " errors");

		return view;
	}
}
