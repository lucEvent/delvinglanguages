package com.delvinglanguages.kernel.test;

import com.delvinglanguages.core.DReference;

public class TestReferenceState {

	public DReference reference;
	public boolean passed;

	public int fallos_match;
	public int fallos_complete;
	public int fallos_write;

	public TestReferenceState(DReference reference) {
		this.reference = reference;
		passed = false;
		fallos_match = 0;
		fallos_complete = 0;
		fallos_write = 0;
	}
}
