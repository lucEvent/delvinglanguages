package com.delvinglanguages.kernel.set;

import java.util.ArrayList;
import java.util.TreeSet;

import com.delvinglanguages.kernel.DReference;

public class DReferences extends ArrayList<DReference> {

	private static final long serialVersionUID = -5980092409698427943L;

	public DReferences() {
		super();
	}

	public DReferences(TreeSet<DReference> collection) {
		super(collection);
	}

	public DReferences(int capacity) {
		super(capacity);
	}

}
