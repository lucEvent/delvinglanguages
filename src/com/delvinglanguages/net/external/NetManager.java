package com.delvinglanguages.net.external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.delvinglanguages.net.internal.NetWork;
import com.delvinglanguages.settings.Settings;

public class NetManager {

	private NetWork net;

	public NetManager(NetWork network) {
		net = network;
	}

	public StringBuilder getPage(URL page) {
		StringBuilder content = new StringBuilder();
		try {
			HttpURLConnection con = (HttpURLConnection) page.openConnection();
			con.connect();
			BufferedReader buff = new BufferedReader(new InputStreamReader((InputStream) con.getContent()));
			char[] temp = new char[2048];
			int n;
			long t = System.currentTimeMillis();
			while ((n = buff.read(temp, 0, 2048)) > 0) {
				content.append(temp, 0, n);
			}
			debug("Time off reading: " + (System.currentTimeMillis() - t) + "ms.");
			buff.close();
			con.disconnect();
		} catch (IOException ioe) {
			debug("IOError en getPage:" + ioe.getMessage());
		}
		return content;
	}

	private void debug(String text) {
		if (Settings.DEBUG)
			android.util.Log.d("##NetManager##", text);
	}

}