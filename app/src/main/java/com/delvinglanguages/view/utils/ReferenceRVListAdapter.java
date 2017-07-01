package com.delvinglanguages.view.utils;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;

import com.delvinglanguages.kernel.DReference;

public class ReferenceRVListAdapter extends SortedList<DReference> {

    public ReferenceRVListAdapter(final RecyclerView.Adapter adapter)
    {
        super(DReference.class, new SortedList.Callback<DReference>() {

            @Override
            public int compare(DReference o1, DReference o2)
            {
                return o1.compareTo(o2);
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
            public boolean areContentsTheSame(DReference oldItem, DReference newItem)
            {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areItemsTheSame(DReference item1, DReference item2)
            {
                return item1.id == item2.id;
            }

        });
    }

}
