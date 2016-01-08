package com.delvinglanguages.data;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.delvinglanguages.data.util.InStream;
import com.delvinglanguages.data.util.OutStream;
import com.delvinglanguages.kernel.DrawerWord;
import com.delvinglanguages.kernel.Estadisticas;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.set.DrawerWords;
import com.delvinglanguages.kernel.set.Languages;
import com.delvinglanguages.kernel.set.Tests;
import com.delvinglanguages.kernel.set.ThemePairs;
import com.delvinglanguages.kernel.set.Themes;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.theme.ThemePair;
import com.delvinglanguages.net.internal.ProgressHandler;
import com.delvinglanguages.settings.Settings;

import java.io.File;

public class BackUp {

    private static final String backupfilename = "backup.delv";
    private File backupfile;

    public BackUp() {

        String estado = Environment.getExternalStorageState();
        if (!estado.equals(Environment.MEDIA_MOUNTED)) {
            debug("No nay media montada\n");
        }
        File externalDir = Environment.getExternalStorageDirectory();

        File folder = new File(externalDir.getAbsolutePath() + File.separator + "Delving");
        folder.mkdirs();
        backupfile = new File(folder, backupfilename);
    }

    public void createBackUp(Context context) {
        DataBaseManager database = new DataBaseManager(context);
        try {
            OutStream stream = new OutStream(backupfile);

            Languages idiomas = database.readLanguages();
            stream.writeInt(idiomas.size());
            for (int i = 0; i < idiomas.size(); i++) {  // Por cada idioma
                Language language = idiomas.get(i);
                debug(language.language_delved_name);

                stream.writeInt(language.CODE);
                stream.writeString(language.language_delved_name);
                stream.writeInt(language.settings);

                // Statistics
                Estadisticas e = language.statistics;
                stream.writeInt(e.intentos);
                stream.writeInt(e.aciertos1);
                stream.writeInt(e.aciertos2);
                stream.writeInt(e.aciertos3);
                stream.writeInt(e.fallos);

                Words words = database.readWords(language.id, new ProgressHandler(null));
                stream.writeInt(words.size());
                for (Word w : words) {  // Por cada palabra del idioma
                    stream.writeString(w.getName());
                    stream.writeString(w.getPronunciation());
                    stream.writeInt(w.getPriority());
                    stream.writeString(w.getInflexionsAsString());
                }

                DrawerWords drawer = database.readDrawerWords(language.id);
                stream.writeInt(drawer.size());
                for (DrawerWord n : drawer) {    // Por cada entrada del store
                    stream.writeString(n.name);
                }

                Themes themes = database.readThemes(language.id);
                stream.writeInt(themes.size());
                for (Theme theme : themes) {    // Por cada theme
                    stream.writeString(theme.getName());
                    stream.writeInt(theme.getPairs().size());
                    for (ThemePair pair : theme.getPairs()) {   // Por cada themepair del theme
                        stream.writeString(pair.inDelved);
                        stream.writeString(pair.inNative);
                    }
                }

                Tests tests = database.readTests(language.id);
                stream.writeInt(tests.size());
                for (Test test : tests) {   // Por cada Test
                    stream.writeString(test.name);
                    stream.writeString(test.encapsulate());
                }
                Toast.makeText(context, language.language_delved_name + " saved", Toast.LENGTH_SHORT).show();
            }
            stream.close();
        } catch (Exception e) {
            debug("Exception: " + e.toString());
        }
    }

    public void recoverBackUp(Context context) {
        DatabaseBackUpManager database = new DatabaseBackUpManager(context);
        database.openWritableDatabase();
        try {
            InStream stream = new InStream(backupfile);

            int nIdiomas = stream.readInt();
            for (int i = 0; i < nIdiomas; i++) {    // Por cada idioma
                int Lcode = stream.readInt();
                String Lname = stream.readString();
                int Lsettings = stream.readInt();

                //      debug((i + 1) + ". " + Lname + "[" + Lcode + "]");
                Language language = database.insertLanguage(Lcode, Lname, Lsettings);

                // Statistics
                language.statistics.intentos = stream.readInt();
                language.statistics.aciertos1 = stream.readInt();
                language.statistics.aciertos2 = stream.readInt();
                language.statistics.aciertos3 = stream.readInt();
                language.statistics.fallos = stream.readInt();
                database.insertStatistics(language.statistics);

                int nWords = stream.readInt();
                for (int j = 0; j < nWords; j++) {   // For each WORD of the language
                    String name = stream.readString();
                    String pronunciation = stream.readString();
                    int priority = stream.readInt();
                    String inflexionsString = stream.readString();
                    database.insertWord(name, inflexionsString, language.id, pronunciation, priority);
                    //         debug(" -" + name);
                }

                int nDrawerWords = stream.readInt();
                for (int j = 0; j < nDrawerWords; j++) {    // For each entry of warehouse
                    String note = stream.readString();
                    database.insertStoreWord(note, language.id);
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
                    String tName = stream.readString();
                    String tContent = stream.readString();
                    database.insertTest(tName, tContent, language.id);
                }
            }
            stream.close();
        } catch (Exception e) {
            debug("Exception: " + e.toString() + "\n\n");
            e.printStackTrace();
            database.closeAndCancelDatabase();
            return;
        }
        database.closeAndRollDatabase();
    }

    private static void debug(String text) {
        if (Settings.DEBUG)
            android.util.Log.d("##BackUp##", text);
    }

}
