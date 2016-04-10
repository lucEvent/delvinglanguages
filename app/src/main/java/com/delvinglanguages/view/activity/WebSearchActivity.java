package com.delvinglanguages.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.delvinglanguages.R;
import com.delvinglanguages.Settings;
import com.delvinglanguages.kernel.Inflexion;
import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.kernel.util.Inflexions;
import com.delvinglanguages.net.external.WordReference;
import com.delvinglanguages.view.utils.AppCode;
import com.delvinglanguages.view.lister.WebSearchLister;

import java.util.ArrayList;
import java.util.TreeSet;

public class WebSearchActivity extends AppCompatActivity implements TextWatcher {

    private EditText input;

    private WebSearchLister adapter;

    private WordReference dictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_web_search);

        input = (EditText) findViewById(R.id.input);
        input.addTextChangedListener(this);

        adapter = new WebSearchLister(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        int from_code = new KernelManager(this).getCurrentLanguage().CODE;
        int to_code = Settings.getAppLanguageCode();
        dictionary = new WordReference(from_code, to_code, handler);

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
        }
        input.requestFocus();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            ArrayList<WebSearchLister.SearchItem> items = new ArrayList<>();

            TreeSet<String>[] data = (TreeSet<String>[]) msg.obj;
            for (int i = 0; i < data.length; i++)
                if (!data[i].isEmpty())
                    items.add(new WebSearchLister.SearchItem(1 << i, data[i].toArray(new String[data[i].size()])));

            adapter.setNewDataSet(items);
        }
    };

    private String searchedWord;

    public void onAddAction(View v) {
        Inflexions inflexions = new Inflexions();
        ArrayList<String> translations = new ArrayList<>();
        for (int i = 0; i < adapter.getItemCount(); i++) {

            WebSearchLister.SearchItem item = adapter.getItem(i);

            for (int j = 0; j < item.translations.length; j++)
                if (item.selectedTranslations[j])
                    translations.add(item.translations[j]);

            if (!translations.isEmpty()) {
                inflexions.add(new Inflexion(new String[]{}, translations.toArray(new String[translations.size()]), item.type));
                translations.clear();
            }
        }

        searchedWord = Character.toUpperCase(searchedWord.charAt(0)) + searchedWord.substring(1);

        Intent intent = new Intent(getApplicationContext(), ReferenceEditorActivity.class);
        intent.putExtra(AppCode.ACTION, ReferenceEditorActivity.ACTION_SEARCH);
        intent.putExtra(AppCode.DREFERENCE_NAME, searchedWord);
        intent.putExtra(AppCode.DREFERENCE_INFLEXIONS, inflexions.toString());
        startActivity(intent);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        searchedWord = input.getText().toString();
        dictionary.searchTerm(searchedWord);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

}