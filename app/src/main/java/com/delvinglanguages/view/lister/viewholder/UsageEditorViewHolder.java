package com.delvinglanguages.view.lister.viewholder;

import android.view.View;
import android.widget.ImageView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.Usage;

public class UsageEditorViewHolder extends UsageViewHolder {

    private View view;
    private ImageView edit, delete;

    public UsageEditorViewHolder(View v, View.OnClickListener onEditListener,
                                 View.OnClickListener onDeleteListener)
    {
        super(v);

        view = v;
        edit = (ImageView) v.findViewById(R.id.edit);
        delete = (ImageView) v.findViewById(R.id.delete);

        view.setOnClickListener(onEditListener);
        edit.setOnClickListener(onEditListener);
        delete.setOnClickListener(onDeleteListener);
    }

    @Override
    public void populate(Usage usage)
    {
        super.populate(usage);

        view.setTag(usage);
        edit.setTag(usage);
        delete.setTag(usage);
    }

}