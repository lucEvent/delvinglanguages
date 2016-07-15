package com.delvinglanguages.view.fragment.practise;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.view.lister.ReferenceLister;
import com.delvinglanguages.view.utils.TestListener;

public class TestResultFragment extends android.app.Fragment {

    public static TestResultFragment getInstance(Handler handler, Test test)
    {
        TestResultFragment f = new TestResultFragment();
        f.handler = handler;
        f.test = test;
        return f;
    }

    private Handler handler;
    private Test test;
    private ReferenceLister adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.f_test, container, false);

        Context context = getActivity();

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
        public void onClick(View v)
        {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.msg_confirm_to_delete_xxx)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            onConfirmDelete();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .create()
                    .show();
        }
    };

    private void onConfirmDelete()
    {
        handler.obtainMessage(TestListener.TEST_DELETED).sendToTarget();
    }

    private View.OnClickListener onStartTestAction = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            handler.obtainMessage(TestListener.TEST_START).sendToTarget();
        }
    };

    private View.OnLongClickListener onDeleteLongPress = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v)
        {
            showMenuItemToast(v, R.string.delete);
            return true;
        }
    };

    private View.OnLongClickListener onStartTestLongPress = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v)
        {
            showMenuItemToast(v, R.string.start);
            return true;
        }
    };

    private void showMenuItemToast(View v, int msg_id)
    {
        Toast toast = Toast.makeText(v.getContext(), msg_id, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.START, v.getLeft(), v.getTop() + v.getHeight());
        toast.show();
    }

}
