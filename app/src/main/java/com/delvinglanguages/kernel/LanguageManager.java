package com.delvinglanguages.kernel;

import android.content.Context;

import com.delvinglanguages.kernel.util.AppFormat;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.DrawerReferences;
import com.delvinglanguages.kernel.util.Inflexions;
import com.delvinglanguages.kernel.util.RemovedItem;
import com.delvinglanguages.kernel.util.RemovedItems;
import com.delvinglanguages.kernel.util.Wrapper;

public class LanguageManager extends KernelManager {

    public LanguageManager(Context context)
    {
        super(context);
    }

    /**
     * Getters
     **/
    public static String getLanguageName(int language_id)
    {
        Language l = languages.getLanguageById(language_id);
        return l != null ? l.language_name : null;
    }

    public DReference getReference(String name)
    {
        return getCurrentLanguage().getReference(name);
    }

    public DReferences getReferences()
    {
        Language l = getCurrentLanguage();
        while (!l.isDictionaryCreated()) ;
        return getCurrentLanguage().getReferences();
    }

    public DrawerReferences getDrawerReferences()
    {
        Language l = getCurrentLanguage();
        if (l.drawer_references == null) {
            l.setDrawerReferences(dbManager.readDrawerReferences(l.id));
        }
        return l.drawer_references;
    }

    public RemovedItems getRemovedItems()
    {
        Language l = getCurrentLanguage();
        if (l.removed_items == null)
            l.setRemovedItems(dbManager.readRemovedItems(l.id));

        return l.removed_items;
    }

    public void getContentOf(Language language)
    {
        loadContentOf(language);
    }

    /**
     * Creates
     **/
    public void createDrawerReference(String reference)
    {
        Language language = getCurrentLanguage();
        DrawerReference newReference = dbManager.insertDrawerReference(language.id, reference);
        language.addReference(newReference);

        RecordManager.drawerWordAdded(language.id, language.code, newReference.id);
        synchronizeNewItem(language.id, newReference.id, newReference);
    }

    public void createReference(String name, String pronunciation, Inflexions inflexions)
    {
        Language language = getCurrentLanguage();

        DReference newReference = dbManager.insertReference(language.id, AppFormat.formatReferenceName(name),
                inflexions, pronunciation);
        language.addReference(newReference);

        RecordManager.referenceAdded(language.id, language.code, newReference.id);
        synchronizeNewItem(language.id, newReference.id, newReference);
    }

    public void createReference(DrawerReference drawerReference, String name, String pronunciation, Inflexions inflexions)
    {
        createReference(name, pronunciation, inflexions);
        deleteDrawerReference(drawerReference);
    }

    /**
     * Update
     **/
    public void updateReference(DReference reference, String name, String pronunciation, Inflexions inflexions)
    {
        Language language = getCurrentLanguage();
        language.updateReference(reference, name, pronunciation, inflexions);
        dbManager.updateReference(reference, language.id);

        RecordManager.referenceModified(language.id, language.code, reference.id);
        synchronizeUpdateItem(language.id, reference.id, reference);
    }

    /**
     * Deletes
     **/
    public void deleteDrawerReference(DrawerReference drawerReference)
    {
        Language language = getCurrentLanguage();
        language.deleteDrawerReference(drawerReference);
        dbManager.deleteDrawerReference(drawerReference.id, language.id);

        RecordManager.drawerWordDeleted(language.id, language.code, drawerReference.id);
        synchronizeDeleteItem(language.id, drawerReference.id, Wrapper.TYPE_DRAWER_REFERENCE);
    }

    public void removeReference(DReference reference)
    {
        Language language = getCurrentLanguage();
        language.removeReference(reference);
        dbManager.removeReference(language.id, reference);

        RecordManager.referenceRemoved(language.id, language.code, reference.id);
        synchronizeDeleteItem(language.id, reference.id, Wrapper.TYPE_REFERENCE);
    }

    public void deleteAllRemovedItems()
    {
        Language language = getCurrentLanguage();
        language.deleteAllRemovedItems();
        dbManager.deleteAllRemovedItems(language.id);

        RecordManager.languageRecycleBinCleared(language.id, language.code);
    }

    /**
     * Restores
     **/
    public void restoreItem(RemovedItem removedItem)
    {
        Language language = getCurrentLanguage();
        language.restoreItem(removedItem);
        dbManager.restoreRemovedItem(language.id, removedItem);

        RecordManager.itemRecovered(language.id, language.code, removedItem.wrap_type, removedItem.id);
        //   synchronizeNewItem(language.id, reference.id, reference);//// TODO: 12/07/2017 correct synchronization
    }

    /**
     * Others
     */
    public void saveStatistics()
    {
        Language language = getCurrentLanguage();
        dbManager.updateStatistics(language.statistics);

        synchronizeUpdateItem(language.id, language.statistics.id, language.statistics);
    }

    public void resetStatistics()
    {
        Language language = getCurrentLanguage();
        language.statistics.reset();
        dbManager.updateStatistics(language.statistics);

        RecordManager.languageStatisticsCleared(language.id, language.code);
        synchronizeUpdateItem(language.id, language.statistics.id, language.statistics);
    }

    public void exercise(int exercise_type, DReference reference, int attempt)
    {
        Language language = getCurrentLanguage();
        language.statistics.newAttempt(attempt);
        if (attempt == 1) {
            reference.priority -= 1;
            dbManager.updateReferencePriority(reference, language.id);
        }
        RecordManager.referencePractised(exercise_type, language.id, language.code, reference.id);
    }

}
