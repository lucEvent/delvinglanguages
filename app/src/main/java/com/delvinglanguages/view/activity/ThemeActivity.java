package com.delvinglanguages.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.theme.ThemeManager;
import com.delvinglanguages.view.lister.ThemePairLister;
import com.delvinglanguages.view.utils.AppCode;

public class ThemeActivity extends AppCompatActivity {

    private ThemeManager dataManager;

    private Theme theme;

    private ThemePairLister adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_theme);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        dataManager = new ThemeManager(this);

        int theme_id = getIntent().getExtras().getInt(AppCode.THEME_ID);
        theme = dataManager.getThemes().getThemeById(theme_id);

        adapter = new ThemePairLister(theme.getPairs());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        setTitle(theme.getName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AppCode.THEME_MODIFIED) {
            setResult(resultCode);
            finish();
        }
    }

    public void actionEdit(View v) {

        Intent intent = new Intent(this, ThemeEditorActivity.class);
        intent.putExtra(AppCode.THEME_ID, theme.id);
        startActivityForResult(intent, 0);

    }

    public void actionDelete(View v) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.msg_confirm_to_delete_theme, theme.getName()))
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        onConfirmDelete();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show();
    }

    private void onConfirmDelete() {
        dataManager.deleteTheme(theme);
        setResult(AppCode.THEME_DELETED);
        finish();
    }
}

