package com.delvinglanguages.net;

import android.content.Context;
import android.content.Intent;

import com.delvinglanguages.AppSettings;
import com.delvinglanguages.kernel.util.Wrapper;

public class SyncManager {

    private boolean synchronize;
    private Context context;

    public SyncManager(Context context)
    {
        this.context = context;
        setState();
    }

    public void setState()
    {
        synchronize = AppSettings.isOnlineBackUpEnabled();
    }

    public void synchronize()
    {
        if (synchronize) {
            Intent intent = new Intent(context, SyncService.class);
            intent.putExtra(SyncService.PETITION_KEY, SyncService.SYNCHRONIZE);
            context.startService(intent);
        }
    }

    public void stopSynchronize()
    {
        if (synchronize) {
            context.stopService(new Intent(context, SyncService.class));
            synchronize = false;
        }
    }

    protected final void synchronizeNewList(int id)
    {
        if (synchronize) {
            Intent intent = new Intent(context, SyncService.class);
            intent.putExtra(SyncService.PETITION_KEY, SyncService.ADD_LIST);
            intent.putExtra(SyncService.LIST_ID_KEY, id);
            context.startService(intent);
        }
    }

    protected final void synchronizeUpdatedList(int id)
    {
        if (synchronize) {
            Intent intent = new Intent(context, SyncService.class);
            intent.putExtra(SyncService.PETITION_KEY, SyncService.UPDATE_LIST);
            intent.putExtra(SyncService.LIST_ID_KEY, id);
            context.startService(intent);
        }
    }

    protected void synchronizeDeleteList(int id)
    {
        if (synchronize) {
            Intent intent = new Intent(context, SyncService.class);
            intent.putExtra(SyncService.PETITION_KEY, SyncService.DELETE_LIST);
            intent.putExtra(SyncService.LIST_ID_KEY, id);
            context.startService(intent);
        }
    }

    protected final void synchronizeNewItem(int list_id, int item_id, Wrapper item)
    {
        if (synchronize) {
            Intent intent = new Intent(context, SyncService.class);
            intent.putExtra(SyncService.PETITION_KEY, SyncService.ADD_ITEM);
            intent.putExtra(SyncService.LIST_ID_KEY, list_id);
            intent.putExtra(SyncService.ITEM_ID_KEY, item_id);
            intent.putExtra(SyncService.ITEM_TYPE_KEY, item.wrapType());
            intent.putExtra(SyncService.ITEM_WRAPPER_KEY, item.wrap());
            context.startService(intent);
        }
    }

    protected final void synchronizeUpdateItem(int list_id, int item_id, Wrapper item)
    {
        if (synchronize) {
            Intent intent = new Intent(context, SyncService.class);
            intent.putExtra(SyncService.PETITION_KEY, SyncService.UPDATE_ITEM);
            intent.putExtra(SyncService.LIST_ID_KEY, list_id);
            intent.putExtra(SyncService.ITEM_ID_KEY, item_id);
            intent.putExtra(SyncService.ITEM_TYPE_KEY, item.wrapType());
            intent.putExtra(SyncService.ITEM_WRAPPER_KEY, item.wrap());
            context.startService(intent);
        }
    }

    protected final void synchronizeDeleteItem(int list_id, int item_id, int type)
    {
        if (synchronize) {
            Intent intent = new Intent(context, SyncService.class);
            intent.putExtra(SyncService.PETITION_KEY, SyncService.DELETE_ITEM);
            intent.putExtra(SyncService.LIST_ID_KEY, list_id);
            intent.putExtra(SyncService.ITEM_ID_KEY, item_id);
            intent.putExtra(SyncService.ITEM_TYPE_KEY, type);
            context.startService(intent);
        }
    }

}
