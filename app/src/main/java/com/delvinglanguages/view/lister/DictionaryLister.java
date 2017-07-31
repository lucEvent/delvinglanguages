package com.delvinglanguages.view.lister;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.Item;
import com.delvinglanguages.view.lister.viewholder.MainWebSearchViewHolder;
import com.delvinglanguages.view.utils.MainSearch;

public class DictionaryLister extends ReferenceLister {

    protected final Resources resources;

    public DictionaryLister(DReferences dataSet, boolean phv_enabled, View.OnClickListener itemListener, Resources resources)
    {
        super(dataSet, phv_enabled, itemListener);

        this.resources = resources;
    }

    @Override
    public int getItemViewType(int position)
    {
        return dataSet.get(position).itemType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int itemType)
    {
        if (itemType == Item.WEB_SEARCH) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new MainWebSearchViewHolder(inflater.inflate(R.layout.i_main_web_search, parent, false));
        } else
            return super.onCreateViewHolder(parent, itemType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        Item item = dataSet.get(position);
        if (item.itemType == Item.WEB_SEARCH)
            MainWebSearchViewHolder.populateViewHolder((MainWebSearchViewHolder) holder, (MainSearch) item, resources, itemListener);
        else
            super.onBindViewHolder(holder, position);
    }

}
