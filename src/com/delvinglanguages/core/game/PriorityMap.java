package com.delvinglanguages.core.game;

import java.util.ArrayList;
import java.util.HashMap;

import com.delvinglanguages.core.DReference;

public class PriorityMap extends HashMap<Integer, ArrayList<DReference>> {

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
