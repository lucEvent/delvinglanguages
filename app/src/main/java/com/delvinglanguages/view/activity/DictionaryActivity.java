package com.delvinglanguages.view.activity;

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
import com.delvinglanguages.view.utils.SearchEngine;

public class DictionaryActivity extends AppCompatActivity implements View.OnClickListener {

    private DelvingListManager dataManager;
    private DReferences references;

    private RecyclerView recyclerView;

    private SearchEngine searchEngine;
    private DReference editingPhrasalVerb;

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

        if (!references.isEmpty()) {

            ReferenceLister adapter = new DictionaryLister(references, dataManager.getCurrentList().arePhrasalVerbsEnabled(), this, getResources());

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setAutoMeasureEnabled(true);

            recyclerView = (RecyclerView) findViewById(R.id.list);
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
                searchEngine.remove(editingPhrasalVerb);

                if (references.isEmpty())
                    displayNoContentMessage();
                break;
            case AppCode.DREFERENCE_UPDATED:
                searchEngine.update(editingPhrasalVerb);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.m_dictionary, menu);

        if (!references.isEmpty()) {
            SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();

            searchEngine = new SearchEngine(this, recyclerView, search, references);
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

    @Override
    public void onClick(View v)
    {
        Item item = (Item) v.getTag();

        if (item instanceof DReference) {
            editingPhrasalVerb = (DReference) item;

            Intent intent = new Intent(this, DReferenceActivity.class);
            intent.putExtra(AppCode.DREFERENCE_NAME, editingPhrasalVerb.name);
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
