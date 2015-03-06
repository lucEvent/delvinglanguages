package com.delvinglanguages.core.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.util.Log;

import com.delvinglanguages.core.DReference;

public class Game extends Random {
	
	private static final long serialVersionUID = 4488522107985935293L;

	protected static final String DEBUG = "##Game##";

	protected ArrayList<DReference> references;
	protected PriorityMap priorityMap;
	protected boolean running;
	
	public Game(ArrayList<DReference> references) {
		this.references = references;
		createPriorityMap();
	}
	
	private void createPriorityMap() {
		//Log.d(DEBUG, "############ Creating map #############");
		running = true;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				priorityMap = new PriorityMap();
				
				for (int i = 0; i < references.size(); i++) {
					DReference ref = references.get(i);
					ArrayList<DReference> set = priorityMap.get(ref.priority);
					if (set == null) {
						set = new ArrayList<DReference>();
						priorityMap.put(ref.priority, set);
					}
		//			Log.v(DEBUG, "...."+ref.name+": "+ref.priority);
					set.add(ref);
				}
				running = false;
			//	Log.d(DEBUG, "############ Map created #############");
			}
		}).start();
	}

	public DReference nextReference() {
		while (running);

		Integer priority = priorityMap.getMaxKey();
		ArrayList<DReference> set = priorityMap.get(priority);

		DReference p = set.remove(nextInt(set.size()));
		if (set.isEmpty()) {
			priorityMap.remove(priority);
			if (priorityMap.size() == 0) {
				createPriorityMap();
			}
		}

		Log.v(DEBUG, "Getting ref with prior:"+priority);
		return p;
	}
	
	public char nextLetter(boolean upperCase) {
		if (upperCase) {
			return (char) (nextInt('Z' - 'A' + 1) + 'A');
		} else {
			return (char) (nextInt('z' - 'a' + 1) + 'a');
		}
	}
	
	public int nextPosition(boolean[] aciertos) {
		int cand;
		do {
			cand = nextInt(aciertos.length);
		} while (aciertos[cand]);
		return cand;
	}
}
