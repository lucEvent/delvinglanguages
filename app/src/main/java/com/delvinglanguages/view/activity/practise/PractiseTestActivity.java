package com.delvinglanguages.view.activity.practise;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.test.TestManager;
import com.delvinglanguages.kernel.util.Tests;
import com.delvinglanguages.view.lister.TestLister;
import com.delvinglanguages.view.utils.TestListener;

public class PractiseTestActivity extends AppCompatActivity {

    private TestLister adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_list_with_button);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(
                getString(R.string.title_activity_practisetest,
                        new KernelManager(this).getCurrentLanguage().language_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Tests tests = new TestManager(this).getTests();

        if (tests.isEmpty())
            onAddAction(null);

        adapter = new TestLister(tests, onTestSelected);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        adapter.notifyDataSetChanged();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case TestListener.TEST_REMOVED:
            case TestListener.TEST_CREATED:
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private View.OnClickListener onTestSelected = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            Test test = (Test) v.getTag();

            Intent intent = new Intent(PractiseTestActivity.this, TestActivity.class);
            intent.putExtra(AppCode.TEST_ID, test.id);
            startActivityForResult(intent, AppCode.ACTION_MODIFY);
        }
    };

    public void onAddAction(View v)
    {
        Intent intent = new Intent(this, TestEditorActivity.class);
        startActivityForResult(intent, AppCode.ACTION_CREATE);
    }

}
