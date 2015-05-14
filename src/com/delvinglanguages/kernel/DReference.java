package com.delvinglanguages.kernel;

import java.util.ArrayList;

import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.kernel.set.Words;

public class DReference implements Comparable<DReference> {

	private static final String DEBUG = "##DReference##";

	public final int id;

	private Translation ref;

	public ArrayList<Link> links;

	public int priority;

	public DReference(String ref) {
		id = ref.hashCode();
		this.ref = new Translation(ref, 0);
		priority = Integer.MIN_VALUE;
		links = new ArrayList<Link>();
	}

	public DReference(String ref, int type) {
		this(ref);
		this.ref.type = type;
	}

	public String getName() {
		return ref.name;
	}

	public int getType() {
		return ref.type;
	}

	public Translations getTranslations() {
		Translations res = new Translations();
		for (Link link : links) {
			res.add(link.reference.ref);
		}
		return res;
	}

	public void addReference(DReference reference, Word owner) {
		links.add(new Link(reference, owner));
		// type |= owner.getType();
		if (owner.getPriority() > priority) {
			priority = owner.getPriority();
		}
	}

	public void addReference(DReference reference, Word owner, int type) {
		addReference(reference, owner);
		ref.type |= type;
	}

	public void removeReferencesto(Word enDelv) {
		for (int i = 0; i < links.size(); i++) {
			if (links.get(i).owner.id == enDelv.id) {
				links.remove(i).reference.removeReference(this);
				i--;
			}
		}
		// type = 0;
		priority = Integer.MIN_VALUE;
		for (Link link : links) {
			// type |= link.owner.getType();
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

	public Character getCap() {
		return ref.name.charAt(0);
	}

	public boolean isNoun() {
		return (ref.type & 0x1) != 0;
	}

	public boolean isVerb() {
		return (ref.type & 0x2) != 0;
	}

	public boolean isAdjective() {
		return (ref.type & 0x4) != 0;
	}

	public boolean isAdverb() {
		return (ref.type & 0x8) != 0;
	}

	public boolean isPhrasalVerb() {
		return (ref.type & 0x10) != 0;
	}

	public boolean isExpression() {
		return (ref.type & 0x20) != 0;
	}

	public boolean isOther() {
		return (ref.type & 0x40) != 0;
	}

	public String getTranslation() {
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < links.size(); i++) {
			DReference ref = links.get(i).reference;
			if (i != 0) {
				res.append(", ");
			}
			res.append(ref.ref.name);
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
			list.add(link.reference.ref.name);
		}
		return list;
	}

	public String getPronunciation() {
		return links.get(0).owner.getPronunciation();
	}

	public Words getPureOwners() {
		Words res = new Words();
		res.add(links.get(0).owner);
		for (int i = 1; i < links.size(); i++) {
			Word p = links.get(i).owner;
			if (!res.contains(p)) {
				res.add(p);
			}
		}
		return res;
	}

	public int getDBID() {
		return links.get(0).owner.id;
	}

	@Override
	public int compareTo(DReference another) {
		int comp = IDDelved.collator.compare(ref.name, another.ref.name);
		if (comp != 0) {
			return comp;
		}
		return Integer.valueOf(this.ref.type).compareTo(another.ref.type);
	}

}
