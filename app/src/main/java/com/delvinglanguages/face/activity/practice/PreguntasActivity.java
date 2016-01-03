package com.delvinglanguages.face.activity.practice;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.game.MatchGame;
import com.delvinglanguages.kernel.game.MatchGame.QuestionModel;
import com.delvinglanguages.settings.Settings;

public class PreguntasActivity extends Activity implements OnClickListener {

    protected final int NUM_RESP = 6;

    protected Language idioma;
    protected MatchGame gamecontroller;

    protected QuestionModel pActual;
    protected int intento;

    protected TextView palabra, pregunta;
    protected Button resps[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.a_match, null);
        Settings.setBackgroundTo(view);
        setContentView(view);

        // Iniciamos elementos del kernel
        idioma = KernelControl.getCurrentLanguage();
        gamecontroller = new MatchGame(LanguageKernelControl.getReferences());

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

        setTitle(getString(R.string.title_practising) + " " + idioma.language_delved_name);
        nuevaPregunta();
    }

    @Override
    protected void onPause() {
        super.onPause();
        KernelControl.saveStatistics();
    }

    /**
     * ************** METODOS ONCLICK ****************************
     **/
    @Override
    public void onClick(View v) {
        if ((Boolean) v.getTag()) {
            acierto(v);
        } else {
            fallo(v);
        }
    }

    /**
     * ***************** METODOS PRIVADOS *************************
     **/
    private Handler mHandler = new Handler();

    private void acierto(View v) {

        v.getBackground().setColorFilter(0xFF33CC00, PorterDuff.Mode.MULTIPLY);
        for (int i = 0; i < NUM_RESP; i++) {
            resps[i].setClickable(false);
        }

        new Thread(new Runnable() {

            public void run() {
                KernelControl.exercise(pActual.reference, intento);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
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
        pregunta.setText(pActual.reference.pronunciation);

        for (int i = 0; i < NUM_RESP; i++) {
            resps[i].getBackground().setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
            resps[i].setText(pActual.answers[i]);
            resps[i].setClickable(true);
            resps[i].setTag(pActual.correct[i]);
        }

    }

}