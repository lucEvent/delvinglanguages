package com.delvinglanguages.kernel;

import java.io.Serializable;
import java.util.ArrayList;

import com.delvinglanguages.settings.Settings;

public class Translation implements Serializable {

	private static final long serialVersionUID = 8040926703240665247L;

	public final int id;
	public String name;
	public int type;

	public Translation(int id, String translation, int type) {
		this.id = id;
		this.name = translation;
		this.type = type;
	}

	public ArrayList<String> getItems() {
		return formatArray(name);
	}

	public static ArrayList<String> formatArray(String string) {
		ArrayList<String> list = new ArrayList<String>();

		int indi = 0, caps = 0;
		int indf;
		for (indf = 0; indf < string.length(); ++indf) {
			char car = string.charAt(indf);
			if (!Character.isLetter(car)) {
				if (car == ',') {
					if (caps > 0)
						continue;
					while (string.charAt(indi) == ' ') {
						indi++;
					}
					list.add(string.substring(indi, indf));
					indi = indf + 1;
				} else if (car == '(' || car == '[' || car == '{') {
					caps++;
				} else if (car == ')' || car == ']' || car == '}') {
					caps--;
				}
			}
		}
		if (caps == 0) {
			try {
				while (string.charAt(indi) == ' ') {
					indi++;
				}
			} catch (IndexOutOfBoundsException e) {
				debug("##Error por:" + string + "##");
			}
			list.add(string.substring(indi, indf));
		}
		return list;
	}

	private static void debug(String text) {
		if (Settings.DEBUG)
			android.util.Log.d("##Translation##", text);
	}

}
