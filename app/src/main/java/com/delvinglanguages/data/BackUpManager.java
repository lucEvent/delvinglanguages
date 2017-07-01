package com.delvinglanguages.data;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.AppSettings;
import com.delvinglanguages.R;
import com.delvinglanguages.data.util.InStream;
import com.delvinglanguages.data.util.OutStream;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DrawerReference;
import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.theme.ThemePair;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.DrawerReferences;
import com.delvinglanguages.kernel.util.Languages;
import com.delvinglanguages.kernel.util.Statistics;
import com.delvinglanguages.kernel.util.Tests;
import com.delvinglanguages.kernel.util.ThemePairs;
import com.delvinglanguages.kernel.util.Themes;
import com.delvinglanguages.view.utils.MessageListener;

import java.io.File;
import java.util.Random;

public class BackUpManager {

    private static final int VERSION2 = 1002;
    private static final int CURRENT_VERSION = VERSION2;

    private Handler handler;

    public BackUpManager(Handler handler)
    {
        this.handler = handler;
    }

    public void restoreData(Context context, Uri backupfileuri)
    {
        BackUpDatabaseManager database = new BackUpDatabaseManager(context);
        database.openWritableDatabase();
        try {
            handler.obtainMessage(MessageListener.MESSAGE_INT, R.string.msg_preparing_file).sendToTarget();
            InStream stream = new InStream(context.getContentResolver().openInputStream(backupfileuri));

            int version = stream.readInt();

            int nLangs = version == VERSION2 ? stream.readInt() : version;
            handler.obtainMessage(MessageListener.MESSAGE, "\n" + context.getString(R.string.msg_found_languages, nLangs)).sendToTarget();
            for (int i = 0; i < nLangs; i++) {    // For each language
                int lang_id = version == VERSION2 ? stream.readInt() : Math.abs(new Random().nextInt());
                int lang_code = stream.readInt();
                String lang_name = stream.readString();
                int lang_settings = stream.readInt();
                lang_id = database.insertLanguage(lang_id, lang_code, lang_name, lang_settings);

                // Statistics
                Statistics statistics = new Statistics(lang_id);
                statistics.attempts = stream.readInt();
                statistics.hits_at_1st = stream.readInt();
                statistics.hits_at_2nd = stream.readInt();
                statistics.hits_at_3rd = stream.readInt();
                statistics.misses = stream.readInt();
                database.updateStatistics(statistics);

                int nReferences = stream.readInt();
                for (int j = 0; j < nReferences; j++) {   // For each WORD of the language
                    int id = version == VERSION2 ? stream.readInt() : j + 1;
                    String name = stream.readString();
                    String pronunciation = stream.readString();
                    int priority = stream.readInt();
                    String inflexionsString = stream.readString();
                    database.insertReference(id, lang_id, name, inflexionsString, pronunciation, priority);
                    //         debug(" -" + name);
                }

                int nDrawerReferences = stream.readInt();
                for (int j = 0; j < nDrawerReferences; j++) {    // For each entry of warehouse
                    int id = version == VERSION2 ? stream.readInt() : j + 1;
                    String note = stream.readString();
                    database.insertDrawerReference(id, lang_id, note);
                }

                int nThemes = stream.readInt();
                for (int j = 0; j < nThemes; j++) { // Por cada theme
                    int id = version == VERSION2 ? stream.readInt() : j + 1;
                    String thName = stream.readString();
                    ThemePairs thPairs = new ThemePairs();
                    int nthPairs = stream.readInt();
                    for (int k = 0; k < nthPairs; k++) {
                        String thpDelv = stream.readString();
                        String thpNatv = stream.readString();
                        thPairs.add(new ThemePair(thpDelv, thpNatv));
                    }
                    database.insertTheme(id, lang_id, thName, thPairs);
                }

                int nTests = stream.readInt();
                for (int j = 0; j < nTests; j++) {  // Por cada Test
                    int id = version == VERSION2 ? stream.readInt() : j + 1;
                    String test_name = stream.readString();
                    int test_runTimes = stream.readInt();
                    String test_content = stream.readString();
                    int theme_id = version == VERSION2 ? stream.readInt() : -1;
                    database.insertTest(id, lang_id, test_name, test_runTimes, test_content, theme_id);
                }
                String message = "\n" + (i + 1) + ". " + lang_name + "\n  " +
                        context.getString(R.string.msg_n_references, nReferences) + "\n  " +
                        context.getString(R.string.msg_n_drawerreferences, nDrawerReferences) + "\n  " +
                        context.getString(R.string.msg_n_themes, nThemes) + "\n  " +
                        context.getString(R.string.msg_n_tests, nTests);

                handler.obtainMessage(MessageListener.MESSAGE, message).sendToTarget();
            }
            stream.close();

        } catch (Exception e) {

            handler.obtainMessage(MessageListener.ERROR, "\nException:" + e.toString()).sendToTarget();
            AppSettings.printerror("[BUM] Exception in restoreData", e);
            database.closeWritableDatabase();
            return;
        }
        database.closeWritableDatabase();
        new KernelManager(context).invalidateData();
        handler.obtainMessage(AppCode.IMPORT_SUCCESSFUL).sendToTarget();
    }


    public void backupData(Context context, String filename, Languages languages)
    {
        BackUpDatabaseManager database = new BackUpDatabaseManager(context);
        database.openReadableDatabase();
        try {
            // Getting backup file
            handler.obtainMessage(MessageListener.MESSAGE_INT, R.string.msg_preparing_file).sendToTarget();

            String state = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                handler.obtainMessage(MessageListener.ERROR, "\n" + context.getString(R.string.msg_could_access_disc)).sendToTarget();
                return;
            }
            File externalDir = Environment.getExternalStorageDirectory();

            File folder = new File(externalDir, "Delving");
            folder.mkdirs();
            File backupfile = new File(folder, filename + ".delv");

            // Starting backup
            OutStream stream = new OutStream(backupfile);
            stream.writeInt(CURRENT_VERSION);

            stream.writeInt(languages.size());
            for (Language language : languages) { // For each language
                handler.obtainMessage(MessageListener.MESSAGE, "\n" + context.getString(R.string.msg_saving_, language.language_name)).sendToTarget();
                stream.writeInt(language.id);
                stream.writeInt(language.code);
                stream.writeString(language.language_name);
                stream.writeInt(language.settings);

                Statistics e = language.statistics;   // Statistics
                stream.writeInt(e.attempts);
                stream.writeInt(e.hits_at_1st);
                stream.writeInt(e.hits_at_2nd);
                stream.writeInt(e.hits_at_3rd);
                stream.writeInt(e.misses);

                DReferences references = database.readReferences(language.id);
                stream.writeInt(references.size());
                for (DReference w : references) {  // For each reference of the language
                    stream.writeInt(w.id);
                    stream.writeString(w.name);
                    stream.writeString(w.pronunciation);
                    stream.writeInt(w.priority);
                    stream.writeString(w.getInflexions().wrap());
                }

                DrawerReferences drawer = database.readDrawerReferences(language.id);
                stream.writeInt(drawer.size());
                for (DrawerReference n : drawer) {    // For each reference in the drawer of the language
                    stream.writeInt(n.id);
                    stream.writeString(n.name);
                }

                Themes themes = database.readThemes(language.id);
                stream.writeInt(themes.size());
                for (Theme theme : themes) {    // For each theme of the language
                    stream.writeInt(theme.id);
                    stream.writeString(theme.getName());
                    stream.writeInt(theme.getPairs().size());
                    for (ThemePair pair : theme.getPairs()) {   // For each themepair of the theme
                        stream.writeString(pair.inDelved);
                        stream.writeString(pair.inNative);
                    }
                }

                Tests tests = database.readTests(language.id);
                stream.writeInt(tests.size());
                for (Test test : tests) {   // For each test of the language
                    stream.writeInt(test.id);
                    stream.writeString(test.name);
                    stream.writeInt(test.getRunTimes());
                    stream.writeString(Test.wrapContent(test));
                    stream.writeInt(test.theme_id);
                }

                String message = "\n" + context.getString(R.string.msg_language_saved_, language.language_name, references.size(), drawer.size(), themes.size(), tests.size());
                handler.obtainMessage(MessageListener.MESSAGE, message).sendToTarget();
            }
            stream.close();
        } catch (Exception e) {
            handler.obtainMessage(MessageListener.ERROR, "\nException:" + e.toString()).sendToTarget();
            AppSettings.printerror("[BUM] Exception in backupData", e);
        } finally {

            database.closeReadableDatabase();

        }
        handler.obtainMessage(AppCode.EXPORT_SUCCESSFUL).sendToTarget();
    }

}
