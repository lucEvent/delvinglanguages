package com.delvinglanguages.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.LanguageManager;
import com.delvinglanguages.kernel.MergeManager;
import com.delvinglanguages.kernel.util.Languages;

// // TODO: 19/08/2016 This activity must implement a asynctask
public class LanguageMergerActivity extends Activity {

    private Language language_src;
    private Language language_dst;

    private Languages candidates;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_language_merger);

        KernelManager dataManager = new KernelManager(this);
        language_src = dataManager.getCurrentLanguage();

        candidates = new Languages(dataManager.getLanguages());
        candidates.remove(language_src);

        int index = 0;
        String[] languages = new String[candidates.size()];
        for (Language l : candidates)
            languages[index++] = l.language_name;

        new AlertDialog.Builder(this)
                .setTitle(R.string.msg_select_language_to_merge_with)
                .setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, languages), onLanguageDstSelected)
                .setCancelable(false)
                .setNegativeButton(R.string.cancel, onCancel)
                .create()
                .show();
    }

    private DialogInterface.OnClickListener onCancel = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            LanguageMergerActivity.this.finish();
        }
    };

    private DialogInterface.OnClickListener onLanguageDstSelected = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            estimateMergingWith(candidates.get(which));
        }
    };

    private MergeManager.MergePlan mergePlan;

    private void estimateMergingWith(Language language)
    {
        language_dst = language;
        final ProgressDialog progressDialog = ProgressDialog.show(this,
                getString(R.string.msg_estimating_merge_of, language_src.language_name, language_dst.language_name),
                getString(R.string.msg_be_patience));
        progressDialog.setCanceledOnTouchOutside(false);

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                mergePlan = estimateMerging(language_src, language_dst, progressDialog);

                handler.post(new Runnable() {
                    @Override
                    public void run()
                    {
                        AlertDialog.Builder dbuilder = new AlertDialog.Builder(LanguageMergerActivity.this)
                                .setCancelable(false)
                                .setNegativeButton(R.string.cancel, onCancel)
                                .setMessage(R.string.msg_temp_conflicts_treatment)
                                .setPositiveButton(R.string.continue_, onMergeConfirmed);

                        if (mergePlan.num_conflicts == 0)
                            dbuilder.setTitle(R.string.msg_no_conflicts_found);
                        else
                            dbuilder.setTitle(getString(R.string.msg_conflicts_need_supervision, mergePlan.num_conflicts));

                        dbuilder.create().show();
                    }
                });
            }
        }).start();
    }

    private Handler handler = new Handler();

    private MergeManager.MergePlan estimateMerging(final Language src, final Language dst, final ProgressDialog dialog)
    {
        MergeManager manager = new MergeManager(this);

        handler.post(new Runnable() {
            @Override
            public void run()
            {
                dialog.setMessage(getString(R.string.msg_reading, src.language_name));
            }
        });
        manager.getLanguageContent(src);

        handler.post(new Runnable() {
            @Override
            public void run()
            {
                dialog.setMessage(getString(R.string.msg_reading, dst.language_name));
            }
        });
        manager.getLanguageContent(dst);

        handler.post(new Runnable() {
            @Override
            public void run()
            {
                dialog.setMessage(getString(R.string.msg_calculating_changes));
            }
        });
        MergeManager.MergePlan mergePlan = manager.createMergePlan(src, dst);

        dialog.dismiss();

        return mergePlan;
    }

    private DialogInterface.OnClickListener onMergeConfirmed = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            handler.post(new Runnable() {
                @Override
                public void run()
                {

                    final ProgressDialog progressDialog = ProgressDialog.show(LanguageMergerActivity.this,
                            getString(R.string.msg_merging), getString(R.string.msg_be_patience));
                    progressDialog.setCanceledOnTouchOutside(false);

                    new Thread(new Runnable() {
                        @Override
                        public void run()
                        {
                            merge(language_dst, mergePlan);

                            progressDialog.dismiss();

                            handler.post(new Runnable() {
                                @Override
                                public void run()
                                {
                                    dialogRemoveCurrentLanguage();
                                }
                            });
                        }
                    }).start();
                }
            });
        }
    };

    private void merge(Language dst, MergeManager.MergePlan mergePlan)
    {
        MergeManager manager = new MergeManager(this);
        manager.merge(dst, mergePlan);
        dst.clear();
    }

    private void dialogRemoveCurrentLanguage()
    {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.msg_remove_language_after_merge))
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        new LanguageManager(getApplicationContext()).deleteLanguage();
                        LanguageMergerActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.no, onCancel)
                .create()
                .show();
    }

}
