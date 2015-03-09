package com.delvinglanguages.core;

import java.util.ArrayList;

public class DReference implements Comparable<DReference> {

	private static final String DEBUG = "##DReference##";

	public final int id;

	public String name;

	public ArrayList<Link> links;

	public int type, priority;

	public DReference(String ref) {
		id = ref.hashCode();
		name = ref;
		type = 0;
		priority = Integer.MIN_VALUE;
		links = new ArrayList<Link>();
	}

	public void addReference(DReference reference, Word owner) {
		links.add(new Link(reference, owner));
		type |= owner.getType();
		if (owner.getPriority() > priority) {
			priority = owner.getPriority();
		}
	}

	public void removeReferencesto(Word enDelv) {
		for (int i = 0; i < links.size(); i++) {
			if (links.get(i).owner.id == enDelv.id) {
				links.remove(i).reference.removeReference(this);
				i--;
			}
		}
		type = 0;
		priority = Integer.MIN_VALUE;
		for (Link link : links) {
			type |= link.owner.getType();
			if (link.owner.getPriority() > priority) {
				priority = link.owner.getPriority();
			}
		}
	}

	public void removeReference(DReference ref) {
		for (int i = 0; i < links.size(); i++) {
			if (ref.id == links.get(i).reference.id) {
				links.remove(i);
				return;
			}
		}
	}

	@Override
	public int compareTo(DReference another) {
		return name.compareToIgnoreCase(another.name);
	}

	public Character getCap() {
		return name.charAt(0);
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
			DReference ref = links.get(i).reference;
			if (i != 0) {
				res.append(", ");
			}
			res.append(ref.name);
		}
		return res.toString();
	}

	public ArrayList<String> getTranslationArray(ArrayList<String> list) {
		if (list == null) {
			list = new ArrayList<String>(links.size());
		} else {
			list.clear();
		}

		for (Link link : links) {
			list.add(link.reference.name);
		}
		return list;
	}

	public String getPronunciation() {
		return links.get(0).owner.getPronunciation();
	}

	public ArrayList<Word> getPureOwners() {
		ArrayList<Word> res = new ArrayList<Word>();
		res.add(links.get(0).owner);
		for (int i = 1; i < links.size(); i++) {
			Word p = links.get(i).owner;
			if (!res.contains(p)) {
				res.add(p);
			}
		}
		return res;
	}

}
