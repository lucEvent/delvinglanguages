package com.delvinglanguages.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.AppData;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DelvingList;
import com.delvinglanguages.kernel.Inflexion;
import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.kernel.LanguageCode;
import com.delvinglanguages.kernel.util.Inflexions;
import com.delvinglanguages.net.WordReference;
import com.delvinglanguages.net.YandexTranslator;
import com.delvinglanguages.net.utils.OnlineDictionary;
import com.delvinglanguages.net.utils.Search;
import com.delvinglanguages.view.lister.WebSearchLister;

import java.util.ArrayList;
import java.util.TreeSet;

public class WebSearchActivity extends AppCompatActivity implements TextWatcher {

    private String lto, lfrom;
    private int to, from;
    private OnlineDictionary dictionary;

    private EditText input;

    private WebSearchLister adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_web_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("");

        input = (EditText) findViewById(R.id.input);

        adapter = new WebSearchLister(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        DelvingList current = new KernelManager(this).getCurrentList();
        from = current.from_code;
        to = current.to_code;

        dictionary = new WordReference(from, to, handler);
        if (!dictionary.isTranslationAvailable(from, to))
            dictionary = new YandexTranslator(from, to, handler);

        lfrom = AppData.getLanguageName(from);
        lto = AppData.getLanguageName(to);

        TextView copyright = (TextView) findViewById(R.id.copyright);
        if (dictionary.isTranslationAvailable(from, to)) {
            input.addTextChangedListener(this);

            copyright.setText(dictionary instanceof WordReference ? "© WordReference.com" : "Powered by Yandex.Translate");

        } else {
            copyright.setVisibility(View.GONE);

            TextView messager = (TextView) findViewById(R.id.messager);
            messager.setVisibility(View.VISIBLE);
            messager.setText(getString(R.string.msg_cannot_translate_from_to, lfrom, lto));
        }

        updateLanguages();

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);

        input.requestFocus();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String term = extras.getString(AppCode.SEARCH_TERM);

            input.setText(term);
            input.setSelection(term.length());
            onTextChanged(term, 0, 0, term.length());
        }
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

    @SuppressWarnings("ConstantConditions")
    private void updateLanguages()
    {
        ((TextView) findViewById(R.id.from_language)).setText(lfrom);
        ((TextView) findViewById(R.id.to_language)).setText(lto);

        findViewById(R.id.from_flag).setBackgroundResource(LanguageCode.getFlagResId(from));
        findViewById(R.id.to_flag).setBackgroundResource(LanguageCode.getFlagResId(to));
    }

    private int currentSearchCode = -1;
    private Search currentSearch;
    private boolean isSearchInverse = false;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg)
        {
            Search data = (Search) msg.obj;
            if (data.code > currentSearchCode) {
                currentSearchCode = data.code;
                currentSearch = data;

                ArrayList<WebSearchLister.SearchItem> items = new ArrayList<>();

                for (int i = 0; i < data.translations.length; i++) {
                    TreeSet<String> t = data.translations[i];
                    if (!t.isEmpty())
                        items.add(new WebSearchLister.SearchItem(1 << i, t.toArray(new String[t.size()])));
                }
                adapter.setNewDataSet(items);

                findViewById(R.id.messager).setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
                if (items.isEmpty())
                    currentSearch = null;
            }
        }
    };

    public void onAddAction(View v)
    {
        if (currentSearch == null) {
            Toast.makeText(this, R.string.msg_nothing_to_add, Toast.LENGTH_SHORT).show();
            return;
        }
  /*      if (!addEnabled) {
            Toast.makeText(this, R.string.msg_impossible_to_add_search, Toast.LENGTH_SHORT).show();
            return;
        }*/

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

        String searchedWord = Character.toUpperCase(currentSearch.searchTerm.charAt(0)) + currentSearch.searchTerm.substring(1);

        Intent intent = new Intent(getApplicationContext(), ReferenceEditorActivity.class);
        intent.putExtra(AppCode.ACTION, isSearchInverse ? ReferenceEditorActivity.ACTION_SEARCH_INVERSE : ReferenceEditorActivity.ACTION_SEARCH);
        intent.putExtra(AppCode.DREFERENCE_NAME, searchedWord);
        intent.putExtra(AppCode.INFLEXIONS_WRAPPER, inflexions.wrap());
        startActivity(intent);
    }

    public void onSwapAction(View v)
    {
        int aux = from;
        from = to;
        to = aux;
        dictionary.updateLanguages(from, to);

        String laux = lfrom;
        lfrom = lto;
        lto = laux;

        updateLanguages();

        isSearchInverse = !isSearchInverse;
        onTextChanged("", 0, 0, 0);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        String searchedWord = input.getText().toString();
        if (!searchedWord.isEmpty())
            dictionary.search(new Search(searchedWord));
        else {
            currentSearch = null;
            adapter.clearDataSet();
        }
    }

    @Override
    public void afterTextChanged(Editable s)
    {
    }

    public void onCopyright(View view)
    {
        String url = dictionary instanceof WordReference ? "http://www.wordreference.com/" : "http://translate.yandex.com/";

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

}