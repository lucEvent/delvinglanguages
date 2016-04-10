package com.delvinglanguages.view.fragment.practise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.Settings;
import com.delvinglanguages.kernel.game.CompleteGame;
import com.delvinglanguages.kernel.test.TestReferenceState;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.view.utils.AppAnimator;
import com.delvinglanguages.view.utils.AppCode;

public class TestCompleteFragment extends TestFragment {

    public TestCompleteFragment() {
        super();
    }

    private TextView view_discovering;
    private Button key[];

    private int position, fcolor, cursor, attempt;

    private StringBuilder descubierta;
    private CompleteGame.Action[] teclas;
    private String palabraUpp;

    private Thread flash;
    private boolean muststop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.a_practise_complete, container, false);

        CompleteGame gameManager = new CompleteGame(new DReferences());

        TextView view_reference = (TextView) view.findViewById(R.id.reference_name);
        view_discovering = (TextView) view.findViewById(R.id.reference_discovering);
        view.findViewById(R.id.configuration).setVisibility(View.INVISIBLE);

        int[] button_ids = new int[]{R.id.key_a1, R.id.key_a2, R.id.key_a3, R.id.key_a4,
                R.id.key_b1, R.id.key_b2, R.id.key_b3, R.id.key_b4};
        key = new Button[button_ids.length];
        for (int i = 0; i < key.length; i++) {
            key[i] = (Button) view.findViewById(button_ids[i]);
            key[i].setTag(i);
            key[i].setOnClickListener(onKey);
        }

        attempt = 1;
        palabraUpp = reference.reference.name.toUpperCase();

        position = 0;
        char c = palabraUpp.charAt(0);
        int pos = 1;
        if (!Character.isLetter(c)) {
            char fin = 0;
            String emp = "" + c;
            if (c == '(') fin = ')';
            else if (c == '[') fin = ']';
            else if (c == '{') fin = '}';

            if (fin != 0) {
                int ifin = palabraUpp.indexOf("" + fin);
                if (ifin != -1)
                    emp = palabraUpp.substring(0, ifin);
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

        view_discovering.setText(descubierta);
        view_reference.setText(reference.reference.getTranslationsAsString().toUpperCase());

        teclas = gameManager.char_merger(palabraUpp, 8);

        for (int i = 0; i < key.length; i++) {
            key[i].setEnabled(true);
            key[i].setClickable(true);
            key[i].setText("" + teclas[i].letter);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean[] shownType = AppAnimator.getTypeStatusVector();
        AppAnimator.typeAnimation(getActivity(), shownType, reference.reference.type);
    }

    private View.OnClickListener onKey = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tecla = (Integer) v.getTag();
            if (teclas[tecla].position == position) {
                flashcolor(0xFF00FF00);

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

                    next();

                } else if (teclas[tecla].visibleUntil <= position) {
                    if (teclas[tecla].replaceBy != null) {
                        Button b = (Button) v;
                        teclas[tecla] = teclas[tecla].replaceBy;
                        b.setText("" + teclas[tecla].letter);
                    }
                }
            } else {
                attempt++;
                flashcolor(0xFFFF0000);

                if (attempt == 4)
                    next();
            }
        }
    };

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

    protected void flashcolor(int color) {
        fcolor = color;
        if (flash != null && flash.getState() == Thread.State.RUNNABLE) {
            muststop = true;
            while (flash.getState() != Thread.State.TERMINATED) ;
        }
        muststop = false;
        flash = createFlashThread();
        flash.start();
    }

    private void next() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(Settings.TEST_AFTER_HIT_WAITING_TIME);
                } catch (InterruptedException ignored) {
                }

                reference.complete.attempts++;
                reference.complete.errors += attempt - 1;

                if (attempt == 1) {

                    reference.stage = TestReferenceState.TestStage.WRITE;
                    handler.obtainMessage(AppCode.TEST_ROUND_PASSED).sendToTarget();

                } else
                    handler.obtainMessage(AppCode.TEST_ROUND_SKIPPED).sendToTarget();
            }
        }).start();
    }

}


