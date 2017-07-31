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
import com.delvinglanguages.kernel.DelvingList;
import com.delvinglanguages.kernel.DrawerReference;
import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.kernel.RecordManager;
import com.delvinglanguages.kernel.subject.Subject;
import com.delvinglanguages.kernel.subject.SubjectPair;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.DelvingLists;
import com.delvinglanguages.kernel.util.DrawerReferences;
import com.delvinglanguages.kernel.util.Statistics;
import com.delvinglanguages.kernel.util.SubjectPairs;
import com.delvinglanguages.kernel.util.Subjects;
import com.delvinglanguages.kernel.util.Tests;
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

            int nLists = version == VERSION2 ? stream.readInt() : version;
            handler.obtainMessage(MessageListener.MESSAGE, context.getString(R.string.msg_found_languages, nLists)).sendToTarget();
            for (int i = 0; i < nLists; i++) {    // For each list
                int list_id = version == VERSION2 ? stream.readInt() : Math.abs(new Random().nextInt());
                int list_lang_codes = stream.readInt();
                String list_name = stream.readString();
                int list_settings = stream.readInt();
                list_id = database.insertDelvingList(list_id, list_lang_codes & 0xFF, list_lang_codes >> 16, list_name, list_settings);

                // Statistics
                Statistics statistics = new Statistics(list_id);
                statistics.attempts = stream.readInt();
                statistics.hits_at_1st = stream.readInt();
                statistics.hits_at_2nd = stream.readInt();
                statistics.hits_at_3rd = stream.readInt();
                statistics.misses = stream.readInt();
                database.updateStatistics(statistics);

                int nReferences = stream.readInt();
                for (int j = 0; j < nReferences; j++) {   // For each WORD of the list
                    int id = version == VERSION2 ? stream.readInt() : j + 1;
                    String name = stream.readString();
                    String pronunciation = stream.readString();
                    int priority = stream.readInt();
                    String inflexionsString = stream.readString();
                    database.insertReference(id, list_id, name, inflexionsString, pronunciation, priority);
                    //         debug(" -" + name);
                }

                int nDrawerReferences = stream.readInt();
                for (int j = 0; j < nDrawerReferences; j++) {    // For each entry of warehouse
                    int id = version == VERSION2 ? stream.readInt() : j + 1;
                    String note = stream.readString();
                    database.insertDrawerReference(id, list_id, note);
                }

                int nSubjects = stream.readInt();
                for (int j = 0; j < nSubjects; j++) { // Por cada theme
                    int id = version == VERSION2 ? stream.readInt() : j + 1;
                    String thName = stream.readString();
                    SubjectPairs thPairs = new SubjectPairs();
                    int nthPairs = stream.readInt();
                    for (int k = 0; k < nthPairs; k++) {
                        String thpDelv = stream.readString();
                        String thpNatv = stream.readString();
                        thPairs.add(new SubjectPair(thpDelv, thpNatv));
                    }
                    database.insertSubject(id, list_id, thName, thPairs);
                }

                int nTests = stream.readInt();
                for (int j = 0; j < nTests; j++) {  // Por cada Test
                    int id = version == VERSION2 ? stream.readInt() : j + 1;
                    String test_name = stream.readString();
                    int test_runTimes = stream.readInt();
                    String test_content = stream.readString();
                    int subject_id = version == VERSION2 ? stream.readInt() : -1;
                    database.insertTest(id, list_id, test_name, test_runTimes, test_content, subject_id);
                }
                String message = (i + 1) + ". " + list_name + "\n  [" +
                        context.getString(R.string.msg_n_references, nReferences) + "\n  " +
                        context.getString(R.string.msg_n_drawerreferences, nDrawerReferences) + "\n  " +
                        context.getString(R.string.msg_n_subjects, nSubjects) + "\n  " +
                        context.getString(R.string.msg_n_tests, nTests) + "]";

                handler.obtainMessage(MessageListener.MESSAGE, message).sendToTarget();
            }
            stream.close();

            RecordManager.appImport(nLists);

        } catch (Exception e) {

            handler.obtainMessage(MessageListener.ERROR).sendToTarget();
            AppSettings.printerror("[BUM] Exception in restoreData", e);
            database.closeWritableDatabase();
            return;
        }
        database.closeWritableDatabase();
        new KernelManager(context).invalidateData();
        handler.obtainMessage(AppCode.IMPORT_SUCCESSFUL).sendToTarget();
    }


    public void backupData(Context context, String filename, DelvingLists delvingLists)
    {
        BackUpDatabaseManager database = new BackUpDatabaseManager(context);
        database.openReadableDatabase();
        try {
            // Getting backup file
            handler.obtainMessage(MessageListener.MESSAGE_INT, R.string.msg_preparing_file).sendToTarget();

            String state = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                handler.obtainMessage(MessageListener.ERROR, context.getString(R.string.msg_could_access_disc)).sendToTarget();
                return;
            }
            File externalDir = Environment.getExternalStorageDirectory();

            File folder = new File(externalDir, "Delving");
            folder.mkdirs();
            File backupfile = new File(folder, filename + ".delv");

            // Starting backup
            OutStream stream = new OutStream(backupfile);
            stream.writeInt(CURRENT_VERSION);

            stream.writeInt(delvingLists.size());
            for (DelvingList delvingList : delvingLists) { // For each list
                handler.obtainMessage(MessageListener.MESSAGE, context.getString(R.string.msg_saving_, delvingList.name)).sendToTarget();
                stream.writeInt(delvingList.id);
                stream.writeInt(delvingList.getCodes());
                stream.writeString(delvingList.name);
                stream.writeInt(delvingList.settings);

                Statistics e = delvingList.statistics;   // Statistics
                stream.writeInt(e.attempts);
                stream.writeInt(e.hits_at_1st);
                stream.writeInt(e.hits_at_2nd);
                stream.writeInt(e.hits_at_3rd);
                stream.writeInt(e.misses);

                DReferences references = database.readReferences(delvingList.id);
                stream.writeInt(references.size());
                for (DReference w : references) {  // For each reference of the list
                    stream.writeInt(w.id);
                    stream.writeString(w.name);
                    stream.writeString(w.pronunciation);
                    stream.writeInt(w.priority);
                    stream.writeString(w.getInflexions().wrap());
                }

                DrawerReferences drawer = database.readDrawerReferences(delvingList.id);
                stream.writeInt(drawer.size());
                for (DrawerReference n : drawer) {    // For each reference in the drawer of the list
                    stream.writeInt(n.id);
                    stream.writeString(n.name);
                }

                Subjects subjects = database.readSubjects(delvingList.id);
                stream.writeInt(subjects.size());
                for (Subject subject : subjects) {    // For each subject of the list
                    stream.writeInt(subject.id);
                    stream.writeString(subject.getName());
                    stream.writeInt(subject.getPairs().size());
                    for (SubjectPair pair : subject.getPairs()) {   // For each subjectpair of the subject
                        stream.writeString(pair.inDelved);
                        stream.writeString(pair.inNative);
                    }
                }

                Tests tests = database.readTests(delvingList.id);
                stream.writeInt(tests.size());
                for (Test test : tests) {   // For each test of the list
                    stream.writeInt(test.id);
                    stream.writeString(test.name);
                    stream.writeInt(test.getRunTimes());
                    stream.writeString(Test.wrapContent(test));
                    stream.writeInt(test.subject_id);
                }

                String message = context.getString(R.string.msg_language_saved_, delvingList.name, references.size(), drawer.size(), subjects.size(), tests.size());
                handler.obtainMessage(MessageListener.MESSAGE, message).sendToTarget();
            }
            stream.close();

            RecordManager.appExport(delvingLists.size());

            handler.obtainMessage(AppCode.EXPORT_SUCCESSFUL).sendToTarget();

        } catch (Exception e) {
            handler.obtainMessage(MessageListener.ERROR).sendToTarget();
            AppSettings.printerror("[BUM] Exception in backupData", e);
        } finally {

            database.closeReadableDatabase();

        }

    }

}
