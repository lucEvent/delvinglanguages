package com.delvinglanguages.view.lister.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.subject.Subject;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.util.RemovedItem;
import com.delvinglanguages.kernel.util.Wrapper;

public class RecycledItemViewHolder extends RecyclerView.ViewHolder {

    private ImageButton restore;
    private TextView title, subtitle;

    public RecycledItemViewHolder(View v, View.OnClickListener itemListener)
    {
        super(v);
        title = (TextView) v.findViewById(R.id.word);
        subtitle = (TextView) v.findViewById(R.id.translation);
        restore = (ImageButton) v.findViewById(R.id.restore);
        restore.setOnClickListener(itemListener);
    }

    public static void populateViewHolder(RecycledItemViewHolder holder, RemovedItem removedItem)
    {
        switch (removedItem.wrap_type) {
            case Wrapper.TYPE_REFERENCE:
                DReference reference = removedItem.castToReference();

                holder.title.setText(reference.name);
                holder.subtitle.setText(reference.getTranslationsAsString());

                break;
            case Wrapper.TYPE_SUBJECT:
                Subject subject = removedItem.castToSubject();
                Context context = holder.subtitle.getContext();

                holder.title.setText(subject.getName());
                holder.subtitle.setText(context.getString(R.string.x_words, subject.getPairs().size()));
                break;
            case Wrapper.TYPE_TEST:
                Test test = removedItem.castToTest();
                context = holder.subtitle.getContext();

                holder.title.setText(test.name);

                StringBuilder testStats = new StringBuilder();
                testStats.append(context.getString(R.string.msg_test_done_times, test.getRunTimes())).append("\n");
                testStats.append(context.getString(R.string.msg_test_accuracy, test.getAccuracy())).append("\n");
                testStats.append(context.getString(R.string.x_words, test.size()));

                holder.subtitle.setText(testStats);
                break;
        }
        holder.restore.setTag(removedItem);
    }

}
