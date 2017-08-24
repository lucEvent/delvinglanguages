package com.delvinglanguages.view.lister;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.Usage;
import com.delvinglanguages.kernel.util.Usages;
import com.delvinglanguages.view.lister.viewholder.UsageEditorViewHolder;

import java.util.ArrayList;
import java.util.Collection;

public class UsageEditorLister extends RecyclerView.Adapter<UsageEditorViewHolder> {

    private ArrayList<Usage> dataSet;

    private LayoutInflater inflater;

    private View.OnClickListener onModifyListener, onDeleteListener;

    public UsageEditorLister(Context context, Usages dataSet, View.OnClickListener onModifyListener,
                             View.OnClickListener onDeleteListener)
    {
        this.inflater = LayoutInflater.from(context);
        this.dataSet = new ArrayList<>(dataSet.values());
        this.onModifyListener = onModifyListener;
        this.onDeleteListener = onDeleteListener;
    }

    @Override
    public UsageEditorViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = inflater.inflate(R.layout.i_usage_edit, parent, false);
        return new UsageEditorViewHolder(v, onModifyListener, onDeleteListener);
    }

    @Override
    public void onBindViewHolder(UsageEditorViewHolder holder, int position)
    {
        holder.populate(dataSet.get(position));
    }

    @Override
    public int getItemCount()
    {
        return dataSet.size();
    }

    public void updateUsage(Usage usage)
    {
        int position = dataSet.indexOf(usage);
        notifyItemChanged(position, usage);
    }

}