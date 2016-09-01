package com.delvinglanguages.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.AppSettings;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.Inflexion;
import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.kernel.util.Inflexions;
import com.delvinglanguages.net.MicrosoftTranslator;
import com.delvinglanguages.net.WordReference;
import com.delvinglanguages.net.utils.OnlineDictionary;
import com.delvinglanguages.net.utils.Search;
import com.delvinglanguages.view.lister.WebSearchLister;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.TreeSet;

public class WebSearchActivity extends AppCompatActivity implements TextWatcher {

    private String lto, lfrom;
    private int to, from;
    private Drawable dto, dfrom;
    private OnlineDictionary dictionary;

    private EditText input;

    private WebSearchLister adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_web_search);

        input = (EditText) findViewById(R.id.input);

        adapter = new WebSearchLister(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        from = new KernelManager(this).getCurrentLanguage().code;
        to = AppSettings.getAppLanguageCode();
        dictionary = new WordReference(from, to, handler);

        String[] slanguages = getResources().getStringArray(R.array.languages);
        lfrom = slanguages[from];
        lto = slanguages[to];

        if (!dictionary.isTranslationAvailable(from, to))
            dictionary = new MicrosoftTranslator(from, to, handler);

        if (dictionary.isTranslationAvailable(from, to))
            input.addTextChangedListener(this);
        else {
            TextView msgr = (TextView) findViewById(R.id.messager);
            msgr.setVisibility(View.VISIBLE);
            msgr.setText(getString(R.string.msg_cannot_translate_from_to, lfrom, lto));
        }

        AssetManager amanager = getAssets();
        try {
            InputStream reader = amanager.open(from + ".png");
            dfrom = Drawable.createFromStream(reader, null);
            dfrom.setBounds(0, 0, 10, 10);
            reader.close();

            reader = amanager.open(to + ".png");
            dto = Drawable.createFromStream(reader, null);
            dto.setBounds(0, 0, 10, 10);
            reader.close();
        } catch (IOException ignored) {
        }
        updateLanguages();

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
        }
        input.requestFocus();
    }

    @SuppressWarnings("ConstantConditions")
    private void updateLanguages()
    {
        ((TextView) findViewById(R.id.from_language)).setText(lfrom);
        ((TextView) findViewById(R.id.to_language)).setText(lto);

        findViewById(R.id.from_flag).setBackground(dfrom);
        findViewById(R.id.to_flag).setBackground(dto);
    }

    private int currentSearchCode = -1;
    private Search currentSearch;
    private boolean addEnabled = true;

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
        if (currentSearch == null || !addEnabled)
            return;

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
        intent.putExtra(AppCode.ACTION, ReferenceEditorActivity.ACTION_SEARCH);
        intent.putExtra(AppCode.DREFERENCE_NAME, searchedWord);
        intent.putExtra(AppCode.DREFERENCE_INFLEXIONS, inflexions.wrap());
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

        Drawable daux = dfrom;
        dfrom = dto;
        dto = daux;
        updateLanguages();

        addEnabled = !addEnabled;
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

}