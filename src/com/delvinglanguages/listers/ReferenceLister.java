package com.delvinglanguages.listers;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.DReference;
import com.delvinglanguages.settings.Configuraciones;

public class ReferenceLister extends ArrayAdapter<DReference> {

	private ArrayList<DReference> values;
	private Context context;

	public ReferenceLister(Context context, ArrayList<DReference> values) {
		super(context, R.layout.i_word, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View viewres = inflater.inflate(R.layout.i_word, parent, false);

		DReference ref = values.get(position);
		TextView word = (TextView) viewres.findViewById(R.id.lp_word);
		TextView tran = (TextView) viewres.findViewById(R.id.lp_translation);

		// Tipos
		TextView labels[] = new TextView[Configuraciones.NUM_TYPES];
		// noun //verb //adj //adv //phrasal //expresion //other
		labels[0] = (TextView) viewres.findViewById(R.id.lp_noun);
		labels[1] = (TextView) viewres.findViewById(R.id.lp_verb);
		labels[2] = (TextView) viewres.findViewById(R.id.lp_adj);
		labels[3] = (TextView) viewres.findViewById(R.id.lp_adverb);
		labels[4] = (TextView) viewres.findViewById(R.id.lp_phrasal);
		labels[5] = (TextView) viewres.findViewById(R.id.lp_expression);
		labels[6] = (TextView) viewres.findViewById(R.id.lp_other);

		int type = ref.owners.get(0).getType(); // De momento, pero habra que
												// optimizarlo
		for (int i = 0; i < Configuraciones.NUM_TYPES; ++i) {
			if ((type & (1 << i)) != 0) {
				labels[i].setBackgroundColor(Configuraciones.type_colors[i]);
			} else {
				labels[i].setBackgroundColor(0xFFCCCCCC);
			}
		}

		word.setText(ref.item);
		tran.setText(ref.getTranslation());
		return viewres;
	}

}
