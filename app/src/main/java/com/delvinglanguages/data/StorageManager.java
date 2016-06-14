package com.delvinglanguages.data;

import android.os.Environment;

import com.delvinglanguages.data.util.InStream;
import com.delvinglanguages.data.util.OutStream;
import com.delvinglanguages.kernel.HistorialItem;

import java.io.File;
import java.util.ArrayList;

public class StorageManager {

    private static final String FOLDERNAME = "Delving";

    private static File folder;

    public StorageManager(boolean thisMethodisNotUsed)
    {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            System.err.println("No nay media montada\n");
            return;
        }
        File extDir = Environment.getExternalStorageDirectory();

        folder = new File(extDir.getAbsolutePath() + File.separator + FOLDERNAME);
        folder.mkdirs();
    }

    public ArrayList<HistorialItem> getHistorial()
    {
        ArrayList<HistorialItem> res = new ArrayList<>();
        try {
            InStream in = new InStream(new File(folder, "historial.dat"));
            int elems = in.readInt();
            for (int i = 0; i < elems; i++) {
                String content = in.readString();
                res.add(new HistorialItem(content));
            }
            in.close();
        } catch (Exception e) {
            System.err.println("Exception: " + e.toString());
        }
        return res;
    }

    public void saveHistorial(ArrayList<HistorialItem> historial)
    {
        try {
            OutStream out = new OutStream(new File(folder, "historial.dat"));
            out.writeInt(historial.size());
            for (HistorialItem hitem : historial) {
                out.writeString(hitem.toString());
            }
            out.close();
        } catch (Exception e) {
            System.err.println("Exception: " + e.toString());
        }
    }

}
