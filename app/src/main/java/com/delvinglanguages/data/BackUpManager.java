package com.delvinglanguages.data;

import android.os.Environment;

import com.delvinglanguages.net.internal.TaskHandler;
import com.delvinglanguages.settings.Settings;

import java.io.File;

public class BackUpManager {

    private TaskHandler handler;

    public BackUpManager(TaskHandler handler) {
        this.handler = handler;
    }

    public void createBackUp() {
        handler.onTaskStart();
        // 0. Init counters
        int c_languages = 0;
        int c_words = 0;
        int c_drawers = 0;
        int c_tests = 0;
        int c_themes = 0;

        //// TODO: 31/12/2015
    }

    private File getBackUpFile() {
        String estado = Environment.getExternalStorageState();
        if (!estado.equals(Environment.MEDIA_MOUNTED)) {
            debug("No nay media montada\n");
            return null;
        }
        File externalDir = Environment.getExternalStorageDirectory();

        File folder = new File(externalDir.getAbsolutePath() + File.separator + "Delving");
        folder.mkdirs();
        return new File(folder, "backup.delv");
    }

    private static void debug(String text) {
        if (Settings.DEBUG)
            android.util.Log.d("##BackUpManager##", text);
    }

}
