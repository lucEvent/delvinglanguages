package com.delvinglanguages.view.lister.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.test.Test;

public class TestViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private TextView title, subtitle;

    public TestViewHolder(View v)
    {
        super(v);
        view = v;
        title = (TextView) view.findViewById(R.id.title);
        subtitle = (TextView) view.findViewById(R.id.subtitle);
    }

    public static void populateViewHolder(TestViewHolder holder, Test test)
    {
        Context context = holder.view.getContext();

        holder.title.setText(test.name);

        StringBuilder testStats = new StringBuilder();
        testStats.append(context.getString(R.string.msg_test_done_times, test.getRunTimes())).append("\n");
        testStats.append(context.getString(R.string.msg_test_accuracy, test.getAccuracy())).append("\n");
        testStats.append(context.getString(R.string.x_words, test.references.size()));

        holder.subtitle.setText(testStats);
        holder.view.setTag(test);
    }

}
