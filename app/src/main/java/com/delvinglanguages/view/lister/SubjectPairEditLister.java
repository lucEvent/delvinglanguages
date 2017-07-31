package com.delvinglanguages.view.lister;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.subject.SubjectPair;
import com.delvinglanguages.kernel.util.SubjectPairs;

public class SubjectPairEditLister extends RecyclerView.Adapter<SubjectPairEditLister.ViewHolder> {

    private SubjectPairs dataset;
    private View.OnClickListener clickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView pair1, pair2;
        public ImageButton edit, delete;

        public ViewHolder(View v, View.OnClickListener clickListener)
        {
            super(v);
            pair1 = (TextView) v.findViewById(R.id.pair_1);
            pair2 = (TextView) v.findViewById(R.id.pair_2);

            edit = (ImageButton) v.findViewById(R.id.edit);
            delete = (ImageButton) v.findViewById(R.id.delete);

            delete.setOnClickListener(clickListener);
            edit.setOnClickListener(clickListener);
        }

    }

    public SubjectPairEditLister(SubjectPairs dataset, View.OnClickListener clickListener)
    {
        this.dataset = dataset;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_subject_pair_edit, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        SubjectPair pair = dataset.get(position);

        holder.pair1.setText(pair.inDelved);
        holder.pair2.setText(pair.inNative);

        holder.edit.setTag(pair);
        holder.delete.setTag(pair);
    }

    @Override
    public int getItemCount()
    {
        return dataset.size();
    }

}
