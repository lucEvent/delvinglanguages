package com.delvinglanguages.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DrawerReference;
import com.delvinglanguages.kernel.Inflexion;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.LanguageManager;
import com.delvinglanguages.kernel.util.Inflexions;
import com.delvinglanguages.view.dialog.AddTranslationDialog;
import com.delvinglanguages.view.lister.InflexionEditLister;
import com.delvinglanguages.view.lister.InflexionLister;
import com.delvinglanguages.view.utils.PhoneticKeyboard;
import com.delvinglanguages.view.utils.ReferenceHandler;
import com.delvinglanguages.view.utils.ReferenceListener;

public class ReferenceEditorActivity extends AppCompatActivity implements ReferenceListener {

    public static final int ACTION_CREATE = 0;
    public static final int ACTION_CREATE_FROM_DRAWER = 1;
    public static final int ACTION_MODIFY = 2;
    public static final int ACTION_SEARCH = 3;

    private static class EditorData {

        int action;
        DrawerReference drawerReference;
        DReference reference;
        Inflexions inflexions;

        Inflexion editingInflexion;

    }

    private EditorData data;

    private LanguageManager dataManager;

    private EditText in_reference, in_pronunciation;

    private PhoneticKeyboard phonetic_keyboard;

    private InflexionLister adapter;

    private AddTranslationDialog inflexionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_reference_editor);

        data = new EditorData();
        dataManager = new LanguageManager(this);

        in_reference = (EditText) findViewById(R.id.in_reference);
        in_pronunciation = (EditText) findViewById(R.id.in_pronunciation);

        phonetic_keyboard = new PhoneticKeyboard(this, in_pronunciation, dataManager.getCurrentLanguage().code);

        Bundle bundle = getIntent().getExtras();
        data.action = bundle.getInt(AppCode.ACTION);
        switch (data.action) {
            case ACTION_CREATE_FROM_DRAWER:
                int id = bundle.getInt(AppCode.DRAWER_ID);
                data.drawerReference = dataManager.getCurrentLanguage().drawer_references.getReferenceById(id);
                data.inflexions = new Inflexions();

                in_reference.setText(data.drawerReference.name);
                in_reference.setSelection(data.drawerReference.name.length());

            case ACTION_CREATE:
                data.inflexions = new Inflexions();

                break;
            case ACTION_MODIFY:
                String ref_name = bundle.getString(AppCode.DREFERENCE_NAME);
                data.reference = dataManager.getCurrentLanguage().getReference(ref_name);
                data.inflexions = data.reference.getInflexions().clone();

                in_reference.setText(data.reference.name);
                in_reference.setSelection(data.reference.name.length());
                in_pronunciation.setText(data.reference.pronunciation);
                break;
            case ACTION_SEARCH:
                ref_name = bundle.getString(AppCode.DREFERENCE_NAME);
                in_reference.setText(ref_name);
                in_reference.setSelection(ref_name.length());

                data.inflexions = Inflexions.fromWrapper(bundle.getString(AppCode.INFLEXIONS_WRAPPER));
        }

        adapter = new InflexionEditLister(this, data.inflexions, onModifyInflexion, onDeleteInflexion);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        inflexionManager = new AddTranslationDialog(this, new ReferenceHandler(this), dataManager.getCurrentLanguage().getSetting(Language.MASK_PHRASAL_VERBS));
    }

    @Override
    public void onBackPressed()
    {
        if (phonetic_keyboard.isVisible())
            phonetic_keyboard.hide();
        else
            actionCancel(null);
    }

    public void actionAddTranslation(View v)
    {
        data.editingInflexion = null;
        inflexionManager.show();
        phonetic_keyboard.hide();
    }

    public void actionSave(View v)
    {
        String reference = in_reference.getText().toString();
        if (reference.isEmpty()) {
            showMessage(R.string.msg_missing_word);
            return;
        }
        String pronunciation = in_pronunciation.getText().toString();

        if (data.inflexions.isEmpty()) {
            showMessage(R.string.msg_missing_translations);
            return;
        }

        switch (data.action) {
            case ACTION_CREATE:
            case ACTION_SEARCH:
                dataManager.createReference(reference, pronunciation, data.inflexions);
                setResult(AppCode.DREFERENCE_CREATED);
                break;
            case ACTION_CREATE_FROM_DRAWER:
                dataManager.createReference(data.drawerReference, reference, pronunciation, data.inflexions);
                setResult(AppCode.DREFERENCE_CREATED);
                break;
            case ACTION_MODIFY:
                dataManager.updateReference(data.reference, reference, pronunciation, data.inflexions);
                setResult(AppCode.DREFERENCE_UPDATED);
                finish();
                return;
        }
        finish();

        Intent intent = new Intent(this, DReferenceActivity.class);
        intent.putExtra(AppCode.DREFERENCE_NAME, reference);
        startActivity(intent);
    }

    public void actionCancel(View v)
    {
        new AlertDialog.Builder(this)
                .setTitle(R.string.msg_go_back_without_saving)
                .setNegativeButton(R.string.go_out, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        ReferenceEditorActivity.this.finish();
                    }
                })
                .setPositiveButton(R.string.continue_, null)
                .show();
    }

    private View.OnClickListener onModifyInflexion = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            data.editingInflexion = (Inflexion) v.getTag();
            inflexionManager.show(data.editingInflexion);
            phonetic_keyboard.hide();
        }
    };

    private View.OnClickListener onDeleteInflexion = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
// TODO: 28/03/2016 Preguntar confirmacion!
            Inflexion inflexion = (Inflexion) v.getTag();
            int index = data.inflexions.indexOf(inflexion);
            data.inflexions.remove(index);
            adapter.notifyItemRemoved(index);
        }
    };

    @Override
    public void onInflexionAdded(Inflexion inflexion)
    {
        if (data.editingInflexion != null) {
            data.editingInflexion.setTranslations(inflexion.getTranslations());
            data.editingInflexion.setInflexions(inflexion.getInflexions());
            data.editingInflexion.setType(inflexion.getType());

            adapter.notifyItemChanged(data.inflexions.indexOf(data.editingInflexion));

            data.editingInflexion = null;
        } else {
            data.inflexions.add(inflexion);
            adapter.notifyItemInserted(data.inflexions.size());
        }
    }

    @Override
    public void onMessage(int res_id)
    {
        showMessage(res_id);
    }

    @Override
    public void onMessage(String msg)
    {
        android.widget.Toast.makeText(this, msg, android.widget.Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError()
    {

    }

    protected void showMessage(int text)
    {
        android.widget.Toast.makeText(this, text, android.widget.Toast.LENGTH_SHORT).show();
    }

}
