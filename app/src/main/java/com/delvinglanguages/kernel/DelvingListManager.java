package com.delvinglanguages.kernel;

import android.content.Context;

import com.delvinglanguages.kernel.util.AppFormat;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.DrawerReferences;
import com.delvinglanguages.kernel.util.Inflexions;
import com.delvinglanguages.kernel.util.RemovedItem;
import com.delvinglanguages.kernel.util.RemovedItems;
import com.delvinglanguages.kernel.util.Wrapper;

public class DelvingListManager extends KernelManager {

    public DelvingListManager(Context context)
    {
        super(context);
    }

    /**
     * Getters
     **/
    public static String getListName(int list_id)
    {
        DelvingList l = lists.getListById(list_id);
        return l != null ? l.name : null;
    }

    public DReference getReference(String name)
    {
        return getCurrentList().getReference(name);
    }

    public DReferences getReferences()
    {
        DelvingList l = getCurrentList();
        while (!l.isDictionaryCreated()) ;
        return getCurrentList().getReferences();
    }

    public DrawerReferences getDrawerReferences()
    {
        DelvingList l = getCurrentList();
        if (l.drawer_references == null) {
            l.setDrawerReferences(dbManager.readDrawerReferences(l.id));
        }
        return l.drawer_references;
    }

    public RemovedItems getRemovedItems()
    {
        DelvingList l = getCurrentList();
        if (l.removed_items == null)
            l.setRemovedItems(dbManager.readRemovedItems(l.id));

        return l.removed_items;
    }

    public void getContentOf(DelvingList delvingList)
    {
        loadContentOf(delvingList);
    }

    /**
     * Creates
     **/
    public void createDrawerReference(String reference)
    {
        DelvingList delvingList = getCurrentList();
        DrawerReference newReference = dbManager.insertDrawerReference(delvingList.id, reference);
        delvingList.addReference(newReference);

        RecordManager.drawerWordAdded(delvingList.id, delvingList.from_code, newReference.id);
        synchronizeNewItem(delvingList.id, newReference.id, newReference);
    }

    public void createReference(String word, String pronunciation, Inflexions inflexions)
    {
        DelvingList delvingList = getCurrentList();

        DReference newReference = dbManager.insertReference(delvingList.id, AppFormat.formatReferenceName(word),
                inflexions, pronunciation);
        delvingList.addReference(newReference);

        RecordManager.referenceAdded(delvingList.id, delvingList.from_code, newReference.id);
        synchronizeNewItem(delvingList.id, newReference.id, newReference);
    }

    public void createReferenceInverse(String word, Inflexions inflexions)
    {
        DelvingList delvingList = getCurrentList();

        for (Inflexion i : inflexions) {

            for (String t : i.getTranslations()) {
                Inflexions infs = new Inflexions(1);
                infs.add(new Inflexion(new String[]{}, new String[]{word}, i.getType()));

                DReference newReference = dbManager.insertReference(delvingList.id, t, infs, "");
                delvingList.addReference(newReference);

                RecordManager.referenceAdded(delvingList.id, delvingList.from_code, newReference.id);
                synchronizeNewItem(delvingList.id, newReference.id, newReference);
            }
        }
    }

    public void createReference(DrawerReference drawerReference, String word, String pronunciation, Inflexions inflexions)
    {
        createReference(word, pronunciation, inflexions);
        deleteDrawerReference(drawerReference);
    }

    /**
     * Update
     **/
    public void updateReference(DReference reference, String name, String pronunciation, Inflexions inflexions)
    {
        DelvingList delvingList = getCurrentList();
        delvingList.updateReference(reference, name, pronunciation, inflexions);
        dbManager.updateReference(reference, delvingList.id);

        RecordManager.referenceModified(delvingList.id, delvingList.from_code, reference.id);
        synchronizeUpdateItem(delvingList.id, reference.id, reference);
    }

    /**
     * Deletes
     **/
    public void deleteDrawerReference(DrawerReference drawerReference)
    {
        DelvingList delvingList = getCurrentList();
        delvingList.deleteDrawerReference(drawerReference);
        dbManager.deleteDrawerReference(drawerReference.id, delvingList.id);

        RecordManager.drawerWordDeleted(delvingList.id, delvingList.from_code, drawerReference.id);
        synchronizeDeleteItem(delvingList.id, drawerReference.id, Wrapper.TYPE_DRAWER_REFERENCE);
    }

    public void removeReference(DReference reference)
    {
        DelvingList delvingList = getCurrentList();
        delvingList.removeReference(reference);
        dbManager.removeReference(delvingList.id, reference);

        RecordManager.referenceRemoved(delvingList.id, delvingList.from_code, reference.id);
        synchronizeDeleteItem(delvingList.id, reference.id, Wrapper.TYPE_REFERENCE);
    }

    public void deleteAllRemovedItems()
    {
        DelvingList delvingList = getCurrentList();
        delvingList.deleteAllRemovedItems();
        dbManager.deleteAllRemovedItems(delvingList.id);

        RecordManager.listRecycleBinCleared(delvingList.id, delvingList.from_code);
    }

    /**
     * Restores
     **/
    public void restoreItem(RemovedItem removedItem)
    {
        DelvingList delvingList = getCurrentList();
        delvingList.restoreItem(removedItem);
        dbManager.restoreRemovedItem(delvingList.id, removedItem);

        RecordManager.itemRecovered(delvingList.id, delvingList.from_code, removedItem.wrap_type, removedItem.id);
        //   synchronizeNewItem(list.id, reference.id, reference);//// TODO: 12/07/2017 correct synchronization
    }

    /**
     * Others
     */
    public void saveStatistics()
    {
        DelvingList delvingList = getCurrentList();
        dbManager.updateStatistics(delvingList.statistics);

        synchronizeUpdateItem(delvingList.id, delvingList.statistics.id, delvingList.statistics);
    }

    public void resetStatistics()
    {
        DelvingList delvingList = getCurrentList();
        delvingList.statistics.reset();
        dbManager.updateStatistics(delvingList.statistics);

        RecordManager.listStatisticsCleared(delvingList.id, delvingList.from_code);
        synchronizeUpdateItem(delvingList.id, delvingList.statistics.id, delvingList.statistics);
    }

    public void exercise(int exercise_type, DReference reference, int attempt)
    {
        DelvingList delvingList = getCurrentList();
        delvingList.statistics.newAttempt(attempt);
        if (attempt == 1) {
            reference.priority -= 1;
            dbManager.updateReferencePriority(reference, delvingList.id);
        }
        RecordManager.referencePractised(exercise_type, delvingList.id, delvingList.from_code, reference.id);
    }

}
