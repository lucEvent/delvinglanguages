package com.delvinglanguages.face.activity.practice;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.DReference;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.core.Word;
import com.delvinglanguages.core.game.WriteGame;
import com.delvinglanguages.face.listeners.SpecialKeysBar;
import com.delvinglanguages.settings.Configuraciones;

public class WriteWordsActivity extends Activity implements TextWatcher {

	protected DReference refActual;
	protected WriteGame gamecontroller;

	protected ImageButton help, next, swap;

	protected TextView palabra;
	protected EditText input;
	protected ProgressBar progress;
	protected TextView labels[];

	private boolean iswrong;

	private int intento;
	protected Handler mHandler = new Handler();

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_write);

		View background = findViewById(R.id.background);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		new SpecialKeysBar(this, null);

		help = (ImageButton) findViewById(R.id.help);
		swap = (ImageButton) findViewById(R.id.swap);
		next = (ImageButton) findViewById(R.id.next);
		palabra = (TextView) findViewById(R.id.word);
		input = (EditText) findViewById(R.id.input);
		progress = (ProgressBar) findViewById(R.id.progress);
		progress.getProgressDrawable().setColorFilter(0xFF33CC00,
				PorterDuff.Mode.MULTIPLY);

		input.addTextChangedListener(this);

		if (this.getClass() == WriteWordsActivity.class) {
			gamecontroller = new WriteGame(ControlCore.getReferences());
			String temp = getString(R.string.title_practising);
			setTitle(temp + " " + ControlCore.getIdiomaActual(this).getName());
		}

		labels = new TextView[Configuraciones.NUM_TYPES];
		labels[Word.NOUN] = (TextView) findViewById(R.id.noun);
		labels[Word.VERB] = (TextView) findViewById(R.id.verb);
		labels[Word.ADJECTIVE] = (TextView) findViewById(R.id.adjective);
		labels[Word.ADVERB] = (TextView) findViewById(R.id.adverb);
		labels[Word.PHRASAL] = (TextView) findViewById(R.id.phrasal);
		labels[Word.EXPRESSION] = (TextView) findViewById(R.id.expression);
		labels[Word.OTHER] = (TextView) findViewById(R.id.other);

		IDDelved idioma = ControlCore.getIdiomaActual(this);
		if (!idioma.getSettings(IDDelved.MASK_PH)) {
			labels[Word.PHRASAL].setVisibility(View.GONE);
		}

		siguientePalabra();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (this.getClass() == WriteWordsActivity.class) {
			ControlCore.saveStatistics();
		}
	}

	protected void siguientePalabra() {
		intento = 1;
		refActual = gamecontroller.nextReference();
		int type = refActual.type;
		for (int i = 0; i < Configuraciones.NUM_TYPES; ++i) {
			if ((type & (1 << i)) != 0) {
				labels[i].setBackgroundColor(Configuraciones.type_colors[i]);
			} else {
				labels[i].setBackgroundColor(0xFFCCCCCC);
			}
		}
		progress.setMax(refActual.name.length());
		progress.setProgress(0);
		iswrong = false;
		input.setText("");
		palabra.setText(refActual.getTranslation().toUpperCase());
		help.setEnabled(true);
		swap.setEnabled(true);
		next.setEnabled(true);
		help.setClickable(true);
		swap.setClickable(true);
		next.setClickable(true);
	}

	public void help(View v) {
		String answer = input.getText().toString();
		int len = answer.length();
		if (iswrong) {
			if (len == 0) {
				answer = "";
			} else {
				answer = answer.substring(0, len - 1);
			}
		} else {
			if (len != refActual.name.length()) {
				answer += refActual.name.charAt(len);
			}
		}
		input.setText(answer);
		input.setSelection(answer.length());
	}

	public void swap(View v) {
		// FALTA
	}

	public void next(View v) {
		siguientePalabra();
	}

	/** **************** TEXTWATCHER ******************* **/

	@Override
	public void afterTextChanged(Editable s) {
		String answer = input.getText().toString();
		if (refActual.name.toLowerCase().startsWith(answer.toLowerCase())) {

			progress.setProgress(answer.length());
			if (iswrong) {
				progress.getProgressDrawable().setColorFilter(0xFF33CC00,
						PorterDuff.Mode.MULTIPLY);
				iswrong = false;
			}

			if (answer.length() < refActual.name.length()) {
				fullfill();
			} else if (refActual.name.equalsIgnoreCase(answer)) {
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
			progress.setProgress(refActual.name.length());
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

	protected void fullfill() {
		String answer = input.getText().toString();
		StringBuilder toAdd = new StringBuilder();
		int index = answer.length();
		int length = refActual.name.length();
		loop: while (true) {
			char c = refActual.name.charAt(index);
			while (c == ' ') {
				toAdd.append(c);
				index++;
				if (index == length) {
					break loop;
				}
				c = refActual.name.charAt(index);
			}
			char end;
			if (c == '(') {
				end = ')';
			} else if (c == '[') {
				end = ']';
			} else if (c == '{') {
				end = '}';
			} else {
				break loop;
			}
			while (c != end) {
				toAdd.append(c);
				index++;
				if (index == length) {
					break loop;
				}
				c = refActual.name.charAt(index);
			}
			toAdd.append(c);
		}
		if (toAdd.length() != 0) {
			String t = answer + toAdd;
			input.setText(t);
			input.setSelection(t.length());
		}
	}

	public void specialKeyAction(View v) {
		String t = input.getText().toString();
		if (t.length() == 0) {
			t = t + v.getTag();
		} else {
			Button b = (Button) v;
			t = t + b.getText();
		}
		input.setText(t);
		input.setSelection(t.length());
	}

}
