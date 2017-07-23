package com.delvinglanguages.view.lister.viewholder;


import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.view.utils.MainSearch;

public class MainWebSearchViewHolder extends RecyclerView.ViewHolder {

    public View container;
    private TextView content;
    private Button button1, button2;

    public MainWebSearchViewHolder(View v)
    {
        super(v);
        container = v;
        content = (TextView) v.findViewById(R.id.content);
        button1 = (Button) v.findViewById(R.id.button_add_to_drawer);
        button2 = (Button) v.findViewById(R.id.button_search_online);
    }

    public static void populateViewHolder(MainWebSearchViewHolder holder, MainSearch data, Resources resources, View.OnClickListener onClickListener)
    {
        if (data.withMessage) {
            holder.content.setVisibility(View.VISIBLE);
            holder.content.setText(Html.fromHtml(resources.getString(R.string.msg_no_results_found_for, data.term)));
        } else
            holder.content.setVisibility(View.GONE);

        holder.button1.setOnClickListener(onClickListener);
        holder.button1.setTag(data);

        holder.button2.setOnClickListener(onClickListener);
        holder.button2.setTag(data);
    }

}
