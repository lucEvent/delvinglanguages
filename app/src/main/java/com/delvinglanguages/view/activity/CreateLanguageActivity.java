package com.delvinglanguages.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.view.lister.SpinnerLanguageLister;

public class CreateLanguageActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private String[] languages;

    private Switch s_phrasals;
    private Spinner spinner;
    private EditText input;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_create_language);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.create_language);

        languages = getResources().getStringArray(R.array.languages);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new SpinnerLanguageLister(this, languages));
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);

        findViewById(R.id.button_create).setOnClickListener(this);
        findViewById(R.id.button_cancel).setOnClickListener(this);
        findViewById(R.id.container_phrasal_verbs).setOnClickListener(this);

        s_phrasals = (Switch) findViewById(R.id.switch_phrasal_verbs);

        input = (EditText) findViewById(R.id.input);
    }

    @Override
    public void onBackPressed()
    {
        setResult(AppCode.RESULT_LANGUAGE_CREATED_CANCELED);
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
        String in = input.getText().toString();
        boolean update = in.length() == 0;
        if (!update)
            for (String s : languages)
                if (s.equals(in)) {
                    update = true;
                    break;
                }
        if (update) {
            input.setText(languages[pos]);
            input.setSelection(languages[pos].length());
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
                int code = spinner.getSelectedItemPosition();

                boolean ph = s_phrasals.isChecked();
                int settings = Language.configure(ph);

                new KernelManager(this)
                        .createLanguage(code, name, settings);

                setResult(AppCode.RESULT_LANGUAGE_CREATED);
                finish();

                break;
            case R.id.button_cancel:
                setResult(AppCode.RESULT_LANGUAGE_CREATED_CANCELED);
                finish();

                break;
            case R.id.container_phrasal_verbs:
                s_phrasals.toggle();
                break;
        }
    }

}