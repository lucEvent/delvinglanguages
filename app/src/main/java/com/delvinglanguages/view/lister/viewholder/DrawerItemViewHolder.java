package com.delvinglanguages.view.lister.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DrawerReference;

public class DrawerItemViewHolder extends RecyclerView.ViewHolder {

    private View parent;
    private TextView view;

    public DrawerItemViewHolder(View v)
    {
        super(v);
        parent = v;
        view = (TextView) v.findViewById(R.id.text);
    }

    public static void populateViewHolder(DrawerItemViewHolder holder, DrawerReference dref)
    {
        holder.view.setText(dref.name);
        holder.parent.setTag(dref.id);
    }

}
