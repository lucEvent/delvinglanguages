package com.delvinglanguages.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.LanguageManager;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.view.lister.ReferenceLister;
import com.delvinglanguages.view.utils.AppCode;
import com.delvinglanguages.view.utils.ContentLoader;

public class DictionaryActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener {

    private DReferences references;

    private boolean phrasalsEnabled;

    private ReferenceLister adapter;

    private View title_bar;
    private SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_dictionary);

        title_bar = findViewById(R.id.title_bar);
        search = (SearchView) findViewById(R.id.search_bar);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose()
            {
                hideSearchAction();
                return true;
            }
        });

        //phrasalsEnabled = LanguageKernelControl.getLanguageSettings(Language.MASK_PH);

        references = new LanguageManager(this).getReferences();
        queriedReferences = references;
        lastQuery = "";

        adapter = new ReferenceLister(references, phrasalsEnabled, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new ContentLoader(layoutManager) {
            @Override
            public void onLoadMore()
            {
                adapter.loadMoreData();
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AppCode.DREFERENCE_UPDATED || resultCode == AppCode.DREFERENCE_DELETED) {
            String query = lastQuery;
            lastQuery = "____";
            onQueryTextChange(query);
        }
    }

    public void startSearch(View view)
    {
        title_bar.setVisibility(View.GONE);
        search.setVisibility(SearchView.VISIBLE);

        search.setIconified(false);
    }

    private void hideSearchAction()
    {
        title_bar.setVisibility(View.VISIBLE);
        search.setVisibility(SearchView.GONE);
    }

    private DReferences queriedReferences;
    private String lastQuery;

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        hideSearchAction();
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
