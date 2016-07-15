package com.delvinglanguages.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.delvinglanguages.Main;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.view.lister.AvailableLanguageLister;
import com.delvinglanguages.view.utils.LanguageListener;
import com.delvinglanguages.view.utils.MessageListener;

public class CreateLanguageFragment extends android.app.Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private String[] languages;

    private Switch s_phrasals;
    private Spinner spinner;
    private EditText input;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.f_create_language, container, false);

        languages = getResources().getStringArray(R.array.languages);

        initViews(view);

        return view;
    }

    private void initViews(View parent)
    {
        spinner = (Spinner) parent.findViewById(R.id.spinner);
        spinner.setAdapter(new AvailableLanguageLister(getActivity(), languages));
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);

        parent.findViewById(R.id.button_create).setOnClickListener(this);
        parent.findViewById(R.id.button_cancel).setOnClickListener(this);
        parent.findViewById(R.id.container_phrasal_verbs).setOnClickListener(this);

        s_phrasals = (Switch) parent.findViewById(R.id.switch_phrasal_verbs);

        input = (EditText) parent.findViewById(R.id.input);
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
                    Main.handler.obtainMessage(MessageListener.MESSAGE_INT, R.string.msg_missing_language_name).sendToTarget();
                    return;
                }
                int code = spinner.getSelectedItemPosition();

                boolean ph = s_phrasals.isChecked();
                int settings = Language.configure(ph);

                Main.handler.obtainMessage(LanguageListener.LANGUAGE_CREATED_OK, new Object[]{code, name, settings}).sendToTarget();
                break;
            case R.id.button_cancel:
                Main.handler.obtainMessage(LanguageListener.LANGUAGE_CREATED_CANCELED, null).sendToTarget();
                break;
            case R.id.container_phrasal_verbs:
                s_phrasals.toggle();
                break;
        }
    }

}