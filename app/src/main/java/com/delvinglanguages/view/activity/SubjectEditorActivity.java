package com.delvinglanguages.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.AppData;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DelvingList;
import com.delvinglanguages.kernel.subject.Subject;
import com.delvinglanguages.kernel.subject.SubjectManager;
import com.delvinglanguages.kernel.subject.SubjectPair;
import com.delvinglanguages.kernel.util.SubjectPairs;
import com.delvinglanguages.view.lister.SubjectPairEditLister;
import com.delvinglanguages.view.utils.NoContentViewHelper;

public class SubjectEditorActivity extends AppCompatActivity {

    private enum State {
        CREATE, EDIT
    }

    private static class EditorData {

        State state;
        Subject subject;
        SubjectPairs pairs;

    }

    private EditorData data;
    private int modifyingPosition = -1;

    private SubjectManager dataManager;
    private SubjectPairEditLister adapter;
    private NoContentViewHelper noContentViewHelper;

    private EditText in_pair1, in_pair2;
    private EditText in_name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_subject_editor);

        in_name = (EditText) findViewById(R.id.in_subject_name);
        in_pair1 = (EditText) findViewById(R.id.in_pair1);
        in_pair2 = (EditText) findViewById(R.id.in_pair2);
        noContentViewHelper = new NoContentViewHelper(findViewById(R.id.no_content), R.string.msg_no_content_entries);

        dataManager = new SubjectManager(this);
        data = new EditorData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(AppCode.SUBJECT_ID)) {

            data.state = State.EDIT;

            int subject_id = bundle.getInt(AppCode.SUBJECT_ID);
            data.subject = dataManager.getSubjects().getSubjectById(subject_id);
            data.pairs = (SubjectPairs) data.subject.getPairs().clone();

            in_name.setText(data.subject.getName());
            in_name.setSelection(data.subject.getName().length());

        } else {

            data.state = State.CREATE;
            data.pairs = new SubjectPairs();

            noContentViewHelper.displayMessage();
        }

        adapter = new SubjectPairEditLister(data.pairs, onModifySubjectPair);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        DelvingList current = dataManager.getCurrentList();
        in_pair1.setHint(getString(R.string.hint_in_, AppData.getLanguageName(current.from_code)));
        in_pair2.setHint(getString(R.string.hint_in_, AppData.getLanguageName(current.to_code)));
    }

    @Override
    public void onBackPressed()
    {
        actionCancel(null);
    }

    private View.OnClickListener onModifySubjectPair = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            SubjectPair pair = (SubjectPair) v.getTag();
            int position = data.pairs.indexOf(pair);
            if (v.getId() == R.id.edit) {
                modifyingPosition = position;

                in_pair1.setText(pair.inDelved);
                in_pair2.setText(pair.inNative);
            } else {

                data.pairs.remove(position);
                adapter.notifyItemRemoved(position);

                if (adapter.getItemCount() == 0)
                    noContentViewHelper.displayMessage();
            }
        }
    };

    public void actionAddPair(View v)
    {
        String pair1 = in_pair1.getText().toString();
        String pair2 = in_pair2.getText().toString();
        if (pair1.isEmpty() || pair2.isEmpty()) {
            showMessage(R.string.msg_missing_content);
            if (pair1.isEmpty()) in_pair1.requestFocus();
            else in_pair2.requestFocus();
            return;
        }
        if (modifyingPosition != -1) {
            SubjectPair modifying = data.pairs.get(modifyingPosition);
            modifying.inDelved = pair1;
            modifying.inNative = pair2;
            adapter.notifyItemChanged(modifyingPosition);
            modifyingPosition = -1;
        } else {
            if (adapter.getItemCount() == 0)
                noContentViewHelper.hide();

            data.pairs.add(new SubjectPair(pair1, pair2));
            adapter.notifyItemInserted(data.pairs.size() - 1);
        }
        in_pair1.setText("");
        in_pair2.setText("");
        in_pair1.requestFocus();
    }

    public void actionSave(View v)
    {
        String subject_name = in_name.getText().toString();
        if (subject_name.isEmpty()) {
            showMessage(R.string.msg_missing_subject_name);
            in_name.requestFocus();
            return;
        }
        if (data.pairs.isEmpty()) {
            showMessage(R.string.msg_missing_subject_content);
            return;
        }

        if (data.state == State.CREATE) {

            data.subject = dataManager.addSubject(subject_name, data.pairs);
            setResult(AppCode.SUBJECT_CREATED);

            Intent intent = new Intent(this, SubjectActivity.class);
            intent.putExtra(AppCode.SUBJECT_ID, data.subject.id);
            startActivity(intent);

        } else {

            dataManager.updateSubject(data.subject, subject_name, data.pairs);
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

    private void showMessage(int text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
