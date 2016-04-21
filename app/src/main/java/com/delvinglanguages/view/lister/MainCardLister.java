package com.delvinglanguages.view.lister;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.view.lister.viewholder.MainStatsViewHolder;
import com.delvinglanguages.view.lister.viewholder.MainTypesViewHolder;
import com.delvinglanguages.view.lister.viewholder.ReferenceViewHolder;
import com.delvinglanguages.view.lister.viewholder.TestViewHolder;
import com.delvinglanguages.view.lister.viewholder.ThemeViewHolder;

import java.util.ArrayList;

public class MainCardLister extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int CHUNK = 20;

    private static final int VIEW_TYPES = 100;
    private static final int VIEW_STATS = 101;
    private static final int VIEW_DREFERENCE = 102;
    private static final int VIEW_THEME = 103;
    private static final int VIEW_TEST = 104;

    /* Variables */
    private final Resources resources;
    private final ArrayList<Object> dataset;

    private int datasetVisibleCount;
    private boolean searching;
    private boolean phrasalsEnabled;

    private View.OnClickListener itemListener;

    public MainCardLister(Resources resources, boolean phrasalsEnabled, View.OnClickListener itemListener)
    {
        this.resources = resources;
        this.dataset = new ArrayList<>();
        this.phrasalsEnabled = phrasalsEnabled;
        this.itemListener = itemListener;

        this.datasetVisibleCount = 0;
        this.searching = false;
    }

    @Override
    public int getItemViewType(int position)
    {
        Object o = dataset.get(position);
        if (searching) {
            if (o instanceof DReference)
                return VIEW_DREFERENCE;
            if (o instanceof Theme)
                return VIEW_THEME;
            if (o instanceof Test)
                return VIEW_TEST;
            return -1;
        }
        if (o instanceof MainTypesViewHolder.Data)
            return VIEW_TYPES;
        if (o instanceof MainStatsViewHolder.Data)
            return VIEW_STATS;
        System.out.println("Position :" + position + " debuelve -1");
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPES:
                return new MainTypesViewHolder(inflater.inflate(R.layout.i_main_types, parent, false));
            case VIEW_STATS:
                return new MainStatsViewHolder(inflater.inflate(R.layout.i_main_statistics, parent, false));
            case VIEW_DREFERENCE:
                View view = inflater.inflate(R.layout.i_dreference, parent, false);
                view.setOnClickListener(itemListener);
                return new ReferenceViewHolder(view);
            case VIEW_THEME:
                view = inflater.inflate(R.layout.i_drawer_word, parent, false);
                view.setOnClickListener(itemListener);
                return new ThemeViewHolder(view);
            case VIEW_TEST:
                view = inflater.inflate(R.layout.i_drawer_word, parent, false);
                view.setOnClickListener(itemListener);
                return new TestViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        Object o = dataset.get(position);
        if (searching) {
            if (o instanceof DReference) {
                ReferenceViewHolder.populateViewHolder((ReferenceViewHolder) holder, (DReference) o, phrasalsEnabled);
            } else if (o instanceof Theme) {
                ThemeViewHolder.populateViewHolder((ThemeViewHolder) holder, (Theme) o);
            } else if (o instanceof Test) {
                TestViewHolder.populateViewHolder((TestViewHolder) holder, (Test) o);
            }
        } else if (o instanceof MainTypesViewHolder.Data) {
            MainTypesViewHolder.populateViewHolder((MainTypesViewHolder) holder, (MainTypesViewHolder.Data) o, resources);
        } else if (o instanceof MainStatsViewHolder.Data) {
            MainStatsViewHolder.populateViewHolder((MainStatsViewHolder) holder, (MainStatsViewHolder.Data) o, resources);
        }
    }

    @Override
    public int getItemCount()
    {
        return datasetVisibleCount;
    }

    public void addItem(Object item)
    {
        dataset.add(item);
        notifyItemInserted(datasetVisibleCount);
        datasetVisibleCount++;
    }

    public void setNewDataSet(ArrayList<Object> dataset)
    {
        searching = true;
        this.dataset.clear();
        this.dataset.addAll(dataset);

        int newVisibleCount = Math.min(dataset.size(), CHUNK);
        if (newVisibleCount > datasetVisibleCount) {
            notifyItemRangeChanged(0, datasetVisibleCount);
            notifyItemRangeInserted(datasetVisibleCount, newVisibleCount - datasetVisibleCount);
        } else if (newVisibleCount < datasetVisibleCount) {
            notifyItemRangeChanged(0, newVisibleCount);
            notifyItemRangeRemoved(newVisibleCount, datasetVisibleCount - newVisibleCount);
        } else
            notifyDataSetChanged();

        this.datasetVisibleCount = newVisibleCount;
    }

    public void loadMoreData()
    {
        int dataAdded = Math.min(this.datasetVisibleCount + CHUNK, dataset.size()) - datasetVisibleCount;
        try {
            notifyItemRangeInserted(datasetVisibleCount, dataAdded);
        } catch (Exception ignored) {
        }
        this.datasetVisibleCount += dataAdded;
    }

}
