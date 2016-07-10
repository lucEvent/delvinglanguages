package com.delvinglanguages.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.delvinglanguages.AppSettings;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.LanguageManager;
import com.delvinglanguages.kernel.manager.DReferenceNavigator;
import com.delvinglanguages.view.lister.InflexionLister;
import com.delvinglanguages.view.utils.AppCode;

public class DReferenceActivity extends AppCompatActivity {

    private LanguageManager dataManager;

    private DReferenceNavigator navigator;

    private InflexionLister adapter;

    private FloatingActionButton editButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_dreference);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataManager = new LanguageManager(this);
        navigator = new DReferenceNavigator(this, dataManager.getCurrentLanguage().getLocale(),
                Language.getLocale(AppSettings.getAppLanguageCode()));

        //Buttons
        editButton = (FloatingActionButton) findViewById(R.id.edit_button);
        deleteButton = (FloatingActionButton) findViewById(R.id.delete_button);
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);

        //Reference
        String reference_str = getIntent().getExtras().getString(AppCode.DREFERENCE_NAME);
        DReference reference = dataManager.getReference(reference_str);
        navigator.forward(reference);

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


        editButton.setEnabled(!editButton.isEnabled());
        deleteButton.setEnabled(!deleteButton.isEnabled());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppCode.DREFERENCE_UPDATED) {
            setResult(resultCode);
            finish();
        }
    }

    public void actionEdit(View v)
    {
        Intent intent = new Intent(this, ReferenceEditorActivity.class);
        intent.putExtra(AppCode.ACTION, ReferenceEditorActivity.ACTION_MODIFY);
        intent.putExtra(AppCode.DREFERENCE_NAME, navigator.current().name);
        startActivityForResult(intent, 0);
    }

    public void actionDelete(View v)
    {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.msg_confirm_to_delete_word, navigator.current().name))
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        onConfirmDelete();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show();
    }

    private void onConfirmDelete()
    {
        dataManager.deleteReferenceTemporarily(navigator.current());
        setResult(AppCode.DREFERENCE_DELETED);
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

