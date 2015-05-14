package com.delvinglanguages.kernel.game;

import java.util.HashMap;

import com.delvinglanguages.kernel.set.DReferences;

public class PriorityMap extends HashMap<Integer, DReferences> {

	private static final long serialVersionUID = -407589731976685065L;

	public PriorityMap() {
	}

	public Integer getMaxKey() {
		int res = Integer.MIN_VALUE;
		for (int priority : keySet()) {
			if (priority > res) {
				res = priority;
			}
		}
		return res;
	}

}
