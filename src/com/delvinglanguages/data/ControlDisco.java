package com.delvinglanguages.data;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.os.Environment;

import com.delvinglanguages.kernel.HistorialItem;
import com.delvinglanguages.settings.Settings;

public class ControlDisco extends IOOperations {

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
		int result = 0;
		try {
			File file = new File(folder, STATE);
			// file.delete();

			initInputBuffer(file);
			result = readInteger();
			bufferIn.close();
		} catch (Exception e) {
			debug("Exception: " + e.toString());
		}
		return result;
	}

	public void saveLastLaguage(int position) {
		try {
			initOutputBuffer(new File(folder, STATE));
			writeInteger(position);
			bufferOut.close();
		} catch (Exception e) {
			debug("Exception: " + e.toString());
		}
	}

	public String[] readParams(String settings, int elems) {
		String[] params = new String[elems];
		try {
			initInputBuffer(new File(folder, settings));

			for (int i = 0; i < elems; i++) {
				params[i] = readString();
			}

			bufferIn.close();
		} catch (Exception e) {
			debug("Exception: " + e.toString());
			params = null;
		}
		return params;
	}

	public void saveParams(String settings, String[] params) {
		try {
			initOutputBuffer(new File(folder, settings));

			for (int i = 0; i < params.length; i++) {
				writeString(params[i]);
			}

			bufferOut.close();
		} catch (Exception e) {
			debug("Exception: " + e.toString());
		}

	}

	public ArrayList<HistorialItem> getHistorial() {
		ArrayList<HistorialItem> res = new ArrayList<HistorialItem>();
		try {
			initInputBuffer(new File(folder, "historial.dat"));

			int elems = readInteger();
			for (int i = 0; i < elems; i++) {
				String content = readString();
				res.add(new HistorialItem(content));
			}

			bufferIn.close();
		} catch (Exception e) {
			debug("Exception: " + e.toString());
		}
		return res;
	}

	public void saveHistorial(ArrayList<HistorialItem> H) {
		try {
			initOutputBuffer(new File(folder, "historial.dat"));

			int elems = H.size();
			writeInteger(elems);
			for (int i = 0; i < elems; i++) {
				writeString(H.get(i).toString());
			}

			bufferIn.close();
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
			initInputBuffer(file);

			byte[] buffer = data.toString().getBytes();
			bufferOut.write(buffer, 0, buffer.length);
			bufferOut.close();
		} catch (Exception e) {
			debug("Exception: " + e.toString());
		}
		return file;
	}

	public void copyFile(File forig, File fcopy) {
		try {
			initInputBuffer(forig);

			bufferOut = new BufferedOutputStream(new FileOutputStream(fcopy.getAbsolutePath()));

			int len;
			byte[] buffer = new byte[1024];
			while ((len = bufferIn.read(buffer, 0, buffer.length)) > 0) {
				bufferOut.write(buffer, 0, len);
			}
			bufferIn.close();
			bufferOut.close();
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
