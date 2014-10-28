package com.delvinglanguages.face.activities.practice;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.Cerebro;
import com.delvinglanguages.core.Cerebro.Action;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.DReference;
import com.delvinglanguages.settings.Configuraciones;

public class CompleteActivity extends Activity implements OnClickListener {

	private static final String DEBUG = "##Complete##";

	protected DReference refActual;
	protected Cerebro cerebro;

	protected TextView pista, hidden, pronounce;
	protected TextView labels[];
	protected Button letras[];

	protected int position, fcolor, cursor, intento;

	protected StringBuilder descubierta;
	protected Action[] teclas;
	protected String palabraUpp;

	protected Thread flash;
	protected boolean muststop;
	protected Handler mHandler = new Handler();

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_complete);

		RelativeLayout bg = (RelativeLayout) findViewById(R.id.ac_bg);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			bg.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			bg.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		pista = (TextView) findViewById(R.id.ac_word);
		hidden = (TextView) findViewById(R.id.ac_solution);
		pronounce = (TextView) findViewById(R.id.ac_pronounce);

		cerebro = new Cerebro(ControlCore.getReferences());

		labels = new TextView[Configuraciones.NUM_TYPES];
		labels[0] = (TextView) findViewById(R.id.ac_noun);
		labels[1] = (TextView) findViewById(R.id.ac_verb);
		labels[2] = (TextView) findViewById(R.id.ac_adj);
		labels[3] = (TextView) findViewById(R.id.ac_adv);
		labels[4] = (TextView) findViewById(R.id.ac_phrasal);
		labels[5] = (TextView) findViewById(R.id.ac_expression);
		labels[6] = (TextView) findViewById(R.id.ac_other);

		letras = new Button[8];
		letras[0] = (Button) findViewById(R.id.ac_a1);
		letras[1] = (Button) findViewById(R.id.ac_a2);
		letras[2] = (Button) findViewById(R.id.ac_a3);
		letras[3] = (Button) findViewById(R.id.ac_a4);
		letras[4] = (Button) findViewById(R.id.ac_b1);
		letras[5] = (Button) findViewById(R.id.ac_b2);
		letras[6] = (Button) findViewById(R.id.ac_b3);
		letras[7] = (Button) findViewById(R.id.ac_b4);
		for (int i = 0; i < letras.length; i++) {
			letras[i].setOnClickListener(this);
			letras[i].setTag(i);
		}

		String temp = getString(R.string.title_practising);
		setTitle(temp + " " + ControlCore.getIdiomaActual(this).getName());

		siguientePregunta(cerebro.nextReference());
	}

	@Override
	protected void onPause() {
		super.onPause();
		ControlCore.saveStatistics();
	}

	protected void siguientePregunta(DReference ref) {
		intento = 1;
		refActual = ref;
		palabraUpp = ref.item.toUpperCase();

		position = 0;
		int type = refActual.type;
		for (int i = 0; i < Configuraciones.NUM_TYPES; ++i) {
			if ((type & (1 << i)) != 0) {
				labels[i].setBackgroundColor(Configuraciones.type_colors[i]);
			} else {
				labels[i].setBackgroundColor(0xFFCCCCCC);
			}
		}
		char c = palabraUpp.charAt(0);
		int pos = 1;
		if (!Character.isLetter(c)) {
			char fin = 0;
			String emp = "" + c;
			if (c == '(') {
				fin = ')';
			} else if (c == '[') {
				fin = ']';
			} else if (c == '{') {
				fin = '}';
			}
			if (fin != 0) {
				int ifin = palabraUpp.indexOf("" + fin);
				if (ifin != -1) {
					emp = palabraUpp.substring(0, ifin);
				}
			}
			descubierta = new StringBuilder(emp);
			pos = emp.length();
			cursor = pos;
		} else {
			descubierta = new StringBuilder("_");
			cursor = 0;
		}
		for (; pos < palabraUpp.length(); pos++) {
			descubierta.append(" _");
		}

		hidden.setText(descubierta);
		pista.setText(refActual.getTranslation().toUpperCase());
		pronounce.setText("");

		teclas = cerebro.char_merger(palabraUpp, 8);

		for (int i = 0; i < letras.length; i++) {
			letras[i].setEnabled(true);
			letras[i].setClickable(true);
			letras[i].setText("" + teclas[i].letter);

		}
	}

	@Override
	public void onClick(View v) {
		int tecla = (Integer) v.getTag();
		if (teclas[tecla].position == position) {
			flashcolor(0xFF00FF00);

			String toappend = teclas[tecla].string;
			descubierta.replace(cursor, cursor + (toappend.length() << 1),
					toappend);

			cursor += toappend.length();
			position++;

			hidden.setText(descubierta);

			if (cursor == palabraUpp.length()) {
				pronounce.setText("[" + refActual.getPronunciation() + "]");
				for (int i = 0; i < letras.length; i++) {
					letras[i].setClickable(false);
					letras[i].setEnabled(false);
				}
				new Thread(new Runnable() {
					public void run() {
						ControlCore.ejercicio(refActual,intento);
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								siguientePregunta(cerebro.nextReference());
							}
						});
					}
				}).start();
			} else if (teclas[tecla].visibleUntil <= position) {
				if (teclas[tecla].replaceBy != null) {
					Button b = (Button) v;
					teclas[tecla] = teclas[tecla].replaceBy;
					b.setText("" + teclas[tecla].letter);
				}
			}
		} else {
			intento++;
			flashcolor(0xFFFF0000);
		}
	}

	/**
	 * ******************** OPERACIONES FLASH ********************** /
	 **/

	protected Thread createFlashThread() {
		return new Thread(new Runnable() {

			@Override
			public void run() {
				while ((fcolor & 0xFF000000) != 0 && !muststop) {
					fcolor -= 0x11000000;
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							hidden.setBackgroundColor(fcolor);
						}
					});

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

	}

	protected void flashcolor(int color) {
		fcolor = color;
		if (flash != null && flash.getState() == Thread.State.RUNNABLE) {
			muststop = true;
			while (flash.getState() != Thread.State.TERMINATED)
				;
		}
		muststop = false;
		flash = createFlashThread();
		flash.start();
	}
}