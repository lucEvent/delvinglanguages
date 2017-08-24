package com.delvinglanguages.view.activity.practise;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.AppSettings;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DelvingListManager;
import com.delvinglanguages.kernel.game.CompleteGame;
import com.delvinglanguages.kernel.record.Record;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.view.utils.AppAnimator;

public class PractiseCompleteActivity extends Activity {

    protected DelvingListManager dataManager;
    protected CompleteGame gameManager;
    protected Handler handler;


    protected DReference currentReference;

    protected TextView view_reference, view_discovering;
    protected Button key[];

    protected int position, fcolor, cursor, intento;

    protected StringBuilder descubierta;
    protected CompleteGame.Action[] teclas;
    protected String palabraUpp;

    protected Thread flash;
    protected boolean muststop;

    private boolean shownType[];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        dataManager = new DelvingListManager(this);
        // Select proper references
        DReferences references;
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(AppCode.DREFERENCE_NAME_NUM)) {
            int num = extras.getInt(AppCode.DREFERENCE_NAME_NUM);
            references = new DReferences(num);

            for (int i = 0; i < num; i++)
                references.add(dataManager.getReference(extras.getString(AppCode.DREFERENCE_NAME + i)));

        } else
            references = dataManager.getReferences();
        //
        gameManager = new CompleteGame(references);

        handler = new Handler();

        initUI();
        nextReference(gameManager.nextReference());
    }

    public void initUI()
    {
        setContentView(R.layout.a_practise_complete);

        view_reference = (TextView) findViewById(R.id.reference_name);
        view_discovering = (TextView) findViewById(R.id.reference_discovering);

        key = new Button[8];
        key[0] = (Button) findViewById(R.id.key_a1);
        key[1] = (Button) findViewById(R.id.key_a2);
        key[2] = (Button) findViewById(R.id.key_a3);
        key[3] = (Button) findViewById(R.id.key_a4);
        key[4] = (Button) findViewById(R.id.key_b1);
        key[5] = (Button) findViewById(R.id.key_b2);
        key[6] = (Button) findViewById(R.id.key_b3);
        key[7] = (Button) findViewById(R.id.key_b4);
        for (int i = 0; i < key.length; i++)
            key[i].setTag(i);

        if (!dataManager.getCurrentList().arePhrasalVerbsEnabled())
            findViewById(R.id.phrasal_verb).setVisibility(View.GONE);

        shownType = AppAnimator.getTypeStatusVector();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        initUI();
        displayFields();
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        dataManager.saveStatistics();
    }

    protected void nextReference(DReference ref)
    {
        intento = 1;
        currentReference = ref;
        palabraUpp = ref.name.toUpperCase();

        position = 0;
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
        for (; pos < palabraUpp.length(); pos++)
            descubierta.append(" _");

        teclas = gameManager.char_merger(palabraUpp, 8);

        displayFields();
    }

    private void displayFields()
    {
        AppAnimator.typeAnimation(this, shownType, currentReference.type);

        view_discovering.setText(descubierta);
        view_reference.setText(currentReference.getTranslationsAsString().toUpperCase());

        for (int i = 0; i < key.length; i++) {
            key[i].setEnabled(true);
            key[i].setClickable(true);
            key[i].setText("" + teclas[i].letter);
        }
    }

    public void onKey(View v)
    {
        int tecla = (Integer) v.getTag();
        if (teclas[tecla].position == position) {
            flashcolor(AppSettings.PROGRESS_COLOR_OK);

            String toappend = teclas[tecla].string;
            descubierta.replace(cursor, cursor + (toappend.length() << 1), toappend);

            cursor += toappend.length();
            position++;

            view_discovering.setText(descubierta);

            if (cursor == palabraUpp.length()) {
                for (Button k : key) {
                    k.setClickable(false);
                    k.setEnabled(false);
                }
                new Thread(new Runnable() {
                    public void run()
                    {
                        dataManager.exercise(Record.PRACTISED_COMPLETE, currentReference, intento);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ignored) {
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run()
                            {
                                nextReference(gameManager.nextReference());
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
            flashcolor(AppSettings.PROGRESS_COLOR_MISS);
        }
    }

    /**
     * ******************** OPERACIONES FLASH ********************** /
     **/
    protected Thread createFlashThread()
    {
        return new Thread(new Runnable() {

            @Override
            public void run()
            {
                while ((fcolor & 0xFF000000) != 0 && !muststop) {
                    fcolor -= 0x11000000;
                    handler.post(new Runnable() {
                        @Override
                        public void run()
                        {
                            view_discovering.setBackgroundColor(fcolor);
                        }
                    });

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });
    }

    protected void flashcolor(int color)
    {
        fcolor = color;
        if (flash != null && flash.getState() == Thread.State.RUNNABLE) {
            muststop = true;
            while (flash.getState() != Thread.State.TERMINATED) ;
        }
        muststop = false;
        flash = createFlashThread();
        flash.start();
    }

    public void onConfigurationAction(View v)
    {
        // TODO: 02/04/2016
        Toast.makeText(this, R.string.msg_in_next_releases, Toast.LENGTH_SHORT).show();
    }

}