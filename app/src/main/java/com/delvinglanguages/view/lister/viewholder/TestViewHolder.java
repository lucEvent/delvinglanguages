package com.delvinglanguages.view.lister.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.delvinglanguages.kernel.test.Test;

public class TestViewHolder extends RecyclerView.ViewHolder {

    private TextView view;

    public TestViewHolder(View v)
    {
        super(v);

        view = (TextView) v;
    }

    public static void populateViewHolder(TestViewHolder holder, Test test)
    {
        holder.view.setText(test.name);
        holder.view.setTag(test);
    }

}
