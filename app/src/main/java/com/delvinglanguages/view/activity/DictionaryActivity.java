package com.delvinglanguages.view.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DelvingListManager;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.Item;
import com.delvinglanguages.view.lister.DictionaryLister;
import com.delvinglanguages.view.lister.ReferenceLister;
import com.delvinglanguages.view.utils.MainSearch;
import com.delvinglanguages.view.utils.NoContentViewHelper;

public class DictionaryActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener {

    private DelvingListManager dataManager;
    private DReferences references;

    private ReferenceLister adapter;

    private SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_dictionary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dataManager = new DelvingListManager(this);
        references = dataManager.getReferences();
        queriedReferences = references;
        lastQuery = "";

        if (!references.isEmpty()) {

            adapter = new DictionaryLister(references, dataManager.getCurrentList().arePhrasalVerbsEnabled(), this, getResources());

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setAutoMeasureEnabled(true);

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
            recyclerView.setHasFixedSize(false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

        } else

            displayNoContentMessage();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case AppCode.DREFERENCE_REMOVED:
                references = dataManager.getReferences();
                queriedReferences = references;

                if (references.isEmpty())
                    displayNoContentMessage();

            case AppCode.DREFERENCE_UPDATED:
                onQueryTextChange(lastQuery);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.m_dictionary, menu);

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search = (SearchView) menu.findItem(R.id.search).getActionView();

        if (!references.isEmpty()) {
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
            search.setOnQueryTextListener(this);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    private DReferences queriedReferences;
    private String lastQuery;

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        invalidateOptionsMenu();
        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(search.getWindowToken(), 0);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query)
    {
        adapter.clear();

        DReferences searchableRefs;
        if (query.startsWith(lastQuery))
            searchableRefs = queriedReferences;
        else
            searchableRefs = references;

        lastQuery = query;

        query = query.toLowerCase();
        boolean foundPerfectMatch = false;

        DReferences newQueriedReference = new DReferences();
        for (DReference ref : searchableRefs)
            if (ref.hasContent(query)) {
                newQueriedReference.add(ref);
                adapter.addItem(ref);

                if (ref.name.toLowerCase().equals(query))
                    foundPerfectMatch = true;
            }
        queriedReferences = newQueriedReference;

        if (adapter.isEmpty())
            adapter.addItem(new MainSearch(query, true));
        else if (!foundPerfectMatch)
            adapter.addItem(new MainSearch(query, false));

        return true;
    }

    @Override
    public void onClick(View v)
    {
        Item item = (Item) v.getTag();

        if (item instanceof DReference) {
            DReference ref = (DReference) item;

            Intent intent = new Intent(this, DReferenceActivity.class);
            intent.putExtra(AppCode.DREFERENCE_NAME, ref.name);
            startActivityForResult(intent, AppCode.ACTION_MODIFY);
        } else {
            MainSearch searchItem = (MainSearch) item;

            if (v.getId() == R.id.button_add_to_drawer) {
                dataManager.createDrawerReference(searchItem.term);
                Toast.makeText(v.getContext(), v.getResources().getString(R.string.msg_added_to_drawer, searchItem.term), Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, WebSearchActivity.class);
            intent.putExtra(AppCode.SEARCH_TERM, searchItem.term);
            startActivity(intent);
        }
    }

    private void displayNoContentMessage()
    {
        new NoContentViewHelper(findViewById(R.id.no_content), R.string.msg_no_content_dictionary)
                .displayMessage();
    }

}
