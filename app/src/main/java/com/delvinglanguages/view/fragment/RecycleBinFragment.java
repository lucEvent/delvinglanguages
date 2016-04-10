package com.delvinglanguages.view.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.LanguageManager;
import com.delvinglanguages.view.lister.RecycleBinLister;

public class RecycleBinFragment extends ListFragment {

    private LanguageManager dataManager;
    private RecycleBinLister adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_list_with_button, container, false);

        dataManager = new LanguageManager(getActivity());

        view.findViewById(R.id.button).setVisibility(View.GONE);

        adapter = new RecycleBinLister(getActivity(), dataManager.getRemovedReferences(), onRestoreItem);
        setListAdapter(adapter);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, R.id.delete_all, 0, R.string.delete_all).setIcon(R.drawable.ic_delete_all);
    }

    private View.OnClickListener onRestoreItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dataManager.restoreReference((DReference) v.getTag());
            adapter.notifyDataSetChanged();
        }
    };

}

