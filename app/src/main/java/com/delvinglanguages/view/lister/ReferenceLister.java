package com.delvinglanguages.view.lister;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.Item;
import com.delvinglanguages.view.lister.viewholder.ReferenceViewHolder;
import com.delvinglanguages.view.utils.ItemListRVAdapter;

import java.util.Collection;

public class ReferenceLister extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final ItemListRVAdapter dataSet;
    protected boolean phv_enabled;
    protected View.OnClickListener itemListener;

    public ReferenceLister(DReferences dataSet, boolean phv_enabled, View.OnClickListener itemListener)
    {
        this.phv_enabled = phv_enabled;
        this.itemListener = itemListener;

        this.dataSet = new ItemListRVAdapter(this);
        for (DReference n : dataSet)
            this.dataSet.add(n);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_dreference, parent, false);
        v.setOnClickListener(itemListener);
        return new ReferenceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        ReferenceViewHolder.populateViewHolder((ReferenceViewHolder) holder, (DReference) dataSet.get(position), phv_enabled);
    }

    @Override
    public int getItemCount()
    {
        return dataSet.size();
    }

    public final void setPhrasals(boolean enabled)
    {
        this.phv_enabled = enabled;
    }

    public final void addItem(Item item)
    {
        synchronized (this.dataSet) {
            dataSet.add(item);
        }
    }

    public final void setNewDataSet(Collection<Item> newDataSet)
    {
        synchronized (this.dataSet) {
            dataSet.clear();
            dataSet.addAll(newDataSet);
        }
    }

    public final void clear()
    {
        synchronized (this.dataSet) {
            dataSet.clear();
        }
    }

    public final boolean isEmpty()
    {
        return dataSet.size() == 0;
    }

}
