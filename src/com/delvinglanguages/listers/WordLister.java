package com.delvinglanguages.listers;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.Word;
import com.delvinglanguages.settings.Configuraciones;

public class WordLister extends ArrayAdapter<Word> {

	private ArrayList<Word> values;
	private LayoutInflater inflater;
	private boolean phMode;

	public WordLister(Context context, ArrayList<Word> values, boolean phMode) {
		super(context, R.layout.i_word, values);
		this.values = values;
		this.phMode = phMode;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.i_word, parent, false);
		}
		Word pal = values.get(position);
		TextView word = (TextView) view.findViewById(R.id.word);
		TextView tranlation = (TextView) view.findViewById(R.id.translation);

		// Tipos
		TextView labels[] = new TextView[Configuraciones.NUM_TYPES];
		// noun //verb //adj //adv //phrasal //expresion //other
		labels[Word.NOUN] = (TextView) view.findViewById(R.id.noun);
		labels[Word.VERB] = (TextView) view.findViewById(R.id.verb);
		labels[Word.ADJECTIVE] = (TextView) view.findViewById(R.id.adj);
		labels[Word.ADVERB] = (TextView) view.findViewById(R.id.adverb);
		labels[Word.PHRASAL] = (TextView) view.findViewById(R.id.phrasal);
		labels[Word.EXPRESSION] = (TextView) view.findViewById(R.id.expression);
		labels[Word.OTHER] = (TextView) view.findViewById(R.id.other);

		int type = pal.getType();
		for (int i = 0; i < Configuraciones.NUM_TYPES; ++i) {
			if ((type & (1 << i)) != 0) {
				labels[i].setBackgroundColor(Configuraciones.type_colors[i]);
			} else {
				labels[i].setBackgroundColor(0xFFCCCCCC);
			}
		}
		if (!phMode) {
			labels[Word.PHRASAL].setVisibility(View.GONE);
		}

		word.setText(pal.getName());
		tranlation.setText(pal.getTranslation());
		return view;
	}

}
