package com.delvinglanguages.view.lister.sorted;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;

import com.delvinglanguages.view.lister.util.ReferenceSelector;

public class ReferenceSelectorList extends SortedList<ReferenceSelector> {

    public ReferenceSelectorList(final RecyclerView.Adapter adapter)
    {
        super(ReferenceSelector.class, new SortedList.Callback<ReferenceSelector>() {

            @Override
            public int compare(ReferenceSelector o1, ReferenceSelector o2)
            {
                return o1.reference.compareTo(o2.reference);
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
            public boolean areContentsTheSame(ReferenceSelector s1, ReferenceSelector s2)
            {
                return s1.id == s2.id;
            }

            @Override
            public boolean areItemsTheSame(ReferenceSelector s1, ReferenceSelector s2)
            {
                return s1.id == s2.id;
            }

        });
    }

}
