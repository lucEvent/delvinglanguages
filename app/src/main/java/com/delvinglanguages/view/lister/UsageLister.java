package com.delvinglanguages.view.lister;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.Usage;
import com.delvinglanguages.kernel.util.Usages;
import com.delvinglanguages.view.lister.viewholder.UsageViewHolder;

public class UsageLister extends RecyclerView.Adapter<UsageViewHolder> {

    private Usages dataSet;

    private LayoutInflater inflater;

    public UsageLister(Context context, Usages dataSet)
    {
        this.inflater = LayoutInflater.from(context);
        this.dataSet = dataSet;
    }

    @Override
    public UsageViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = inflater.inflate(R.layout.i_translation_with_usage, parent, false);
        return new UsageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UsageViewHolder holder, int position)
    {
        Usage usage = dataSet.get(position);
        holder.populate(usage);
    }

    @Override
    public int getItemCount()
    {
        return dataSet.size();
    }

}