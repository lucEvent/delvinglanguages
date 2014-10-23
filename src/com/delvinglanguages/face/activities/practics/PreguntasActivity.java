package com.delvinglanguages.face.activities.practics;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.Cerebro;
import com.delvinglanguages.core.Cerebro.QuestionModel;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.settings.Configuraciones;

public class PreguntasActivity extends Activity implements OnClickListener {

	private static final String DEBUG = "##PreguntaAct##";
	
	protected final int NUM_RESP = 6;

	// Elementos del core
	protected IDDelved idioma;
	protected Cerebro calculador;

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

		// Iniciamos elementos del core
		idioma = ControlCore.getIdiomaActual(this);
		calculador = new Cerebro(ControlCore.getReferences());

		// Iniciamos elementos de la activity
		setContentView(R.layout.a_match);

		// Configuramos background
		LinearLayout bg = (LinearLayout) findViewById(R.id.apreg_bg);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			bg.setBackgroundDrawable(Configuraciones.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			bg.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		// Iniciamos elementos graficos
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
		pActual = calculador.nextQuestion(NUM_RESP);
		palabra.setText(pActual.reference.item);
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

class MatchActivity extends Activity implements OnTouchListener {

	private static final String DEBUG = "##MatchActivity##";

	private ImageView image;

	private Bitmap bitmap;

	private int width, height;

	private static Canvas canvas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		width = getIntent().getExtras().getInt("width");
		height = getIntent().getExtras().getInt("height");
		if (width > height) {
			int tmp = width;
			width = height;
			height = tmp;
		}

		bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		canvas = new Canvas(bitmap);

		image = new ImageView(this);
		image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		image.setImageBitmap(bitmap);

		makePaints();
		draw(width >> 1, height >> 1, 0);

		setContentView(image);
	}

	private Paint buttons, background, text;

	private void makePaints() {
		// Button style

		// Text style
		text = new Paint();
		text.setTextSize(100);
		text.setColor(Color.BLACK);
		text.setTextAlign(Align.CENTER);
		text.setStyle(Paint.Style.FILL);

		// Background style
		background = new Paint();
		background.setStyle(Paint.Style.FILL);
		background.setARGB(255, 1, 101, 2);

	}

	private void draw(float x, float y, double gforce) {
		// Background
		canvas.drawRect(0, 0, width, height, background);

		// Buttons
		// Text

		image.invalidate();
	}

	/** ******************* * METODOS TOUCH LISTENER * *********************** **/

	private boolean dragging;

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			break;
		case MotionEvent.ACTION_MOVE:

			break;
		case MotionEvent.ACTION_UP:
			dragging = false;
			break;
		}
		return false;
	}

}