package com.delvinglanguages.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
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
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.theme.ThemeManager;
import com.delvinglanguages.view.activity.practise.TestActivity;
import com.delvinglanguages.view.lister.ThemePairLister;

public class ThemeActivity extends AppCompatActivity {

    private ThemeManager dataManager;

    private Theme theme;

    private ThemePairLister adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_theme);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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

        if (resultCode == AppCode.THEME_MODIFIED) {

            CollapsingToolbarLayout toolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
            toolbar.setTitle(theme.getName());

            adapter.setNewDataSet(theme.getPairs());

        }
    }

    public void actionPractise(View v)
    {
        Test test = dataManager.toTest(this, theme);

        Intent intent = new Intent(this, TestActivity.class);
        intent.putExtra(AppCode.TEST_ID, test.id);
        startActivity(intent);
    }

    private Dialog editOptionsDialog;

    public void actionSelectOption(View v)
    {
        editOptionsDialog = new AlertDialog.Builder(this)
                .setView(R.layout.d_edit_delete)
                .create();

        editOptionsDialog.show();
    }

    public void actionEdit(View v)
    {
        editOptionsDialog.dismiss();
        Intent intent = new Intent(this, ThemeEditorActivity.class);
        intent.putExtra(AppCode.THEME_ID, theme.id);
        startActivityForResult(intent, 0);
    }

    public void actionDelete(View v)
    {
        editOptionsDialog.dismiss();
        new AlertDialog.Builder(this)
                .setTitle(R.string.msg_confirm_to_delete_xxx)
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
        dataManager.deleteTheme(theme);
        setResult(AppCode.THEME_DELETED);
        finish();
    }

}

