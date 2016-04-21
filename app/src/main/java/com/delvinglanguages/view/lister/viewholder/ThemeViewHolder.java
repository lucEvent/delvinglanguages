package com.delvinglanguages.view.lister.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.delvinglanguages.kernel.theme.Theme;

public class ThemeViewHolder extends RecyclerView.ViewHolder {

    private TextView view;

    public ThemeViewHolder(View v)
    {
        super(v);
        view = (TextView) v;
    }

    public static void populateViewHolder(ThemeViewHolder holder, Theme theme)
    {
        holder.view.setText(theme.getName());
        holder.view.setTag(theme);
    }

}
