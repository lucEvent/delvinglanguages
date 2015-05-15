package com.delvinglanguages.face.activity.practice;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.game.Game;
import com.delvinglanguages.settings.Settings;

public class ListeningActivity extends Activity implements OnInitListener {

	private static final String DEBUG = "##ListeningActivity##";

	private TextView word, pron, tran;

	private TextToSpeech speechEngine;

	private Game game;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.a_listening, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		game = new Game(LanguageKernelControl.getReferences());

		word = (TextView) view.findViewById(R.id.word);
		pron = (TextView) view.findViewById(R.id.pronunciation);
		tran = (TextView) view.findViewById(R.id.translations);

		onNext(null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		speechEngine = new TextToSpeech(this, this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		speechEngine.stop();
		speechEngine.shutdown();
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			int r = speechEngine.setLanguage(LanguageKernelControl.getCurrentLanguage().getLocale());
		}
	}

	public void onNext(View v) {
		DReference ref = game.nextReference();

		word.setText(ref.getName());
		tran.setText(ref.getTranslation());
		pron.setText(ref.getPronunciation());

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				onSpeech(null);
			}
		}).start();
	}

	public void onSpeech(View v) {
		speechEngine.speak(word.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
	}

	/*
	 * @Override protected void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState);
	 * 
	 * Intent checkIntent = new Intent();
	 * checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
	 * startActivityForResult(checkIntent, DATA_CHECK_CODE);
	 * 
	 * 
	 * }
	 * 
	 * @Override protected void onActivityResult(int requestCode, int
	 * resultCode, Intent data) { super.onActivityResult(requestCode,
	 * resultCode, data);
	 * 
	 * if (requestCode == 0) { if (resultCode ==
	 * TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) { // success, create the TTS
	 * instance // speech = new TextToSpeech(this, this);
	 * 
	 * int res = speech.setLanguage(Locale.CANADA); Locale list[] =
	 * Locale.getAvailableLocales(); for (int i = 0; i < list.length; i++) { int
	 * r = speech.isLanguageAvailable(list[i]); if (r ==
	 * TextToSpeech.LANG_AVAILABLE || r == TextToSpeech.LANG_COUNTRY_AVAILABLE
	 * || r == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE) {
	 * 
	 * Log.d("##ListeningAct##", i + ". ##################");
	 * Log.d("##ListeningAct##", "Name: " + list[i].getDisplayName());
	 * Log.d("##ListeningAct##", "Country: " + list[i].getCountry());
	 * Log.d("##ListeningAct##", "Language: " + list[i].getLanguage());
	 * Log.d("##ListeningAct##", "####################"); }
	 * 
	 * }
	 * 
	 * switch (res) {
	 * 
	 * case TextToSpeech.LANG_AVAILABLE: Log.d("##ListeningAct##",
	 * "Lang avaliable");
	 * 
	 * break; case TextToSpeech.LANG_COUNTRY_AVAILABLE:
	 * Log.d("##ListeningAct##", "lang country avaliable");
	 * 
	 * break; case TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE:
	 * 
	 * Log.d("##ListeningAct##", "lang country var avaliable"); break; case
	 * TextToSpeech.LANG_MISSING_DATA: Log.d("##ListeningAct##",
	 * "lang missing data");
	 * 
	 * break; case TextToSpeech.LANG_NOT_SUPPORTED: Log.d("##ListeningAct##",
	 * "lang not supported");
	 * 
	 * }
	 * 
	 * String l = speech.getDefaultEngine();
	 * 
	 * Log.d("##ListeningAct##", "####################");
	 * Log.d("##ListeningAct##", "Name: " + l);
	 * 
	 * Log.d("##ListeningAct##", "####################");
	 * 
	 * } else { // missing data, install it startActivity(new
	 * Intent().setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA)); } }
	 * 
	 * }
	 */

}
