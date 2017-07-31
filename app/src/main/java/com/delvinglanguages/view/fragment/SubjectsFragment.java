package com.delvinglanguages.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.subject.Subject;
import com.delvinglanguages.kernel.subject.SubjectManager;
import com.delvinglanguages.view.activity.SubjectActivity;
import com.delvinglanguages.view.activity.SubjectEditorActivity;
import com.delvinglanguages.view.lister.SubjectLister;
import com.delvinglanguages.view.utils.NoContentViewHelper;

public class SubjectsFragment extends Fragment implements OnClickListener {

    private SubjectLister adapter;
    private NoContentViewHelper noContentViewHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.f_list_with_button, container, false);

        Context context = getActivity();

        adapter = new SubjectLister(new SubjectManager(context).getSubjects(), onSubjectClickListener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.button).setOnClickListener(this);

        noContentViewHelper = new NoContentViewHelper(view.findViewById(R.id.no_content), R.string.msg_no_content_subjects);
        if (adapter.getItemCount() == 0)
            noContentViewHelper.displayMessage();

        return view;
    }

    private OnClickListener onSubjectClickListener = new OnClickListener() {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(getActivity(), SubjectActivity.class);
            intent.putExtra(AppCode.SUBJECT_ID, ((Subject) v.getTag()).id);

            startActivityForResult(intent, 0);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.notifyDataSetChanged();

        if (adapter.getItemCount() > 0)
            noContentViewHelper.hide();
        else
            noContentViewHelper.displayMessage();
    }

    @Override
    public void onClick(View v)
    {
        startActivityForResult(new Intent(getActivity(), SubjectEditorActivity.class), 0);
    }

}
