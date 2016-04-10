package com.delvinglanguages.view.lister;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.view.lister.viewholder.ReferenceViewHolder;

public class ReferenceLister extends RecyclerView.Adapter<ReferenceViewHolder> {

    public static final int CHUNK = 20;

    private int datasetVisibleCount;

    private DReferences dataset;
    private final boolean phrasalsEnabled;
    private View.OnClickListener itemListener;

    public ReferenceLister(DReferences dataset, boolean phrasalsEnabled, View.OnClickListener itemListener) {
        this.dataset = dataset;
        this.phrasalsEnabled = phrasalsEnabled;
        this.itemListener = itemListener;

        this.datasetVisibleCount = Math.min(CHUNK, dataset.size());
    }

    @Override
    public ReferenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_dreference, parent, false);
        v.setOnClickListener(itemListener);
        return new ReferenceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReferenceViewHolder holder, int position) {
        ReferenceViewHolder.populateViewHolder(holder, dataset.get(position), phrasalsEnabled);
    }

    @Override
    public int getItemCount() {
        return datasetVisibleCount;
    }

    public void setNewDataSet(DReferences dataset) {
        this.dataset = dataset;

        int newVisibleCount = Math.min(dataset.size(), CHUNK);
        if (newVisibleCount > datasetVisibleCount) {
            notifyItemRangeChanged(0, datasetVisibleCount - 1);
            notifyItemRangeInserted(datasetVisibleCount, newVisibleCount - 1);
        } else if (newVisibleCount < datasetVisibleCount) {
            notifyItemRangeChanged(0, newVisibleCount - 1);
            notifyItemRangeRemoved(newVisibleCount, datasetVisibleCount - 1);
        } else
            notifyDataSetChanged();

        this.datasetVisibleCount = newVisibleCount;
    }

    public void loadMoreData() {
        int dataAdded = Math.min(this.datasetVisibleCount + CHUNK, dataset.size()) - datasetVisibleCount;
        try {
            notifyItemRangeInserted(datasetVisibleCount, datasetVisibleCount + dataAdded - 1);
        } catch (Exception ignored) {
        }
        this.datasetVisibleCount += dataAdded;
    }

}
