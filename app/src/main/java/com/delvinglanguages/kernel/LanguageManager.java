package com.delvinglanguages.kernel;

import android.content.Context;

import com.delvinglanguages.kernel.util.AppFormat;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.DrawerReferences;
import com.delvinglanguages.kernel.util.Inflexions;

public class LanguageManager extends KernelManager {

    public LanguageManager(Context context) {
        super(context);
    }

    /**
     * Getters
     **/
    public DReference getReference(String name) {
        return getCurrentLanguage().getReference(name);
    }

    public DReferences getReferences() {
        Language l = getCurrentLanguage();
        while (!l.isDictionaryCreated()) ;
        return getCurrentLanguage().getReferences();
    }

    public DrawerReferences getDrawerReferences() {
        Language l = getCurrentLanguage();
        if (l.drawer_references == null) {
            l.setDrawerReferences(dbManager.readDrawerReferences(l.id));
        }
        return l.drawer_references;
    }

    public DReferences getRemovedReferences() {//// TODO: 29/03/2016  Update remove to tests and themes!!!!
        Language l = getCurrentLanguage();
        if (l.removed_references == null) {
            l.setRemovedReferences(dbManager.readRemovedReferences(l.id));
        }
        return l.removed_references;
    }

    /**
     * Creates
     **/
    public void createReference(String reference) {
        Language l = getCurrentLanguage();
        l.addReference(dbManager.insertDrawerReference(reference, l.id));
    }

    public void createReference(String name, String pronunciation, Inflexions inflexions) {
        DReference R = dbManager.insertReference(
                AppFormat.formatReferenceName(name), inflexions, getCurrentLanguage().id,
                pronunciation, DReference.INITIAL_PRIORITY);

        getCurrentLanguage().addReference(R);
    }

    public void createReference(DrawerReference drawerReference, String name, String pronunciation, Inflexions inflexions) {
        createReference(name, pronunciation, inflexions);
        deleteReference(drawerReference);
    }

    /**
     * Update
     **/
    public void updateReference(DReference reference, String name, String pronunciation, Inflexions inflexions) {
        getCurrentLanguage().updateReference(reference, name, pronunciation, inflexions);
        dbManager.updateReference(reference);
    }

    /**
     * Deletes
     **/
    public void deleteReference(DrawerReference drawerReference) {
        getCurrentLanguage().deleteDrawerReference(drawerReference);
        dbManager.deleteDrawerReference(drawerReference.id);
    }

    /**
     * Not distributed yet. Just revised
     */
    public void exercise(DReference reference, int attempt) {
        getCurrentLanguage().statistics.nuevoIntento(attempt);
        if (attempt == 1) {
            reference.priority -= 1;
            dbManager.updateReferencePriority(reference);
        }
    }

    public void saveStatistics() {
        dbManager.updateStatistics(getCurrentLanguage().statistics);
    }

    public void resetStatistics() {
        getCurrentLanguage().statistics.reset();
        saveStatistics();
    }

    public void deleteLanguage() {
        Language language = getCurrentLanguage();
        dbManager.deleteLanguage(language);
        languages.remove(language);
    }

    /*****************************************
     * hasta aqui revisadas
     ************************************************/

    public void deleteReferenceTemporarily(DReference reference) {
        Language language = getCurrentLanguage();
        dbManager.deleteReferenceTemporarily(language.id, reference.id);
        language.removeReference(reference);
    }

    public void restoreReference(DReference reference) {
        Language language = getCurrentLanguage();
        dbManager.restoreReference(reference.id);
        language.restoreReference(reference);
    }

    public void deleteAllRemovedReferences() {
        Language language = getCurrentLanguage();
        language.deleteAllRemovedReferences();
        dbManager.deleteAllRemovedReferences(language.id);
    }

}
