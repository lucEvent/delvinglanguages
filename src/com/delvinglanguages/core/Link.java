package com.delvinglanguages.core;

public class Link {
	
	public DReference reference;
	public Word owner;

	public Link(DReference reference, Word owner) {
		this.reference = reference;
		this.owner = owner;
	}
}
