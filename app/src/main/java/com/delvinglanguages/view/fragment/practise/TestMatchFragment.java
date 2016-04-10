package com.delvinglanguages.view.fragment.practise;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.Settings;
import com.delvinglanguages.kernel.LanguageManager;
import com.delvinglanguages.kernel.game.MatchGame;
import com.delvinglanguages.kernel.manager.PronunciationManager;
import com.delvinglanguages.kernel.test.TestReferenceState;
import com.delvinglanguages.view.utils.AppAnimator;
import com.delvinglanguages.view.utils.AppCode;

public class TestMatchFragment extends TestFragment {

    public TestMatchFragment() {
        super();
    }

    private MatchGame.RoundData roundData;
    private int attempt = 1;
    private int n_options = 6;
    private int own_options_max = 1;

    private Button button_answer[];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.a_practise_match, container, false);

        LanguageManager dataManager = new LanguageManager(getActivity());
        MatchGame gameManager = new MatchGame(dataManager.getReferences());

        ((TextView) view.findViewById(R.id.reference_name)).setText(reference.reference.name);
        ((TextView) view.findViewById(R.id.reference_inflexions)).setText(reference.reference.getInflexionsAsString());

        view.findViewById(R.id.configuration).setVisibility(View.INVISIBLE);

        roundData = gameManager.nextRound(reference.reference, n_options, own_options_max);

        int[] button_ids = new int[]{R.id.answer_1, R.id.answer_2, R.id.answer_3,
                R.id.answer_4, R.id.answer_5, R.id.answer_6, R.id.answer_7, R.id.answer_8};

        button_answer = new Button[button_ids.length];
        for (int i = 0; i < button_answer.length; i++) {
            button_answer[i] = (Button) view.findViewById(button_ids[i]);
            button_answer[i].setClickable(true);
            button_answer[i].setOnClickListener(onAnswerSelected);
        }
        for (int i = 0; i < n_options; i++) {
            button_answer[i].setText(roundData.options[i].first);
            button_answer[i].setTag(roundData.options[i].second);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean[] shownType = AppAnimator.getTypeStatusVector();
        AppAnimator.typeAnimation(getActivity(), shownType, reference.reference.type);
    }

    private View.OnClickListener onAnswerSelected = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ((Boolean) v.getTag())
                acierto(v);

            else
                fallo(v);
        }
    };

    private void acierto(View v) {
        v.getBackground().setColorFilter(0xFF33CC00, PorterDuff.Mode.MULTIPLY);
        for (int i = 0; i < n_options; i++)
            button_answer[i].setClickable(false);

        next();
    }

    private void fallo(View v) {
        attempt++;
        v.setClickable(false);
        v.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        if (attempt == 4) {
            for (int i = 0; i < n_options; i++) {
                if (roundData.options[i].second) {
                    acierto(button_answer[i]);
                }
            }
        }
    }

    private void next() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(Settings.TEST_AFTER_HIT_WAITING_TIME);
                } catch (InterruptedException ignored) {
                }

                reference.match.attempts++;
                reference.match.errors += attempt - 1;

                if (attempt == 1) {

                    reference.stage = TestReferenceState.TestStage.COMPLETE;
                    handler.obtainMessage(AppCode.TEST_ROUND_PASSED).sendToTarget();

                } else
                    handler.obtainMessage(AppCode.TEST_ROUND_SKIPPED).sendToTarget();
            }
        }).start();
    }

}

