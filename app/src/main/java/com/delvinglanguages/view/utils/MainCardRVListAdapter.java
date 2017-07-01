package com.delvinglanguages.view.utils;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.util.Item;

public class MainCardRVListAdapter extends SortedList<Item> {

    public MainCardRVListAdapter(final RecyclerView.Adapter adapter)
    {
        super(Item.class, new SortedList.Callback<Item>() {

            @Override
            public int compare(Item o1, Item o2)
            {
                return getComparableString(o1).compareTo(getComparableString(o2));
            }

            private String getComparableString(Item item)
            {
                switch (item.itemType) {
                    case Item.DREFERENCE:
                        return ((DReference) item).name;
                    case Item.THEME:
                        return ((Theme) item).getName();
                    case Item.TEST:
                        return ((Test) item).name;
                    case Item.TYPES_DATA:
                        return "2";
                    case Item.STATS_DATA:
                        return "1";
                }
                return "";
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
            public boolean areContentsTheSame(Item oldItem, Item newItem)
            {
                return oldItem.id == newItem.id && oldItem.itemType == newItem.itemType;
            }

            @Override
            public boolean areItemsTheSame(Item item1, Item item2)
            {
                return item1.id == item2.id && item1.itemType == item2.itemType;
            }

        });
    }

}
