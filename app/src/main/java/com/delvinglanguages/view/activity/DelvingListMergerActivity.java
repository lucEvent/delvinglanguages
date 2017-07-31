package com.delvinglanguages.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DelvingList;
import com.delvinglanguages.kernel.DelvingListManager;
import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.kernel.MergeManager;
import com.delvinglanguages.kernel.util.DelvingLists;
import com.delvinglanguages.view.utils.DelvingListListener;

// // TODO: 19/08/2016 This activity must implement a asynctask
public class DelvingListMergerActivity extends Activity {

    private DelvingList list_src;
    private DelvingList list_dst;

    private DelvingLists candidates;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_delving_list_merger);

        KernelManager dataManager = new KernelManager(this);
        list_src = dataManager.getCurrentList();

        candidates = new DelvingLists(dataManager.getDelvingLists());
        candidates.remove(list_src);

        int index = 0;
        String[] lists = new String[candidates.size()];
        for (DelvingList l : candidates)
            lists[index++] = l.name;

        new AlertDialog.Builder(this)
                .setTitle(R.string.msg_select_language_to_merge_with)
                .setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lists), onDelvingListDstSelected)
                .setCancelable(false)
                .setNegativeButton(R.string.cancel, onCancel)
                .create()
                .show();
    }

    private DialogInterface.OnClickListener onCancel = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            DelvingListMergerActivity.this.finish();
        }
    };

    private DialogInterface.OnClickListener onDelvingListDstSelected = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            estimateMergingWith(candidates.get(which));
        }
    };

    private MergeManager.MergePlan mergePlan;

    private void estimateMergingWith(DelvingList delvingList)
    {
        list_dst = delvingList;
        final ProgressDialog progressDialog = ProgressDialog.show(this,
                getString(R.string.msg_estimating_merge_of, list_src.name, list_dst.name),
                getString(R.string.msg_be_patience));
        progressDialog.setCanceledOnTouchOutside(false);

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                mergePlan = estimateMerging(list_src, list_dst, progressDialog);

                handler.post(new Runnable() {
                    @Override
                    public void run()
                    {
                        AlertDialog.Builder dbuilder = new AlertDialog.Builder(DelvingListMergerActivity.this)
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

    private MergeManager.MergePlan estimateMerging(final DelvingList src, final DelvingList dst, final ProgressDialog dialog)
    {
        MergeManager manager = new MergeManager(this);

        handler.post(new Runnable() {
            @Override
            public void run()
            {
                dialog.setMessage(getString(R.string.msg_reading, src.name));
            }
        });
        manager.getDelvingListContent(src);

        handler.post(new Runnable() {
            @Override
            public void run()
            {
                dialog.setMessage(getString(R.string.msg_reading, dst.name));
            }
        });
        manager.getDelvingListContent(dst);

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

                    final ProgressDialog progressDialog = ProgressDialog.show(DelvingListMergerActivity.this,
                            getString(R.string.msg_merging), getString(R.string.msg_be_patience));
                    progressDialog.setCanceledOnTouchOutside(false);

                    new Thread(new Runnable() {
                        @Override
                        public void run()
                        {
                            merge(list_dst, mergePlan);

                            progressDialog.dismiss();

                            handler.post(new Runnable() {
                                @Override
                                public void run()
                                {
                                    dialogRemoveCurrentList();
                                }
                            });
                        }
                    }).start();
                }
            });
        }
    };

    private void merge(DelvingList dst, MergeManager.MergePlan mergePlan)
    {
        MergeManager manager = new MergeManager(this);
        manager.merge(dst, mergePlan);
        dst.clear();
    }

    private void dialogRemoveCurrentList()
    {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.msg_remove_language_after_merge))
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        onRemoveList();
                    }
                })
                .setNegativeButton(R.string.no, onCancel)
                .create()
                .show();
    }

    private void onRemoveList()
    {
        new DelvingListManager(this).deleteCurrentList();
        Intent intent = new Intent();
        intent.putExtra(AppCode.LIST_ID, list_dst.id);
        setResult(DelvingListListener.LIST_MERGED_AND_REMOVED, intent);
        finish();
    }

}
