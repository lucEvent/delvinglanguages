package com.delvinglanguages.view.lister;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.theme.ThemePair;
import com.delvinglanguages.kernel.util.ThemePairs;

public class ThemePairLister extends RecyclerView.Adapter<ThemePairLister.ViewHolder> {

    private ThemePairs dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView pair1, pair2;

        public ViewHolder(View v)
        {
            super(v);
            pair1 = (TextView) v.findViewById(R.id.pair_1);
            pair2 = (TextView) v.findViewById(R.id.pair_2);
        }

    }

    public ThemePairLister(ThemePairs dataset)
    {
        this.dataset = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_theme_pair, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        ThemePair pair = dataset.get(position);

        holder.pair1.setText(pair.inDelved);
        holder.pair2.setText(pair.inNative);
    }

    @Override
    public int getItemCount()
    {
        return dataset.size();
    }

}
