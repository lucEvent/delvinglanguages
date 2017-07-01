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

import com.delvinglanguages.AppCode;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.LanguageManager;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.view.lister.ReferenceLister;

public class DictionaryActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener {

    private LanguageManager dataManager;
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

        dataManager = new LanguageManager(this);
        references = dataManager.getReferences();
        queriedReferences = references;
        lastQuery = "";

        adapter = new ReferenceLister(references, dataManager.getCurrentLanguage().getSetting(Language.MASK_PHRASAL_VERBS), this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case AppCode.DREFERENCE_DELETED:
                references = dataManager.getReferences();
                queriedReferences = references;

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

        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(this);

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
        if (query.startsWith(lastQuery))
            queriedReferences = filter(queriedReferences, query);
        else
            queriedReferences = filter(references, query);

        lastQuery = query;
        adapter.setNewDataSet(queriedReferences);
        return true;
    }

    private DReferences filter(DReferences models, String query)
    {
        query = query.toLowerCase();

        DReferences filteredList = new DReferences();
        for (DReference ref : models)
            if (ref.hasContent(query)) filteredList.add(ref);

        return filteredList;
    }

    @Override
    public void onClick(View v)
    {
        DReference ref = (DReference) v.getTag();

        Intent intent = new Intent(this, DReferenceActivity.class);
        intent.putExtra(AppCode.DREFERENCE_NAME, ref.name);
        startActivityForResult(intent, AppCode.ACTION_MODIFY);
    }

}
