package com.delvinglanguages.view.lister;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.view.lister.sorted.ReferenceSelectorList;
import com.delvinglanguages.view.lister.util.ReferenceSelector;
import com.delvinglanguages.view.lister.viewholder.ReferenceSelectorViewHolder;

import java.util.ArrayList;
import java.util.Collection;

public class ReferenceSelectorLister extends SearchableLister<ReferenceSelector> implements View.OnClickListener {

    private final DReferences selectedReferences;
    private final ReferenceSelectorList dataSet;
    private final boolean phv_enabled;

    public ReferenceSelectorLister(ArrayList<ReferenceSelector> dataSet, boolean phv_enabled)
    {
        this.phv_enabled = phv_enabled;
        this.selectedReferences = new DReferences();

        this.dataSet = new ReferenceSelectorList(this);
        for (ReferenceSelector s : dataSet)
            this.dataSet.add(s);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_dreference, parent, false);
        v.setOnClickListener(this);
        return new ReferenceSelectorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        ((ReferenceSelectorViewHolder) holder).bind(dataSet.get(position), phv_enabled, position);
    }

    @Override
    public int getItemCount()
    {
        return dataSet.size();
    }

    @Override
    public void onClick(View v)
    {
        int position = (int) v.getTag();

        ReferenceSelector selector = dataSet.get(position);
        selector.selected = !selector.selected;

        if (selector.selected)
            selectedReferences.add(selector.reference);
        else
            selectedReferences.remove(selector.reference);

        dataSet.updateItemAt(position, selector);
    }

    @Override
    public void addItem(ReferenceSelector item)
    {
    }

    @Override
    public final void updateItem(ReferenceSelector item)
    {
    }

    @Override
    public final void removeItem(ReferenceSelector item)
    {
    }

    @Override
    public void replaceAll(ArrayList<? extends ReferenceSelector> newItems)
    {
        dataSet.beginBatchedUpdates();
        for (int i = dataSet.size() - 1; i >= 0; i--) {
            final ReferenceSelector item = dataSet.get(i);
            if (!newItems.contains(item))
                dataSet.remove(item);
        }
        dataSet.addAll((Collection<ReferenceSelector>) newItems);
        dataSet.endBatchedUpdates();
    }

    public final boolean isEmpty()
    {
        return dataSet.size() == 0;
    }

    public DReferences getSelectedReferences()
    {
        return selectedReferences;
    }

}
