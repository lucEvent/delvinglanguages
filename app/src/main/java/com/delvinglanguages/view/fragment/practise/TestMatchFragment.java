package com.delvinglanguages.view.fragment.practise;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.delvinglanguages.AppSettings;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.LanguageManager;
import com.delvinglanguages.kernel.game.MatchGame;
import com.delvinglanguages.kernel.test.TestReferenceState;
import com.delvinglanguages.view.utils.AppAnimator;
import com.delvinglanguages.view.utils.TestListener;

public class TestMatchFragment extends TestFragment {

    public static TestMatchFragment getInstance(Handler handler, TestReferenceState reference, TestReferenceState.TestStage nextStage)
    {
        TestMatchFragment f = new TestMatchFragment();
        f.reference = reference;
        f.handler = handler;
        f.nextStage = nextStage;
        return f;
    }

    private MatchGame.RoundData roundData;
    private int attempt = 1;
    private int n_options = 6;
    private int own_options_max = 1;

    private Button button_answer[];

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LanguageManager dataManager = new LanguageManager(getActivity());
        MatchGame gameManager = new MatchGame(dataManager.getReferences());
        roundData = gameManager.nextRound(reference.reference, n_options, own_options_max);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        FrameLayout view = new FrameLayout(getActivity());
        view.addView(onCreateView(inflater, container));
        return view;
    }

    private View onCreateView(LayoutInflater inflater, ViewGroup container)
    {
        View view = inflater.inflate(R.layout.a_practise_match, container, false);

        ((TextView) view.findViewById(R.id.reference_name)).setText(reference.reference.name);
        ((TextView) view.findViewById(R.id.reference_inflexions)).setText(reference.reference.getInflexionsAsString());

        view.findViewById(R.id.configuration).setVisibility(View.INVISIBLE);

        int[] button_ids = new int[]{R.id.answer_1, R.id.answer_2, R.id.answer_3,
                R.id.answer_4, R.id.answer_5, R.id.answer_6, R.id.answer_7, R.id.answer_8};

        button_answer = new Button[button_ids.length];
        for (int i = 0; i < roundData.options.length; i++) {
            button_answer[i] = (Button) view.findViewById(button_ids[i]);
            button_answer[i].setOnClickListener(onAnswerSelected);

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
        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        FrameLayout parent = (FrameLayout) getView();
        parent.removeAllViews();
        parent.addView(onCreateView((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), (ViewGroup) parent.getParent()));
        parent.invalidate();

        boolean[] shownType = AppAnimator.getTypeStatusVector();
        AppAnimator.typeAnimation(getActivity(), shownType, reference.reference.type);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        boolean[] shownType = AppAnimator.getTypeStatusVector();
        AppAnimator.typeAnimation(getActivity(), shownType, reference.reference.type);
    }

    private View.OnClickListener onAnswerSelected = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            int pos = (int) v.getTag();
            roundData.options[pos].isChecked = true;
            if (roundData.options[pos].isCorrect)
                acierto(v);
            else
                fallo(v);
        }
    };

    private void acierto(View v)
    {
        unveilCorrectAnswers();
        for (int i = 0; i < n_options; i++)
            button_answer[i].setClickable(false);

        next();
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

    private void next()
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    Thread.sleep(AppSettings.TEST_AFTER_HIT_WAITING_TIME);
                } catch (InterruptedException ignored) {
                }

                reference.match.attempts++;
                reference.match.errors += attempt - 1;

                if (attempt == 1) {

                    reference.stage = nextStage;
                    handler.obtainMessage(TestListener.TEST_ROUND_PASSED).sendToTarget();

                } else
                    handler.obtainMessage(TestListener.TEST_ROUND_SKIPPED).sendToTarget();
            }
        }).start();
    }

}

