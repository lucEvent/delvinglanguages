package com.delvinglanguages.view.lister.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.Usage;

public class UsageViewHolder extends RecyclerView.ViewHolder {

    private TextView translation, usage;

    public UsageViewHolder(View v)
    {
        super(v);

        translation = (TextView) v.findViewById(R.id.translation);
        usage = (TextView) v.findViewById(R.id.usage);
    }

    public void populate(Usage usage)
    {
        this.translation.setText(usage.translation);
        this.usage.setText(usage.usage);
    }

}