package com.delvinglanguages.face.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.delvinglanguages.R;
import com.delvinglanguages.face.activity.practice.CompleteActivity;
import com.delvinglanguages.face.activity.practice.ListeningActivity;
import com.delvinglanguages.face.activity.practice.PreguntasActivity;
import com.delvinglanguages.face.activity.practice.WriteWordsActivity;
import com.delvinglanguages.face.activity.test.Test_1_SelectActivity;
import com.delvinglanguages.listers.OptionLister;

public class PractiseFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.a_simple_list, container, false);

        setListAdapter((new OptionLister(getActivity(), getResources().getStringArray(R.array.prac_opt))));

        return view;
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Activity activity = getActivity();

        switch (position) {
            case 0:// Match
                startActivity(new Intent(activity, PreguntasActivity.class));
                break;
            case 1:// Complete
                startActivity(new Intent(activity, CompleteActivity.class));
                break;
            case 2:// Write
                startActivity(new Intent(activity, WriteWordsActivity.class));
                break;
            case 3:// Listening
                startActivity(new Intent(activity, ListeningActivity.class));
                break;
            case 4:// Test you
                startActivity(new Intent(activity, Test_1_SelectActivity.class));
                break;
        }
    }
}
