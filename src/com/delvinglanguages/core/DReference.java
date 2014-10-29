package com.delvinglanguages.core;

import java.util.ArrayList;

import android.util.Log;

import com.delvinglanguages.debug.Debug;

public class DReference implements Comparable<DReference> {

	private static final String DEBUG = "##DReference##";

	public int id;

	public String item;

	public ArrayList<DReference> links;

	public ArrayList<Word> owners;

	public int type, priority;

	public DReference(String ref) {
		id = ref.hashCode();
		item = ref;
		type = 0;
		priority = Integer.MIN_VALUE;
		links = new ArrayList<DReference>();
		owners = new ArrayList<Word>();
	}

	private int debug = 0;

	public void addReference(DReference ref, Word owner) {
		debug++;
		links.add(ref);
		owners.add(owner);
		type |= owner.getType();
		if (owner.getPriority() > priority) {
			priority = owner.getPriority();
		}
	}

	public void removeReferencesto(Word enDelv) {
		for (int i = 0; i < owners.size(); i++) {
			if (owners.get(i).id == enDelv.id) {
				links.remove(i).removeReference(this);
				owners.remove(i);
				type = 0;
				priority = Integer.MIN_VALUE;
				for (Word owner : owners) {
					type |= owner.getType();
					if (owner.getPriority() > priority) {
						priority = owner.getPriority();
					}
				}
				i--;
			}
		}

	}

	public void removeReference(DReference ref) {
		for (int i = 0; i < links.size(); i++) {
			if (ref.id == links.get(i).id) {
				links.remove(i);
				owners.remove(i);
				return;
			}
		}
	}

	@Override
	public int compareTo(DReference another) {
		return item.compareToIgnoreCase(another.item);
	}

	public Character getCap() {
		return item.charAt(0);
	}

	public boolean isNoun() {
		return (type & 0x1) != 0;
	}

	public boolean isVerb() {
		return (type & 0x2) != 0;
	}

	public boolean isAdjective() {
		return (type & 0x4) != 0;
	}

	public boolean isAdverb() {
		return (type & 0x8) != 0;
	}

	public boolean isPhrasalVerb() {
		return (type & 0x10) != 0;
	}

	public boolean isExpression() {
		return (type & 0x20) != 0;
	}

	public boolean isOther() {
		return (type & 0x40) != 0;
	}

	public String getTranslation() {
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < links.size(); i++) {
			DReference ref = links.get(i);
			if (i != 0) {
				res.append(", ");
			}
			res.append(ref.item);
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
			list.add(ref.item);
		}
		return list;
	}

	public String getPronunciation() {
		return owners.get(0).getPronunciation();
	}

	public ArrayList<Word> getPureOwners() {
		ArrayList<Word> res = new ArrayList<Word>();
		res.add(owners.get(0));
		for (int i = 1; i < owners.size(); i++) {
			Word p = owners.get(i);
			if (!res.contains(p)) {
				res.add(p);
			}
		}
		return res;
	}

}
