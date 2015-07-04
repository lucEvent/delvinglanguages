package com.delvinglanguages.kernel;

import java.util.TreeSet;

import android.content.Context;

import com.delvinglanguages.kernel.phrasals.PhrasalVerbs;
import com.delvinglanguages.kernel.set.DReferences;
import com.delvinglanguages.kernel.set.DrawerWords;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.net.internal.BackgroundTaskMessenger;

public class LanguageKernelControl extends KernelControl {

	public LanguageKernelControl(Context context) {
		super(context);
	}

	public static Words getWords() {
		loadLanguage(currentLanguage);
		return currentLanguage.getWords();
	}

	public static DReferences getReferences() {
		return currentLanguage.getReferences();
	}

	public static Words getRemovedWords() {
		loadLanguage(currentLanguage);
		return currentLanguage.getRemovedWords();
	}

	public static DrawerWords getDrawerWords() {
		loadLanguage(currentLanguage);
		return currentLanguage.getDrawerWords();
	}

	public static TreeSet<DReference> getSubdictionary(Character cap) {
		return currentLanguage.getDiccionaryAt(cap);
	}

	public static boolean isDictionaryCreated() {
		return currentLanguage.isDictionaryCreated();
	}

	public static int getCode() {
		return currentLanguage.CODE;
	}

	public static boolean isNativeLanguage() {
		return currentLanguage.isNativeLanguage();
	}

	public static DReference getReference(String name) {
		return currentLanguage.getReference(name);
	}

	public static int getTensesArrayResId() {
		return currentLanguage.getTensesArrayResId();
	}

	public static int getSubjectArrayResId() {
		return currentLanguage.getSubjectArrayResId();
	}

	public static boolean getLanguageSettings(int mask) {
		return currentLanguage.getSettings(mask);
	}

	public static String getLanguageName() {
		return currentLanguage.getName();
	}

	public static boolean languageHas(Word word) {
		return currentLanguage.contains(word);
	}

	public static DReferences getPhrasalVerbs() {
		return currentLanguage.getPhrasalVerbs();
	}

	public static PhrasalVerbs getPhrasalVerbsManager(BackgroundTaskMessenger handler) {
		return new PhrasalVerbs(currentLanguage.CODE, currentLanguage.getPhrasalVerbs(), handler);
	}

	public static DReferences getVerbs() {
		return currentLanguage.getVerbs();
	}

}
