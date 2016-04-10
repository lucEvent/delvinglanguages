package com.delvinglanguages.view.lister;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.util.DReferences;

public class RecycleBinLister extends ArrayAdapter<DReference> {

    private LayoutInflater inflater;
    private OnClickListener onRestoreItemListener;

    public RecycleBinLister(Context context, DReferences values, OnClickListener onClickListener) {
        super(context, R.layout.i_recycle_bin, values);
        this.onRestoreItemListener = onClickListener;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.i_recycle_bin, parent, false);
        }
        DReference reference = getItem(position);

        TextView name = (TextView) view.findViewById(R.id.word);
        TextView translation = (TextView) view.findViewById(R.id.translation);
        ImageButton restore = (ImageButton) view.findViewById(R.id.restore);

        name.setText(reference.name);
        translation.setText(reference.getTranslationsAsString());
        restore.setTag(reference);
        restore.setOnClickListener(onRestoreItemListener);
        return view;
    }

}
