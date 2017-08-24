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

import java.util.ArrayList;
import java.util.Collection;

public class ReferenceLister extends SearchableLister<Item> {

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
        ((ReferenceViewHolder) holder).bind((DReference) dataSet.get(position), phv_enabled);
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

    @Override
    public final void addItem(Item item)
    {
        synchronized (this.dataSet) {
            dataSet.add(item);
        }
    }

    @Override
    public final void updateItem(Item item)
    {
        synchronized (this.dataSet) {
            int position = dataSet.indexOf(item);
            dataSet.updateItemAt(position, item);
        }
    }

    @Override
    public final void removeItem(Item item)
    {
        synchronized (this.dataSet) {
            dataSet.remove(item);
        }
    }

    @Override
    public void replaceAll(ArrayList<? extends Item> newItems)
    {
        synchronized (this.dataSet) {
            dataSet.beginBatchedUpdates();
            for (int i = dataSet.size() - 1; i >= 0; i--) {
                final Item item = dataSet.get(i);
                if (!newItems.contains(item))
                    dataSet.remove(item);
            }
            dataSet.addAll((Collection<Item>) newItems);
            dataSet.endBatchedUpdates();
        }
    }

    public final void removeItem(int position)
    {
        synchronized (this.dataSet) {
            dataSet.removeItemAt(position);
        }
    }


    public final void clear()
    {
        synchronized (this.dataSet) {
            dataSet.clear();
        }
    }

    @Override
    public final boolean isEmpty()
    {
        return dataSet.size() == 0;
    }

}
