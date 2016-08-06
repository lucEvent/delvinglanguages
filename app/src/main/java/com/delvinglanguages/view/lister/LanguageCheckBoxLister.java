package com.delvinglanguages.view.lister;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.util.Languages;

public class LanguageCheckBoxLister extends ArrayAdapter<Language> implements View.OnClickListener {

    private boolean[] checks;
    private LayoutInflater inflater;

    public LanguageCheckBoxLister(Context context, Languages values)
    {
        super(context, R.layout.i_checkbox, values);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checks = new boolean[values.size()];
        for (int i = 0; i < checks.length; i++)
            checks[i] = false;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null) {
            view = inflater.inflate(R.layout.i_checkbox, parent, false);

            view.setOnClickListener(this);
            view.findViewById(R.id.checkbox).setOnClickListener(this);
        }
        Language language = getItem(position);

        ((TextView) view.findViewById(R.id.text)).setText(language.language_name);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        checkBox.setTag(position);
        checkBox.setChecked(checks[position]);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        CheckBox checkBox;
        if (v instanceof CheckBox) {
            checkBox = (CheckBox) v;
        } else {
            checkBox = (CheckBox) v.findViewById(R.id.checkbox);
            checkBox.toggle();
        }
        checks[(int) checkBox.getTag()] = checkBox.isChecked();
    }

    public boolean[] getChecks()
    {
        return checks;
    }

}