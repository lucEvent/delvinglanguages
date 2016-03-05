package com.delvinglanguages.face.fragment;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.delvinglanguages.R;
import com.delvinglanguages.face.AppCode;
import com.delvinglanguages.face.activity.add.AddWordFromSearchActivity;
import com.delvinglanguages.kernel.Inflexion;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.listers.SearchLister;
import com.delvinglanguages.net.external.WordReference;
import com.delvinglanguages.settings.Settings;

import java.util.ArrayList;
import java.util.TreeSet;

public class SearchFragment extends ListFragment implements OnClickListener, TextWatcher {

    private static EditText input;
    private static Button search, add;

    private WordReference dictionary;

    private static SearchLister adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.a_search, container, false);

        Settings.setBackgroundTo(view);

        input = (EditText) view.findViewById(R.id.input);
        search = (Button) view.findViewById(R.id.search);
        add = (Button) view.findViewById(R.id.add);

        input.addTextChangedListener(this);
        search.setOnClickListener(this);
        add.setOnClickListener(this);
        add.setEnabled(false);

        adapter = new SearchLister(getActivity());
        setListAdapter(adapter);

        int from_code = LanguageKernelControl.getCurrentLanguage().CODE;
        int to_code = Settings.NativeLanguageCode;
        dictionary = new WordReference(from_code, to_code, handler);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
        }
        search.requestFocus();
        return view;
    }

    private String searchedWord;

    @Override
    public void onClick(View v) {
        if (v == search) {
            searchedWord = input.getText().toString();
            dictionary.searchTerm(searchedWord);

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } else if (v == add) {
            ArrayList<String>[] translations = new ArrayList[Word.NUMBER_OF_TYPES];
            for (int i = 0; i < adapter.getCount(); ++i) {
                SearchLister.SearchItem item = adapter.getItem(i);
                if (item.selected) {
                    if (translations[item.type] == null)
                        translations[item.type] = new ArrayList<String>();
                    translations[item.type].add(item.name);
                }
            }

            ArrayList<Inflexion> inflexions = new ArrayList<Inflexion>();
            for (int type = 0; type < translations.length; type++) {
                ArrayList<String> inflexion = translations[type];
                if (inflexion != null) {
                    inflexions.add(new Inflexion(new String[]{}, inflexion.toArray(new String[inflexion.size()]), type));
                }
            }
            searchedWord = Character.isUpperCase(searchedWord.charAt(0)) + searchedWord.substring(1);

            Intent intent = new Intent(getActivity(), AddWordFromSearchActivity.class);
            intent.putExtra(AppCode.NAME, searchedWord);
            intent.putExtra(AppCode.TRANSLATION, inflexions);
            startActivity(intent);

        }
    }

    private static Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            TreeSet<String>[] data = (TreeSet<String>[]) msg.obj;
            for (int i = 0; i < data.length; i++) {
                int type = i + 1;
                for (String term : data[i]) {
                    adapter.addItem(type, term);
                }
            }
            add.setEnabled(true);

        }
    };

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        add.setEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    private void debug(String message) {
        if (Settings.DEBUG)
            System.out.println("##SearchFragment## " + message);
    }

}
