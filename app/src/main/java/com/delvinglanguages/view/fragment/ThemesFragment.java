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
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.theme.ThemeManager;
import com.delvinglanguages.view.activity.ThemeActivity;
import com.delvinglanguages.view.activity.ThemeEditorActivity;
import com.delvinglanguages.view.lister.ThemeLister;

public class ThemesFragment extends Fragment implements OnClickListener {

    private ThemeLister adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.f_list_with_button, container, false);

        Context context = getActivity();

        adapter = new ThemeLister(new ThemeManager(context).getThemes(), onThemeClickListener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.button).setOnClickListener(this);

        return view;
    }

    private OnClickListener onThemeClickListener = new OnClickListener() {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(getActivity(), ThemeActivity.class);
            intent.putExtra(AppCode.THEME_ID, ((Theme) v.getTag()).id);

            startActivityForResult(intent, 0);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v)
    {
        startActivityForResult(new Intent(getActivity(), ThemeEditorActivity.class), 0);
    }

}
