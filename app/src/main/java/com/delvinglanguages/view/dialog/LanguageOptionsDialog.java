package com.delvinglanguages.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.delvinglanguages.R;

public class LanguageOptionsDialog {

    public AlertDialog dialog;

    public LanguageOptionsDialog(Context context, View.OnClickListener listener, boolean enablePhrasal)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.d_more_language_options, null);

        view.findViewById(R.id.option_themes).setOnClickListener(listener);
        if (!enablePhrasal)
            view.findViewById(R.id.option_phrasal_verbs).setVisibility(LinearLayout.GONE);
        else
            view.findViewById(R.id.option_phrasal_verbs).setOnClickListener(listener);

        view.findViewById(R.id.option_verbs).setVisibility(LinearLayout.GONE);
        //   view.findViewById(R.id.option_verbs).setOnClickListener(listener);

        view.findViewById(R.id.option_web_search).setOnClickListener(listener);
        view.findViewById(R.id.option_pronunciation).setOnClickListener(listener);
        view.findViewById(R.id.option_recycle_bin).setOnClickListener(listener);
        view.findViewById(R.id.option_settings).setOnClickListener(listener);

        dialog = new AlertDialog.Builder(context)
                .setView(view)
                .create();
    }

    public void show()
    {
        dialog.show();
    }

}
