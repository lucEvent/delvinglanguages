package com.delvinglanguages.listers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.IDDelved;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.settings.Settings;

public class WordLister extends ArrayAdapter<Word> {

	private Words values;
	private LayoutInflater inflater;
	private boolean phMode;

	public WordLister(Context context, Words values) {
		super(context, R.layout.i_word, values);
		this.values = values;
		this.phMode = LanguageKernelControl.getLanguageSettings(IDDelved.MASK_PH);
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		TextView labels[] = new TextView[Settings.NUM_TYPES];
		labels[Word.NOUN] = (TextView) view.findViewById(R.id.noun);
		labels[Word.VERB] = (TextView) view.findViewById(R.id.verb);
		labels[Word.ADJECTIVE] = (TextView) view.findViewById(R.id.adj);
		labels[Word.ADVERB] = (TextView) view.findViewById(R.id.adverb);
		labels[Word.PHRASAL] = (TextView) view.findViewById(R.id.phrasal);
		labels[Word.EXPRESSION] = (TextView) view.findViewById(R.id.expression);
		labels[Word.OTHER] = (TextView) view.findViewById(R.id.other);

		int type = pal.getType();
		for (int i = 0; i < Settings.NUM_TYPES; ++i) {
			if ((type & (1 << i)) != 0) {
				labels[i].setBackgroundColor(Settings.type_colors[i]);
			} else {
				labels[i].setBackgroundColor(0xFFCCCCCC);
			}
		}
		if (!phMode) {
			labels[Word.PHRASAL].setVisibility(View.GONE);
		}

		word.setText(pal.getName());
		tranlation.setText(pal.getTranslationString());
		return view;
	}

}
