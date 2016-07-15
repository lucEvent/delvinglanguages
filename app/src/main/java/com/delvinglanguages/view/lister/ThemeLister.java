package com.delvinglanguages.view.lister;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.util.Themes;
import com.delvinglanguages.view.lister.viewholder.ThemeViewHolder;

public class ThemeLister extends RecyclerView.Adapter<ThemeViewHolder> {

    private Themes themes;

    private View.OnClickListener itemListener;

    public ThemeLister(Themes themes, View.OnClickListener itemListener)
    {
        this.themes = themes;
        this.itemListener = itemListener;
    }

    @Override
    public ThemeViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_theme, parent, false);
        v.setOnClickListener(itemListener);
        return new ThemeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ThemeViewHolder holder, int position)
    {
        ThemeViewHolder.populateViewHolder(holder, themes.get(position));
    }

    @Override
    public int getItemCount()
    {
        return themes.size();
    }

}