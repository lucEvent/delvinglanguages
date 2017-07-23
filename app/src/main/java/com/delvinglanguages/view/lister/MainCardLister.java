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
import com.delvinglanguages.kernel.util.Item;
import com.delvinglanguages.view.lister.viewholder.MainStatsViewHolder;
import com.delvinglanguages.view.lister.viewholder.MainTypesViewHolder;
import com.delvinglanguages.view.lister.viewholder.MainWebSearchViewHolder;
import com.delvinglanguages.view.lister.viewholder.ReferenceViewHolder;
import com.delvinglanguages.view.lister.viewholder.TestViewHolder;
import com.delvinglanguages.view.lister.viewholder.ThemeViewHolder;
import com.delvinglanguages.view.utils.MainCardRVListAdapter;
import com.delvinglanguages.view.utils.MainSearch;

import java.util.Collection;

public class MainCardLister extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /* Variables */
    private final Resources resources;
    private final MainCardRVListAdapter dataSet;

    private boolean phrasalsEnabled;

    private View.OnClickListener itemListener;

    public MainCardLister(Resources resources, boolean phrasalsEnabled, View.OnClickListener itemListener)
    {
        this.resources = resources;
        this.dataSet = new MainCardRVListAdapter(this);
        this.phrasalsEnabled = phrasalsEnabled;
        this.itemListener = itemListener;
    }

    @Override
    public int getItemViewType(int position)
    {
        return dataSet.get(position).itemType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int itemType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (itemType) {
            case Item.TYPES_DATA:
                return new MainTypesViewHolder(inflater.inflate(R.layout.i_main_types, parent, false));
            case Item.STATS_DATA:
                return new MainStatsViewHolder(inflater.inflate(R.layout.i_main_statistics, parent, false));
            case Item.DREFERENCE:
                View view = inflater.inflate(R.layout.i_dreference, parent, false);
                view.setOnClickListener(itemListener);
                return new ReferenceViewHolder(view);
            case Item.THEME:
                view = inflater.inflate(R.layout.i_theme, parent, false);
                view.setOnClickListener(itemListener);
                return new ThemeViewHolder(view);
            case Item.TEST:
                view = inflater.inflate(R.layout.i_test, parent, false);
                view.setOnClickListener(itemListener);
                return new TestViewHolder(view);
            case Item.WEB_SEARCH:
                return new MainWebSearchViewHolder(inflater.inflate(R.layout.i_main_web_search, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        Item item = dataSet.get(position);
        switch (item.itemType) {
            case Item.TYPES_DATA:
                MainTypesViewHolder.populateViewHolder((MainTypesViewHolder) holder, (MainTypesViewHolder.Data) item, resources, phrasalsEnabled);
                break;
            case Item.STATS_DATA:
                MainStatsViewHolder.populateViewHolder((MainStatsViewHolder) holder, (MainStatsViewHolder.Data) item, resources);
                break;
            case Item.DREFERENCE:
                ReferenceViewHolder.populateViewHolder((ReferenceViewHolder) holder, (DReference) item, phrasalsEnabled);
                break;
            case Item.THEME:
                ThemeViewHolder.populateViewHolder((ThemeViewHolder) holder, (Theme) item);
                break;
            case Item.TEST:
                TestViewHolder.populateViewHolder((TestViewHolder) holder, (Test) item);
                break;
            case Item.WEB_SEARCH:
                MainWebSearchViewHolder.populateViewHolder((MainWebSearchViewHolder) holder, (MainSearch) item, resources, itemListener);
        }
    }

    @Override
    public int getItemCount()
    {
        return dataSet.size();
    }

    public void setPhrasals(boolean enabled)
    {
        this.phrasalsEnabled = enabled;
    }

    public void addItem(Item item)
    {
        dataSet.add(item);
    }

    public final void setNewDataSet(Collection<Item> newDataSet)
    {
        synchronized (this.dataSet) {
            dataSet.beginBatchedUpdates();

            dataSet.clear();

            for (Item n : newDataSet)
                dataSet.add(n);

            dataSet.endBatchedUpdates();
        }
    }

    public void clear()
    {
        dataSet.beginBatchedUpdates();
        dataSet.clear();
        dataSet.endBatchedUpdates();
    }

    public boolean isEmpty()
    {
        return dataSet.size() == 0;
    }

}
