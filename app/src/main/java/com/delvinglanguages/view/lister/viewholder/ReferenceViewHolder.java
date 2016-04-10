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

    public ReferenceViewHolder(View v) {
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

    public static void populateViewHolder(ReferenceViewHolder holder, DReference reference, boolean showPhrasal) {
        int type = reference.type;

        holder.dreference.setText(reference.name);
        holder.translation.setText(reference.getTranslationsAsString());

        holder.noun.setBackgroundResource((type & DReference.NOUN) != 0 ? R.color.noun : R.color.none);
        holder.verb.setBackgroundResource((type & DReference.VERB) != 0 ? R.color.verb : R.color.none);
        holder.adjc.setBackgroundResource((type & DReference.ADJECTIVE) != 0 ? R.color.adjective : R.color.none);
        holder.advb.setBackgroundResource((type & DReference.ADVERB) != 0 ? R.color.adverb : R.color.none);
        if (showPhrasal) {
            holder.phrv.setBackgroundResource((type & DReference.PHRASAL_VERB) != 0 ? R.color.phrasal : R.color.none);
            holder.phrv.setVisibility(View.VISIBLE);
        } else {
            holder.phrv.setVisibility(View.GONE);
        }
        holder.expr.setBackgroundResource((type & DReference.EXPRESSION) != 0 ? R.color.expression : R.color.none);
        holder.prep.setBackgroundResource((type & DReference.PREPOSITION) != 0 ? R.color.preposition : R.color.none);
        holder.conj.setBackgroundResource((type & DReference.CONJUNCTION) != 0 ? R.color.conjunction : R.color.none);
        holder.othr.setBackgroundResource((type & DReference.OTHER) != 0 ? R.color.other : R.color.none);

        holder.container.setTag(reference);
    }

}
