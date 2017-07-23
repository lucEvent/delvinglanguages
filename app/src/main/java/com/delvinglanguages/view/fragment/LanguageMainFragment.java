package com.delvinglanguages.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.Main;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.LanguageFetchManager;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.view.activity.DReferenceActivity;
import com.delvinglanguages.view.activity.DictionaryActivity;
import com.delvinglanguages.view.activity.LanguageActivity;
import com.delvinglanguages.view.activity.ReferenceEditorActivity;
import com.delvinglanguages.view.activity.ThemeActivity;
import com.delvinglanguages.view.activity.ThemeEditorActivity;
import com.delvinglanguages.view.activity.WebSearchActivity;
import com.delvinglanguages.view.activity.practise.TestActivity;
import com.delvinglanguages.view.dialog.LanguageOptionsDialog;
import com.delvinglanguages.view.lister.MainCardLister;
import com.delvinglanguages.view.lister.viewholder.MainStatsViewHolder;
import com.delvinglanguages.view.lister.viewholder.MainTypesViewHolder;
import com.delvinglanguages.view.utils.DataHandler;
import com.delvinglanguages.view.utils.DataListener;
import com.delvinglanguages.view.utils.LanguageListener;
import com.delvinglanguages.view.utils.MainSearch;

public class LanguageMainFragment extends Fragment
        implements TextWatcher, View.OnClickListener, View.OnFocusChangeListener, DataListener {

    private LanguageFetchManager dataManager;
    private Language currentLanguage;

    private MainCardLister listManager;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity context)
    {
        super.onAttach(context);
        dataManager = new LanguageFetchManager(context, new DataHandler(this));
        currentLanguage = dataManager.getCurrentLanguage();
        dataManager.fetchLanguageContents(currentLanguage);
    }

    private EditText view_search;
    private boolean searchActive = false;
    private ImageButton btn_srch_left, btn_srch_right;
    private View optionPad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.f_language_main, container, false);

        view_search = (EditText) view.findViewById(R.id.search_edittext);
        view_search.addTextChangedListener(this);
        view_search.setOnFocusChangeListener(this);

        btn_srch_left = (ImageButton) view.findViewById(R.id.search_button_left);
        btn_srch_right = (ImageButton) view.findViewById(R.id.search_button_right);
        btn_srch_left.setOnClickListener(this);
        btn_srch_right.setOnClickListener(this);

        optionPad = view.findViewById(R.id.option_pad);

        view.findViewById(R.id.option_practise).setOnClickListener(this);
        view.findViewById(R.id.option_dictionary).setOnClickListener(this);
        view.findViewById(R.id.option_drawer).setOnClickListener(this);
        view.findViewById(R.id.option_more).setOnClickListener(this);

        listManager = new MainCardLister(getResources(), currentLanguage.getSetting(Language.MASK_PHRASAL_VERBS), onListItemClick);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean supportsPredictiveItemAnimations()
            {
                return false;
            }
        };
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listManager);

        displayLanguageData();

        return view;
    }

    public void invalidate()
    {
        if (dataManager != null) {
            currentLanguage = dataManager.getCurrentLanguage();

            listManager.setPhrasals(currentLanguage.getSetting(Language.MASK_PHRASAL_VERBS));

            dataManager.fetchLanguageContents(currentLanguage);
            displayLanguageData();
        }
    }

    public void refreshValues()
    {
        listManager.notifyDataSetChanged();
        listManager.setPhrasals(currentLanguage.getSetting(Language.MASK_PHRASAL_VERBS));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(currentLanguage.language_name);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.search_button_left:
                if (searchActive)
                    view_search.clearFocus();
                break;
            case R.id.search_button_right:
                if (!searchActive)
                    view_search.requestFocus();
                else
                    view_search.setText("");

                break;
            default:
                onOptionSelected(v.getId());
        }
    }

    private View.OnClickListener onMoreOptionsClick = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            onOptionSelected(v.getId());
        }
    };

    private Dialog optionsDialog;

    private void onOptionSelected(int option)
    {
        Intent intent = new Intent(getActivity(), LanguageActivity.class);
        switch (option) {
            case R.id.option_practise:

                if (dataManager.getReferences().isEmpty()) {
                    Toast.makeText(getActivity(), R.string.msg_dictionary_empty, Toast.LENGTH_LONG).show();
                    return;
                }
                intent.putExtra(AppCode.FRAGMENT, LanguageActivity.Option.PRACTISE);

                break;
            case R.id.option_dictionary:

                intent = new Intent(getActivity(), DictionaryActivity.class);
                break;

            case R.id.option_drawer:

                intent.putExtra(AppCode.FRAGMENT, LanguageActivity.Option.DRAWER);
                break;

            case R.id.option_more:

                if (optionsDialog != null && optionsDialog.isShowing())
                    return;

                optionsDialog = new LanguageOptionsDialog(getActivity(), onMoreOptionsClick, currentLanguage.getSetting(Language.MASK_PHRASAL_VERBS)).dialog;
                optionsDialog.show();
                return;

            case R.id.option_themes:

                intent.putExtra(AppCode.FRAGMENT, LanguageActivity.Option.THEMES);
                optionsDialog.dismiss();

                break;
            case R.id.option_verbs:
                // TODO: 21/07/2016
                //intent.putExtra(AppCode.FRAGMENT, LanguageActivity.Option.VERBS);
                Toast.makeText(getActivity(), R.string.msg_in_next_releases, Toast.LENGTH_SHORT).show();
                optionsDialog.dismiss();
                return; // break;

            case R.id.option_phrasal_verbs:
                // TODO: 21/07/2016
                //intent.putExtra(AppCode.FRAGMENT, LanguageActivity.Option.PHRASAL_VERBS);
                Toast.makeText(getActivity(), R.string.msg_in_next_releases, Toast.LENGTH_SHORT).show();
                optionsDialog.dismiss();
                return; // break;

            case R.id.option_web_search:

                intent = new Intent(getActivity(), WebSearchActivity.class);
                optionsDialog.dismiss();

                break;
            case R.id.option_pronunciation:

                intent.putExtra(AppCode.FRAGMENT, LanguageActivity.Option.PRONUNCIATION);
                optionsDialog.dismiss();

                break;
            case R.id.option_recycle_bin:

                intent.putExtra(AppCode.FRAGMENT, LanguageActivity.Option.RECYCLE_BIN);
                optionsDialog.dismiss();

                break;
            case R.id.option_settings:

                intent.putExtra(AppCode.FRAGMENT, LanguageActivity.Option.SETTINGS);
                optionsDialog.dismiss();

                break;
        }
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case LanguageListener.LANGUAGE_NAME_CHANGED:
                getActivity().setTitle(dataManager.getCurrentLanguage().language_name);
                break;
            case LanguageListener.LANGUAGE_MERGED_AND_REMOVED:
                int id = data.getExtras().getInt(AppCode.LANGUAGE_ID);
                Main.handler.obtainMessage(resultCode, id, -1).sendToTarget();
                return;
        }
        Main.handler.obtainMessage(resultCode).sendToTarget();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence search, int start, int before, int count)
    {
        search = search.toString().toLowerCase();

        if (search.length() == 0) {
            displayLanguageData();
            return;
        }

        listManager.clear();

        boolean foundPerfectMatch = false;

        for (DReference ref : currentLanguage.dictionary.getDictionary())
            if (ref.hasContent(search)) {
                listManager.addItem(ref);

                if (ref.name.toLowerCase().equals(search))
                    foundPerfectMatch = true;
            }

        for (Theme theme : currentLanguage.themes)
            if (theme.hasContent(search))
                listManager.addItem(theme);

        for (Test test : currentLanguage.tests)
            if (test.hasContent(search))
                listManager.addItem(test);

        if (listManager.isEmpty())
            listManager.addItem(new MainSearch((String) search, true));
        else if (!foundPerfectMatch)
            listManager.addItem(new MainSearch((String) search, false));

    }

    private void displayLanguageData()
    {
        listManager.clear();

        MainStatsViewHolder.Data statsData = new MainStatsViewHolder.Data();
        statsData.statistics = currentLanguage.statistics;
        statsData.onResetStatistics = onResetStatistics;
        listManager.addItem(statsData);

        dataManager.fetchLanguageContentNumbers(currentLanguage);
    }

    @Override
    public void afterTextChanged(Editable s)
    {
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if (hasFocus)
            onSearchActionStart();
        else
            onSearchActionEnd();
    }

    private void onSearchActionStart()
    {
        btn_srch_left.setImageResource(R.drawable.ic_arrow_back);
        btn_srch_right.setImageResource(R.drawable.ic_cancel);
        optionPad.setVisibility(View.GONE);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view_search, 0);

        searchActive = true;
    }

    private void onSearchActionEnd()
    {
        btn_srch_left.setImageResource(R.mipmap.ic_launcher);
        btn_srch_right.setImageResource(R.drawable.ic_search);
        optionPad.setVisibility(View.VISIBLE);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view_search.getWindowToken(), 0);

        searchActive = false;
    }

    @Override
    public void onMainDataCounters(Object obj)
    {
        MainTypesViewHolder.Data data = (MainTypesViewHolder.Data) obj;
        data.onAddReference = onAddReference;
        data.onAddTheme = onAddTheme;
        listManager.addItem(data);
    }

    private View.OnClickListener onListItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            Object item = v.getTag();
            Intent intent;

            if (item instanceof DReference) {

                intent = new Intent(getActivity(), DReferenceActivity.class);
                intent.putExtra(AppCode.DREFERENCE_NAME, ((DReference) item).name);

            } else if (item instanceof Theme) {

                intent = new Intent(getActivity(), ThemeActivity.class);
                intent.putExtra(AppCode.THEME_ID, ((Theme) item).id);

            } else if (item instanceof Test) {

                intent = new Intent(getActivity(), TestActivity.class);
                intent.putExtra(AppCode.TEST_ID, ((Test) item).id);

            } else if (item instanceof MainSearch) {

                MainSearch searchItem = (MainSearch) item;

                if (v.getId() == R.id.button_add_to_drawer) {
                    dataManager.createDrawerReference(searchItem.term);
                    Toast.makeText(v.getContext(), v.getResources().getString(R.string.msg_added_to_drawer, searchItem.term), Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(getActivity(), WebSearchActivity.class);
                intent.putExtra(AppCode.SEARCH_TERM, searchItem.term);

            } else
                return;

            startActivity(intent);
        }
    };

    private View.OnClickListener onResetStatistics = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            dataManager.resetStatistics();
            listManager.notifyItemChanged(0);
        }
    };

    private View.OnClickListener onAddTheme = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            startActivity(new Intent(getActivity(), ThemeEditorActivity.class));
        }
    };

    private View.OnClickListener onAddReference = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(getActivity(), ReferenceEditorActivity.class);
            intent.putExtra(AppCode.ACTION, ReferenceEditorActivity.ACTION_CREATE);
            startActivity(intent);
        }
    };

}
