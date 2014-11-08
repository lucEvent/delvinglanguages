package com.delvinglanguages.face.activity.practice;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.delvinglanguages.R;

public class ListeningActivity extends Activity implements OnInitListener,
		OnClickListener {

	private int DATA_CHECK_CODE = 1234;

	private TextToSpeech speech;

	private Button buton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_listening);

		buton = (Button) findViewById(R.id.alis_buton);
		buton.setOnClickListener(this);

		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, DATA_CHECK_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// success, create the TTS instance
				speech = new TextToSpeech(this, this);

				int res = speech.setLanguage(Locale.CANADA);
				Locale list[] = Locale.getAvailableLocales();
				for (int i = 0; i < list.length; i++) {
					int r = speech.isLanguageAvailable(list[i]);
					if (r == TextToSpeech.LANG_AVAILABLE
							|| r == TextToSpeech.LANG_COUNTRY_AVAILABLE
							|| r == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE) {

						Log.d("##ListeningAct##", i + ". ##################");
						Log.d("##ListeningAct##",
								"Name: " + list[i].getDisplayName());
						Log.d("##ListeningAct##",
								"Country: " + list[i].getCountry());
						Log.d("##ListeningAct##",
								"Language: " + list[i].getLanguage());
						Log.d("##ListeningAct##", "####################");
					}

				}

				switch (res) {

				case TextToSpeech.LANG_AVAILABLE:
					Log.d("##ListeningAct##", "Lang avaliable");

					break;
				case TextToSpeech.LANG_COUNTRY_AVAILABLE:
					Log.d("##ListeningAct##", "lang country avaliable");

					break;
				case TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE:

					Log.d("##ListeningAct##", "lang country var avaliable");
					break;
				case TextToSpeech.LANG_MISSING_DATA:
					Log.d("##ListeningAct##", "lang missing data");

					break;
				case TextToSpeech.LANG_NOT_SUPPORTED:
					Log.d("##ListeningAct##", "lang not supported");

				}

				String l = speech.getDefaultEngine();

				Log.d("##ListeningAct##", "####################");
				Log.d("##ListeningAct##", "Name: " + l);

				Log.d("##ListeningAct##", "####################");

			} else {
				// missing data, install it
				startActivity(new Intent()
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA));
			}
		}

	}

	@Override
	public void onInit(int status) {
		Log.d("##ListeningAct##", "OnInit");
	}

	@Override
	public void onClick(View v) {

		String myText1 = "Did you sleep well?";
		String myText2 = "I hope so, because it's time to wake up.";

		speech.speak(myText1, TextToSpeech.QUEUE_FLUSH, null);
		speech.speak(myText2, TextToSpeech.QUEUE_ADD, null);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		speech.stop();
		speech.shutdown();
	}

}
