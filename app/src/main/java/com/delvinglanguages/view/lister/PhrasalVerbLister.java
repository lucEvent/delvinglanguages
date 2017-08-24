package com.delvinglanguages.view.lister;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.phrasalverb.PhrasalVerb;
import com.delvinglanguages.view.lister.sorted.PhrasalVerbSortedList;
import com.delvinglanguages.view.lister.viewholder.PhrasalVerbViewHolder;

import java.util.ArrayList;
import java.util.Collection;

public class PhrasalVerbLister extends SearchableLister<PhrasalVerb> {

    private final PhrasalVerbSortedList dataSet;
    private View.OnClickListener itemListener;
    private LayoutInflater inflater;

    public PhrasalVerbLister(Context context, Collection<PhrasalVerb> dataSet, View.OnClickListener itemListener)
    {
        this.itemListener = itemListener;
        this.inflater = LayoutInflater.from(context);

        this.dataSet = new PhrasalVerbSortedList(this);
        for (PhrasalVerb n : dataSet)
            this.dataSet.add(n);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = inflater.inflate(R.layout.i_phrasal_verb, parent, false);
        return new PhrasalVerbViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        ((PhrasalVerbViewHolder) holder).bind(dataSet.get(position), inflater, itemListener);
    }

    @Override
    public int getItemCount()
    {
        return dataSet.size();
    }

    @Override
    public final void addItem(PhrasalVerb item)
    {
        synchronized (this.dataSet) {
            dataSet.add(item);
        }
    }

    @Override
    public final void updateItem(PhrasalVerb item)
    {
        synchronized (this.dataSet) {
            int position = dataSet.indexOf(item);
            dataSet.updateItemAt(position, item);
        }
    }

    @Override
    public final void removeItem(PhrasalVerb item)
    {
        synchronized (this.dataSet) {
            dataSet.remove(item);
        }
    }

    @Override
    public void replaceAll(ArrayList<? extends PhrasalVerb> newItems)
    {
        synchronized (this.dataSet) {
            dataSet.beginBatchedUpdates();
            for (int i = dataSet.size() - 1; i >= 0; i--) {
                final PhrasalVerb item = dataSet.get(i);
                if (!newItems.contains(item))
                    dataSet.remove(item);
            }
            dataSet.addAll((Collection<PhrasalVerb>) newItems);
            dataSet.endBatchedUpdates();
        }
    }

    @Override
    public final boolean isEmpty()
    {
        return dataSet.size() == 0;
    }

}
