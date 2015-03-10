package com.delvinglanguages.core;

import android.content.Context;

public class TenseKernelControl extends ControlCore {

	public TenseKernelControl(Context context) {
		super(context);

	}

	public Tense getTense(DReference verb, int tense) {
		return database.getTense(actualLang.getID(), verb.id, tense, verb.name);
	}

	public void addTense(int verbId, int tenseId, String verbName,
			String forms, String pronunciations) {
		database.insertTense(actualLang.getID(), verbId, verbName, tenseId,
				forms, pronunciations);
	}

}
