package com.delvinglanguages.data;

import android.os.Environment;

import com.delvinglanguages.data.util.InStream;
import com.delvinglanguages.data.util.OutStream;
import com.delvinglanguages.kernel.HistorialItem;
import com.delvinglanguages.settings.Settings;

import java.io.File;
import java.util.ArrayList;

public class ControlDisco {

    private static final String FOLDERNAME = "Delving";
    private static final String STATE = "state.dat";

    private static File folder;

    public ControlDisco() {
        String estado = Environment.getExternalStorageState();
        if (!estado.equals(Environment.MEDIA_MOUNTED)) {
            debug("No nay media montada\n");
            return;
        }
        File extDir = Environment.getExternalStorageDirectory();

        folder = new File(extDir.getAbsolutePath() + File.separator + FOLDERNAME);
        folder.mkdirs();
    }

    public int getLastLanguage() {
        int result = -1;
        try {
            InStream in = new InStream(new File(folder, STATE));
            result = in.readInt();
            in.close();
        } catch (Exception e) {
            debug("Exception: " + e.toString());
        }
        return result;
    }

    public void saveLastLaguage(int position) {
        try {
            OutStream out = new OutStream(new File(folder, STATE));
            out.writeInt(position);
            out.close();
        } catch (Exception e) {
            debug("Exception: " + e.toString());
        }
    }

    public String[] readParams(String settings) {
        try {
            InStream in = new InStream(new File(folder, settings));

            String[] params = new String[in.readInt()];
            for (int i = 0; i < params.length; i++) {
                params[i] = in.readString();
            }

            in.close();
            return params;
        } catch (Exception e) {
            debug("Exception: " + e.toString());
            return null;
        }
    }

    public void saveParams(String settings, String[] params) {
        try {
            OutStream out = new OutStream(new File(folder, settings));
            out.writeInt(params.length);
            for (String param : params) {
                out.writeString(param);
            }
            out.close();
        } catch (Exception e) {
            debug("Exception: " + e.toString());
        }

    }

    public ArrayList<HistorialItem> getHistorial() {
        ArrayList<HistorialItem> res = new ArrayList<HistorialItem>();
        try {
            InStream in = new InStream(new File(folder, "historial.dat"));
            int elems = in.readInt();
            for (int i = 0; i < elems; i++) {
                String content = in.readString();
                res.add(new HistorialItem(content));
            }
            in.close();
        } catch (Exception e) {
            debug("Exception: " + e.toString());
        }
        return res;
    }

    public void saveHistorial(ArrayList<HistorialItem> historial) {
        try {
            OutStream out = new OutStream(new File(folder, "historial.dat"));
            out.writeInt(historial.size());
            for (HistorialItem hitem : historial) {
                out.writeString(hitem.toString());
            }
            out.close();
        } catch (Exception e) {
            debug("Exception: " + e.toString());
        }
    }

    public File getFile(String name) {
        File file = new File(folder, name);
        if (!file.exists()) {
            file = null;
        }
        return file;
    }

    public File saveFile(String name, StringBuilder data) {
        File file = new File(folder, name);
        try {
            OutStream out = new OutStream(file);
            byte[] buffer = data.toString().getBytes();
            out.write(buffer, 0, buffer.length);
            out.close();
        } catch (Exception e) {
            debug("Exception: " + e.toString());
        }
        return file;
    }

    public void copyFile(File forig, File fcopy) {
        try {
            InStream in = new InStream(forig);
            OutStream out = new OutStream(new File(fcopy.getAbsolutePath()));

            int len;
            byte[] buffer = new byte[1024];
            while ((len = in.read(buffer, 0, buffer.length)) > 0) {
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            debug("Exception: " + e.toString());
        }
    }

    public File createTempFile(String prefix, String sufix) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile(prefix, sufix, folder);
        } catch (Exception e) {
            debug("Exception en locationForImage: " + e.toString());
        }
        return tempFile;
    }

    private void debug(String text) {
        if (Settings.DEBUG)
            android.util.Log.d("##ControlDisco##", text);
    }

}
