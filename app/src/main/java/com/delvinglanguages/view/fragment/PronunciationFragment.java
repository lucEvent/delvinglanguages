package com.delvinglanguages.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.LanguageManager;
import com.delvinglanguages.view.dialog.NotifierDialog;

import java.util.Locale;

public class PronunciationFragment extends android.app.Fragment implements View.OnClickListener, TextToSpeech.OnInitListener {

    private EditText input;

    private TextToSpeech speechEngine;

    private Locale locale;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        speechEngine = new TextToSpeech(getActivity(), this);
        locale = new LanguageManager(getActivity()).getCurrentLanguage().getLocale();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.f_pronunciation, container, false);

        input = (EditText) view.findViewById(R.id.input);
        ImageButton ib = (ImageButton) view.findViewById(R.id.speech);

        ib.setOnClickListener(this);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);

        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (speechEngine != null) {
            speechEngine.stop();
            speechEngine.shutdown();
        }
    }

    @Override
    public void onInit(int status)
    {
        if (status == TextToSpeech.SUCCESS) {

            int availability = speechEngine.isLanguageAvailable(locale);
            if (availability == TextToSpeech.LANG_MISSING_DATA || availability == TextToSpeech.LANG_NOT_SUPPORTED)

                new NotifierDialog(getActivity()).notify_languageTTSnotAvailable(locale.getDisplayLanguage());

            else
                speechEngine.setLanguage(locale);
        }
    }

    @Override
    public void onClick(View button)
    {
        String tts = input.getText().toString();

        int availability = speechEngine.isLanguageAvailable(locale);
        if (availability == TextToSpeech.LANG_MISSING_DATA || availability == TextToSpeech.LANG_NOT_SUPPORTED)

            new NotifierDialog(getActivity()).notify_languageTTSnotAvailable(locale.getDisplayLanguage());

        else
            speechEngine.speak(tts, TextToSpeech.QUEUE_FLUSH, null, "");
    }

}
