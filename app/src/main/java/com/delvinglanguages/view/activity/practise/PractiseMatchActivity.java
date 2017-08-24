package com.delvinglanguages.view.activity.practise;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.AppSettings;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DelvingListManager;
import com.delvinglanguages.kernel.LanguageCode;
import com.delvinglanguages.kernel.game.MatchGame;
import com.delvinglanguages.kernel.manager.PronunciationManager;
import com.delvinglanguages.kernel.record.Record;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.view.utils.AppAnimator;

public class PractiseMatchActivity extends AppCompatActivity {

    private static final long TIME_UNVEILING = 1500;

    protected DelvingListManager dataManager;
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
        gameManager = new MatchGame(references);
        pronunciationManager = new PronunciationManager(this, LanguageCode.getLocale(dataManager.getCurrentList().from_code), true);

        initUI();
        nextReference();
    }

    public void initUI()
    {
        setContentView(R.layout.a_practise_match);
        view_reference = (TextView) findViewById(R.id.reference_name);
        view_inflexions = (TextView) findViewById(R.id.reference_inflexions);

        int[] button_ids = new int[]{R.id.answer_1, R.id.answer_2, R.id.answer_3,
                R.id.answer_4, R.id.answer_5, R.id.answer_6, R.id.answer_7, R.id.answer_8};

        button_answer = new Button[button_ids.length];
        for (int i = 0; i < button_answer.length; i++)
            button_answer[i] = (Button) findViewById(button_ids[i]);

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

        displayFields();
    }

    private void displayFields()
    {
        view_reference.setText(roundData.reference.name);
        view_inflexions.setText(roundData.reference.getInflexionsAsString());

        for (int i = 0; i < n_options; i++) {
            button_answer[i].setText(roundData.options[i].value);
            button_answer[i].setTag(i);

            if (attempt > 1 && roundData.options[i].isChecked) {
                button_answer[i].setClickable(false);
                button_answer[i].getBackground().setColorFilter(AppSettings.PROGRESS_COLOR_MISS, AppSettings.PROGRESS_COLOR_MODE);
            } else {
                button_answer[i].setClickable(true);
                button_answer[i].getBackground().setColorFilter(null);
            }
        }

        AppAnimator.typeAnimation(this, shownType, roundData.reference.type);
    }

    public void onAnswerSelected(View v)
    {
        int pos = (int) v.getTag();
        roundData.options[pos].isChecked = true;
        if (roundData.options[pos].isCorrect)
            acierto(v);
        else
            fallo(v);
    }

    private Handler mHandler = new Handler();

    private void acierto(View view)
    {
        unveilCorrectAnswers();
        for (int i = 0; i < n_options; i++)
            button_answer[i].setClickable(false);

        new Thread(new Runnable() {

            public void run()
            {
                dataManager.exercise(Record.PRACTISED_MATCH, roundData.reference, attempt);
                try {
                    Thread.sleep(TIME_UNVEILING);
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
        if (attempt == 4)
            acierto(null);
    }

    private void unveilCorrectAnswers()
    {
        for (int i = 0; i < n_options; i++)
            if (roundData.options[i].isCorrect) {
                button_answer[i].getBackground().setColorFilter(AppSettings.PROGRESS_COLOR_OK, AppSettings.PROGRESS_COLOR_MODE);
                button_answer[i].invalidate();
            }
    }

    public void onConfigurationAction(View v)
    {
        // TODO: 02/04/2016
        Toast.makeText(this, R.string.msg_in_next_releases, Toast.LENGTH_SHORT).show();
    }

    public void onPronunciationAction(View v)
    {
        pronunciationManager.pronounce(roundData.reference.name);
    }

}
