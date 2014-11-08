package com.delvinglanguages.core;

import java.util.ArrayList;

import com.delvinglanguages.R;

public class Historial {

	public static final int ITEM_LANG_CREATED = 0;
	public static final int ITEM_LANG_ERASED = 1;
	public static final int ITEM_LANG_INTEGRATED = 2;
	public static final int ITEM_STORE_ADDED = 3;
	public static final int ITEM_WORD_CREATED = 4;
	public static final int ITEM_WORD_VIEWED = 5;
	public static final int ITEM_WORD_MODIFIED = 6;
	public static final int ITEM_WORD_REMOVED = 7;
	public static final int ITEM_BIN_CLEARED = 8;
	public static final int ITEM_TENSE_ADDED = 9;
	public static final int ITEM_TENSE_MODIFIED = 10;
	public static final int ITEM_TEST_CREATED = 11;
	public static final int ITEM_TEST_DONE = 12;
	public static final int ITEM_TEST_REMOVED = 13;
	public static final int ITEM_PRACTISED_MATCH = 14;
	public static final int ITEM_PRACTISED_COMPLETE = 15;
	public static final int ITEM_PRACTISED_WRITE = 16;
	public static final int ITEM_SET_CHANGE_NATIVE_NAME = 17;
	public static final int ITEM_SET_CHANGE_BACKGROUND = 18;
	public static final int ITEM_SET_CHANGE_DOUBLEDIRECTION = 19;
	public static final int ITEM_CHANGE_LANGNAME = 20;
	public static final int ITEM_CHANGE_EN_PRHASAL = 21;
	public static final int ITEM_CHANGE_EN_ADJ = 22;
	public static final int ITEM_STATISTICS_CLEARED = 23;

	private ArrayList<HistorialItem> historial;

	public Historial(ArrayList<HistorialItem> historial) {
		this.historial = historial;
	}

	public void addItem(HistorialItem item) {
		historial.add(item);
	}

	public ArrayList<HistorialItem> getHistorial() {
		return historial;
	}

	public String pack() {
		StringBuilder res = new StringBuilder();
		for (HistorialItem item : historial) {
			res.append(item.toString()).append('.');
		}
		return res.toString();
	}

}
