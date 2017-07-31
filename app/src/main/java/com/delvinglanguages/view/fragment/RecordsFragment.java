package com.delvinglanguages.view.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.RecordManager;
import com.delvinglanguages.view.lister.RecordLister;
import com.delvinglanguages.view.utils.NoContentViewHelper;

public class RecordsFragment extends Fragment {

    private RecordLister adapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.f_list, container, false);

        Context context = getActivity();

        if (!RecordManager.getRecords().isEmpty()) {

            adapter = new RecordLister(RecordManager.getRecords());

            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setAutoMeasureEnabled(true);

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

        } else
            displayNoContentMessage(view);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.add(Menu.NONE, R.id.clear_records, 0, R.string.delete_all)
                .setIcon(R.drawable.ic_delete_all_white)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.clear_records:
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.msg_confirm_to_clear_all_items)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                RecordManager.clearRecords();
                                adapter.clear();
                                displayNoContentMessage(getView());
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .create().show();
        }
        return true;
    }

    private void displayNoContentMessage(View rootView)
    {
        new NoContentViewHelper(rootView.findViewById(R.id.no_content), R.string.msg_no_content_records)
                .displayMessage();
    }

}
