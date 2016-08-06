package com.delvinglanguages.view.lister;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.util.DrawerReferences;
import com.delvinglanguages.view.lister.viewholder.DrawerItemViewHolder;

public class DrawerLister extends RecyclerView.Adapter<DrawerItemViewHolder> {

    private DrawerReferences items;

    private View.OnClickListener onClickItemListener;
    private LayoutInflater inflater;

    public DrawerLister(Context context, DrawerReferences items, View.OnClickListener itemListener)
    {
        this.items = items;
        this.onClickItemListener = itemListener;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public DrawerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = inflater.inflate(R.layout.i_drawer_word, parent, false);
        v.setOnClickListener(onClickItemListener);
        return new DrawerItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DrawerItemViewHolder holder, int position)
    {
        DrawerItemViewHolder.populateViewHolder(holder, items.get(position));
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

}