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

import com.delvinglanguages.R;
import com.delvinglanguages.Settings;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.theme.ThemeManager;
import com.delvinglanguages.kernel.theme.ThemePair;
import com.delvinglanguages.kernel.util.ThemePairs;
import com.delvinglanguages.view.lister.ThemePairEditLister;
import com.delvinglanguages.view.utils.AppCode;

public class ThemeEditorActivity extends AppCompatActivity {

    private enum State {
        CREATE, EDIT
    }

    private static class EditorData {

        State state;
        Theme theme;
        ThemePairs pairs;

    }

    private EditorData data;
    private int modifyingPosition = -1;

    private ThemeManager dataManager;
    private ThemePairEditLister adapter;

    private EditText in_pair1, in_pair2;
    private EditText in_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_theme_editor);

        in_name = (EditText) findViewById(R.id.in_theme_name);
        in_pair1 = (EditText) findViewById(R.id.in_pair1);
        in_pair2 = (EditText) findViewById(R.id.in_pair2);

        dataManager = new ThemeManager(this);
        data = new EditorData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(AppCode.THEME_ID)) {

            data.state = State.EDIT;

            int theme_id = bundle.getInt(AppCode.THEME_ID);
            data.theme = dataManager.getThemes().get(theme_id);
            data.pairs = (ThemePairs) data.theme.getPairs().clone();

            in_name.setText(data.theme.getName());
            in_name.setSelection(data.theme.getName().length());

        } else {

            data.state = State.CREATE;
            data.pairs = new ThemePairs();

        }

        adapter = new ThemePairEditLister(data.pairs, onModifyThemePair);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        in_pair1.setHint(getString(R.string.hint_in_, dataManager.getCurrentLanguage().language_name));
        in_pair2.setHint(getString(R.string.hint_in_, getResources().getStringArray(R.array.languages)[Settings.getAppLanguageCode()]));

    }

    @Override
    public void onBackPressed() {
        actionCancel(null);
    }

    private View.OnClickListener onModifyThemePair = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ThemePair pair = (ThemePair) v.getTag();
            int position = data.pairs.indexOf(pair);
            if (v.getId() == R.id.edit) {
                modifyingPosition = position;

                in_pair1.setText(pair.inDelved);
                in_pair2.setText(pair.inNative);
            } else {

                data.pairs.remove(position);
                adapter.notifyItemRemoved(position);

            }
        }
    };

    public void actionAddPair(View v) {
        String pair1 = in_pair1.getText().toString();
        String pair2 = in_pair2.getText().toString();
        if (pair1.isEmpty() || pair2.isEmpty()) {
            showMessage(R.string.msg_missing_content);
            if (pair1.isEmpty()) in_pair1.requestFocus();
            else in_pair2.requestFocus();
            return;
        }
        if (modifyingPosition != -1) {
            ThemePair modifying = data.pairs.get(modifyingPosition);
            modifying.inDelved = pair1;
            modifying.inNative = pair2;
            adapter.notifyItemChanged(modifyingPosition);
            modifyingPosition = -1;
        } else {
            data.pairs.add(new ThemePair(pair1, pair2));
            adapter.notifyItemInserted(data.pairs.size() - 1);
        }
        in_pair1.setText("");
        in_pair2.setText("");
        in_pair1.requestFocus();
    }

    public void actionSave(View v) {
        String theme_name = in_name.getText().toString();
        if (theme_name.isEmpty()) {
            showMessage(R.string.msg_missing_theme_name);
            in_name.requestFocus();
            return;
        }
        if (data.pairs.isEmpty()) {
            showMessage(R.string.msg_missing_theme_content);
            return;
        }

        if (data.state == State.CREATE) {

            data.theme = dataManager.addTheme(theme_name, data.pairs);
            setResult(AppCode.THEME_CREATED);

        } else {

            dataManager.updateTheme(data.theme, theme_name, data.pairs);
            setResult(AppCode.THEME_MODIFIED);
        }

        Intent intent = new Intent(this, ThemeActivity.class);
        intent.putExtra(AppCode.THEME_ID, data.theme.id);

        startActivity(intent);

        finish();
    }

    public void actionCancel(View v) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.msg_go_back_without_saving)
                .setNegativeButton(R.string.go_out, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ThemeEditorActivity.this.finish();
                    }
                })
                .setPositiveButton(R.string.continue_, null)
                .show();
    }

    private void showMessage(int text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
