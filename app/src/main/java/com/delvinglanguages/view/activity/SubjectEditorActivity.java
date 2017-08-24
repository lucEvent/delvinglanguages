package com.delvinglanguages.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.subject.Subject;
import com.delvinglanguages.kernel.subject.SubjectManager;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.view.lister.ReferenceLister;
import com.delvinglanguages.view.utils.ListItemSwipeCallback;
import com.delvinglanguages.view.utils.ListItemSwipeListener;
import com.delvinglanguages.view.utils.NoContentViewHelper;

public class SubjectEditorActivity extends AppCompatActivity implements ListItemSwipeListener {

    private enum State {
        CREATE, EDIT
    }

    private static class EditorData {

        State state;
        Subject subject;
        DReferences references;

    }

    private EditorData data;
    private DReference editingReference;

    private SubjectManager dataManager;
    private ReferenceLister adapter;
    private NoContentViewHelper noContentViewHelper;

    private EditText in_name;
    private View rem_help;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_subject_editor);

        in_name = (EditText) findViewById(R.id.in_subject_name);
        rem_help = findViewById(R.id.help_message);
        noContentViewHelper = new NoContentViewHelper(findViewById(R.id.no_content), R.string.msg_no_content_entries);

        dataManager = new SubjectManager(this);
        data = new EditorData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(AppCode.SUBJECT_ID)) {

            data.state = State.EDIT;

            int subject_id = bundle.getInt(AppCode.SUBJECT_ID);
            data.subject = dataManager.getSubjects().getSubjectById(subject_id);
            data.references = (DReferences) dataManager.getReferences(data.subject).clone();

            in_name.setText(data.subject.getName());
            in_name.setSelection(data.subject.getName().length());

        } else {

            data.state = State.CREATE;
            data.references = new DReferences();

            noContentViewHelper.displayMessage();
            rem_help.setVisibility(View.GONE);
        }

        adapter = new ReferenceLister(data.references, dataManager.getCurrentList().arePhrasalVerbsEnabled(), onEditReference);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new ListItemSwipeCallback(this, (TextView) findViewById(R.id.swipe_message));
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onBackPressed()
    {
        actionCancel(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (resultCode) {
            case AppCode.DREFERENCE_CREATED:
                String refName = intent.getExtras().getString(AppCode.DREFERENCE_NAME);
                DReference newRef = dataManager.getReference(refName);
                adapter.addItem(newRef);
                if (!data.references.contains(newRef))
                    data.references.add(newRef);

                if (!adapter.isEmpty()) {
                    noContentViewHelper.hide();
                    rem_help.setVisibility(View.VISIBLE);
                }
                break;
            case AppCode.DREFERENCE_REMOVED:
                data.references.remove(editingReference);
                adapter.removeItem(editingReference);

                if (adapter.isEmpty()) {
                    noContentViewHelper.displayMessage();
                    rem_help.setVisibility(View.GONE);
                }
                break;
            case AppCode.DREFERENCE_UPDATED:
                adapter.updateItem(editingReference);
                break;
            case AppCode.DREFERENCE_SELECTED:
                Bundle extras = intent.getExtras();

                int num = extras.getInt(AppCode.DREFERENCE_NAME_NUM);
                for (int i = 0; i < num; i++) {
                    newRef = dataManager.getReference(extras.getString(AppCode.DREFERENCE_NAME + i));

                    adapter.addItem(newRef);
                    if (!data.references.contains(newRef))
                        data.references.add(newRef);
                }

                if (!adapter.isEmpty()) {
                    noContentViewHelper.hide();
                    rem_help.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private View.OnClickListener onEditReference = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            editingReference = (DReference) v.getTag();

            Intent intent = new Intent(SubjectEditorActivity.this, DReferenceActivity.class);
            intent.putExtra(AppCode.DREFERENCE_NAME, editingReference.name);
            startActivityForResult(intent, AppCode.ACTION_MODIFY);
        }
    };

    public void onAddNewReference(View view)
    {
        Intent intent = new Intent(this, ReferenceEditorActivity.class);
        intent.putExtra(AppCode.ACTION, ReferenceEditorActivity.ACTION_CREATE_FOR_SUBJECT);
        startActivityForResult(intent, AppCode.ACTION_CREATE);
    }

    public void onAddExistingReference(View view)
    {
        Intent intent = new Intent(this, ReferenceSelectorActivity.class);
        startActivityForResult(intent, AppCode.ACTION_SELECT);
    }

    public void actionSave(View v)
    {
        String subject_name = in_name.getText().toString();
        if (subject_name.isEmpty()) {
            showMessage(R.string.msg_missing_subject_name);
            in_name.requestFocus();
            return;
        }
        if (data.references.isEmpty()) {
            showMessage(R.string.msg_missing_subject_content);
            return;
        }

        if (data.state == State.CREATE) {

            data.subject = dataManager.addSubject(subject_name, data.references);
            setResult(AppCode.SUBJECT_CREATED);

            Intent intent = new Intent(this, SubjectActivity.class);
            intent.putExtra(AppCode.SUBJECT_ID, data.subject.id);
            startActivity(intent);

        } else {

            dataManager.updateSubject(data.subject, subject_name, data.references);
            setResult(AppCode.SUBJECT_MODIFIED);
        }
        finish();
    }

    public void actionCancel(View v)
    {
        new AlertDialog.Builder(this)
                .setTitle(R.string.msg_go_back_without_saving)
                .setNegativeButton(R.string.go_out, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        SubjectEditorActivity.this.finish();
                    }
                })
                .setPositiveButton(R.string.continue_, null)
                .show();
    }

    @Override
    public void onItemDismiss(int position)
    {
        data.references.remove(position);
        adapter.removeItem(position);

        if (adapter.isEmpty()) {
            noContentViewHelper.displayMessage();
            rem_help.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition)
    {
    }

    private void showMessage(int text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
