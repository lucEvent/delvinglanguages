package com.delvinglanguages.view.lister.sorted;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;

import com.delvinglanguages.kernel.phrasalverb.PhrasalVerb;

public class PhrasalVerbSortedList extends SortedList<PhrasalVerb> {

    public PhrasalVerbSortedList(final RecyclerView.Adapter adapter)
    {
        super(PhrasalVerb.class, new SortedList.Callback<PhrasalVerb>() {

            @Override
            public int compare(PhrasalVerb o1, PhrasalVerb o2)
            {
                return o1.verb.compareTo(o2.verb);
            }

            @Override
            public void onInserted(int position, int count)
            {
                adapter.notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count)
            {
                adapter.notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition)
            {
                adapter.notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count)
            {
                adapter.notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(PhrasalVerb s1, PhrasalVerb s2)
            {
                return s1 == s2 && s1.variants.size() == s2.variants.size();
            }

            @Override
            public boolean areItemsTheSame(PhrasalVerb s1, PhrasalVerb s2)
            {
                return s1 == s2;
            }

        });
    }

}
