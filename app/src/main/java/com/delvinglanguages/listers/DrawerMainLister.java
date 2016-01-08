package com.delvinglanguages.listers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.set.Languages;

public class DrawerMainLister extends ArrayAdapter<Language> {

    private View[] views;

    private Languages languages;

    private LayoutInflater inflater;

    public DrawerMainLister(Context context, Languages languages) {
        super(context, -1, languages);
        clear(languages);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (views[position] != null) return views[position];

        if (position == 0) {

            view = inflater.inflate(R.layout.i_drawer_main, parent, false);
            view.setOnClickListener(null);

        } else {

            Language language = languages.get(position - 1);
            view = inflater.inflate(R.layout.i_language, parent, false);
            ((TextView) view.findViewById(R.id.name)).setText(language.language_delved_name);
//            view.findViewById(R.id.logo).setBackgroundDrawable(site.icon.getConstantState().newDrawable());
            view.setTag(language);

        }
        views[position] = view;
        return view;
    }

    public void clear(Languages languages) {
        this.languages = languages;
        this.views = new View[languages.size() + 1];
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return languages.size() + 1;
    }

    private void debug(String text) {
        android.util.Log.d("##DrawerMainLister##", text);
    }

}