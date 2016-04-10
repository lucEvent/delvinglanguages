package com.delvinglanguages.view.activity.practise;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.game.TestGame;
import com.delvinglanguages.kernel.manager.PronunciationManager;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.test.TestManager;
import com.delvinglanguages.kernel.test.TestReferenceState;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.view.fragment.practise.TestCompleteFragment;
import com.delvinglanguages.view.fragment.practise.TestDelvingFragment;
import com.delvinglanguages.view.fragment.practise.TestFragment;
import com.delvinglanguages.view.fragment.practise.TestListeningFragment;
import com.delvinglanguages.view.fragment.practise.TestMatchFragment;
import com.delvinglanguages.view.fragment.practise.TestResultFragment;
import com.delvinglanguages.view.fragment.practise.TestWriteFragment;
import com.delvinglanguages.view.utils.AppCode;

public class TestActivity extends AppCompatActivity {

    private TestManager dataManager;
    private TestGame testManager;
    private Test currentTest;
    private TestReferenceState currentReference;

    private PronunciationManager pronunciationManager;

    private int round;
    private boolean testRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_test);

        dataManager = new TestManager(this);
        testManager = new TestGame(new DReferences());
        pronunciationManager = new PronunciationManager(this, dataManager.getCurrentLanguage().getLocale());

        int test_id = getIntent().getExtras().getInt(AppCode.TEST_ID);
        currentTest = dataManager.getTests().getTestById(test_id);

        testRunning = false;
        setMainFragment();
    }

    @Override
    public void onBackPressed() {
        if (!testRunning) {
            super.onBackPressed();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.msg_go_out_from_test)
                .setNegativeButton(R.string.go_out, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setMainFragment();
                    }
                })
                .setPositiveButton(R.string.continue_, null)
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataManager.updateTest(currentTest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pronunciationManager.destroy();
    }

    private void setFragment(TestReferenceState refState) {
        this.currentReference = refState;

        Fragment fragment = null;
        switch (refState.stage) {
            case DELVING:
                fragment = new TestDelvingFragment();
                break;
            case MATCH:
                fragment = new TestMatchFragment();
                break;
            case COMPLETE:
                fragment = new TestCompleteFragment();
                break;
            case WRITE:
                fragment = new TestWriteFragment();
                break;
            case LISTENING:
                fragment = new TestListeningFragment();
                break;
        }
        ((TestFragment) fragment).setHandler(handler);
        ((TestFragment) fragment).setReference(refState);

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.fragment_in, R.animator.fragment_out)
                .replace(R.id.content, fragment)
                .commit();
    }

    private void setMainFragment() {
        TestResultFragment fragment = new TestResultFragment();
        fragment.setData(handler, currentTest);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment)
                .commit();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == AppCode.TEST_START) {
                testRunning = true;
                round = 0;

                for (TestReferenceState refState : currentTest.references)
                    refState.stage = TestReferenceState.TestStage.DELVING;

                setFragment(testManager.nextTestReference(currentTest));
                return;
            }

            if (msg.what == AppCode.TEST_ROUND_PASSED)
                round++;

            if (round == currentTest.references.size() * 5) {
                currentTest.run_finished();
                testRunning = false;
                setMainFragment();
                return;
            }
            setFragment(testManager.nextTestReference(currentTest));
        }
    };

    public void onPronunciationAction(View v) {
        pronunciationManager.pronounce(currentReference.reference.name);
    }

}
