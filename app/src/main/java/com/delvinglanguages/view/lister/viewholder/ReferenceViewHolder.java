package com.delvinglanguages.view.lister.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;

public class ReferenceViewHolder extends RecyclerView.ViewHolder {

    private View container;
    private TextView dreference, translation;
    private View noun, verb, adjc, advb, phrv, expr, prep, conj, othr;

    public ReferenceViewHolder(View v)
    {
        super(v);

        dreference = (TextView) v.findViewById(R.id.word);
        translation = (TextView) v.findViewById(R.id.translation);

        noun = v.findViewById(R.id.noun);
        verb = v.findViewById(R.id.verb);
        adjc = v.findViewById(R.id.adjective);
        advb = v.findViewById(R.id.adverb);
        phrv = v.findViewById(R.id.phrasal);
        expr = v.findViewById(R.id.expression);
        prep = v.findViewById(R.id.preposition);
        conj = v.findViewById(R.id.conjunction);
        othr = v.findViewById(R.id.other);

        container = v;
    }

    public static void populateViewHolder(ReferenceViewHolder holder, DReference reference, boolean showPhrasal)
    {
        int type = reference.type;

        holder.dreference.setText(reference.name);
        holder.translation.setText(reference.getTranslationsAsString());

        holder.noun.setVisibility((type & DReference.NOUN) != 0 ? View.VISIBLE : View.INVISIBLE);
        holder.verb.setVisibility((type & DReference.VERB) != 0 ? View.VISIBLE : View.INVISIBLE);
        holder.adjc.setVisibility((type & DReference.ADJECTIVE) != 0 ? View.VISIBLE : View.INVISIBLE);
        holder.advb.setVisibility((type & DReference.ADVERB) != 0 ? View.VISIBLE : View.INVISIBLE);
        if (showPhrasal)
            holder.phrv.setVisibility((type & DReference.PHRASAL_VERB) != 0 ? View.VISIBLE : View.INVISIBLE);
        else
            holder.phrv.setVisibility(View.GONE);

        holder.expr.setVisibility((type & DReference.EXPRESSION) != 0 ? View.VISIBLE : View.INVISIBLE);
        holder.prep.setVisibility((type & DReference.PREPOSITION) != 0 ? View.VISIBLE : View.INVISIBLE);
        holder.conj.setVisibility((type & DReference.CONJUNCTION) != 0 ? View.VISIBLE : View.INVISIBLE);
        holder.othr.setVisibility((type & DReference.OTHER) != 0 ? View.VISIBLE : View.INVISIBLE);

        holder.container.setTag(reference);
    }

}
