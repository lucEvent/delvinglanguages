package com.delvinglanguages.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DelvingList;
import com.delvinglanguages.kernel.DelvingListManager;
import com.delvinglanguages.kernel.manager.DReferenceNavigator;
import com.delvinglanguages.view.lister.InflexionLister;
import com.delvinglanguages.view.utils.HorizontalFloatingButtonBar;

public class DReferenceActivity extends AppCompatActivity {

    private DelvingListManager dataManager;

    private DReferenceNavigator navigator;

    private InflexionLister adapter;

    private HorizontalFloatingButtonBar floatingButtonBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_dreference);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Buttons
        floatingButtonBar = (HorizontalFloatingButtonBar) findViewById(R.id.option_buttons);
        floatingButtonBar.addButton(R.drawable.ic_edit, R.string.edit, onEdit);
        floatingButtonBar.addButton(R.drawable.ic_delete, R.string.delete, onRemove);

        dataManager = new DelvingListManager(this);
        DelvingList delvingList = dataManager.getCurrentList();

        //Reference
        String reference_str = getIntent().getExtras().getString(AppCode.DREFERENCE_NAME);
        navigator = new DReferenceNavigator(this, delvingList, delvingList.from_code, delvingList.to_code, reference_str == null);
        floatingButtonBar.setEnabled(reference_str == null);

        if (reference_str == null)
            reference_str = getIntent().getExtras().getString(AppCode.DREFERENCE_NAME_INVERSE);

        DReference reference = navigator.forward(reference_str);
        displayDReference(reference);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed()
    {
        if (navigator.hasMore())
            displayDReference(navigator.back());
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return true;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        navigator.destroy();
    }

    private void displayDReference(DReference reference)
    {
        if (adapter == null) {
            adapter = new InflexionLister(this, reference.getInflexions(), onNavigationForward);
            findViewById(R.id.textview_pronunciation).setOnClickListener(navigator.onPronunciationAction);
        } else
            adapter.setNewDataSet(reference.getInflexions());

        ((CollapsingToolbarLayout) findViewById(R.id.toolbar_layout)).setTitle(reference.name);

        ((TextView) findViewById(R.id.textview_pronunciation)).setText(reference.pronunciation);

        floatingButtonBar.close();
        floatingButtonBar.setEnabled(!floatingButtonBar.isEnabled());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppCode.DREFERENCE_UPDATED) {
            DReference ref = navigator.current();

            ((CollapsingToolbarLayout) findViewById(R.id.toolbar_layout)).setTitle(ref.name);
            ((TextView) findViewById(R.id.textview_pronunciation)).setText(ref.pronunciation);
            adapter.setNewDataSet(ref.getInflexions());

            setResult(resultCode);
        }
    }

    private View.OnClickListener onEdit = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(v.getContext(), ReferenceEditorActivity.class);
            intent.putExtra(AppCode.ACTION, ReferenceEditorActivity.ACTION_MODIFY);
            intent.putExtra(AppCode.DREFERENCE_NAME, navigator.current().name);
            startActivityForResult(intent, 0);
        }
    };

    private View.OnClickListener onRemove = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            new AlertDialog.Builder(v.getContext())
                    .setTitle(R.string.msg_confirm_to_remove)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            onConfirmRemove();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .create()
                    .show();
        }
    };

    private void onConfirmRemove()
    {
        dataManager.removeReference(navigator.current());
        setResult(AppCode.DREFERENCE_REMOVED);
        finish();
    }

    private View.OnClickListener onNavigationForward = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            TextView tv = (TextView) v;
            String translation = (String) tv.getText();
            displayDReference(navigator.forward(translation));
        }
    };

}

