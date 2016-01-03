package com.delvinglanguages.kernel;

import android.content.Context;

import com.delvinglanguages.data.ControlDisco;
import com.delvinglanguages.data.DataBaseManager;
import com.delvinglanguages.kernel.set.Languages;
import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.net.internal.ProgressHandler;
import com.delvinglanguages.net.internal.TaskHandler;
import com.delvinglanguages.net.internal.TaskHandler.TaskState;
import com.delvinglanguages.settings.Settings;

public class KernelControl {

    public static Context context;
    protected static DataBaseManager dbManager;
    private static ControlDisco sdcard;
    private static Languages languages;
    protected static Language currentLanguage;

    //
    public static Words integrateWords;
    public static Language integrateLanguage;

    public KernelControl(Context context) {
        KernelControl.context = context;
        initializeAll();
    }

    private static void initializeAll() {
        if (dbManager == null) {
            dbManager = new DataBaseManager(context);
        }
        if (languages == null) {
            languages = dbManager.readLanguages();
        }
        if (sdcard == null) {
            sdcard = new ControlDisco();
        }
    }

    public static Language getCurrentLanguage() {
        if (currentLanguage == null) {
            initializeAll();

            int position = sdcard.getLastLanguage();
            if (position != -1) currentLanguage = languages.get(position);
        }
        return currentLanguage;
    }

    public static Language setCurrentLanguage(int position) {
        currentLanguage = languages.get(position);
        sdcard.saveLastLaguage(position);
        return currentLanguage;
    }

    public static void setCurrentLanguage(Language language) {
        setCurrentLanguage(languages.indexOf(language));
    }

    public static int getNumLanguages() {
        return languages.size();
    }

    public static Languages getLanguages() {
        return languages;
    }

    public static void loadLanguage(Language language) {
        if (!language.isLoaded()) {
            dbManager.readLanguage(language, new ProgressHandler(null));
        }
    }

    public static void loadLanguage(final Language language, final ProgressHandler progress, final TaskHandler comm) {
        if (!language.isLoaded()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    dbManager.readLanguage(language, progress);
                    comm.onTaskDone(-1, TaskState.TASK_DONE, null);
                }
            }).start();
        }
    }

    public static int addLanguage(int code, String name, int settings) {
        Language newlanguage = dbManager.insertLanguage(code, name, settings);
        languages.add(newlanguage);
        return languages.size() - 1;
    }

    public static void addWord(String name, Translations translations, String pronunciation, int priority) {
        Word W = dbManager.insertWord(Word.format(name), translations, currentLanguage.id, pronunciation, priority);
        currentLanguage.addWord(W);
    }

    public static void updateWord(Word word, String name, Translations translation, String pronunciation) {
        currentLanguage.updateWord(word, name, translation, pronunciation);
        dbManager.updateWord(word);
    }

    public static void deleteWordTemporarily(Word word) {
        dbManager.deleteWordTemporarily(currentLanguage.id, word.id);
        currentLanguage.removeWord(word);
    }

    public static void restoreWord(int position) {
        dbManager.restoreWord(currentLanguage.removed_words.get(position).id);
        currentLanguage.restoreWord(position);
    }

    public static void deleteLanguage() {
        dbManager.deleteLanguage(currentLanguage);
        languages.remove(currentLanguage);

        currentLanguage = null;
    }

    public static void saveStatistics() {
        dbManager.updateStatistics(currentLanguage.statistics);
    }

    public static void clearStatistics() {
        currentLanguage.statistics.clear();
        saveStatistics();
    }

    public static void deleteAllRemovedWords() {
        currentLanguage.deleteAllRemovedWords();
        dbManager.deleteAllRemovedWords(currentLanguage.id);
    }

    public static void addToStore(String note) {
        currentLanguage.addWord(dbManager.insertStoreWord(note, currentLanguage.id));
    }

    public static void addWord(DrawerWord sword, String name, Translations translations, String pronuntiation) {
        addWord(name, translations, pronuntiation, Word.INITIAL_PRIORITY);
        removeFromStore(sword);
    }

    public static void removeFromStore(DrawerWord sword) {
        currentLanguage.deleteDrawerWord(sword);
        dbManager.deleteStoredWord(sword.id);
    }

    public static void updateLanguage(int code, String newname) {
        currentLanguage.updateCode(code);
        currentLanguage.setName(newname);

        dbManager.updateLanguage(currentLanguage);
    }

    public static void updateLanguageSettings(boolean status, int mask) {
        currentLanguage.setSettings(status, mask);
        dbManager.updateLanguage(currentLanguage);
    }

    public static Words integrateLanguage(int destinyLanguagePosition) {
        //// TODO: 03/01/2016 rething and redo
        integrateWords = new Words();
     /*   integrateLanguage = languages.get(destinyLanguagePosition);
        Language destino = integrateLanguage;
        loadLanguage(destino);
        while (!destino.isDictionaryCreated()) {
        }

        // Copiando el Diccionario
        Words fuentes = currentLanguage.words;
        for (Word pinsert : fuentes) {
            Word porig = destino.getWords(pinsert.getName());
            if (porig == null) {
                destino.addWord(pinsert);
                dbManager.updateWordLanguage(pinsert, destino.id);
            } else {
                integrateWords.add(porig);
                integrateWords.add(pinsert);
            }
        }

        // Copiando la warehouse
        DrawerWords notafuente = currentLanguage.drawer_words;
        for (int i = 0; i < notafuente.size(); ++i) {
            destino.addWord(notafuente.get(i));
        }
        dbManager.updateDrawerWordsLanguage(currentLanguage.id, destino.id);
        */
        return integrateWords;
    }

    public static void exercise(DReference ref, int intento) {
        currentLanguage.statistics.nuevoIntento(intento);
        if (intento == 1) {
            ref.priority += -1;
        }
        for (Word word : ref.appearances) {
            word.updatePriority(ref.priority);
            dbManager.updateWordPriority(word);
        }
    }

    private static void debug(String text) {
        if (Settings.DEBUG)
            android.util.Log.d("##KernelControl##", text);
    }

}
