package com.delvinglanguages.view.lister;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.Inflexion;
import com.delvinglanguages.kernel.util.Inflexions;

public class InflexionEditLister extends InflexionLister {

    private View.OnClickListener onModifyListener, onDeleteListener;

    public static class ViewHolder extends InflexionLister.ViewHolder {

        View itemBox;
        ImageView edit, delete;

        public ViewHolder(View v)
        {
            super(v);

            itemBox = v;
            edit = (ImageView) v.findViewById(R.id.edit);
            delete = (ImageView) v.findViewById(R.id.delete);
        }
    }

    public InflexionEditLister(Context context, Inflexions dataset, View.OnClickListener onModifyListener,
                               View.OnClickListener onDeleteListener)
    {
        super(context, dataset, null, null);

        this.onModifyListener = onModifyListener;
        this.onDeleteListener = onDeleteListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = inflater.inflate(R.layout.i_inflexion_edit, parent, false);

        ViewHolder holder = new ViewHolder(v);
        holder.itemBox.setOnClickListener(onModifyListener);
        holder.edit.setOnClickListener(onModifyListener);
        holder.delete.setOnClickListener(onDeleteListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(InflexionLister.ViewHolder holder, int position)
    {
        super.onBindViewHolder(holder, position);

        Inflexion inflexion = dataset.get(position);
        ((ViewHolder) holder).itemBox.setTag(inflexion);
        ((ViewHolder) holder).edit.setTag(inflexion);
        ((ViewHolder) holder).delete.setTag(inflexion);

        for (int i = 0; i < holder.list_translations.getChildCount(); i++)
            holder.list_translations.getChildAt(i).setClickable(false);
    }

}