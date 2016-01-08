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
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.set.Words;

public class RemovedWordLister extends ArrayAdapter<Word> implements OnClickListener {

    private LayoutInflater inflater;

    public RemovedWordLister(Context context, Words values) {
        super(context, R.layout.i_bin, values);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.i_bin, parent, false);
        }
        Word word = getItem(position);

        TextView name = (TextView) view.findViewById(R.id.word);
        TextView translation = (TextView) view.findViewById(R.id.translation);
        ImageButton restore = (ImageButton) view.findViewById(R.id.restore);

        name.setText(word.getName());
        translation.setText(word.getTranslationsAsString());
        restore.setTag(position);
        restore.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View button) {
        KernelControl.restoreWord((Integer) button.getTag());
        notifyDataSetChanged();
    }

}
