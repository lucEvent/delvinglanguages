package com.delvinglanguages.view.lister.viewholder;

import android.view.View;

import com.delvinglanguages.R;
import com.delvinglanguages.view.lister.util.ReferenceSelector;

public class ReferenceSelectorViewHolder extends ReferenceViewHolder {

    public ReferenceSelectorViewHolder(View v)
    {
        super(v);
    }

    public void bind(ReferenceSelector selector, boolean showPhrasal, int position)
    {
        super.bind(selector.reference, showPhrasal);

        container.setBackgroundResource(selector.selected ? R.drawable.ripple_selected : R.drawable.ripple);
        container.setTag(position);
    }

}
