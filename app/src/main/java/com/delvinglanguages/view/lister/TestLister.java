package com.delvinglanguages.view.lister;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.util.Tests;
import com.delvinglanguages.view.lister.viewholder.TestViewHolder;

public class TestLister extends RecyclerView.Adapter<TestViewHolder> {

    private Tests dataset;
    private View.OnClickListener itemListener;

    public TestLister(Tests dataset, View.OnClickListener clickListener)
    {
        this.dataset = dataset;
        this.itemListener = clickListener;
    }

    @Override
    public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_drawer_word, parent, false);
        v.setOnClickListener(itemListener);
        return new TestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TestViewHolder holder, int position)
    {
        TestViewHolder.populateViewHolder(holder, dataset.get(position));
    }

    @Override
    public int getItemCount()
    {
        return dataset.size();
    }

}