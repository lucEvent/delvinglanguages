package com.delvinglanguages.view.lister;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.view.lister.viewholder.ReferenceViewHolder;
import com.delvinglanguages.view.utils.ReferenceRVListAdapter;

public class ReferenceLister extends RecyclerView.Adapter<ReferenceViewHolder> {

    private final ReferenceRVListAdapter dataSet;
    private final boolean phv_enabled;
    private View.OnClickListener itemListener;

    public ReferenceLister(DReferences dataSet, boolean phv_enabled, View.OnClickListener itemListener)
    {
        this.phv_enabled = phv_enabled;
        this.itemListener = itemListener;

        this.dataSet = new ReferenceRVListAdapter(this);
        this.dataSet.addAll(dataSet);
    }

    @Override
    public ReferenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_dreference, parent, false);
        v.setOnClickListener(itemListener);
        return new ReferenceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReferenceViewHolder holder, int position)
    {
        ReferenceViewHolder.populateViewHolder(holder, dataSet.get(position), phv_enabled);
    }

    @Override
    public int getItemCount()
    {
        return dataSet.size();
    }

    public void setNewDataSet(DReferences newDataSet)
    {
        synchronized (this.dataSet) {
            dataSet.beginBatchedUpdates();

            dataSet.clear();

            for (DReference n : newDataSet)
                dataSet.add(n);

            dataSet.endBatchedUpdates();
        }
    }

}
