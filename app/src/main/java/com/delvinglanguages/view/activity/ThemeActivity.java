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
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.theme.ThemeManager;
import com.delvinglanguages.view.activity.practise.TestActivity;
import com.delvinglanguages.view.lister.ThemePairLister;
import com.delvinglanguages.view.utils.HorizontalFloatingButtonBar;

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

        //Buttons
        HorizontalFloatingButtonBar floatingButtonBar = (HorizontalFloatingButtonBar) findViewById(R.id.option_buttons);
        floatingButtonBar.addButton(R.drawable.ic_play, R.string.test, onTest);
        floatingButtonBar.addButton(R.drawable.ic_edit, R.string.edit, onEdit);
        floatingButtonBar.addButton(R.drawable.ic_delete, R.string.delete, onDelete);

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

    private View.OnClickListener onTest = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            Test test = dataManager.toTest(v.getContext(), theme);

            Intent intent = new Intent(v.getContext(), TestActivity.class);
            intent.putExtra(AppCode.TEST_ID, test.id);
            startActivity(intent);
        }
    };

    private View.OnClickListener onEdit = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(v.getContext(), ThemeEditorActivity.class);
            intent.putExtra(AppCode.THEME_ID, theme.id);
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
        dataManager.removeTheme(theme);
        setResult(AppCode.THEME_REMOVED);
        finish();
    }

}

