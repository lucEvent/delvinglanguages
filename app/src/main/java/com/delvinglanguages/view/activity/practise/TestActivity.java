package com.delvinglanguages.view.activity.practise;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.LanguageCode;
import com.delvinglanguages.kernel.RecordManager;
import com.delvinglanguages.kernel.game.TestGame;
import com.delvinglanguages.kernel.manager.PronunciationManager;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.test.TestManager;
import com.delvinglanguages.kernel.test.TestReferenceState;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.view.fragment.practise.TestCompleteFragment;
import com.delvinglanguages.view.fragment.practise.TestDelvingFragment;
import com.delvinglanguages.view.fragment.practise.TestListeningFragment;
import com.delvinglanguages.view.fragment.practise.TestMatchFragment;
import com.delvinglanguages.view.fragment.practise.TestResultFragment;
import com.delvinglanguages.view.fragment.practise.TestWriteFragment;
import com.delvinglanguages.view.utils.TestHandler;
import com.delvinglanguages.view.utils.TestListener;

public class TestActivity extends AppCompatActivity implements TestListener {

    private TestManager dataManager;
    private TestGame testManager;
    private Test currentTest;
    private TestReferenceState currentReference;
    private boolean showPhrasal;

    private Handler handler;
    private PronunciationManager pronunciationManager;

    private int round;
    private boolean testRunning;
    private boolean ttsAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_test);

        handler = new TestHandler(this);
        dataManager = new TestManager(this);
        testManager = new TestGame(new DReferences());
        pronunciationManager = new PronunciationManager(this, LanguageCode.getLocale(dataManager.getCurrentList().from_code), false);

        showPhrasal = dataManager.getCurrentList().arePhrasalVerbsEnabled();

        int test_id = getIntent().getExtras().getInt(AppCode.TEST_ID);
        currentTest = dataManager.getTests().getTestById(test_id);

        testRunning = false;
        setMainFragment();
    }

    @Override
    public void onBackPressed()
    {
        if (!testRunning) {
            super.onBackPressed();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.msg_go_out_from_test)
                .setMessage(R.string.msg_progress_wont_save)
                .setNegativeButton(R.string.go_out, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        testRunning = false;
                        setMainFragment();
                    }
                })
                .setPositiveButton(R.string.continue_, null)
                .show();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        dataManager.updateTest(currentTest);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        pronunciationManager.destroy();
    }

    private void setFragment(TestReferenceState refState)
    {
        this.currentReference = refState;
        ttsAvailable = pronunciationManager.isLanguageAvailable();

        Fragment fragment = null;
        switch (refState.stage) {
            case DELVING:
                fragment = TestDelvingFragment.getInstance(handler, refState, TestReferenceState.TestStage.MATCH);
                break;
            case MATCH:
                fragment = TestMatchFragment.getInstance(handler, refState, TestReferenceState.TestStage.COMPLETE, showPhrasal);
                break;
            case COMPLETE:
                fragment = TestCompleteFragment.getInstance(handler, refState, TestReferenceState.TestStage.WRITE, showPhrasal);
                break;
            case WRITE:
                fragment = TestWriteFragment.getInstance(handler, refState, ttsAvailable ? TestReferenceState.TestStage.LISTENING : TestReferenceState.TestStage.END, showPhrasal);
                break;
            case LISTENING:
                fragment = TestListeningFragment.getInstance(handler, refState, TestReferenceState.TestStage.END, showPhrasal);
                break;
        }

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.fragment_in, R.animator.fragment_out)
                .replace(R.id.content, fragment)
                .commit();
    }

    private void setMainFragment()
    {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content, TestResultFragment.getInstance(handler, currentTest, showPhrasal))
                .commit();
    }

    @Override
    public void onTestRemoved()
    {
        dataManager.removeTest(currentTest);
        setResult(TestListener.TEST_REMOVED);
        finish();
    }

    @Override
    public void onTestStart()
    {
        testRunning = true;
        round = 0;

        currentTest.start();
        for (TestReferenceState refState : currentTest.states)
            refState.stage = TestReferenceState.TestStage.DELVING;

        setFragment(testManager.nextTestReference(currentTest));
    }

    @Override
    public void onTestRoundPassed()
    {
        round++;
        nextStep();
    }

    @Override
    public void onTestRoundSkipped()
    {
        nextStep();
    }

    private void nextStep()
    {
        if (round >= currentTest.size() * (ttsAvailable ? 5 : 4)) {
            currentTest.finish();
            testRunning = false;
            RecordManager.testDone(dataManager.getCurrentList().id, dataManager.getCurrentList().from_code, currentTest.id);
            setMainFragment();
            return;
        }
        setFragment(testManager.nextTestReference(currentTest));
    }

    public void onPronunciationAction(View v)
    {
        if (ttsAvailable)
            pronunciationManager.pronounce(currentReference.reference.name);
        else
            Toast.makeText(this, R.string.msg_pronunciation_not_available, Toast.LENGTH_SHORT).show();
    }

}
