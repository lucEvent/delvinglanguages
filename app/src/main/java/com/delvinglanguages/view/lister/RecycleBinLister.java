package com.delvinglanguages.view.lister;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.view.lister.viewholder.RecycledItemViewHolder;

public class RecycleBinLister extends RecyclerView.Adapter<RecycledItemViewHolder> {

    private DReferences items;

    private View.OnClickListener onRestoreItemListener;

    public RecycleBinLister(DReferences items, View.OnClickListener itemListener)
    {
        this.items = items;
        this.onRestoreItemListener = itemListener;
    }

    @Override
    public RecycledItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_recycle_bin, parent, false);
        return new RecycledItemViewHolder(v, onRestoreItemListener);
    }

    @Override
    public void onBindViewHolder(RecycledItemViewHolder holder, int position)
    {
        RecycledItemViewHolder.populateViewHolder(holder, items.get(position));
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

}
