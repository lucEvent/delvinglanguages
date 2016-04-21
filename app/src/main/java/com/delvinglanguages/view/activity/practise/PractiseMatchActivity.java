package com.delvinglanguages.view.activity.practise;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.AppSettings;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.LanguageManager;
import com.delvinglanguages.kernel.game.MatchGame;
import com.delvinglanguages.kernel.manager.PronunciationManager;
import com.delvinglanguages.view.utils.AppAnimator;

public class PractiseMatchActivity extends AppCompatActivity {

    protected LanguageManager dataManager;
    protected MatchGame gameManager;
    private PronunciationManager pronunciationManager;

    protected MatchGame.RoundData roundData;
    protected int attempt;
    protected int n_options = 6;
    protected int own_options_max = 1;
    private boolean shownType[];

    protected TextView view_reference, view_inflexions;
    protected Button button_answer[];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_practise_match);

        // Iniciamos elementos del kernel
        dataManager = new LanguageManager(this);
        gameManager = new MatchGame(dataManager.getReferences());
        pronunciationManager = new PronunciationManager(this, dataManager.getCurrentLanguage().getLocale());

        view_reference = (TextView) findViewById(R.id.reference_name);
        view_inflexions = (TextView) findViewById(R.id.reference_inflexions);

        int[] button_ids = new int[]{R.id.answer_1, R.id.answer_2, R.id.answer_3,
                R.id.answer_4, R.id.answer_5, R.id.answer_6, R.id.answer_7, R.id.answer_8};

        button_answer = new Button[button_ids.length];
        for (int i = 0; i < button_answer.length; i++)
            button_answer[i] = (Button) findViewById(button_ids[i]);

        shownType = AppAnimator.getTypeStatusVector();

        nextReference();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        dataManager.saveStatistics();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        pronunciationManager.destroy();
    }

    private void nextReference()
    {
        attempt = 1;
        roundData = gameManager.nextRound(n_options, own_options_max);

        view_reference.setText(roundData.reference.name);
        view_inflexions.setText(roundData.reference.getInflexionsAsString());

        for (int i = 0; i < n_options; i++) {
            button_answer[i].getBackground().setColorFilter(0xFFFFFFFF, AppSettings.PROGRESS_COLOR_MODE);
            button_answer[i].setText(roundData.options[i].first);
            button_answer[i].setClickable(true);
            button_answer[i].setTag(roundData.options[i].second);
        }

        AppAnimator.typeAnimation(this, shownType, roundData.reference.type);

    }

    public void onAnswerSelected(View v)
    {
        if ((Boolean) v.getTag()) {
            acierto(v);
        } else {
            fallo(v);
        }
    }

    private Handler mHandler = new Handler();

    private void acierto(View v)
    {

        v.getBackground().setColorFilter(AppSettings.PROGRESS_COLOR_OK, AppSettings.PROGRESS_COLOR_MODE);
        for (int i = 0; i < n_options; i++) {
            button_answer[i].setClickable(false);
        }

        new Thread(new Runnable() {

            public void run()
            {
                dataManager.exercise(roundData.reference, attempt);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run()
                    {
                        nextReference();
                    }
                });
            }
        }).start();
    }

    private void fallo(View v)
    {
        attempt++;
        v.setClickable(false);
        v.getBackground().setColorFilter(AppSettings.PROGRESS_COLOR_MISS, AppSettings.PROGRESS_COLOR_MODE);
        if (attempt == 4) {
            for (int i = 0; i < n_options; i++) {
                if (roundData.options[i].second) {
                    acierto(button_answer[i]);
                }
            }
        }
    }

    public void onConfigurationAction(View v)
    {
        // TODO: 02/04/2016
        Toast.makeText(this, "Available in coming releases. Stay updated!", Toast.LENGTH_SHORT).show();
    }

    public void onPronunciationAction(View v)
    {
        pronunciationManager.pronounce(roundData.reference.name);
    }

}
