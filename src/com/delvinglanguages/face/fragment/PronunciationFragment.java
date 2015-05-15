package com.delvinglanguages.face.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.settings.Settings;

public class PronunciationFragment extends Fragment implements OnClickListener, OnInitListener {

	private static final String DEBUG = "##PronunciationFragment##";

	private EditText input;
	private ImageButton speech;
	private TextToSpeech speechEngine;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		speechEngine = new TextToSpeech(activity, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.a_pronunciation, container, false);
		Settings.setBackgroundTo(view);

		input = (EditText) view.findViewById(R.id.input);
		speech = (ImageButton) view.findViewById(R.id.speech);
		speech.setOnClickListener(this);
		
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
		}

		return view;
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			int r = speechEngine.setLanguage(KernelControl.getCurrentLanguage().getLocale());

		}
	}

	@Override
	public void onClick(View button) {
		String tts = input.getText().toString();
		if (!tts.isEmpty()) {
			speechEngine.speak(tts, TextToSpeech.QUEUE_FLUSH, null);
		}
	}

}
