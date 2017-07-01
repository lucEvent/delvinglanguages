package com.delvinglanguages.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.LanguageManager;
import com.delvinglanguages.view.activity.ReferenceEditorActivity;
import com.delvinglanguages.view.lister.DrawerLister;
import com.delvinglanguages.view.utils.ListItemSwipeCallback;
import com.delvinglanguages.view.utils.ListItemSwipeListener;

public class DrawerFragment extends Fragment implements TextView.OnEditorActionListener, ListItemSwipeListener {

    private EditText input;
    private DrawerLister adapter;

    private LanguageManager dataManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Context context = getActivity();
        View view = inflater.inflate(R.layout.f_drawer, container, false);

        dataManager = new LanguageManager(context);

        adapter = new DrawerLister(context, dataManager.getDrawerReferences(), onItemClick);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new ListItemSwipeCallback(this, (TextView) view.findViewById(R.id.swipe_message));
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        input = (EditText) view.findViewById(R.id.input);
        input.setOnEditorActionListener(this);

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private View.OnClickListener onItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(getActivity(), ReferenceEditorActivity.class);
            intent.putExtra(AppCode.ACTION, ReferenceEditorActivity.ACTION_CREATE_FROM_DRAWER);
            intent.putExtra(AppCode.DRAWER_ID, (Integer) v.getTag());
            startActivity(intent);
        }
    };

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        String in = input.getText().toString().trim();
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

    @Override
    public void onItemDismiss(int position)
    {
        dataManager.deleteReference(dataManager.getDrawerReferences().get(position));
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition)
    {
    }

}
