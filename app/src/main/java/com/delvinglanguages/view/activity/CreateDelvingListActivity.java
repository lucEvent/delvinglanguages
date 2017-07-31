package com.delvinglanguages.view.activity;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DelvingList;
import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.view.lister.SpinnerLanguageLister;

public class CreateDelvingListActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener, TextWatcher {

    private String[] languages;
    private int[] codes;
    private boolean customListName = false;

    private Switch s_phrasals;
    private Spinner spinner_from, spinner_to;
    private EditText input;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_create_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle(R.string.create_language);

        Resources r = getResources();
        languages = r.getStringArray(R.array.languages);
        String[] sCodes = r.getStringArray(R.array.language_codes);
        codes = new int[sCodes.length];
        for (int i = 0; i < sCodes.length; i++)
            codes[i] = Integer.parseInt(sCodes[i]);

        spinner_from = (Spinner) findViewById(R.id.spinner_from);
        spinner_from.setAdapter(new SpinnerLanguageLister(this, languages, codes));
        spinner_from.setOnItemSelectedListener(this);
        spinner_from.setSelection(0);

        spinner_to = (Spinner) findViewById(R.id.spinner_to);
        spinner_to.setAdapter(new SpinnerLanguageLister(this, languages, codes));
        spinner_to.setOnItemSelectedListener(this);
        spinner_to.setSelection(0);

        findViewById(R.id.button_create).setOnClickListener(this);
        findViewById(R.id.button_cancel).setOnClickListener(this);
        findViewById(R.id.container_phrasal_verbs).setOnClickListener(this);

        s_phrasals = (Switch) findViewById(R.id.switch_phrasal_verbs);

        input = (EditText) findViewById(R.id.input);
        input.addTextChangedListener(this);
    }

    @Override
    public void onBackPressed()
    {
        setResult(AppCode.RESULT_LIST_CREATED_CANCELED);
        super.onBackPressed();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(input.getWindowToken(), 0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    {
        if (!customListName) {
            String listName = languages[spinner_from.getSelectedItemPosition()] + " - " + languages[spinner_to.getSelectedItemPosition()];

            input.removeTextChangedListener(this);
            input.setText(listName);
            input.setSelection(listName.length());
            input.addTextChangedListener(this);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.button_create:
                String name = input.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(this, R.string.msg_missing_language_name, Toast.LENGTH_LONG).show();
                    return;
                }

                int from_code = codes[spinner_from.getSelectedItemPosition()];
                int to_code = codes[spinner_to.getSelectedItemPosition()];
                if (from_code == to_code) {
                    Toast.makeText(this, R.string.msg_language_cant_be_same, Toast.LENGTH_LONG).show();
                    return;
                }

                boolean ph = s_phrasals.isChecked();
                int settings = DelvingList.parseSettings(ph);

                new KernelManager(this)
                        .createDelvingList(from_code, to_code, name, settings);

                setResult(AppCode.RESULT_LIST_CREATED);
                finish();

                break;
            case R.id.button_cancel:
                setResult(AppCode.RESULT_LIST_CREATED_CANCELED);
                finish();

                break;
            case R.id.container_phrasal_verbs:
                s_phrasals.toggle();
                break;
        }
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        customListName = true;
        input.removeTextChangedListener(this);
    }

    @Override
    public void afterTextChanged(Editable s)
    {
    }

}