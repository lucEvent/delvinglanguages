package com.delvinglanguages.net.external;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.data.ControlDisco;
import com.delvinglanguages.net.internal.NetWork;

public class NetManager {

	private static final String DEBUG = "##NetManager##";

	public static final int FLAG = 0;

	private NetWork net;

	public NetManager(NetWork network) {
		net = network;
	}

	public void getFlags(final int[] flags) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				ControlDisco disco = new ControlDisco();
				for (int i = 0; i < flags.length; i++) {
					// Buscar en la carpeta
					File f = disco.getFile("flag" + flags[i]);
					if (f == null) {
						// Buscar en la red
						String url = "http://icons.iconarchive.com/icons/gosquared/flag/64/";
						switch (flags[i]) {
						case IDDelved.EN:
							url += "United-Kingdom-icon.png";
							break;
						case IDDelved.SV:
							url += "Sweden-icon.png";
							break;
						case IDDelved.FI:
							url += "Finland-icon.png";
							break;
						case IDDelved.ES:
							url += "Spain-icon.png";
							break;
						default:
							url += "United-Kingdom-icon.png";
							break;
						}
						StringBuilder sb;
						try {
							sb = getPage(new URL(url));
							f = disco.saveFile("flag" + flags[i], sb);
						} catch (MalformedURLException e) {
							Log.d(DEBUG, "Malformed ERROR" + e.toString());
						}
					}
					if (f != null) {
						net.datagram(FLAG, Integer.toString(flags[i]), f);
					}
				}
			}
		}).start();
	}

	public StringBuilder getPage(URL page) {
		StringBuilder content = new StringBuilder();
		try {
			HttpURLConnection con = (HttpURLConnection) page.openConnection();
			con.connect();
			BufferedReader buff = new BufferedReader(new InputStreamReader(
					(InputStream) con.getContent()));
			char[] temp = new char[2048];
			int n;
			long t = System.currentTimeMillis();
			while ((n = buff.read(temp, 0, 2048)) > 0) {
				content.append(temp, 0, n);
			}
			Log.d(DEBUG, "Time off reading: "
					+ (System.currentTimeMillis() - t) + "ms.");
			buff.close();
			con.disconnect();
		} catch (IOException ioe) {
			Log.d(DEBUG, "IOError en getPage:" + ioe.getMessage());
		}
		return content;
	}
}
