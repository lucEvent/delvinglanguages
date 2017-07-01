package com.delvinglanguages.view.lister;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.LanguageCode;

public class SpinnerLanguageLister extends ArrayAdapter<String> {

    private int[] codes;
    private LayoutInflater inflater;

    public SpinnerLanguageLister(Context context, String[] values, int[] codes)
    {
        super(context, R.layout.i_spinner_language, values);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.codes = codes;
    }

    @Override
    public View getDropDownView(int position, @Nullable View view, @NonNull ViewGroup parent)
    {
        return getView(position, view, parent);
    }

    @Override
    public
    @NonNull
    View getView(int position, @Nullable View view, @NonNull ViewGroup parent)
    {
        if (view == null)
            view = inflater.inflate(R.layout.i_spinner_language, parent, false);

        ((TextView) view.findViewById(R.id.title)).setText(getItem(position));
        view.findViewById(R.id.image).setBackgroundResource(LanguageCode.getFlagResId(codes[position]));

        return view;
    }

}