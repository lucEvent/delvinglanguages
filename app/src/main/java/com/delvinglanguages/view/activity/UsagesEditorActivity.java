package com.delvinglanguages.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DelvingListManager;
import com.delvinglanguages.kernel.Usage;
import com.delvinglanguages.kernel.util.Usages;
import com.delvinglanguages.view.lister.UsageEditorLister;
import com.delvinglanguages.view.utils.AppAnimator;

import java.util.HashSet;

public class UsagesEditorActivity extends AppCompatActivity {

    private DelvingListManager dataManager;
    private DReference reference;
    private HashSet<Usage> editedUsages;
    private Usage currentEditingUsage = null;

    private UsageEditorLister adapter;

    private View editor;
    private EditText editor_input;
    private TextView editor_label;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_usages_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Drawable home_drawable = getResources().getDrawable(R.drawable.ic_cancel, null);
        home_drawable.setTint(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(home_drawable);


        dataManager = new DelvingListManager(this);
        reference = dataManager.getReference(getIntent().getExtras().getString(AppCode.DREFERENCE_NAME));
        editedUsages = new HashSet<>();

        adapter = new UsageEditorLister(this, dataManager.getUsages(reference), onEditUsageStart, onDeleteUsage);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        editor = findViewById(R.id.editor_bar);
        editor_input = (EditText) findViewById(R.id.input);
        editor_label = (TextView) findViewById(R.id.label_usage_for);

        setTitle(getString(R.string.usages_of, reference.name));

        editor.getViewTreeObserver().addOnGlobalLayoutListener(hideBarOnLayoutCreated);
    }

    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setTitle(R.string.msg_go_back_without_saving)
                .setNegativeButton(R.string.go_out, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        UsagesEditorActivity.super.onBackPressed();
                    }
                })
                .setPositiveButton(R.string.continue_, null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuItem mi = menu.add(Menu.NONE, R.id.save_changes, 0, R.string.save_changes)
                .setIcon(R.drawable.ic_ok);

        mi.getIcon().setTint(Color.WHITE);
        mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.save_changes:
                dataManager.deleteUsages(reference, new Usages(editedUsages));

                Usages toCreate = new Usages();
                for (Usage u : editedUsages)
                    if (!u.usage.isEmpty())
                        toCreate.add(u);

                dataManager.createUsages(reference, toCreate);
                setResult(AppCode.DREFERENCE_UPDATED);
                finish();
        }
        return true;
    }

    private ViewTreeObserver.OnGlobalLayoutListener hideBarOnLayoutCreated = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout()
        {
            AppAnimator.bottomBarFastOutAnimation(editor);
            editor.getViewTreeObserver().removeOnGlobalLayoutListener(hideBarOnLayoutCreated);
        }
    };

    private View.OnClickListener onEditUsageStart = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            currentEditingUsage = (Usage) v.getTag();
            editor.setVisibility(View.VISIBLE);

            editor_label.setText(getString(R.string.usage_for, currentEditingUsage.translation));
            editor_input.setText(currentEditingUsage.usage);

            AppAnimator.bottomBarInAnimation(editor);
        }
    };

    private View.OnClickListener onDeleteUsage = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            Usage usage = (Usage) v.getTag();
            if (!usage.usage.isEmpty()) {
                usage.usage = "";
                adapter.updateUsage(usage);

                editedUsages.add(usage);
            }
        }
    };

    public void onSaveUsageEdit(View view)
    {
        String usage = editor_input.getText().toString();
        if (usage.isEmpty()) {
            showMessage(R.string.msg_missing_usage);
            editor_input.requestFocus();
            return;
        }
        currentEditingUsage.usage = usage;
        adapter.updateUsage(currentEditingUsage);
        editedUsages.add(currentEditingUsage);
        onCancelUsageEdit(null);
    }

    public void onCancelUsageEdit(View view)
    {
        currentEditingUsage = null;

        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(editor_input.getWindowToken(), 0);

        AppAnimator.bottomBarOutAnimation(editor);
    }

    private void showMessage(int text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}






