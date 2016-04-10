package com.delvinglanguages.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.Inflexion;
import com.delvinglanguages.kernel.util.AppFormat;
import com.delvinglanguages.view.utils.AppCode;

public class AddTranslationDialog {

    private AlertDialog dialog;

    private Handler handler;

    private EditText in_translations, in_inflexions;

    private Button[] types;


    public AddTranslationDialog(Context context, Handler handler) {
        this.handler = handler;

        //Getting view
        View view = LayoutInflater.from(context).inflate(R.layout.d_add_translation, null);

        in_translations = (EditText) view.findViewById(R.id.in_translations);
        in_inflexions = (EditText) view.findViewById(R.id.in_inflexions);

        int[] i_types = new int[]{R.id.button_noun, R.id.button_verb, R.id.button_adjective,
                R.id.button_adverb, R.id.button_phrasal, R.id.button_expression,
                R.id.button_preposition, R.id.button_conjunction, R.id.button_other};
        types = new Button[i_types.length];

        for (int i = 0; i < types.length; i++) {
            types[i] = (Button) view.findViewById(i_types[i]);
            types[i].setOnClickListener(onTypeSelected);
        }

        dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(R.string.add, null)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearFields();
                    }
                })
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface di) {

                InputMethodManager imm = (InputMethodManager) dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
                }
                in_translations.setSelection(in_translations.getText().length());

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        addTranslation();
                    }
                });
            }
        });

    }


    public void show() {
        dialog.show();
        in_translations.requestFocus();
    }

    public void show(Inflexion inflexion) {
        in_translations.setText(AppFormat.arrayToString(inflexion.getTranslations()));
        in_inflexions.setText(AppFormat.arrayToString(inflexion.getInflexions()));
        setType(inflexion.getType());
        show();
    }

    private void addTranslation() {
        String translations = in_translations.getText().toString();
        if (translations.isEmpty()) {
            handler.obtainMessage(AppCode.MESSAGE_INT, R.string.msg_missing_translations).sendToTarget();
            return;
        }
        int type = 0;
        for (int i = 0; i < types.length; i++) {
            if (types[i].isSelected()) {
                type += (1 << i);
            }
        }
        if (type == 0) {
            handler.obtainMessage(AppCode.MESSAGE_INT, R.string.msg_missing_type).sendToTarget();
            return;
        }
        String[] a_translations = AppFormat.formatTranslation(translations);
        String[] a_inflexions = AppFormat.formatTranslation(in_inflexions.getText().toString());

        handler.obtainMessage(AppCode.INFLEXION_ADDED, new Inflexion(a_inflexions, a_translations, type)).sendToTarget();
        clearFields();
        dialog.dismiss();
    }

    private void clearFields() {
        in_translations.setText("");
        in_inflexions.setText("");
        setType(0);
    }

    private void setType(int type) {
        for (int i = 0; i < types.length; ++i) {
            if ((type & (1 << i)) != 0) {
                types[i].setSelected(true);
            } else if (types[i].isSelected()) {
                types[i].setSelected(false);
            }
        }
    }

    private View.OnClickListener onTypeSelected = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (Button b : types)
                if (b.isSelected())
                    b.setSelected(false);

            v.setSelected(true);
        }
    };

}
