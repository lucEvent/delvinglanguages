package com.delvinglanguages.view.fragment.practise;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.delvinglanguages.AppSettings;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.test.TestReferenceState;
import com.delvinglanguages.view.activity.practise.TestActivity;
import com.delvinglanguages.view.utils.AppAnimator;
import com.delvinglanguages.view.utils.TestListener;

public class TestListeningFragment extends TestFragment implements TextWatcher {

    public static TestListeningFragment getInstance(Handler handler, TestReferenceState reference)
    {
        TestListeningFragment f = new TestListeningFragment();
        f.reference = reference;
        f.handler = handler;
        return f;
    }

    protected ProgressBar view_progress;
    protected EditText input;

    private boolean iswrong;
    private int attempt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.a_practise_listening, container, false);

        input = (EditText) view.findViewById(R.id.input);
        input.setText("");
        input.addTextChangedListener(this);

        view_progress = (ProgressBar) view.findViewById(R.id.progress);
        view_progress.getProgressDrawable().setColorFilter(AppSettings.PROGRESS_COLOR_OK, AppSettings.PROGRESS_COLOR_MODE);
        view_progress.setMax(reference.reference.name.length());
        view_progress.setProgress(0);

        view.findViewById(R.id.configuration).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.help).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.next).setVisibility(View.INVISIBLE);

        attempt = 1;
        iswrong = false;

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        boolean[] shownType = AppAnimator.getTypeStatusVector();
        AppAnimator.typeAnimation(getActivity(), shownType, reference.reference.type);
        ((TestActivity) getActivity()).onPronunciationAction(null);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void afterTextChanged(Editable s)
    {
        String answer = input.getText().toString();
        if (reference.reference.name.toLowerCase().startsWith(answer.toLowerCase())) {

            view_progress.setProgress(answer.length());
            if (iswrong) {
                view_progress.getProgressDrawable().setColorFilter(AppSettings.PROGRESS_COLOR_OK, AppSettings.PROGRESS_COLOR_MODE);
                iswrong = false;
            }

            if (answer.length() < reference.reference.name.length())
                fullfill();

            else if (reference.reference.name.equalsIgnoreCase(answer))
                next();

        } else {
            attempt++;
            view_progress.setProgress(reference.reference.name.length());
            if (!iswrong) {
                iswrong = true;
                view_progress.getProgressDrawable().setColorFilter(AppSettings.PROGRESS_COLOR_MISS, AppSettings.PROGRESS_COLOR_MODE);
            }
            if (attempt == 4)
                next();
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

    private void fullfill()
    {
        String answer = input.getText().toString();
        StringBuilder toAdd = new StringBuilder();
        int index = answer.length();
        int length = reference.reference.name.length();
        loop:
        while (true) {
            char c = reference.reference.name.charAt(index);
            while (c == ' ') {
                toAdd.append(c);
                index++;
                if (index == length)
                    break loop;
                c = reference.reference.name.charAt(index);
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
                c = reference.reference.name.charAt(index);
            }
            toAdd.append(c);
        }
        if (toAdd.length() != 0) {
            String t = answer + toAdd;
            input.setText(t);
            input.setSelection(t.length());
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                reference.listening.attempts++;
                reference.listening.errors += attempt - 1;

                if (attempt == 1) {

                    reference.stage = TestReferenceState.TestStage.END;
                    handler.obtainMessage(TestListener.TEST_ROUND_PASSED).sendToTarget();

                } else
                    handler.obtainMessage(TestListener.TEST_ROUND_SKIPPED).sendToTarget();
            }
        }).start();
    }

}
