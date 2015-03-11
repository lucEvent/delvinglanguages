package com.delvinglanguages.core;

import android.content.Context;

public class TenseKernelControl extends ControlCore {

	public TenseKernelControl(Context context) {
		super(context);
	}

	public Tense getTense(DReference verb, int tense) {
		return database.getTense(verb.getDBID(), tense, verb.name);
	}

	public void addTense(DReference verb, int tenseId, String forms,
			String pronunciations) {
		database.insertTense(actualLang.getID(), verb.getDBID(), verb.name,
				tenseId, forms, pronunciations);
	}

}
