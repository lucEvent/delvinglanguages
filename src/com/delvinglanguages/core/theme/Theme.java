package com.delvinglanguages.core.theme;

import com.delvinglanguages.kernel.set.ThemePairs;

public class Theme {

	public final int id;

	private String name;
	private ThemePairs pairs;

	public Theme(int id, String name) {
		this(id, name, new ThemePairs());
	}

	public Theme(int id, String name, ThemePairs pairs) {
		this.id = id;
		this.name = name;
		this.pairs = pairs;
	}

	public String getName() {
		return name;
	}

	public ThemePairs getPairs() {
		return pairs;
	}

	public void setName(String new_name) {
		this.name = new_name;
	}

	public void setPairs(ThemePairs pairs) {
		this.pairs = pairs;
	}

	public void addPair(ThemePair new_pair) {
		pairs.add(new_pair);
	}

	public void removePair(int position) {
		pairs.remove(position);
	}

	public void removePair(ThemePair pair) {
		pairs.remove(pair);
	}

}
