package com.delvinglanguages.core.game;

import java.util.ArrayList;

import android.util.Log;

import com.delvinglanguages.core.DReference;

public class TestGame extends Game {

	private static final long serialVersionUID = -6521732541870366408L;

	public TestGame(ArrayList<DReference> references) {
		super(references);
	}

	public ArrayList<DReference> getWords(int numero, int types) {
		ArrayList<DReference> res = new ArrayList<DReference>(numero);
		while (running);
		if (numero > references.size()) numero = references.size();
		while (numero > 0) {
			Integer priority = priorityMap.getMaxKey();
			ArrayList<DReference> set = priorityMap.get(priority);

			DReference ref = set.remove(nextInt(set.size()));
			Log.v(DEBUG, "Getting ref with prior:"+priority);
			if ((ref.type & types) != 0) {
				res.add(ref);
				numero--;
			}

			if (set.isEmpty()) {
				priorityMap.remove(priority);
				if (priorityMap.size() == 0) {
					break;
				}
			}
		}
		return res;
	}

}
