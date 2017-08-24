package com.delvinglanguages.view.lister;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;


public abstract class SearchableLister<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public abstract void addItem(T item);

    public abstract void removeItem(T item);

    public abstract void updateItem(T item);

    public abstract void replaceAll(ArrayList<? extends T> items);

    public abstract boolean isEmpty();

}
