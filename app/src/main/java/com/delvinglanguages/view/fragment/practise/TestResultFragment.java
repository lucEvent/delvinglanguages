package com.delvinglanguages.view.fragment.practise;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.f_test, container, false);

        Context context = getActivity();

        adapter = new ReferenceLister(test.getReferences(), true, null);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

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

    @Override
    public void onResume()
    {
        super.onResume();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.setTitle(test.name);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.add(Menu.NONE, R.id.delete, 0, R.string.delete)
                .setIcon(R.drawable.ic_delete_white)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(Menu.NONE, R.id.start, 1, R.string.start)
                .setIcon(R.drawable.ic_play_white)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
            case R.id.delete:
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
                break;
            case R.id.start:
                handler.obtainMessage(TestListener.TEST_START).sendToTarget();
        }
        return true;
    }

    private void onConfirmDelete()
    {
        handler.obtainMessage(TestListener.TEST_DELETED).sendToTarget();
    }

}
