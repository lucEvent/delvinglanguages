package com.delvinglanguages.view.utils;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.inputmethod.InputMethodManager;

import com.delvinglanguages.view.lister.SearchableLister;
import com.delvinglanguages.view.lister.util.ReferenceSelector;

import java.util.ArrayList;

public class SelectorSearchEngine implements SearchView.OnQueryTextListener {

    private final Activity context;
    private final RecyclerView recyclerView;
    private final SearchView search;

    private final ArrayList<ReferenceSelector> searchableItems;
    private ArrayList<ReferenceSelector> queriedItems;
    private String lastQuery;

    public SelectorSearchEngine(Activity activity, RecyclerView recyclerView, SearchView search, ArrayList<ReferenceSelector> searchableItems)
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
        ArrayList<ReferenceSelector> searchableRefs = query.startsWith(lastQuery) ? queriedItems : searchableItems;

        lastQuery = query;

        query = query.toLowerCase();

        ArrayList<ReferenceSelector> newQueriedReference = new ArrayList<>();
        for (ReferenceSelector s : searchableRefs)
            if (s.reference.hasContent(query))
                newQueriedReference.add(s);

        SearchableLister<ReferenceSelector> adapter = (SearchableLister<ReferenceSelector>) recyclerView.getAdapter();
        adapter.replaceAll(newQueriedReference);
        recyclerView.scrollToPosition(0);

        queriedItems = newQueriedReference;

        return true;
    }

    public void restart()
    {
        this.queriedItems = searchableItems;
        onQueryTextChange(lastQuery);
    }

}
