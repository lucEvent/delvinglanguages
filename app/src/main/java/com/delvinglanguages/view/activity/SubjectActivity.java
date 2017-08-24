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

import com.delvinglanguages.AppCode;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.subject.Subject;
import com.delvinglanguages.kernel.subject.SubjectManager;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.view.activity.practise.TestActivity;
import com.delvinglanguages.view.lister.ReferenceLister;
import com.delvinglanguages.view.utils.HorizontalFloatingButtonBar;

public class SubjectActivity extends AppCompatActivity {

    private SubjectManager dataManager;

    private Subject subject;

    private ReferenceLister adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_subject);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dataManager = new SubjectManager(this);

        int subject_id = getIntent().getExtras().getInt(AppCode.SUBJECT_ID);
        subject = dataManager.getSubjects().getSubjectById(subject_id);

        adapter = new ReferenceLister(dataManager.getReferences(subject), dataManager.getCurrentList().arePhrasalVerbsEnabled(), onEditReference);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //Buttons
        HorizontalFloatingButtonBar floatingButtonBar = (HorizontalFloatingButtonBar) findViewById(R.id.option_buttons);
        floatingButtonBar.addButton(R.drawable.ic_play, R.string.test, onTest);
        floatingButtonBar.addButton(R.drawable.ic_edit, R.string.edit, onEdit);
        floatingButtonBar.addButton(R.drawable.ic_delete, R.string.delete, onDelete);

        setTitle(subject.getName());
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

    private DReference editingReference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case AppCode.SUBJECT_MODIFIED:
                CollapsingToolbarLayout toolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
                toolbar.setTitle(subject.getName());

                adapter.replaceAll(dataManager.getReferences(subject));
                break;
            case AppCode.DREFERENCE_REMOVED:
                adapter.removeItem(editingReference);
                break;
            case AppCode.DREFERENCE_UPDATED:
                adapter.updateItem(editingReference);
                break;
        }
    }

    private View.OnClickListener onEditReference = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            editingReference = (DReference) v.getTag();

            Intent intent = new Intent(SubjectActivity.this, DReferenceActivity.class);
            intent.putExtra(AppCode.DREFERENCE_NAME, editingReference.name);
            startActivityForResult(intent, AppCode.ACTION_MODIFY);
        }
    };

    private View.OnClickListener onTest = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            Test test = dataManager.toTest(v.getContext(), subject);

            Intent intent = new Intent(v.getContext(), TestActivity.class);
            intent.putExtra(AppCode.TEST_ID, test.id);
            startActivity(intent);
        }
    };

    private View.OnClickListener onEdit = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(v.getContext(), SubjectEditorActivity.class);
            intent.putExtra(AppCode.SUBJECT_ID, subject.id);
            startActivityForResult(intent, 0);
        }
    };

    private View.OnClickListener onDelete = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            new AlertDialog.Builder(v.getContext())
                    .setTitle(R.string.msg_confirm_to_remove)
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
    };

    private void onConfirmDelete()
    {
        dataManager.removeSubject(subject);
        setResult(AppCode.SUBJECT_REMOVED);
        finish();
    }

}

