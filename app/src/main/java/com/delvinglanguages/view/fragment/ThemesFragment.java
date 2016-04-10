package com.delvinglanguages.view.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.theme.ThemeManager;
import com.delvinglanguages.view.activity.ThemeActivity;
import com.delvinglanguages.view.activity.ThemeEditorActivity;
import com.delvinglanguages.view.lister.ThemeLister;
import com.delvinglanguages.view.utils.AppCode;

public class ThemesFragment extends ListFragment implements OnClickListener {

    private ThemeLister adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_list_with_button, container, false);

        adapter = new ThemeLister(getActivity(), new ThemeManager(getActivity()).getThemes());
        setListAdapter(adapter);

        view.findViewById(R.id.button).setOnClickListener(this);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), ThemeActivity.class);
        intent.putExtra(AppCode.THEME_ID, adapter.getItem(position).id);

        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(getActivity(), ThemeEditorActivity.class), 0);
    }

}
