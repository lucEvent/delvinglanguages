package com.delvinglanguages.view.lister;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delvinglanguages.R;

import java.util.ArrayList;

public class WebSearchLister extends RecyclerView.Adapter<WebSearchLister.ViewHolder> {

    private static final int[] COLOR_TYPE = new int[]{R.color.noun, R.color.verb, R.color.adjective,
            R.color.adverb, R.color.phrasal, R.color.expression,
            R.color.preposition, R.color.conjunction, R.color.other};

    private static final int[] STRING_TYPE = {
            R.string.noun_cap, R.string.verb_cap, R.string.adjective_cap, R.string.adverb_cap,
            R.string.phrasal_cap, R.string.expression_cap, R.string.preposition_cap,
            R.string.conjunction_cap, R.string.other_cap
    };

    public static class SearchItem {
        public int type;
        public String[] translations;
        public boolean[] selectedTranslations;

        public SearchItem(int type, String[] translations) {
            this.type = type;
            this.translations = translations;
            this.selectedTranslations = new boolean[translations.length];
            for (int i = 0; i < this.selectedTranslations.length; i++)
                this.selectedTranslations[i] = false;
            this.selectedTranslations[0] = true;
        }
    }

    private ArrayList<SearchItem> dataset;

    private LayoutInflater inflater;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text_type;
        public LinearLayout list_translations;

        public ViewHolder(View v) {
            super(v);

            text_type = (TextView) v.findViewById(R.id.textView_type);
            list_translations = (LinearLayout) v.findViewById(R.id.list_translations);
        }

    }

    public WebSearchLister(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.dataset = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.i_search, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchItem item = dataset.get(position);

        for (int i = 0; i < STRING_TYPE.length; ++i)
            if ((item.type & (1 << i)) != 0) {
                holder.text_type.setText(STRING_TYPE[i]);
                holder.text_type.setBackgroundResource(COLOR_TYPE[i]);
                break;
            }

        holder.list_translations.removeAllViews();
        for (int i = 0; i < item.translations.length; i++) {
            String translation = item.translations[i];

            View view_translation = inflater.inflate(R.layout.i_checkbox, holder.list_translations, false);
            view_translation.setOnClickListener(onSelectAction);

            ((TextView) view_translation.findViewById(R.id.text)).setText(translation);

            CheckBox checkBox = (CheckBox) view_translation.findViewById(R.id.checkbox);
            checkBox.setTag(R.id.position, i);
            checkBox.setTag(R.id.search_item, item);
            checkBox.setChecked(item.selectedTranslations[i]);
            checkBox.setOnClickListener(onSelectAction);

            holder.list_translations.addView(view_translation);
        }

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public SearchItem getItem(int i) {
        return dataset.get(i);
    }

    private View.OnClickListener onSelectAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            System.out.println("On Select Action :)");// TODO: 30/03/2016
            CheckBox checkBox;
            if (v instanceof CheckBox) {
                checkBox = (CheckBox) v;
            } else {
                checkBox = (CheckBox) v.findViewById(R.id.checkbox);
                checkBox.toggle();
            }
            int position = (int) checkBox.getTag(R.id.position);
            SearchItem searchItem = (SearchItem) checkBox.getTag(R.id.search_item);
            searchItem.selectedTranslations[position] = checkBox.isChecked();
        }
    };

    public void setNewDataSet(ArrayList<SearchItem> dataset) {
        System.out.println("Setting new data [items=" + dataset.size() + "]");
        int oldSize = this.dataset.size();
        this.dataset = dataset;

        int changed = Math.min(oldSize, dataset.size());
        for (int i = 0; i < changed; i++) {
            System.out.println("Changed " + i);
            notifyItemChanged(i);
        }
        System.out.println("Comprovando 1: " + oldSize + " > " + changed + " = " + (oldSize > changed));
        System.out.println("Comprovando 2: " + dataset.size() + " > " + changed + " = " + (dataset.size() > changed));
        if (oldSize > changed) {
            for (int i = changed; i < oldSize; i++) {
                System.out.println("Removed " + i);
                notifyItemRemoved(i);
            }
        } else if (dataset.size() > changed) {
            for (int i = changed; i < dataset.size(); i++) {
                System.out.println("Added " + i);
                notifyItemInserted(i);
            }
        }

    }

}
