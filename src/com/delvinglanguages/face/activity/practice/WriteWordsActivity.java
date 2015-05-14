package com.delvinglanguages.face.activity.practice;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.face.view.SpecialKeysBar;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.IDDelved;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.game.WriteGame;
import com.delvinglanguages.settings.Settings;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.a_write, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		help = (ImageButton) findViewById(R.id.help);
		swap = (ImageButton) findViewById(R.id.swap);
		next = (ImageButton) findViewById(R.id.next);
		palabra = (TextView) findViewById(R.id.word);
		input = (EditText) findViewById(R.id.input);
		progress = (ProgressBar) findViewById(R.id.progress);
		progress.getProgressDrawable().setColorFilter(0xFF33CC00, PorterDuff.Mode.MULTIPLY);

		input.addTextChangedListener(this);

		if (this.getClass() == WriteWordsActivity.class) {
			gamecontroller = new WriteGame(LanguageKernelControl.getReferences());
			String temp = getString(R.string.title_practising);
			setTitle(temp + " " + LanguageKernelControl.getLanguageName());
		}

		labels = new TextView[Settings.NUM_TYPES];
		labels[Word.NOUN] = (TextView) findViewById(R.id.noun);
		labels[Word.VERB] = (TextView) findViewById(R.id.verb);
		labels[Word.ADJECTIVE] = (TextView) findViewById(R.id.adjective);
		labels[Word.ADVERB] = (TextView) findViewById(R.id.adverb);
		labels[Word.PHRASAL] = (TextView) findViewById(R.id.phrasal);
		labels[Word.EXPRESSION] = (TextView) findViewById(R.id.expression);
		labels[Word.OTHER] = (TextView) findViewById(R.id.other);

		IDDelved idioma = KernelControl.getCurrentLanguage();
		if (!idioma.getSettings(IDDelved.MASK_PH)) {
			labels[Word.PHRASAL].setVisibility(View.GONE);
		}

		new SpecialKeysBar(findViewById(R.id.letrasespeciales), new EditText[] { input });

		siguientePalabra();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (this.getClass() == WriteWordsActivity.class) {
			KernelControl.saveStatistics();
		}
	}

	protected void siguientePalabra() {
		intento = 1;
		refActual = gamecontroller.nextReference();
		int type = refActual.getType();
		for (int i = 0; i < Settings.NUM_TYPES; ++i) {
			if ((type & (1 << i)) != 0) {
				labels[i].setBackgroundColor(Settings.type_colors[i]);
			} else {
				labels[i].setBackgroundColor(0xFFCCCCCC);
			}
		}
		progress.setMax(refActual.getName().length());
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
			if (len != refActual.getName().length()) {
				answer += refActual.getName().charAt(len);
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
		if (refActual.getName().toLowerCase().startsWith(answer.toLowerCase())) {

			progress.setProgress(answer.length());
			if (iswrong) {
				progress.getProgressDrawable().setColorFilter(0xFF33CC00, PorterDuff.Mode.MULTIPLY);
				iswrong = false;
			}

			if (answer.length() < refActual.getName().length()) {
				fullfill();
			} else if (refActual.getName().equalsIgnoreCase(answer)) {
				help.setEnabled(false);
				swap.setEnabled(false);
				next.setEnabled(false);
				help.setClickable(false);
				swap.setClickable(false);
				next.setClickable(false);

				KernelControl.exercise(refActual, intento);

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
			progress.setProgress(refActual.getName().length());
			if (!iswrong) {
				progress.getProgressDrawable().setColorFilter(0xFFFF0000, PorterDuff.Mode.SRC_IN);
				iswrong = true;
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	protected void fullfill() {
		String answer = input.getText().toString();
		StringBuilder toAdd = new StringBuilder();
		int index = answer.length();
		int length = refActual.getName().length();
		loop: while (true) {
			char c = refActual.getName().charAt(index);
			while (c == ' ') {
				toAdd.append(c);
				index++;
				if (index == length) {
					break loop;
				}
				c = refActual.getName().charAt(index);
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
				c = refActual.getName().charAt(index);
			}
			toAdd.append(c);
		}
		if (toAdd.length() != 0) {
			String t = answer + toAdd;
			input.setText(t);
			input.setSelection(t.length());
		}
	}

}
