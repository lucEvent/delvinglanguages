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
import com.delvinglanguages.kernel.Inflexion;
import com.delvinglanguages.kernel.set.Inflexions;
import com.delvinglanguages.settings.Settings;

public class InflexionLister extends ArrayAdapter<Inflexion> {

    private static final int[] backgrounds = {R.drawable.button_noun_pressed,
            R.drawable.button_verb_pressed, R.drawable.button_adjective_pressed,
            R.drawable.button_adverb_pressed, R.drawable.button_phrasals_pressed,
            R.drawable.button_expression_pressed, R.drawable.button_preposition_pressed,
            R.drawable.button_conjunction_pressed, R.drawable.button_other_pressed};

    private static final int[] s_types = {
            R.string.noun_cap, R.string.verb_cap, R.string.adjective_cap, R.string.adverb_cap,
            R.string.phrasal_cap, R.string.expression_cap, R.string.preposition_cap,
            R.string.conjunction_cap, R.string.other_cap
    };

    private LayoutInflater inflater;

    private boolean modify_enabled;
    private OnClickListener clickListener;

    public InflexionLister(Context context, Inflexions values, boolean modify_enable, OnClickListener clickListener) {
        super(context, R.layout.i_inflexion, values);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.modify_enabled = modify_enable;
        this.clickListener = clickListener;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.i_inflexion, parent, false);
        }
        Inflexion inflexion = getItem(position);

        TextView vtype = (TextView) view.findViewById(R.id.type);

        for (int i = 0; i < s_types.length; ++i) {
            if ((inflexion.getType() & (1 << i)) != 0) {
                vtype.setText(s_types[i]);
                vtype.setBackgroundResource(backgrounds[i]);
                view.setBackgroundResource(backgrounds[i]);
                break;
            }
        }

        LinearLayout listOfTranslations = (LinearLayout) view.findViewById(R.id.translations_list);
        listOfTranslations.removeAllViews();
        for (String T : inflexion.getTranslations()) {
            View v_trans = inflater.inflate(R.layout.i_bulleted_row, null);
            TextView text = (TextView) v_trans.findViewById(R.id.text);
            text.setText(T);
            v_trans.setOnClickListener(clickListener);
            listOfTranslations.addView(v_trans);
        }

        LinearLayout tit_trans = (LinearLayout) view.findViewById(R.id.title_translations);
        LinearLayout tit_forms = (LinearLayout) view.findViewById(R.id.layout_inflexions);
        if (inflexion.hasInflexions()) {
            tit_trans.setVisibility(LinearLayout.VISIBLE);
            tit_forms.setVisibility(LinearLayout.VISIBLE);

            int[] labids = new int[]{R.id.form1, R.id.form2, R.id.form3, R.id.form4, R.id.form5, R.id.form6};
            String[] forms = inflexion.getInflexions();
            for (int i = 0; i < forms.length; i++) {
                ((TextView) view.findViewById(labids[i])).setText(forms[i]);
                ((TextView) view.findViewById(labids[i])).setVisibility(TextView.VISIBLE);
            }
            for (int i = forms.length; i < labids.length; i++) {
                ((TextView) view.findViewById(labids[i])).setVisibility(TextView.GONE);
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

            remove.setOnClickListener(clickListener);
            edit.setOnClickListener(clickListener);
        }
        return view;
    }

    private void debug(String text) {
        if (Settings.DEBUG)
            android.util.Log.d("##TranslationLister##", text);
    }

}
