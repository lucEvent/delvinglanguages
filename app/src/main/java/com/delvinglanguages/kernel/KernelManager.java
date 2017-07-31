package com.delvinglanguages.kernel;

import android.content.Context;

import com.delvinglanguages.AppSettings;
import com.delvinglanguages.data.DatabaseManager;
import com.delvinglanguages.kernel.util.DelvingLists;
import com.delvinglanguages.net.SyncManager;

public class KernelManager extends SyncManager {

    protected static DatabaseManager dbManager;

    protected static DelvingLists lists;

    public KernelManager(Context context)
    {
        super(context);

        if (dbManager == null)
            dbManager = new DatabaseManager(context);

        if (lists == null)
            lists = dbManager.readLists();
    }

    public DelvingLists getDelvingLists()
    {
        return lists;
    }

    public int getNumberOfLists()
    {
        return lists.size();
    }

    public DelvingList getCurrentList()
    {
        int list_id = AppSettings.getCurrentList();
        if (list_id == -1) return null;
        else return lists.getListById(list_id);
    }

    protected synchronized void loadContentOf(DelvingList delvingList)
    {
        if (delvingList.dictionary == null)
            delvingList.setReferences(dbManager.readReferences(delvingList.id));

        if (delvingList.drawer_references == null)
            delvingList.setDrawerReferences(dbManager.readDrawerReferences(delvingList.id));

        if (delvingList.tests == null)
            delvingList.setTests(dbManager.readTests(delvingList.id));

        if (delvingList.subjects == null)
            delvingList.setSubjects(dbManager.readSubjects(delvingList.id));
    }

    public DelvingList createDelvingList(int from_code, int to_code, String name, int settings)
    {
        DelvingList new_list = dbManager.insertDelvingList(from_code, to_code, name, settings);
        lists.add(new_list);
        synchronizeNewList(new_list.id);
        RecordManager.listCreated(new_list.id, from_code);
        return new_list;
    }

    public void updateListName(String new_name)
    {
        DelvingList list = getCurrentList();

        RecordManager.listNameChanged(list.id, list.from_code, list.name, new_name);

        list.setName(new_name);

        dbManager.updateDelvingList(list);
        synchronizeUpdatedList(list.id);
    }

    public void updateListLanguageFromCode(int from_code)
    {
        DelvingList list = getCurrentList();
        list.setFromCode(from_code);

        dbManager.updateDelvingList(list);
        synchronizeUpdatedList(list.id);
        RecordManager.listLanguageCodesChanged(list.id, from_code, list.to_code);
    }

    public void updateListLanguageToCode(int to_code)
    {
        DelvingList list = getCurrentList();
        list.setToCode(to_code);

        dbManager.updateDelvingList(list);
        synchronizeUpdatedList(list.id);
        RecordManager.listLanguageCodesChanged(list.id, list.from_code, to_code);
    }

    public void phrasalVerbsStateChanged(boolean state)
    {
        DelvingList list = getCurrentList();
        list.setPhrasalVerbsState(state);

        RecordManager.listPhVStateChanged(list.id, list.from_code, state);

        dbManager.updateDelvingList(list);
        synchronizeUpdatedList(list.id);
    }

    public void deleteCurrentList()
    {
        DelvingList list = getCurrentList();
        dbManager.deleteDelvingList(list);
        lists.remove(list);
        synchronizeDeleteList(list.id);
        RecordManager.listDeleted(list.id, list.from_code, list.name);
    }

    public void invalidateData()
    {
        lists = dbManager.readLists();
    }

}
