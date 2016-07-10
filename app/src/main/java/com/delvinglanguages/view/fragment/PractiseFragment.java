package com.delvinglanguages.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delvinglanguages.R;
import com.delvinglanguages.view.activity.practise.PractiseCompleteActivity;
import com.delvinglanguages.view.activity.practise.PractiseListeningActivity;
import com.delvinglanguages.view.activity.practise.PractiseMatchActivity;
import com.delvinglanguages.view.activity.practise.PractiseTestActivity;
import com.delvinglanguages.view.activity.practise.PractiseWriteActivity;

public class PractiseFragment extends android.app.Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.f_practise, container, false);

        initViews(view);

        return view;
    }

    private void initViews(View parent)
    {
        parent.findViewById(R.id.practise_match).setOnClickListener(this);
        parent.findViewById(R.id.practise_complete).setOnClickListener(this);
        parent.findViewById(R.id.practise_write).setOnClickListener(this);
        parent.findViewById(R.id.practise_listening).setOnClickListener(this);
        parent.findViewById(R.id.practise_test).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Class practise_selected = null;
        switch (v.getId()) {
            case R.id.practise_match:
                practise_selected = PractiseMatchActivity.class;
                break;
            case R.id.practise_complete:
                practise_selected = PractiseCompleteActivity.class;
                break;
            case R.id.practise_write:
                practise_selected = PractiseWriteActivity.class;
                break;
            case R.id.practise_listening:
                practise_selected = PractiseListeningActivity.class;
                break;
            //TODO: 02/04/2016
            //case R.id.practise_pronunciation:
            //practise_selected = PractisePronunciationActivity.class;
            //break;
            case R.id.practise_test:
                practise_selected = PractiseTestActivity.class;
                break;
        }
        startActivity(new Intent(getActivity(), practise_selected));
    }

}
