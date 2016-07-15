package com.delvinglanguages.view.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.LanguageManager;
import com.delvinglanguages.view.activity.ReferenceEditorActivity;
import com.delvinglanguages.view.lister.DrawerLister;

public class DrawerFragment extends ListFragment implements TextView.OnEditorActionListener {

    private EditText input;
    private DrawerLister adapter;

    private LanguageManager dataManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.f_drawer, container, false);

        initViews(view);

        dataManager = new LanguageManager(getActivity());

        adapter = new DrawerLister(getActivity(), dataManager.getDrawerReferences());
        setListAdapter(adapter);

        return view;
    }

    private void initViews(View parent)
    {
        input = (EditText) parent.findViewById(R.id.input);
        input.setOnEditorActionListener(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(getActivity(), ReferenceEditorActivity.class);
        intent.putExtra(AppCode.ACTION, ReferenceEditorActivity.ACTION_CREATE_FROM_DRAWER);
        intent.putExtra(AppCode.DRAWER_ID, adapter.getItem(position).id);
        startActivityForResult(intent, 0);
        // TODO: 30/03/2016 Treat the result
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        String in = input.getText().toString();
        if (in.length() == 0) {
            Toast.makeText(getActivity(), R.string.msg_missing_word, Toast.LENGTH_SHORT).show();
        } else {
            if (dataManager.getReference(in) != null) {
                String message = String.format(getString(R.string.msg_word_exists), in);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            } else {
                dataManager.createReference(in);
                adapter.notifyDataSetChanged();
                input.setText("");
            }
        }
        return true;
    }

}
