package com.delvinglanguages.view.lister;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.theme.ThemePair;
import com.delvinglanguages.kernel.util.ThemePairs;

public class ThemePairEditLister extends RecyclerView.Adapter<ThemePairEditLister.ViewHolder> {

    private ThemePairs dataset;
    private View.OnClickListener clickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView pair1, pair2;
        public ImageButton edit, delete;

        public ViewHolder(View v, View.OnClickListener clickListener) {
            super(v);
            pair1 = (TextView) v.findViewById(R.id.pair_1);
            pair2 = (TextView) v.findViewById(R.id.pair_2);

            edit = (ImageButton) v.findViewById(R.id.edit);
            delete = (ImageButton) v.findViewById(R.id.delete);

            delete.setOnClickListener(clickListener);
            edit.setOnClickListener(clickListener);
        }

    }

    public ThemePairEditLister(ThemePairs dataset, View.OnClickListener clickListener) {
        this.dataset = dataset;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_theme_pair_edit, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ThemePair pair = dataset.get(position);

        holder.pair1.setText(pair.inDelved);
        holder.pair2.setText(pair.inNative);

        holder.edit.setTag(pair);
        holder.delete.setTag(pair);

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

}
