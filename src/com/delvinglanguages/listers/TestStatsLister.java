package com.delvinglanguages.listers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.set.TestReferenceStates;
import com.delvinglanguages.kernel.test.TestReferenceState;

public class TestStatsLister extends ArrayAdapter<TestReferenceState> {

	private TestReferenceStates values;
	private LayoutInflater inflater;

	public TestStatsLister(Context context, TestReferenceStates values) {
		super(context, R.layout.i_test_stats, values);
		this.values = values;
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

		TestReferenceState refstate = values.get(position);
		word.setText(refstate.reference.name);
		int tot = refstate.fallos_match + refstate.fallos_complete
				+ refstate.fallos_write;

		pmat.setText(refstate.fallos_match + " error(s)");
		pcom.setText(refstate.fallos_complete + " error(s)");
		pwri.setText(refstate.fallos_write + " error(s)");
		ptot.setText(tot + " errors");

		return view;
	}
}
