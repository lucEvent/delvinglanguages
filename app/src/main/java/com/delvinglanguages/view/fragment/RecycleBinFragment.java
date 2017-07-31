package com.delvinglanguages.view.fragment;

import android.app.Fragment;
import android.content.Context;
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
import com.delvinglanguages.kernel.DelvingListManager;
import com.delvinglanguages.kernel.util.RemovedItem;
import com.delvinglanguages.view.lister.RecycleBinLister;
import com.delvinglanguages.view.utils.NoContentViewHelper;

public class RecycleBinFragment extends Fragment {

    private DelvingListManager dataManager;
    private RecycleBinLister adapter;

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

        dataManager = new DelvingListManager(context);

        if (!dataManager.getRemovedItems().isEmpty()) {

            adapter = new RecycleBinLister(dataManager.getRemovedItems(), onRestoreItem);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setAutoMeasureEnabled(true);

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
            recyclerView.setHasFixedSize(false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

        } else
            displayNoContentMessage(view);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.add(Menu.NONE, R.id.clear_recycler_bin, 0, R.string.delete_all)
                .setIcon(R.drawable.ic_delete_all_white)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    private View.OnClickListener onRestoreItem = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            int position = dataManager.getRemovedItems().indexOf((RemovedItem) v.getTag());
            dataManager.restoreItem((RemovedItem) v.getTag());
            adapter.notifyItemRemoved(position);

            if (adapter.getItemCount() == 0)
                displayNoContentMessage(getView());
        }
    };

    private void displayNoContentMessage(View rootView)
    {
        new NoContentViewHelper(rootView.findViewById(R.id.no_content), R.string.msg_no_content_recycle_bin)
                .displayMessage();
    }

}

