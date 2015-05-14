package com.delvinglanguages.kernel;

import java.util.ArrayList;

import android.content.Context;
import android.util.Pair;

public class TenseKernelControl extends KernelControl {

	public TenseKernelControl(Context context) {
		super(context);
	}

	public Tense getTense(DReference verb, int tense) {
		return database.readTense(verb.getDBID(), tense, verb.getName());
	}

	public void addTense(DReference verb, int tenseId, String forms, String pronunciations) {
		addTense(verb.getDBID(), verb.getName(), tenseId, forms, pronunciations);
	}

	public void addTense(int verbId, String verbName, int tenseId, String forms, String pronunciations) {
		database.insertTense(currentLanguage.getID(), verbId, verbName, tenseId, forms, pronunciations);
	}

	public ArrayList<Pair<Integer, Tense>> getTenses() {
		return database.readTenses(currentLanguage.getID());
	}

}
