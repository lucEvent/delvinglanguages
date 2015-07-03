package com.delvinglanguages.kernel;

import java.util.ArrayList;

import com.delvinglanguages.kernel.set.DReferences;
import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.settings.Settings;

public class DReference implements Comparable<DReference> {

	/** ****************** Variables ****************** **/
	public final int translationID;

	public String name;
	public String pronunciation;
	public int priority;

	private int type;

	public Words words;
	public DReferences links;

	/** ****************** Creadoras ****************** **/
	public DReference(String name, int type) {
		this(-1, name, "", -1, type);
	}

	public DReference(String name, String pronunciation, int priority) {
		this(-1, name, pronunciation, priority, -1);
	}

	public DReference(int translationID, String name, String pronunciation, int priority, int type) {
		this.translationID = translationID;
		this.name = name;
		this.pronunciation = pronunciation;
		this.priority = priority;
		this.type = type;
		words = new Words();
		links = new DReferences();
	}

	/** ****************** Getters ****************** **/
	public int getType() {
		int t = this.type;
		if (t == -1) {
			t = 0;
			for (DReference ref : links) {
				t |= ref.type;
			}
		}
		return t;
	}

	public Character getCap() {
		return name.charAt(0);
	}

	public String getTranslation() {
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < links.size(); i++) {
			if (i != 0) {
				res.append(", ");
			}
			res.append(links.get(i).name);
		}
		return res.toString();
	}

	public ArrayList<String> getTranslationArray(ArrayList<String> list) {
		if (list == null) {
			list = new ArrayList<String>(links.size());
		} else {
			list.clear();
		}
		for (DReference ref : links) {
			list.add(ref.name);
		}
		return list;
	}

	public Translations getTranslations() {
		Translations res = new Translations();
		for (DReference ref : links) {
			res.add(new Translation(ref.translationID, ref.name, ref.type));
		}
		return res;
	}

	/** ****************** Askers ****************** **/
	public boolean isNoun() {
		return (getType() & 0x1) != 0;
	}

	public boolean isVerb() {
		return (getType() & 0x2) != 0;
	}

	public boolean isAdjective() {
		return (getType() & 0x4) != 0;
	}

	public boolean isAdverb() {
		return (getType() & 0x8) != 0;
	}

	public boolean isPhrasalVerb() {
		return (getType() & 0x10) != 0;
	}

	public boolean isExpression() {
		return (getType() & 0x20) != 0;
	}

	public boolean isOther() {
		return (getType() & 0x40) != 0;
	}

	@Override
	public int compareTo(DReference another) {
		int comp = Language.collator.compare(this.name, another.name);
		return comp != 0 ? comp : Integer.valueOf(this.type).compareTo(another.type);
	}

	/** ****************** Modificadoras ****************** **/
	public void linkTo(Word word, DReference ref) {
		words.add(word);
		links.add(ref);
	}

	public void unlink(Word word, DReference ref) {
		boolean w = words.remove(word);
		boolean b = links.remove(ref);
		// debug START
		debug("Unlinkeando: [" + name + "] Link" + (b ? "SUCCESS :)" : "FATAL ERROR :("));
		debug("Unlinkeando: [" + name + "] Word" + (w ? "SUCCESS :)" : "FATAL ERROR :("));
		// debug END
	}

	private void debug(String text) {
		if (Settings.DEBUG)
			android.util.Log.d("##DReference##", text);
	}

}
