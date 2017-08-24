package com.delvinglanguages.view.lister.viewholder;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.phrasalverb.PhrasalVerb;

import java.util.ArrayList;

public class PhrasalVerbViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    private View vMenuBtn;
    private TextView vVerb;
    private ViewGroup vTranslationsLayout;

    private ArrayList<View> vTranslations;

    public PhrasalVerbViewHolder(View v)
    {
        super(v);

        vVerb = (TextView) v.findViewById(R.id.word);
        vMenuBtn = v.findViewById(R.id.menu);
        vTranslationsLayout = (ViewGroup) v.findViewById(R.id.translations);

        vMenuBtn.setOnCreateContextMenuListener(this);
        vMenuBtn.setOnClickListener(this);

        vTranslations = new ArrayList<>();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        PhrasalVerb phv = (PhrasalVerb) v.getTag();

        menu.setHeaderIcon(R.drawable.ic_more);
        menu.add(phv.id, R.id.practise_match, 1, R.string.match);
        menu.add(phv.id, R.id.practise_complete, 2, R.string.complete);
        menu.add(phv.id, R.id.practise_write, 3, R.string.write);
        menu.add(phv.id, R.id.practise_listening, 4, R.string.listening);
        menu.add(phv.id, R.id.practise_test, 5, R.string.test);
    }

    @Override
    public void onClick(View v)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
            vMenuBtn.showContextMenu();
        else
            vMenuBtn.showContextMenu(vMenuBtn.getX(), vMenuBtn.getY());
    }

    public void bind(PhrasalVerb phrasalVerb, LayoutInflater inflater, View.OnClickListener itemListener)
    {
        vVerb.setText(phrasalVerb.verb);

        while (phrasalVerb.variants.size() > vTranslations.size()) {
            View view = inflater.inflate(R.layout.i_translation, vTranslationsLayout, false);
            view.setOnClickListener(itemListener);
            vTranslations.add(view);
        }

        vTranslationsLayout.removeAllViews();
        for (int i = 0; i < phrasalVerb.variants.size(); i++) {
            DReference ref = phrasalVerb.variants.get(i);

            int index = ref.name.indexOf(" ");
            String prep = index > 0 ? ref.name.substring(index) : "";

            TextView tv = (TextView) vTranslations.get(i);
            tv.setText(Html.fromHtml("<b>" + prep + "</b>: " + ref.getTranslationsAsString()));
            tv.setTag(ref);

            vTranslationsLayout.addView(tv);
        }

        if (phrasalVerb.variants.size() == 1)
            vMenuBtn.setVisibility(View.GONE);
        else {
            vMenuBtn.setVisibility(View.VISIBLE);
            vMenuBtn.setTag(phrasalVerb);
        }
    }

}
