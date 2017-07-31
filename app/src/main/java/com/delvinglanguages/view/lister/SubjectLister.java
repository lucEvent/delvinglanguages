package com.delvinglanguages.view.lister;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.util.Subjects;
import com.delvinglanguages.view.lister.viewholder.SubjectViewHolder;

public class SubjectLister extends RecyclerView.Adapter<SubjectViewHolder> {

    private Subjects subjects;

    private View.OnClickListener itemListener;

    public SubjectLister(Subjects subjects, View.OnClickListener itemListener)
    {
        this.subjects = subjects;
        this.itemListener = itemListener;
    }

    @Override
    public SubjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_subject, parent, false);
        v.setOnClickListener(itemListener);
        return new SubjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SubjectViewHolder holder, int position)
    {
        SubjectViewHolder.populateViewHolder(holder, subjects.get(position));
    }

    @Override
    public int getItemCount()
    {
        return subjects.size();
    }

}