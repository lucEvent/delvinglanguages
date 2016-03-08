package com.delvinglanguages.face.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.delvinglanguages.R;
import com.delvinglanguages.face.AppCode;
import com.delvinglanguages.face.activity.ReferenceActivity;
import com.delvinglanguages.face.activity.add.AddWordFromVerbActivity;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.set.DReferences;
import com.delvinglanguages.listers.ReferenceLister;

public class VerbsFragment extends ListFragment implements OnClickListener {

    private DReferences verbslist;

    private boolean phMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.a_list_with_button, container, false);
        
        verbslist = LanguageKernelControl.getVerbs();
        phMode = LanguageKernelControl.getLanguageSettings(Language.MASK_PH);
        setListAdapter(new ReferenceLister(getActivity(), verbslist, phMode));

        Button action = ((Button) view.findViewById(R.id.action));
        action.setText(getString(R.string.addverb));
        action.setOnClickListener(this);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        super.onListItemClick(l, v, pos, id);
        Intent intent = new Intent(getActivity(), ReferenceActivity.class);
        intent.putExtra(AppCode.DREFERENCE, verbslist.get(pos).name);
        startActivityForResult(intent, AppCode.ACTION_MODIFY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppCode.ACTION_MODIFY) {
            if (resultCode == Activity.RESULT_OK) {
                verbslist = LanguageKernelControl.getVerbs();
                setListAdapter(new ReferenceLister(getActivity(), verbslist, phMode));
            }
        }
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), AddWordFromVerbActivity.class));
    }

}
