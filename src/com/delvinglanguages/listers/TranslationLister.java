package com.delvinglanguages.listers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.Translation;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.kernel.svenska.SwedishTranslation;
import com.delvinglanguages.net.internal.Messages;
import com.delvinglanguages.net.internal.NetWork;
import com.delvinglanguages.settings.Settings;

public class TranslationLister extends ArrayAdapter<Translation> implements OnClickListener, Messages {

	private static final int[] backgrounds = { R.drawable.button_noun_pressed, R.drawable.button_verb_pressed, R.drawable.button_adjective_pressed,
			R.drawable.button_adverb_pressed, R.drawable.button_phrasals_pressed, R.drawable.button_expression_pressed,
			R.drawable.button_other_pressed };

	private static final String[] s_types = { "NOUN", "VERB", "ADJECTIVE", "ADVERB", "PHRASAL", "EXPRESSION", "OTHER" };

	private Translations values;
	private LayoutInflater inflater;

	private boolean modify_enabled;
	private NetWork network;

	public TranslationLister(Context context, Translations values, boolean modify_enable, NetWork network) {
		super(context, R.layout.i_translation, values);
		this.values = values;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.modify_enabled = modify_enable;
		this.network = network;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.i_translation, parent, false);
		}
		Translation t = values.get(position);
		// read ids elems
		TextView t_type = (TextView) view.findViewById(R.id.type);

		for (int i = 0; i < s_types.length; ++i) {
			if ((t.type & (1 << i)) != 0) {
				t_type.setText(s_types[i]);
				t_type.setBackgroundResource(backgrounds[i]);
				view.setBackgroundResource(backgrounds[i]);
				break;
			}
		}

		LinearLayout listOfTransalations = (LinearLayout) view.findViewById(R.id.translations_list);
		listOfTransalations.removeAllViews();
		for (String s_trans : t.getItems()) {
			listOfTransalations.addView(createTranslationView(s_trans, listOfTransalations));
		}

		LinearLayout tit_trans = (LinearLayout) view.findViewById(R.id.title_translations);
		LinearLayout tit_forms = (LinearLayout) view.findViewById(R.id.title_forms);
		if (LanguageKernelControl.getCode() == Language.SV && t.type <= (1 << Word.ADJECTIVE)) {
			SwedishTranslation SVT = KernelControl.getSwedishForm(t);

			if (SVT.forms != null) {
				tit_trans.setVisibility(LinearLayout.VISIBLE);
				tit_forms.setVisibility(LinearLayout.VISIBLE);

				String[] labels;
				if (t.type == (1 << Word.NOUN)) {
					labels = new String[] { "Sing. indef.: ", "Sing. Def.: ", "Pl. indef.: ", "Pl. def.: ", "", "" };
				} else if (t.type == (1 << Word.VERB)) {
					labels = new String[] { "Imperativ: ", "Infinitiv: ", "Presens: ", "Preteritum: ", "Supinum: ", "" };
				} else if (t.type == (1 << Word.ADJECTIVE)) {
					labels = new String[] { "En-Sing.: ", "Ett-Sing.: ", "Plural: ", "Comparative: ", "Superlative: ", "" };
				} else {
					labels = new String[] { "", "", "", "", "", "" };
				}

				((TextView) view.findViewById(R.id.lab1)).setText(labels[0]);
				((TextView) view.findViewById(R.id.lab2)).setText(labels[1]);
				((TextView) view.findViewById(R.id.lab3)).setText(labels[2]);
				((TextView) view.findViewById(R.id.lab4)).setText(labels[3]);
				((TextView) view.findViewById(R.id.lab5)).setText(labels[4]);
				((TextView) view.findViewById(R.id.lab6)).setText(labels[5]);

				((TextView) view.findViewById(R.id.form1)).setText(SVT.forms[0]);
				((TextView) view.findViewById(R.id.form2)).setText(SVT.forms[1]);
				((TextView) view.findViewById(R.id.form3)).setText(SVT.forms[2]);
				((TextView) view.findViewById(R.id.form4)).setText(SVT.forms[3]);
				((TextView) view.findViewById(R.id.form5)).setText(SVT.forms[4]);
				((TextView) view.findViewById(R.id.form6)).setText(SVT.forms[5]);
			} else {
				tit_trans.setVisibility(LinearLayout.GONE);
				tit_forms.setVisibility(LinearLayout.GONE);
			}
		} else {
			tit_trans.setVisibility(LinearLayout.GONE);
			tit_forms.setVisibility(LinearLayout.GONE);
		}
		if (modify_enabled) {
			ImageButton remove = (ImageButton) view.findViewById(R.id.remove);
			ImageButton edit = (ImageButton) view.findViewById(R.id.edit);

			remove.setVisibility(Button.VISIBLE);
			edit.setVisibility(Button.VISIBLE);

			remove.setTag(position);
			edit.setTag(position);

			remove.setOnClickListener(this);
			edit.setOnClickListener(this);
		}

		return view;
	}

	private View createTranslationView(String s_trans, ViewGroup parent) {

		View view = inflater.inflate(R.layout.i_bulleted_row, null);

		((TextView) view.findViewById(R.id.text)).setText(s_trans);

		return view;
	}

	@Override
	public void onClick(View v) {
		network.datagram(NetWork.ACTION, v.getId() == R.id.edit ? ACTION_EDIT : ACTION_REMOVE, v.getTag());
	}

	private void debug(String text) {
		if (Settings.DEBUG)
			android.util.Log.d("##TranslationLister##", text);
	}

}
