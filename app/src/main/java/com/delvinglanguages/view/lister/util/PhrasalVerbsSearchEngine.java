package com.delvinglanguages.view.lister.util;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.inputmethod.InputMethodManager;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.phrasalverb.PhrasalVerb;
import com.delvinglanguages.view.lister.SearchableLister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

public class PhrasalVerbsSearchEngine implements SearchView.OnQueryTextListener {

    private final Activity context;
    private final RecyclerView recyclerView;
    private final SearchView search;

    private final TreeSet<PhrasalVerb> searchableItems;
    private Collection<PhrasalVerb> queriedItems;
    private String lastQuery;

    public PhrasalVerbsSearchEngine(Activity activity, RecyclerView recyclerView, SearchView search, TreeSet<PhrasalVerb> searchableItems)
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
        Collection<PhrasalVerb> searchables = query.startsWith(lastQuery) ? queriedItems : searchableItems;

        lastQuery = query;

        query = query.toLowerCase();

        ArrayList<PhrasalVerb> newQueriedItems = new ArrayList<>();
        for (PhrasalVerb s : searchables)
            if (s.hasContent(query))
                newQueriedItems.add(s);

        SearchableLister<PhrasalVerb> adapter = (SearchableLister<PhrasalVerb>) recyclerView.getAdapter();
        adapter.replaceAll(newQueriedItems);
        recyclerView.scrollToPosition(0);

        queriedItems = newQueriedItems;

        return true;
    }

    public void remove(DReference reference)
    {
        int index = reference.name.indexOf(" ");
        String verb = reference.name.substring(0, index > 0 ? index : reference.name.length());

        PhrasalVerb phv = searchableItems.ceiling(new PhrasalVerb(verb));
        phv.removeVariant(reference);

        if (phv.variants.isEmpty()) {
            searchableItems.remove(phv);
            queriedItems.remove(phv);
            ((SearchableLister<PhrasalVerb>) recyclerView.getAdapter()).removeItem(phv);
        } else
            update(phv);
    }

    public void update(DReference reference)
    {
        int index = reference.name.indexOf(" ");
        String verb = reference.name.substring(0, index > 0 ? index : reference.name.length());

        update(searchableItems.ceiling(new PhrasalVerb(verb)));
    }

    private void update(PhrasalVerb phrasalVerb)
    {
        if (phrasalVerb.hasContent(lastQuery)) {
            ((SearchableLister<PhrasalVerb>) recyclerView.getAdapter()).updateItem(phrasalVerb);
        } else {
            queriedItems.remove(phrasalVerb);
            ((SearchableLister<PhrasalVerb>) recyclerView.getAdapter()).removeItem(phrasalVerb);
        }
    }

}
