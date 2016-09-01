package com.delvinglanguages.kernel;

import android.content.Context;

import com.delvinglanguages.kernel.util.AppFormat;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.DrawerReferences;
import com.delvinglanguages.kernel.util.Inflexions;
import com.delvinglanguages.kernel.util.Wrapper;

public class LanguageManager extends KernelManager {

    public LanguageManager(Context context)
    {
        super(context);
    }

    /**
     * Getters
     **/
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

    public DReferences getRemovedReferences()
    {//// TODO: 29/03/2016  Update remove to tests and themes!!!!
        Language l = getCurrentLanguage();
        if (l.removed_references == null) {
            l.setRemovedReferences(dbManager.readRemovedReferences(l.id));
        }
        return l.removed_references;
    }

    public void getContentOf(Language language)
    {
        loadContentOf(language);
    }

    /**
     * Creates
     **/
    public void createReference(String reference)
    {
        Language l = getCurrentLanguage();
        DrawerReference newReference = dbManager.insertDrawerReference(l.id, reference);
        l.addReference(newReference);

        synchronizeNewItem(l.id, newReference.id, newReference);
    }

    public void createReference(String name, String pronunciation, Inflexions inflexions)
    {
        Language language = getCurrentLanguage();

        DReference newReference = dbManager.insertReference(language.id, AppFormat.formatReferenceName(name),
                inflexions, pronunciation);
        language.addReference(newReference);

        synchronizeNewItem(language.id, newReference.id, newReference);
    }

    public void createReference(DrawerReference drawerReference, String name, String pronunciation, Inflexions inflexions)
    {
        createReference(name, pronunciation, inflexions);
        deleteReference(drawerReference);
    }

    /**
     * Update
     **/
    public void updateReference(DReference reference, String name, String pronunciation, Inflexions inflexions)
    {
        Language language = getCurrentLanguage();
        language.updateReference(reference, name, pronunciation, inflexions);
        dbManager.updateReference(reference, language.id);

        synchronizeUpdateItem(language.id, reference.id, reference);
    }

    /**
     * Deletes
     **/
    public void deleteReference(DrawerReference drawerReference)
    {
        Language language = getCurrentLanguage();
        language.deleteDrawerReference(drawerReference);
        dbManager.deleteDrawerReference(drawerReference.id, language.id);

        synchronizeDeleteItem(language.id, drawerReference.id, Wrapper.TYPE_DRAWER_REFERENCE);
    }

    public void deleteReferenceTemporarily(DReference reference)
    {
        Language language = getCurrentLanguage();
        dbManager.deleteReferenceTemporarily(language.id, reference.id);
        language.removeReference(reference);

        synchronizeDeleteItem(language.id, reference.id, Wrapper.TYPE_REFERENCE);
    }

    public void deleteAllRemovedReferences()
    {
        Language language = getCurrentLanguage();
        language.deleteAllRemovedReferences();
        dbManager.deleteAllRemovedReferences(language.id);
    }

    /**
     * Restores
     **/
    public void restoreReference(DReference reference)
    {
        Language language = getCurrentLanguage();
        dbManager.restoreReference(reference.id, language.id);
        language.restoreReference(reference);

        synchronizeNewItem(language.id, reference.id, reference);
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

        synchronizeUpdateItem(language.id, language.statistics.id, language.statistics);
    }

    public void exercise(DReference reference, int attempt)
    {
        Language language = getCurrentLanguage();
        language.statistics.newAttempt(attempt);
        if (attempt == 1) {
            reference.priority -= 1;
            dbManager.updateReferencePriority(reference, language.id);
        }
    }

}
