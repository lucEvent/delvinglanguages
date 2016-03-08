package com.delvinglanguages.face.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.listers.RemovedWordLister;

public class BinFragment extends ListFragment implements OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.a_list_with_button, container, false);
        Activity activity = getActivity();

        setListAdapter(new RemovedWordLister(activity, LanguageKernelControl.getRemovedWords()));

        Button action = ((Button) view.findViewById(R.id.action));
        action.setText(getString(R.string.emptybin));
        action.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.emptyingbin);
        builder.setMessage(R.string.clearbinquestion);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                clear();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    private void clear() {
        KernelControl.deleteAllRemovedWords();
        Toast.makeText(getActivity(), R.string.trashemptied, Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

}
