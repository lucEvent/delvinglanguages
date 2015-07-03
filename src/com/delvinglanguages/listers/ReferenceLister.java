package com.delvinglanguages.listers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.set.DReferences;
import com.delvinglanguages.settings.Settings;

public class ReferenceLister extends ArrayAdapter<DReference> {

	private DReferences values;
	private LayoutInflater inflater;

	private final boolean phMode;

	public ReferenceLister(Context context, DReferences values, boolean phMode) {
		super(context, R.layout.i_word, values);
		this.values = values;
		this.phMode = phMode;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.i_word, parent, false);
		}

		DReference ref = values.get(position);
		TextView word = (TextView) view.findViewById(R.id.word);
		TextView tran = (TextView) view.findViewById(R.id.translation);

		// Tipos
		TextView labels[] = new TextView[7];
		labels[Word.NOUN] = (TextView) view.findViewById(R.id.noun);
		labels[Word.VERB] = (TextView) view.findViewById(R.id.verb);
		labels[Word.ADJECTIVE] = (TextView) view.findViewById(R.id.adj);
		labels[Word.ADVERB] = (TextView) view.findViewById(R.id.adverb);
		labels[Word.PHRASAL] = (TextView) view.findViewById(R.id.phrasal);
		labels[Word.EXPRESSION] = (TextView) view.findViewById(R.id.expression);
		labels[Word.OTHER] = (TextView) view.findViewById(R.id.other);

		Settings.setBackgroundColorsforType(labels, ref.getType());
		if (!phMode) {
			labels[Word.PHRASAL].setVisibility(View.GONE);
		}

		word.setText(ref.name);
		tran.setText(ref.getTranslation());
		return view;
	}

}
