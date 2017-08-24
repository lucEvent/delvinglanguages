package com.delvinglanguages.view.lister;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.Inflexion;
import com.delvinglanguages.kernel.Usage;
import com.delvinglanguages.kernel.util.Inflexions;
import com.delvinglanguages.kernel.util.Usages;

public class InflexionLister extends RecyclerView.Adapter<InflexionLister.ViewHolder> {

    private static final int[] COLOR_TYPE = new int[]{R.color.noun, R.color.verb, R.color.adjective,
            R.color.adverb, R.color.phrasal, R.color.expression,
            R.color.preposition, R.color.conjunction, R.color.other};

    private static final int[] STRING_TYPE = {
            R.string.noun_cap, R.string.verb_cap, R.string.adjective_cap, R.string.adverb_cap,
            R.string.phrasal_cap, R.string.expression_cap, R.string.preposition_cap,
            R.string.conjunction_cap, R.string.other_cap
    };

    protected Inflexions dataset;

    private Usages usages;

    protected LayoutInflater inflater;

    private View.OnClickListener itemListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_type, text_inflexions;

        ViewGroup list_translations;

        View title_translations, title_inflexions;

        public ViewHolder(View v)
        {
            super(v);

            text_type = (TextView) v.findViewById(R.id.textview_type);
            text_inflexions = (TextView) v.findViewById(R.id.textview_inflexions);

            list_translations = (ViewGroup) v.findViewById(R.id.list_translations);

            title_translations = v.findViewById(R.id.title_translations);
            title_inflexions = v.findViewById(R.id.title_inflexions);
        }
    }

    public InflexionLister(Context context, Inflexions dataset, Usages usages, View.OnClickListener itemListener)
    {
        this.inflater = LayoutInflater.from(context);
        this.dataset = dataset;
        this.usages = usages != null ? usages : new Usages();
        this.itemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = inflater.inflate(R.layout.i_inflexion, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Inflexion inflexion = dataset.get(position);
        int type = inflexion.getType();

        for (int i = 0; i < STRING_TYPE.length; ++i) {
            if ((type & (1 << i)) != 0) {
                holder.text_type.setText(STRING_TYPE[i]);
                holder.text_type.setBackgroundResource(COLOR_TYPE[i]);
                break;
            }
        }

        if (inflexion.hasInflexions()) {
            holder.text_inflexions.setVisibility(TextView.VISIBLE);
            holder.title_translations.setVisibility(View.VISIBLE);
            holder.title_inflexions.setVisibility(View.VISIBLE);

            StringBuilder sb = new StringBuilder();
            for (String form : inflexion.getInflexions())
                sb.append(form).append("<br>");

            holder.text_inflexions.setText(Html.fromHtml(sb.toString()));

        } else {
            holder.text_inflexions.setVisibility(TextView.GONE);
            holder.title_translations.setVisibility(View.GONE);
            holder.title_inflexions.setVisibility(View.GONE);
        }

        holder.list_translations.removeAllViews();
        for (String translation : inflexion.getTranslations()) {
            View v;

            Usage usage = usages.get(translation);
            if (usage != null && !usage.usage.isEmpty()) {
                v = inflater.inflate(R.layout.i_translation_with_usage, holder.list_translations, false);

                ((TextView) v.findViewById(R.id.translation)).setText(translation);
                ((TextView) v.findViewById(R.id.usage)).setText(usage.usage);
            } else {
                v = inflater.inflate(R.layout.i_translation, holder.list_translations, false);
                ((TextView) v).setText(translation);
            }

            v.setTag(translation);
            v.setOnClickListener(itemListener);
            holder.list_translations.addView(v);
        }
    }

    @Override
    public int getItemCount()
    {
        return dataset.size();
    }

    public void setNewDataSet(Inflexions dataset)
    {
        int oldSize = this.dataset.size();
        int newSize = dataset.size();
        this.dataset = dataset;

        if (newSize > oldSize) {
            notifyItemRangeChanged(0, oldSize);
            notifyItemRangeInserted(oldSize, newSize - oldSize);
        } else if (newSize < oldSize) {
            notifyItemRangeChanged(0, newSize);
            notifyItemRangeRemoved(newSize, oldSize - newSize);
        } else
            notifyDataSetChanged();
    }

}