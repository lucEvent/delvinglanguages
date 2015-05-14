package com.delvinglanguages.kernel.game;

import java.util.Random;

import android.util.Log;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.set.DReferences;
import com.delvinglanguages.kernel.set.TestReferenceStates;

public class Game extends Random {

	private static final long serialVersionUID = 4488522107985935293L;

	protected static final String DEBUG = "##Game##";

	protected DReferences references;
	protected PriorityMap priorityMap;
	protected boolean running;

	public Game(DReferences references) {
		this.references = references;
		createPriorityMap();
	}

	private void createPriorityMap() {
		// Log.d(DEBUG, "############ Creating map #############");
		running = true;
		new Thread(new Runnable() {

			@Override
			public void run() {
				priorityMap = new PriorityMap();

				for (int i = 0; i < references.size(); i++) {
					DReference ref = references.get(i);
					DReferences set = priorityMap.get(ref.priority);
					if (set == null) {
						set = new DReferences();
						priorityMap.put(ref.priority, set);
					}
					// Log.v(DEBUG, "...."+ref.name+": "+ref.priority);
					set.add(ref);
				}
				running = false;
				// Log.d(DEBUG, "############ Map created #############");
			}
		}).start();
	}

	public DReference nextReference() {
		while (running)
			;

		Integer priority = priorityMap.getMaxKey();
		DReferences set = priorityMap.get(priority);

		DReference p = set.remove(nextInt(set.size()));
		if (set.isEmpty()) {
			priorityMap.remove(priority);
			if (priorityMap.size() == 0) {
				createPriorityMap();
			}
		}
		Log.v(DEBUG, "Getting ref with prior:" + priority);
		return p;
	}

	public char nextLetter(boolean upperCase) {
		if (upperCase) {
			return (char) (nextInt('Z' - 'A' + 1) + 'A');
		} else {
			return (char) (nextInt('z' - 'a' + 1) + 'a');
		}
	}

	public int nextPosition(TestReferenceStates refstates) {
		int cand;
		do {
			cand = nextInt(refstates.size());
		} while (refstates.get(cand).passed);
		return cand;
	}
}
