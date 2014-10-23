package com.delvinglanguages.core;

import java.util.Date;

public class HistorialItem {

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

	public final int type;

	public final int itemID;

	public final String description;

	public final long time;

	public HistorialItem(int type, int id, String description, long time) {
		this.type = type;
		this.itemID = id;
		this.description = description;
		this.time = time;
	}

	public StringBuilder getComposedDescription() {
		StringBuilder res = new StringBuilder();
		switch (type) {
		case HistorialItem.ITEM_LANG_CREATED:
		case HistorialItem.ITEM_LANG_ERASED:
		case HistorialItem.ITEM_LANG_INTEGRATED:
		case HistorialItem.ITEM_STORE_ADDED:
		case HistorialItem.ITEM_WORD_CREATED:
		case HistorialItem.ITEM_WORD_VIEWED:
		case HistorialItem.ITEM_WORD_MODIFIED:
		case HistorialItem.ITEM_WORD_REMOVED:
		case HistorialItem.ITEM_BIN_CLEARED:
		case HistorialItem.ITEM_TENSE_ADDED:
		case HistorialItem.ITEM_TENSE_MODIFIED:
		case HistorialItem.ITEM_TEST_CREATED:
		case HistorialItem.ITEM_TEST_DONE:
		case HistorialItem.ITEM_TEST_REMOVED:
		case HistorialItem.ITEM_PRACTISED_MATCH:
		case HistorialItem.ITEM_PRACTISED_COMPLETE:
		case HistorialItem.ITEM_PRACTISED_WRITE:
		case HistorialItem.ITEM_SET_CHANGE_NATIVE_NAME:
		case HistorialItem.ITEM_SET_CHANGE_BACKGROUND:
		case HistorialItem.ITEM_SET_CHANGE_DOUBLEDIRECTION:
		case HistorialItem.ITEM_CHANGE_LANGNAME:
		case HistorialItem.ITEM_CHANGE_EN_PRHASAL:
		case HistorialItem.ITEM_CHANGE_EN_ADJ:
		case HistorialItem.ITEM_STATISTICS_CLEARED:

			break;
		}
		return res;
	}

}
