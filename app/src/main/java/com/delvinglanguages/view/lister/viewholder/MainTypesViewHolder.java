package com.delvinglanguages.view.lister.viewholder;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.delvinglanguages.R;

public class MainTypesViewHolder extends RecyclerView.ViewHolder {

    public static class Data {

        public int[] types;
        public int num_themes, num_tests;
        public View.OnClickListener onAddReference, onAddTheme;

    }

    private TextView content;
    private Button addReference, addTheme;

    public MainTypesViewHolder(View v) {
        super(v);

        content = (TextView) v.findViewById(R.id.main_types_content);
        addReference = (Button) v.findViewById(R.id.button_add_reference);
        addTheme = (Button) v.findViewById(R.id.button_add_theme);
    }

    public static void populateViewHolder(MainTypesViewHolder holder, Data data, Resources resources) {
        String s = resources.getString(R.string.main_types_content_description, data.types[0],
                data.types[1], data.types[2], data.types[3], data.types[4], data.types[5],
                data.types[6], data.types[7], data.types[8], data.num_themes, data.num_tests);
        holder.content.setText(s);
        holder.addReference.setOnClickListener(data.onAddReference);
        holder.addTheme.setOnClickListener(data.onAddTheme);
    }

}
