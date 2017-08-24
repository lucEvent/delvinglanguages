package com.delvinglanguages.view.utils;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.inputmethod.InputMethodManager;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.Item;
import com.delvinglanguages.view.lister.SearchableLister;

public class SearchEngine implements SearchView.OnQueryTextListener {

    private final Activity context;
    private final RecyclerView recyclerView;
    private final SearchView search;

    private final DReferences searchableItems;
    private DReferences queriedItems;
    private String lastQuery;

    public SearchEngine(Activity activity, RecyclerView recyclerView, SearchView search, DReferences searchableItems)
    {
        this.context = activity;
        this.recyclerView = recyclerView;
        this.search = search;
        this.searchableItems = searchableItems;

        queriedItems = searchableItems;
        lastQuery = "";

        SearchManager manager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        search.setSearchableInfo(manager.getSearchableInfo(activity.getComponentName()));
        search.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        context.invalidateOptionsMenu();
        ((InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(search.getWindowToken(), 0);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query)
    {
        DReferences searchableRefs = query.startsWith(lastQuery) ? queriedItems : searchableItems;

        lastQuery = query;

        query = query.toLowerCase();
        boolean foundPerfectMatch = false;

        DReferences newQueriedReference = new DReferences();
        for (DReference ref : searchableRefs)
            if (ref.hasContent(query)) {
                newQueriedReference.add(ref);

                if (ref.name.toLowerCase().equals(query))
                    foundPerfectMatch = true;
            }
        SearchableLister<Item> adapter = (SearchableLister<Item>) recyclerView.getAdapter();
        adapter.replaceAll(newQueriedReference);

        queriedItems = newQueriedReference;

        if (adapter.isEmpty())
            adapter.addItem(new MainSearch(query, true));
        else if (!foundPerfectMatch)
            adapter.addItem(new MainSearch(query, false));

        recyclerView.scrollToPosition(0);

        return true;
    }

    public void remove(DReference reference)
    {
        searchableItems.remove(reference);
        queriedItems.remove(reference);
        ((SearchableLister<Item>) recyclerView.getAdapter()).removeItem(reference);
    }

    public void update(DReference reference)
    {
        if (reference.hasContent(lastQuery)) {
            ((SearchableLister<Item>) recyclerView.getAdapter()).updateItem(reference);
        } else {
            queriedItems.remove(reference);
            ((SearchableLister<Item>) recyclerView.getAdapter()).removeItem(reference);
        }
    }

}
