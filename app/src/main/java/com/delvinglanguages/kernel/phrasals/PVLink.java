package com.delvinglanguages.kernel.phrasals;

import java.util.ArrayList;

public class PVLink implements Comparable<PVLink>, java.io.Serializable {

	private static final long serialVersionUID = 7123911627838073756L;

	public String name;
	public ArrayList<PVLink> links;

	public PVLink(String name) {
		this.name = name;
		this.links = new ArrayList<PVLink>();
	}

	public void linkTo(PVLink link) {
		links.add(link);
	}

	@Override
	public int compareTo(PVLink another) {
		return this.name.compareToIgnoreCase(another.name);
	}

}
