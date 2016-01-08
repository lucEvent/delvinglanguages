package com.delvinglanguages.face.activity.practice;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.game.CompleteGame;
import com.delvinglanguages.kernel.game.CompleteGame.Action;
import com.delvinglanguages.settings.Settings;

public class CompleteActivity extends Activity implements OnClickListener {

    protected DReference refActual;
    protected CompleteGame gamecontroller;

    protected TextView pista, hidden, pronounce;
    protected TextView labels[];
    protected Button letras[];

    protected int position, fcolor, cursor, intento;

    protected StringBuilder descubierta;
    protected Action[] teclas;
    protected String palabraUpp;

    protected Thread flash;
    protected boolean muststop;
    protected Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.a_complete, null);
        Settings.setBackgroundTo(view);
        setContentView(view);

        handler = new Handler();

        pista = (TextView) findViewById(R.id.word);
        hidden = (TextView) findViewById(R.id.solution);
        pronounce = (TextView) findViewById(R.id.pronounce);

        gamecontroller = new CompleteGame(LanguageKernelControl.getReferences());

        labels = new TextView[7];
        labels[Word.NOUN] = (TextView) findViewById(R.id.noun);
        labels[Word.VERB] = (TextView) findViewById(R.id.verb);
        labels[Word.ADJECTIVE] = (TextView) findViewById(R.id.adjective);
        labels[Word.ADVERB] = (TextView) findViewById(R.id.adverb);
        labels[Word.PHRASAL] = (TextView) findViewById(R.id.phrasal);
        labels[Word.EXPRESSION] = (TextView) findViewById(R.id.expression);
        labels[Word.OTHER] = (TextView) findViewById(R.id.other);

        Language idioma = KernelControl.getCurrentLanguage();
        if (!idioma.getSettings(Language.MASK_PH)) {
            labels[Word.PHRASAL].setVisibility(View.GONE);
        }

        letras = new Button[8];
        letras[0] = (Button) findViewById(R.id.key_a1);
        letras[1] = (Button) findViewById(R.id.key_a2);
        letras[2] = (Button) findViewById(R.id.key_a3);
        letras[3] = (Button) findViewById(R.id.key_a4);
        letras[4] = (Button) findViewById(R.id.key_b1);
        letras[5] = (Button) findViewById(R.id.key_b2);
        letras[6] = (Button) findViewById(R.id.key_b3);
        letras[7] = (Button) findViewById(R.id.key_b4);
        for (int i = 0; i < letras.length; i++) {
            letras[i].setOnClickListener(this);
            letras[i].setTag(i);
        }

        String temp = getString(R.string.title_practising);
        setTitle(temp + " " + LanguageKernelControl.getLanguageName());

        siguientePregunta(gamecontroller.nextReference());
    }

    @Override
    protected void onPause() {
        super.onPause();
        KernelControl.saveStatistics();
    }

    protected void siguientePregunta(DReference ref) {
        intento = 1;
        refActual = ref;
        palabraUpp = ref.name.toUpperCase();

        position = 0;
        Settings.setBackgroundColorsforType(labels, refActual.getType());
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
        pista.setText(refActual.getTranslationsAsString().toUpperCase());
        pronounce.setText("");

        teclas = gamecontroller.char_merger(palabraUpp, 8);

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
            descubierta.replace(cursor, cursor + (toappend.length() << 1), toappend);

            cursor += toappend.length();
            position++;

            hidden.setText(descubierta);

            if (cursor == palabraUpp.length()) {
                pronounce.setText("[" + refActual.pronunciation + "]");
                for (int i = 0; i < letras.length; i++) {
                    letras[i].setClickable(false);
                    letras[i].setEnabled(false);
                }
                new Thread(new Runnable() {
                    public void run() {
                        KernelControl.exercise(refActual, intento);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                siguientePregunta(gamecontroller.nextReference());
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
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            hidden.setBackgroundColor(fcolor);
                        }
                    });

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
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