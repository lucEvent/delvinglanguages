package com.delvinglanguages.listers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.settings.Settings;

public class WordLister extends ArrayAdapter<Word> {

    private LayoutInflater inflater;
    private boolean phMode;

    public WordLister(Context context, Words values) {
        super(context, R.layout.i_word, values);
        this.phMode = LanguageKernelControl.getLanguageSettings(Language.MASK_PH);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.i_word, parent, false);
        }
        Word word = getItem(position);
        ((TextView) view.findViewById(R.id.word)).setText(word.getName());
        ((TextView) view.findViewById(R.id.translation)).setText(word.getTranslationsAsString());

        TextView labels[] = new TextView[7];
        labels[Word.NOUN] = (TextView) view.findViewById(R.id.noun);
        labels[Word.VERB] = (TextView) view.findViewById(R.id.verb);
        labels[Word.ADJECTIVE] = (TextView) view.findViewById(R.id.adjective);
        labels[Word.ADVERB] = (TextView) view.findViewById(R.id.adverb);
        labels[Word.PHRASAL] = (TextView) view.findViewById(R.id.phrasal);
        labels[Word.EXPRESSION] = (TextView) view.findViewById(R.id.expression);
        labels[Word.OTHER] = (TextView) view.findViewById(R.id.other);

        Settings.setBackgroundColorsforType(labels, word.getType());
        if (!phMode) {
            labels[Word.PHRASAL].setVisibility(View.GONE);
        }

        view.setTag(word);
        return view;
    }
}
