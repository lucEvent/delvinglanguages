package com.delvinglanguages.view.lister.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.subject.Subject;

public class SubjectViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private TextView title, subtitle;

    public SubjectViewHolder(View v)
    {
        super(v);
        view = v;
        title = (TextView) view.findViewById(R.id.title);
        subtitle = (TextView) view.findViewById(R.id.subtitle);
    }

    public static void populateViewHolder(SubjectViewHolder holder, Subject subject)
    {
        Context context = holder.view.getContext();

        holder.title.setText(subject.getName());
        holder.subtitle.setText(context.getString(R.string.x_words, subject.getPairs().size()));
        holder.view.setTag(subject);
    }

}
