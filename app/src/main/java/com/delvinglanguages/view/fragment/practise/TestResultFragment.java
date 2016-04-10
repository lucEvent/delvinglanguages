package com.delvinglanguages.view.fragment.practise;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.test.TestManager;
import com.delvinglanguages.view.lister.ReferenceLister;
import com.delvinglanguages.view.utils.AppCode;

public class TestResultFragment extends android.app.Fragment {

    public TestResultFragment() {
        super();
    }

    public void setData(Handler handler, Test test) {
        this.handler = handler;
        this.test = test;
    }

    private Handler handler;
    private Test test;
    private TestManager dataManager;
    private ReferenceLister adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_test, container, false);

        Context context = getActivity();

        dataManager = new TestManager(context);
        adapter = new ReferenceLister(test.getReferences(), true, null);

        View button_delete = view.findViewById(R.id.button_delete);
        View button_start_test = view.findViewById(R.id.button_start_test);
        button_delete.setOnClickListener(onDeleteAction);
        button_start_test.setOnClickListener(onStartTestAction);
        button_delete.setOnLongClickListener(onDeleteLongPress);
        button_start_test.setOnLongClickListener(onStartTestLongPress);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ((TextView) view.findViewById(R.id.test_name)).setText(test.name);


        if (!test.hasRun())
            ((TextView) view.findViewById(R.id.content_test_stats)).setText(R.string.msg_test_hasnot_run_yet);

        else {

            StringBuilder testStats = new StringBuilder();

            testStats.append(getActivity().getString(R.string.msg_test_done_times, test.getRunTimes())).append("\n");
            testStats.append(getActivity().getString(R.string.msg_test_accuracy, test.getAccuracy())).append("\n");
            testStats.append(getActivity().getString(R.string.msg_test_best_reference, test.getBestReferenceName())).append("\n");
            testStats.append(getActivity().getString(R.string.msg_test_worst_reference, test.getWorstReferenceName()));

            ((TextView) view.findViewById(R.id.content_test_stats)).setText(testStats);
        }
        return view;
    }

    private View.OnClickListener onDeleteAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.msg_confirm_to_delete_test, test.name))
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            onConfirmDelete();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .create()
                    .show();
        }
    };

    private void onConfirmDelete() {
        dataManager.deleteTest(test);
        getActivity().setResult(AppCode.TEST_DELETED);
        getActivity().finish();
    }

    private View.OnClickListener onStartTestAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handler.obtainMessage(AppCode.TEST_START).sendToTarget();
        }
    };

    private View.OnLongClickListener onDeleteLongPress = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            // TODO: 05/04/2016
            return false;
        }
    };

    private View.OnLongClickListener onStartTestLongPress = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            // TODO: 05/04/2016
            return false;
        }
    };


}
