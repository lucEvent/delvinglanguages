package com.delvinglanguages.data;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;

import com.delvinglanguages.AppCode;
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

public class BackUpManager {

    private Handler handler;

    public BackUpManager(Handler handler)
    {
        this.handler = handler;
    }

    public void restoreData(Context context, Uri backupfileuri)
    {
        DatabaseBackUpManager database = new DatabaseBackUpManager(context);
        database.openWritableDatabase();
        try {
            handler.obtainMessage(MessageListener.MESSAGE_INT, R.string.msg_preparing_file).sendToTarget();
            InStream stream = new InStream(context.getContentResolver().openInputStream(backupfileuri));

            int nLangs = stream.readInt();
            handler.obtainMessage(MessageListener.MESSAGE, "\n" + context.getString(R.string.msg_found_languages, nLangs)).sendToTarget();
            for (int i = 0; i < nLangs; i++) {    // Por cada idioma
                int Lcode = stream.readInt();
                String Lname = stream.readString();
                int Lsettings = stream.readInt();

                Language language = database.insertLanguage(Lcode, Lname, Lsettings);

                // Statistics
                language.statistics.intentos = stream.readInt();
                language.statistics.aciertos1 = stream.readInt();
                language.statistics.aciertos2 = stream.readInt();
                language.statistics.aciertos3 = stream.readInt();
                language.statistics.fallos = stream.readInt();
                database.insertStatistics(language.statistics);

                int nReferences = stream.readInt();
                for (int j = 0; j < nReferences; j++) {   // For each WORD of the language
                    String name = stream.readString();
                    String pronunciation = stream.readString();
                    int priority = stream.readInt();
                    String inflexionsString = stream.readString();
                    database.insertReference(name, inflexionsString, language.id, pronunciation, priority);
                    //         debug(" -" + name);
                }

                int nDrawerReferences = stream.readInt();
                for (int j = 0; j < nDrawerReferences; j++) {    // For each entry of warehouse
                    String note = stream.readString();
                    database.insertDrawerReference(note, language.id);
                }

                int nThemes = stream.readInt();
                for (int j = 0; j < nThemes; j++) { // Por cada theme
                    String thName = stream.readString();
                    ThemePairs thPairs = new ThemePairs();
                    int nthPairs = stream.readInt();
                    for (int k = 0; k < nthPairs; k++) {
                        String thpDelv = stream.readString();
                        String thpNatv = stream.readString();
                        thPairs.add(new ThemePair(thpDelv, thpNatv));
                    }
                    database.insertTheme(language.id, thName, thPairs);
                }

                int nTests = stream.readInt();
                for (int j = 0; j < nTests; j++) {  // Por cada Test
                    String test_name = stream.readString();
                    int test_runTimes = stream.readInt();
                    String test_content = stream.readString();
                    database.insertTest(test_name, test_runTimes, test_content, language.id);
                }
                String message = "\n" + (i + 1) + ". " + Lname + "\n  " +
                        context.getString(R.string.msg_n_references, nReferences) + "\n  " +
                        context.getString(R.string.msg_n_drawerreferences, nDrawerReferences) + "\n  " +
                        context.getString(R.string.msg_n_themes, nThemes) + "\n  " +
                        context.getString(R.string.msg_n_tests, nTests);

                handler.obtainMessage(MessageListener.MESSAGE, message).sendToTarget();
            }
            stream.close();

        } catch (Exception e) {

            handler.obtainMessage(MessageListener.ERROR, "\nException:" + e.toString()).sendToTarget();
            e.printStackTrace();

        } finally {

            database.closeWritableDatabase();

        }
        new KernelManager(context).invalidateData();
        handler.obtainMessage(AppCode.IMPORT_SUCCESSFUL).sendToTarget();
    }


    public void backupData(Context context, String filename, Languages languages)
    {
        DatabaseBackUpManager database = new DatabaseBackUpManager(context);
        database.openReadableDatabase();
        try {
            // Gettings backup file
            handler.obtainMessage(MessageListener.MESSAGE_INT, R.string.msg_preparing_file).sendToTarget();
            String estado = Environment.getExternalStorageState();
            if (!estado.equals(Environment.MEDIA_MOUNTED)) {
                handler.obtainMessage(MessageListener.ERROR, "\n" + context.getString(R.string.msg_could_access_disc)).sendToTarget();
            }
            File externalDir = Environment.getExternalStorageDirectory();

            File folder = new File(externalDir.getAbsolutePath() + File.separator + "Delving");
            folder.mkdirs();
            File backupfile = new File(folder, filename + ".delv");

            // Starting backup
            OutStream stream = new OutStream(backupfile);

            stream.writeInt(languages.size());
            for (Language language : languages) { // For each language
                handler.obtainMessage(MessageListener.MESSAGE, "\n" + context.getString(R.string.msg_saving_, language.language_name)).sendToTarget();
                stream.writeInt(language.CODE);
                stream.writeString(language.language_name);
                stream.writeInt(language.settings);

                Statistics e = language.statistics;   // Statistics
                stream.writeInt(e.intentos);
                stream.writeInt(e.aciertos1);
                stream.writeInt(e.aciertos2);
                stream.writeInt(e.aciertos3);
                stream.writeInt(e.fallos);

                DReferences references = database.readReferences(language.id);
                stream.writeInt(references.size());
                for (DReference w : references) {  // For each reference of the language
                    stream.writeString(w.name);
                    stream.writeString(w.pronunciation);
                    stream.writeInt(w.priority);
                    stream.writeString(w.getInflexions().toString());
                }

                DrawerReferences drawer = database.readDrawerReferences(language.id);
                stream.writeInt(drawer.size());
                for (DrawerReference n : drawer) {    // For each reference in the drawer of the language
                    stream.writeString(n.name);
                }

                Themes themes = database.readThemes(language.id);
                stream.writeInt(themes.size());
                for (Theme theme : themes) {    // For each theme of the language
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
                    stream.writeString(test.name);
                    stream.writeInt(test.getRunTimes());
                    stream.writeString(Test.wrapContent(test));
                }

                String message = "\n" + context.getString(R.string.msg_language_saved_, language.language_name, references.size(), drawer.size(), themes.size(), tests.size());
                handler.obtainMessage(MessageListener.MESSAGE, message).sendToTarget();
            }
            stream.close();
        } catch (Exception e) {
            handler.obtainMessage(MessageListener.ERROR, "\nException:" + e.toString()).sendToTarget();
            e.printStackTrace();
        } finally {

            database.closeReadableDatabase();

        }
        handler.obtainMessage(AppCode.EXPORT_SUCCESSFUL).sendToTarget();
    }

}
