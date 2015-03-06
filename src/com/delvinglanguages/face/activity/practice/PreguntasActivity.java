package com.delvinglanguages.face.activity.practice;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.core.game.MatchGame;
import com.delvinglanguages.core.game.MatchGame.QuestionModel;
import com.delvinglanguages.settings.Configuraciones;

public class PreguntasActivity extends Activity implements OnClickListener {

	private static final String DEBUG = "##PreguntaActivity##";
	
	protected final int NUM_RESP = 6;

	// Elementos del core
	protected IDDelved idioma;
	protected MatchGame gamecontroller;

	// Elementos de la activity
	protected QuestionModel pActual;
	protected int intento;

	// Elementos graficos
	protected TextView palabra, pregunta;
	protected Button resps[];

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_match);

		// Iniciamos elementos del core
		idioma = ControlCore.getIdiomaActual(this);
		gamecontroller = new MatchGame(ControlCore.getReferences());

		View background = findViewById(R.id.background);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		palabra = (TextView) findViewById(R.id.palabra);
		pregunta = (TextView) findViewById(R.id.quesignifica);

		resps = new Button[NUM_RESP];
		resps[0] = (Button) findViewById(R.id.resp1);
		resps[1] = (Button) findViewById(R.id.resp2);
		resps[2] = (Button) findViewById(R.id.resp3);
		resps[3] = (Button) findViewById(R.id.resp4);
		resps[4] = (Button) findViewById(R.id.resp5);
		resps[5] = (Button) findViewById(R.id.resp6);
		for (int i = 0; i < NUM_RESP; i++) {
			resps[i].setOnClickListener(this);
		}

		setTitle(getString(R.string.title_practising) + " " + idioma.getName());
		nuevaPregunta();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(DEBUG, "Guardando estadisticas en disco");
		ControlCore.saveStatistics();
	}

	/** *************** METODOS ONCLICK **************************** **/
	@Override
	public void onClick(View v) {
		Boolean acierto = (Boolean) v.getTag();
		if (acierto == true) {
			acierto(v);
		} else {
			fallo(v);
		}
	}

	/** ****************** METODOS PRIVADOS ************************* **/
	private Handler mHandler = new Handler();

	private void acierto(View v) {

		v.getBackground().setColorFilter(0xFF33CC00, PorterDuff.Mode.MULTIPLY);
		for (int i = 0; i < NUM_RESP; i++) {
			resps[i].setClickable(false);
		}

		new Thread(new Runnable() {

			public void run() {
				ControlCore.ejercicio(pActual.reference, intento);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Log.d(DEBUG, "ERROR EN EL SLEEP");
				}
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						nuevaPregunta();
					}
				});
			}
		}).start();

	}

	private void fallo(View v) {
		intento++;
		v.setClickable(false);
		v.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
		if (intento == 4) {
			for (int i = 0; i < NUM_RESP; i++) {
				if (pActual.correct[i]) {
					acierto(resps[i]);
				}
			}
		}
	}

	private void nuevaPregunta() {
		intento = 1;
		pActual = gamecontroller.nextQuestion(NUM_RESP);
		palabra.setText(pActual.reference.name);
		pregunta.setText(pActual.reference.getPronunciation());

		for (int i = 0; i < NUM_RESP; i++) {
			resps[i].getBackground().setColorFilter(0xFFFFFFFF,
					PorterDuff.Mode.MULTIPLY);
			resps[i].setText(pActual.answers[i]);
			resps[i].setClickable(true);
			resps[i].setTag(pActual.correct[i]);
		}

	}

}