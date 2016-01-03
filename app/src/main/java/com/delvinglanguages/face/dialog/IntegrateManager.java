package com.delvinglanguages.face.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.face.activity.IntegrateRepeatedActivity;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.set.Languages;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.listers.StringLister;

import java.util.ArrayList;

public class IntegrateManager extends Builder implements OnItemClickListener {

    private Activity context;

    private AlertDialog currentDialog;

    public IntegrateManager(Activity context) {
        super(context);
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.d_integrate_1, null, false);

        ListView list = (ListView) view.findViewById(R.id.langlist);

        Languages langs = KernelControl.getLanguages();
        ArrayList<String> values = new ArrayList<String>();
        for (int i = 0; i < langs.size(); ++i) {
            if (KernelControl.getCurrentLanguage() != langs.get(i)) {
                values.add(langs.get(i).language_delved_name);
            }
        }

        list.setAdapter(new StringLister(context, values));
        list.setOnItemClickListener(this);

        setTitle(R.string.integratinglang);
        setMessage(R.string.exp_integrate1);
        setView(view);
    }

    public void start() {
        currentDialog = show();
    }

    /**
     * ****************ON ITEM CLICK LISTENER ***********************
     **/
    @Override
    public void onItemClick(AdapterView<?> ad, View view, final int pos, long id) {
        currentDialog.dismiss();

        setMessage(R.string.exp_integrate2);
        setView(null);
        setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                int poslang = pos;
                if (pos >= KernelControl.getLanguages().indexOf(KernelControl.getCurrentLanguage())) {
                    poslang = pos + 1;
                }
                startintegration(poslang);
            }

        });
        setNegativeButton(R.string.cancel, null);
        currentDialog = show();
    }

    private void startintegration(int position) {
        currentDialog.dismiss();

        Words repited = KernelControl.integrateLanguage(position);

        if (repited.size() == 0) {
            // Cerrar dialog y acabar activity y eliminar idioma
            showMessage(R.string.languageintegrated);
            KernelControl.deleteLanguage();
            context.finish();
            return;
        }

        // Cambiar el mensage
        String message = context.getResources().getString(R.string.exp_integrate3);
        setMessage((repited.size() >> 1) + " " + message);
        setPositiveButton(R.string.handle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                context.startActivity(new Intent(context, IntegrateRepeatedActivity.class));
                context.finish();
            }
        });
        setNegativeButton(R.string.ignore, null);
        show();

    }

    private void showMessage(int text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
