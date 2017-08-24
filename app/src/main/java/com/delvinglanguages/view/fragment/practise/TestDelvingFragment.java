package com.delvinglanguages.view.fragment.practise;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.test.TestReferenceState;
import com.delvinglanguages.kernel.util.Usages;
import com.delvinglanguages.view.lister.InflexionLister;
import com.delvinglanguages.view.utils.TestListener;

public class TestDelvingFragment extends TestFragment {

    public static TestDelvingFragment getInstance(Handler handler, TestReferenceState reference, Usages usages, TestReferenceState.TestStage nextStage)
    {
        TestDelvingFragment f = new TestDelvingFragment();
        f.reference = reference;
        f.usages = usages;
        f.handler = handler;
        f.nextStage = nextStage;
        return f;
    }

    private Usages usages;

    private InflexionLister adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.f_test_delving, container, false);

        Context context = getActivity();

        adapter = new InflexionLister(getActivity(), reference.reference.getInflexions(), usages, null);

        view.findViewById(R.id.actionContinue).setOnClickListener(onContinueAction);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        adapter.setNewDataSet(reference.reference.getInflexions());
        ((CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout)).setTitle(reference.reference.name);
        ((TextView) getActivity().findViewById(R.id.tv_pronunciation)).setText(reference.reference.pronunciation);
    }

    private View.OnClickListener onContinueAction = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            reference.stage = nextStage;
            handler.obtainMessage(TestListener.TEST_ROUND_PASSED).sendToTarget();
        }
    };

}