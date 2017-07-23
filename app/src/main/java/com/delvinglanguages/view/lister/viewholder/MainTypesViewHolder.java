package com.delvinglanguages.view.lister.viewholder;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.util.Item;

public class MainTypesViewHolder extends RecyclerView.ViewHolder {

    public static class Data extends Item {

        public Language language;
        public View.OnClickListener onAddReference, onAddTheme;

        public Data()
        {
            super(-3, Item.TYPES_DATA);
        }

    }

    private TextView content;
    private Button addReference, addTheme;

    public MainTypesViewHolder(View v)
    {
        super(v);
        content = (TextView) v.findViewById(R.id.main_types_content);
        addReference = (Button) v.findViewById(R.id.button_add_reference);
        addTheme = (Button) v.findViewById(R.id.button_add_theme);
    }

    public static void populateViewHolder(MainTypesViewHolder holder, Data data, Resources resources, boolean arePhrasalsEnabled)
    {
        int[] types = data.language.getTypeCounter();
        int num_themes = data.language.themes.size();
        int num_tests = data.language.tests.size();

        String s =
                arePhrasalsEnabled ?
                        resources.getString(R.string.main_types_content_description, types[0],
                                types[1], types[2], types[3], types[4], types[5],
                                types[6], types[7], types[8], num_themes, num_tests)
                        :
                        resources.getString(R.string.main_types_content_description_no_phrasals, types[0],
                                types[1], types[2], types[3], types[5], types[6],
                                types[7], types[8], num_themes, num_tests);

        holder.content.setText(s);
        holder.addReference.setOnClickListener(data.onAddReference);
        holder.addTheme.setOnClickListener(data.onAddTheme);
    }

}
