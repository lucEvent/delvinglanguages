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
import com.delvinglanguages.kernel.IDDelved;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.Translation;
import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.net.internal.Messages;
import com.delvinglanguages.net.internal.NetWork;
import com.delvinglanguages.settings.Settings;

public class TranslationLister extends ArrayAdapter<Translation> implements OnClickListener, Messages {

	private static final int[] backgrounds = { R.drawable.button_bg_noun, R.drawable.button_bg_vb, R.drawable.button_bg_adj,
			R.drawable.button_bg_adv, R.drawable.button_bg_phr_v, R.drawable.button_bg_expr, R.drawable.button_bg_oth };

	private static final String[] s_types = { "NOUN", "VERB", "ADJECTIVE", "ADVERB", "PHRASAL", "EXPRESSION", "OTHER" };

	private static final String DEBUG = "##TranslationLister##";

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

		for (int i = 0; i < Settings.NUM_TYPES; ++i) {
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
		if (LanguageKernelControl.getCode() == IDDelved.SV) {
			LinearLayout tit_trans = (LinearLayout) view.findViewById(R.id.title_translations);
			tit_trans.setVisibility(LinearLayout.VISIBLE);
			LinearLayout tit_forms = (LinearLayout) view.findViewById(R.id.title_forms);
			tit_forms.setVisibility(LinearLayout.VISIBLE);

			TextView t_lab1 = (TextView) view.findViewById(R.id.lab1);
			TextView t_lab2 = (TextView) view.findViewById(R.id.lab2);
			TextView t_lab3 = (TextView) view.findViewById(R.id.lab3);
			TextView t_lab4 = (TextView) view.findViewById(R.id.lab4);
			TextView t_lab5 = (TextView) view.findViewById(R.id.lab5);
			TextView t_lab6 = (TextView) view.findViewById(R.id.lab6);
			TextView t_lab7 = (TextView) view.findViewById(R.id.lab7);

			TextView t_form1 = (TextView) view.findViewById(R.id.form1);
			TextView t_form2 = (TextView) view.findViewById(R.id.form2);
			TextView t_form3 = (TextView) view.findViewById(R.id.form3);
			TextView t_form4 = (TextView) view.findViewById(R.id.form4);
			TextView t_form5 = (TextView) view.findViewById(R.id.form5);
			TextView t_form6 = (TextView) view.findViewById(R.id.form6);
			TextView t_form7 = (TextView) view.findViewById(R.id.form7);

			t_lab6.setVisibility(TextView.GONE);
			t_lab7.setVisibility(TextView.GONE);
			t_form6.setVisibility(TextView.GONE);
			t_form7.setVisibility(TextView.GONE);
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

		((TextView) view.findViewById(R.id.text

		)).setText(s_trans);

		return view;
	}

	@Override
	public void onClick(View v) {
		network.datagram(NetWork.ACTION, v.getId() == R.id.edit ? ACTION_EDIT : ACTION_REMOVE, v.getTag());
	}

}
