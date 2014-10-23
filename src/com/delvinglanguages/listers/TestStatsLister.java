package com.delvinglanguages.listers;

import java.text.DecimalFormat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.Test;
import com.delvinglanguages.core.Test.State;

public class TestStatsLister extends ArrayAdapter<State> {

	private Test test;
	private Context context;

	public TestStatsLister(Context context) {
		super(context, R.layout.i_test_stats,
				ControlCore.testActual.statistics);
		this.context = context;
		test = ControlCore.testActual;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout viewres = (LinearLayout) inflater.inflate(
				R.layout.i_test_stats, parent, false);

		TextView word = (TextView) viewres.findViewById(R.id.its_word);
		TextView pmat = (TextView) viewres.findViewById(R.id.its_match);
		TextView pcom = (TextView) viewres.findViewById(R.id.its_complete);
		TextView pwri = (TextView) viewres.findViewById(R.id.its_write);
		TextView ptot = (TextView) viewres.findViewById(R.id.its_total);

		word.setText(test.references.get(position).item);
		State e = test.statistics.get(position);
		float mat = (float) e.aciertos_match / (float) (e.aciertos_match + e.fallos_match);
		float com = (float) e.aciertos_complete / (float) (e.aciertos_complete + e.fallos_complete);
		float wri = (float) e.aciertos_write / (float) (e.aciertos_write + e.fallos_write);
		float atot = e.aciertos_match + e.aciertos_complete + e.aciertos_write;
		float tot = atot / (float) (atot + e.fallos_match + e.fallos_complete + e.fallos_write);
	
		DecimalFormat df = new DecimalFormat("0.0 %");
	
		pmat.setText(df.format(mat));
		pcom.setText(df.format(com));
		pwri.setText(df.format(wri));
		ptot.setText(df.format(tot));

		return viewres;
	}
}
