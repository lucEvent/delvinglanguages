package com.delvinglanguages.listers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.delvinglanguages.R;

import java.util.ArrayList;

public class SearchLister extends ArrayAdapter<SearchLister.SearchItem> implements View.OnClickListener {

    public class SearchItem {
        public boolean selected;
        public int type;
        public String name;
    }

    private static final int[] TYPE_DRAWABLE = {R.drawable.button_noun_pressed, R.drawable.button_verb_pressed, R.drawable.button_adjective_pressed,
            R.drawable.button_adverb_pressed, R.drawable.button_phrasals_pressed, R.drawable.button_expression_pressed,
            R.drawable.button_preposition_pressed, R.drawable.button_conjunction_pressed, R.drawable.button_other_pressed};

    private static final int[] TYPE_TITLE = {R.string.nn, R.string.vb, R.string.adj, R.string.adv, R.string.phv, R.string.exp, R.string.prp, R.string.cnj, R.string.oth};

    private LayoutInflater inflater;

    public SearchLister(Context context) {
        super(context, R.layout.i_search_row, new ArrayList<SearchItem>());
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(int type, String term) {
        SearchItem item = new SearchItem();
        item.selected = false;
        item.type = type;
        item.name = term;
        add(item);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.i_search_row, parent, false);
        }
        SearchItem item = getItem(position);

        TextView vtype = (TextView) view.findViewById(R.id.type);
        CheckedTextView vcheck = (CheckedTextView) view.findViewById(R.id.text);

        vcheck.setOnClickListener(this);
        vcheck.setTag(position);
        vcheck.setText(item.name);
        vcheck.setChecked(item.selected);

        vtype.setBackgroundResource(TYPE_DRAWABLE[item.type-1]);
        vtype.setText(TYPE_TITLE[item.type-1]);
        vtype.setSelected(true);

        return view;
    }

    @Override
    public void onClick(View v) {
        ((CheckedTextView) v).toggle();

        SearchItem item = getItem((Integer) v.getTag());
        item.selected = !item.selected;
    }

}
