package com.delvinglanguages.face.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.Word;

public class ReferenceTypeView {

	private LayoutInflater inflater;

	public ReferenceTypeView(Context context) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	private String[] types = { "noun", "verb", "adjective", "adverb", "phrasal verb", "expression", "other" };
	private int[] bgs = { R.drawable.button_bg_noun, R.drawable.button_bg_vb, R.drawable.button_bg_adj, R.drawable.button_bg_adv,
			R.drawable.button_bg_phr_v, R.drawable.button_bg_expr, R.drawable.button_bg_oth };

	private int[] labelsIds = { R.id.lab1, R.id.lab2, R.id.lab3, R.id.lab4, R.id.lab5, R.id.lab6, R.id.lab7 };
	private int[] formsIds = { R.id.form1, R.id.form2, R.id.form3, R.id.form4, R.id.form5, R.id.form6, R.id.form7 };

	public View create(int reftype) {
		View view = inflater.inflate(R.layout.i_translation, null);

		view.setBackgroundResource(bgs[reftype]);

		TextView type = (TextView) view.findViewById(R.id.type);
		type.setText(types[reftype]);
		type.setBackgroundResource(bgs[reftype]);

		// ((ListView) view.findViewById(R.id.translations_list)).setAdapter(new
		// TranslationLister(context, reference.getTranslationArray(null),
		// false));

		String[] labels = null, forms = null;
		switch (reftype) {
		case Word.NOUN:

			break;
		case Word.VERB:
		case Word.PHRASAL:

			break;
		case Word.ADJECTIVE:

			break;
		default:

		}

		for (int i = 0; i < labels.length; i++) {
			((TextView) view.findViewById(labelsIds[i])).setText(labels[i]);
			((TextView) view.findViewById(formsIds[i])).setText(forms[i]);
		}

		return view;
	}
}
