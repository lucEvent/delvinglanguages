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
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.set.Inflexions;
import com.delvinglanguages.settings.Settings;

public class InflexionLister extends ArrayAdapter<Inflexion> {

    private static final int[] backgrounds = {R.drawable.button_noun_pressed,
            R.drawable.button_verb_pressed, R.drawable.button_adjective_pressed,
            R.drawable.button_adverb_pressed, R.drawable.button_phrasals_pressed,
            R.drawable.button_expression_pressed, R.drawable.button_other_pressed};

    private static final String[] s_types = {"NOUN", "VERB", "ADJECTIVE", "ADVERB", "PHRASAL", "EXPRESSION", "OTHER"};

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
//todo make it work for any lang, not just swedish
            tit_trans.setVisibility(LinearLayout.VISIBLE);
            tit_forms.setVisibility(LinearLayout.VISIBLE);

            String[] labels;
            int type = inflexion.getType();
            if (type == (1 << Word.NOUN)) {
                labels = new String[]{"Sing. indef.: ", "Sing. Def.: ", "Pl. indef.: ", "Pl. def.: ", ""};
            } else if (type == (1 << Word.VERB)) {
                labels = new String[]{"Imperativ: ", "Infinitiv: ", "Presens: ", "Preteritum: ", "Supinum: "};
            } else if (type == (1 << Word.ADJECTIVE)) {
                labels = new String[]{"En-Sing.: ", "Ett-Sing.: ", "Plural: ", "Comparative: ", "Superlative: "};
            } else {
                labels = new String[]{"", "", "", "", ""};
            }

            ((TextView) view.findViewById(R.id.lab1)).setText(labels[0]);
            ((TextView) view.findViewById(R.id.lab2)).setText(labels[1]);
            ((TextView) view.findViewById(R.id.lab3)).setText(labels[2]);
            ((TextView) view.findViewById(R.id.lab4)).setText(labels[3]);
            TextView tmp_lab = (TextView) view.findViewById(R.id.lab5);
            tmp_lab.setText(labels[4]);
            //   ((TextView) view.findViewById(R.id.lab6)).setText(labels[5]);

            String[] inflexions = inflexion.getInflexions();
            ((TextView) view.findViewById(R.id.form1)).setText(inflexions[0]);
            ((TextView) view.findViewById(R.id.form2)).setText(inflexions[1]);
            ((TextView) view.findViewById(R.id.form3)).setText(inflexions[2]);
            ((TextView) view.findViewById(R.id.form4)).setText(inflexions[3]);
            TextView tmp = (TextView) view.findViewById(R.id.form5);
            if (inflexions.length > 4) {
                tmp.setText(inflexions[4]);
                tmp.setVisibility(TextView.VISIBLE);
                tmp_lab.setVisibility(TextView.VISIBLE);
            } else {
                tmp.setVisibility(TextView.GONE);
                tmp_lab.setVisibility(TextView.GONE);
            }
            //      ((TextView) view.findViewById(R.id.form6)).setText(inflexions.length > 5 ? inflexions[5] : "");

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
