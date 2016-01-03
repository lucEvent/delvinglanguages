package com.delvinglanguages.listers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.set.ThemePairs;
import com.delvinglanguages.kernel.theme.ThemePair;

public class ThemePairInputLister extends ArrayAdapter<ThemePair> implements OnClickListener {

    private LayoutInflater inflater;
    private OnClickListener edit_listener;

    public ThemePairInputLister(Context context, ThemePairs values, OnClickListener listener) {
        super(context, R.layout.i_theme_pair_create, values);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.edit_listener = listener;
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.i_theme_pair_create, parent, false);
        }
        ThemePair pair = getItem(position);
        ((TextView) view.findViewById(R.id.pair_1)).setText(pair.inDelved);
        ((TextView) view.findViewById(R.id.pair_2)).setText(pair.inNative);
        ImageButton delete = (ImageButton) view.findViewById(R.id.delete);
        delete.setTag(position);
        delete.setOnClickListener(this);
        ImageButton edit = (ImageButton) view.findViewById(R.id.edit);
        edit.setTag(position);
        edit.setOnClickListener(edit_listener);
        return view;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        remove(getItem(position));
        notifyDataSetChanged();
    }

}
