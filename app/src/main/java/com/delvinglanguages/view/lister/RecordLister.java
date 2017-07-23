package com.delvinglanguages.view.lister;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.record.Records;
import com.delvinglanguages.view.lister.viewholder.RecordViewHolder;

public class RecordLister extends RecyclerView.Adapter<RecordViewHolder> {

    private Records items;

    public RecordLister(Records items)
    {
        this.items = items;
    }

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_record, parent, false);
        return new RecordViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, int position)
    {
        RecordViewHolder.populateViewHolder(holder, items.get(position));
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public void clear()
    {
        notifyItemRangeRemoved(0, items.size());
        items.clear();
    }
}
