package com.delvinglanguages.view.activity.practise;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.AppSettings;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.LanguageManager;
import com.delvinglanguages.kernel.game.WriteGame;
import com.delvinglanguages.view.utils.AppAnimator;

public class PractiseWriteActivity extends Activity implements TextWatcher {

    private LanguageManager dataManager;
    private WriteGame gameManager;

    private Handler handler;
    private DReference currentReference;

    private TextView view_reference;
    private ProgressBar view_progress;
    private EditText input;
    private ImageButton help, next;

    private boolean[] shownTypes;
    private boolean iswrong;
    private int attempt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_practise_write);

        dataManager = new LanguageManager(this);
        gameManager = new WriteGame(dataManager.getReferences());

        handler = new Handler();

        view_reference = (TextView) findViewById(R.id.reference_name);
        input = (EditText) findViewById(R.id.input);
        input.addTextChangedListener(this);
        view_progress = (ProgressBar) findViewById(R.id.progress);
        view_progress.getProgressDrawable().setColorFilter(AppSettings.PROGRESS_COLOR_OK, AppSettings.PROGRESS_COLOR_MODE);

        help = (ImageButton) findViewById(R.id.help);
        next = (ImageButton) findViewById(R.id.next);

        shownTypes = AppAnimator.getTypeStatusVector();

        nextReference();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        dataManager.saveStatistics();
    }

    protected void nextReference()
    {
        attempt = 1;
        currentReference = gameManager.nextReference();

        AppAnimator.typeAnimation(this, shownTypes, currentReference.type);

        view_progress.setMax(currentReference.name.length());
        view_progress.setProgress(0);
        iswrong = false;
        input.setText("");
        view_reference.setText(currentReference.getTranslationsAsString().toUpperCase());
        help.setEnabled(true);
        next.setEnabled(true);
        help.setClickable(true);
        next.setClickable(true);
    }

    @Override
    public void afterTextChanged(Editable s)
    {
        String answer = input.getText().toString();
        if (currentReference.name.toLowerCase().startsWith(answer.toLowerCase())) {

            view_progress.setProgress(answer.length());
            if (iswrong) {
                view_progress.getProgressDrawable().setColorFilter(AppSettings.PROGRESS_COLOR_OK, AppSettings.PROGRESS_COLOR_MODE);
                iswrong = false;
            }

            if (answer.length() < currentReference.name.length()) {
                fullfill();
            } else if (currentReference.name.equalsIgnoreCase(answer)) {
                help.setEnabled(false);
                next.setEnabled(false);
                help.setClickable(false);
                next.setClickable(false);

                dataManager.exercise(currentReference, attempt);

                new Thread(new Runnable() {
                    public void run()
                    {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            AppSettings.printerror("[PWA] Exception in afterTextChanged (1)", e);
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run()
                            {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    AppSettings.printerror("[PWA] Exception in afterTextChanged (2)", e);
                                }
                                nextReference();
                            }
                        });
                    }
                }).start();
            }
        } else {
            attempt++;
            view_progress.setProgress(currentReference.name.length());
            if (!iswrong) {
                iswrong = true;
                view_progress.getProgressDrawable().setColorFilter(AppSettings.PROGRESS_COLOR_MISS, AppSettings.PROGRESS_COLOR_MODE);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
    }

    protected void fullfill()
    {
        String answer = input.getText().toString();
        StringBuilder toAdd = new StringBuilder();
        int index = answer.length();
        int length = currentReference.name.length();
        loop:
        while (true) {
            char c = currentReference.name.charAt(index);
            while (c == ' ') {
                toAdd.append(c);
                index++;
                if (index == length)
                    break loop;
                c = currentReference.name.charAt(index);
            }
            char end;
            if (c == '(') end = ')';
            else if (c == '[') end = ']';
            else if (c == '{') end = '}';
            else break;

            while (c != end) {
                toAdd.append(c);
                index++;
                if (index == length)
                    break loop;
                c = currentReference.name.charAt(index);
            }
            toAdd.append(c);
        }
        if (toAdd.length() != 0) {
            String t = answer + toAdd;
            input.setText(t);
            input.setSelection(t.length());
        }
    }

    public void onConfigurationAction(View v)
    {
        // TODO: 02/04/2016
        Toast.makeText(this, R.string.msg_in_next_releases, Toast.LENGTH_SHORT).show();
    }

    public void onNextAction(View v)
    {
        nextReference();
    }

    public void onHelpAction(View v)
    {
        String answer = input.getText().toString();
        int len = answer.length();
        if (iswrong) {
            if (len == 0) {
                answer = "";
            } else {
                answer = answer.substring(0, len - 1);
            }
        } else {
            if (len != currentReference.name.length()) {
                answer += currentReference.name.charAt(len);
            }
        }
        input.setText(answer);
        input.setSelection(answer.length());
    }

}
