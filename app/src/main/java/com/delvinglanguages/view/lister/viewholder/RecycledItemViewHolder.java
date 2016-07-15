package com.delvinglanguages.view.lister.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;

public class RecycledItemViewHolder extends RecyclerView.ViewHolder {

    private ImageButton restore;
    private TextView name, translation;

    public RecycledItemViewHolder(View v, View.OnClickListener itemListener)
    {
        super(v);
        name = (TextView) v.findViewById(R.id.word);
        translation = (TextView) v.findViewById(R.id.translation);
        restore = (ImageButton) v.findViewById(R.id.restore);
        restore.setOnClickListener(itemListener);
    }

    public static void populateViewHolder(RecycledItemViewHolder holder, DReference reference)
    {
        holder.name.setText(reference.name);
        holder.translation.setText(reference.getTranslationsAsString());
        holder.restore.setTag(reference);
    }

}
