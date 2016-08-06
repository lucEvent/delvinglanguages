package com.delvinglanguages.view.fragment.practise;

import android.app.Fragment;
import android.os.Handler;

import com.delvinglanguages.kernel.test.TestReferenceState;

public abstract class TestFragment extends Fragment {

    protected Handler handler;
    protected TestReferenceState reference;

    protected TestReferenceState.TestStage nextStage;

    public TestFragment()
    {
        super();
    }

}
