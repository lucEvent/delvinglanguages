package com.delvinglanguages.view.lister;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.subject.Subject;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.Item;
import com.delvinglanguages.view.lister.viewholder.MainStatsViewHolder;
import com.delvinglanguages.view.lister.viewholder.MainTypesViewHolder;
import com.delvinglanguages.view.lister.viewholder.SubjectViewHolder;
import com.delvinglanguages.view.lister.viewholder.TestViewHolder;

public class ItemLister extends DictionaryLister {

    public ItemLister(Resources resources, boolean phv_enabled, View.OnClickListener itemListener)
    {
        super(new DReferences(), phv_enabled, itemListener, resources);
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
            case Item.SUBJECT:
                View view = inflater.inflate(R.layout.i_subject, parent, false);
                view.setOnClickListener(itemListener);
                return new SubjectViewHolder(view);
            case Item.TEST:
                view = inflater.inflate(R.layout.i_test, parent, false);
                view.setOnClickListener(itemListener);
                return new TestViewHolder(view);
        }
        return super.onCreateViewHolder(parent, itemType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        Item item = dataSet.get(position);
        switch (item.itemType) {
            case Item.TYPES_DATA:
                MainTypesViewHolder.populateViewHolder((MainTypesViewHolder) holder, (MainTypesViewHolder.Data) item, resources, phv_enabled);
                break;
            case Item.STATS_DATA:
                MainStatsViewHolder.populateViewHolder((MainStatsViewHolder) holder, (MainStatsViewHolder.Data) item, resources);
                break;
            case Item.SUBJECT:
                SubjectViewHolder.populateViewHolder((SubjectViewHolder) holder, (Subject) item);
                break;
            case Item.TEST:
                TestViewHolder.populateViewHolder((TestViewHolder) holder, (Test) item);
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

}
