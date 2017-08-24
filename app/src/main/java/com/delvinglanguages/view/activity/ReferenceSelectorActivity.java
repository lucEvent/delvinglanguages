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

import com.delvinglanguages.AppCode;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DelvingListManager;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.view.lister.ReferenceSelectorLister;
import com.delvinglanguages.view.lister.util.ReferenceSelector;
import com.delvinglanguages.view.utils.NoContentViewHelper;
import com.delvinglanguages.view.utils.SelectorSearchEngine;

import java.util.ArrayList;

public class ReferenceSelectorActivity extends AppCompatActivity {

    private ArrayList<ReferenceSelector> selectors;

    private RecyclerView recyclerView;

    private SelectorSearchEngine searchEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_reference_selector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        DelvingListManager dataManager = new DelvingListManager(this);
        DReferences references = dataManager.getReferences();


        if (!references.isEmpty()) {

            selectors = new ArrayList<>(references.size());
            for (DReference ref : references)
                selectors.add(new ReferenceSelector(ref));

            ReferenceSelectorLister adapter = new ReferenceSelectorLister(selectors, dataManager.getCurrentList().arePhrasalVerbsEnabled());

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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.m_dictionary, menu);

        if (!selectors.isEmpty()) {
            SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();

            searchEngine = new SelectorSearchEngine(this, recyclerView, search, selectors);
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

    public void onSendSelected(View view)
    {
        DReferences selectedReferences = ((ReferenceSelectorLister) recyclerView.getAdapter()).getSelectedReferences();

        if (!selectedReferences.isEmpty()) {
            Intent intent = new Intent();
            intent.putExtra(AppCode.DREFERENCE_NAME_NUM, selectedReferences.size());

            for (int i = 0; i < selectedReferences.size(); i++)
                intent.putExtra(AppCode.DREFERENCE_NAME + i, selectedReferences.get(i).name);

            setResult(AppCode.DREFERENCE_SELECTED, intent);
        }
        finish();
    }

    private void displayNoContentMessage()
    {
        new NoContentViewHelper(findViewById(R.id.no_content), R.string.msg_no_content_dictionary)
                .displayMessage();
    }

}
