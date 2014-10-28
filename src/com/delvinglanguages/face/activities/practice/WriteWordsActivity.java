package com.delvinglanguages.face.activities.practice;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.Cerebro;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.DReference;
import com.delvinglanguages.core.Palabra;
import com.delvinglanguages.settings.Configuraciones;

public class WriteWordsActivity extends Activity implements OnClickListener,
		TextWatcher {

	private DReference refActual;
	private Cerebro cerebro;

	private ImageButton help, next, swap;

	private TextView palabra;
	private EditText input;
	private ProgressBar progress;
	private TextView labels[];

	private String answer;

	private boolean iswrong;

	private int intento;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_write);

		RelativeLayout bg = (RelativeLayout) findViewById(R.id.background);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			bg.setBackgroundDrawable(Configuraciones.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			bg.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		help = (ImageButton) findViewById(R.id.aww_help);
		swap = (ImageButton) findViewById(R.id.aww_swap);
		next = (ImageButton) findViewById(R.id.aww_new);
		palabra = (TextView) findViewById(R.id.aww_word);
		input = (EditText) findViewById(R.id.aww_input);
		progress = (ProgressBar) findViewById(R.id.aww_progress);
		progress.getProgressDrawable().setColorFilter(0xFF33CC00,
				PorterDuff.Mode.MULTIPLY);

		help.setOnClickListener(this);
		swap.setOnClickListener(this);
		next.setOnClickListener(this);
		input.addTextChangedListener(this);

		cerebro = new Cerebro(ControlCore.getReferences());

		labels = new TextView[Configuraciones.NUM_TYPES];
		labels[0] = (TextView) findViewById(R.id.aww_noun);
		labels[1] = (TextView) findViewById(R.id.aww_verb);
		labels[2] = (TextView) findViewById(R.id.aww_adj);
		labels[3] = (TextView) findViewById(R.id.aww_adv);
		labels[4] = (TextView) findViewById(R.id.aww_phrasal);
		labels[5] = (TextView) findViewById(R.id.aww_expression);
		labels[6] = (TextView) findViewById(R.id.aww_other);

		String temp = getString(R.string.title_practising);
		setTitle(temp + " " + ControlCore.getIdiomaActual(this).getName());

		siguientePalabra();
	}

	@Override
	protected void onPause() {
		super.onPause();
		ControlCore.saveStatistics();
	}

	private void siguientePalabra() {
		intento = 1;
		refActual = cerebro.nextReference();
		int type = refActual.type;
		for (int i = 0; i < Configuraciones.NUM_TYPES; ++i) {
			if ((type & (1 << i)) != 0) {
				labels[i].setBackgroundColor(Configuraciones.type_colors[i]);
			} else {
				labels[i].setBackgroundColor(0xFFCCCCCC);
			}
		}
		progress.setMax(refActual.item.length());
		progress.setProgress(0);
		iswrong = false;
		answer = "";
		input.setText(answer);
		palabra.setText(refActual.getTranslation().toUpperCase());
		help.setEnabled(true);
		swap.setEnabled(true);
		next.setEnabled(true);
		help.setClickable(true);
		swap.setClickable(true);
		next.setClickable(true);
	}

	/** **************** ONCLICKLISTENER ******************* **/
	@Override
	public void onClick(View v) {
		if (v == help) {
			int len = answer.length();
			if (iswrong) {
				if (len == 0) {
					answer = "";
				} else {
					answer = answer.substring(0, len - 1);
				}
			} else {
				if (len != refActual.item.length()) {
					answer += refActual.item.charAt(len);
				}
			}
			input.setText(answer);
			input.setSelection(answer.length());
		} else if (v == swap) {
			// FALTA
		} else if (v == next) {
			siguientePalabra();
		}
	}

	/** **************** TEXTWATCHER ******************* **/
	private Handler mHandler = new Handler();

	@Override
	public void afterTextChanged(Editable s) {
		answer = input.getText().toString();
		if (refActual.item.toLowerCase()
				.startsWith(answer.toLowerCase())) {

			progress.setProgress(answer.length());
			if (iswrong) {
				progress.getProgressDrawable().setColorFilter(0xFF33CC00,
						PorterDuff.Mode.MULTIPLY);
				iswrong = false;
			}

			if (refActual.item.equalsIgnoreCase(answer)) {
				help.setEnabled(false);
				swap.setEnabled(false);
				next.setEnabled(false);
				help.setClickable(false);
				swap.setClickable(false);
				next.setClickable(false);

				ControlCore.ejercicio(refActual, intento);

				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								siguientePalabra();
							}
						});
					}
				}).start();
			}
		} else {
			intento++;
			progress.setProgress(refActual.item.length());
			if (!iswrong) {
				progress.getProgressDrawable().setColorFilter(0xFFFF0000,
						PorterDuff.Mode.SRC_IN);
				iswrong = true;
			}
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

}
