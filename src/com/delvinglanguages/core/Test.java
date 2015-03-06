package com.delvinglanguages.core;

import java.util.ArrayList;

public class Test {

	public final static int STAT_DELVING = 1;
	public final static int STAT_MATCH = 2;
	public final static int STAT_COMPLETE = 3;
	public final static int STAT_WRITE = 4;
	public final static int STAT_STATISTICS = 5;

	private char p = '.';

	public class State {

		public int fallos_match;
		public int fallos_complete;
		public int fallos_write;

		public State() {
			fallos_match = 0;
			fallos_complete = 0;
			fallos_write = 0;
		}
	}

	public int id;
	public String name;

	public ArrayList<DReference> references;
	public int state;
	public boolean passed[];
	public ArrayList<State> statistics;

	public Test(ArrayList<DReference> refs) {
		id = -1;
		references = refs;
		passed = new boolean[refs.size()];
		statistics = new ArrayList<State>(refs.size());
		for (int i = 0; i < refs.size(); i++) {
			passed[i] = false;
			statistics.add(new State());
		}
	}

	public Test(int id, String name, String encapsulation) {
		this.id = id;
		this.name = name;
		String[] elems = encapsulation.split("\\.");
		int index = 0;
		// Numero de palabras
		int size = Integer.parseInt(elems[index++]);
		// ID's de las palabras (separado por puntos)
		references = new ArrayList<DReference>(size);
		for (int i = 0; i < size; i++) {
			references.add(ControlCore.getReference(elems[index++])); // Necesario
		}
		// Estado
		state = Integer.parseInt(elems[index++]);
		// Boleanos Pasados[] (separado por puntos)
		passed = new boolean[size];
		for (int i = 0; i < passed.length; i++) {
			passed[i] = Boolean.parseBoolean(elems[index++]);
		}
		// Estadisticas - se guardan: "Estado.acierto" "." "Estado.fallo" "."
		statistics = new ArrayList<State>(size);
		for (int i = 0; i < passed.length; i++) {
			State e = new State();
			e.fallos_match = Integer.parseInt(elems[index++]);
			e.fallos_complete = Integer.parseInt(elems[index++]);
			e.fallos_write = Integer.parseInt(elems[index++]);
			statistics.add(e);
		}
	}

	public String encapsulate() {
		StringBuilder res = new StringBuilder();
		// Numero de palabras
		res.append(references.size()).append(p);
		// ID's de las palabras (separado por puntos)
		for (DReference ref : references) {
			res.append(ref.name).append(p);
		}
		// Estado
		res.append(state).append(p);
		// Boleanos Pasados[] (separado por puntos)
		for (boolean pass : passed) {
			res.append(pass).append(p);
		}
		// Estadisticas - se guardan: "Estado.acierto" "." "Estado.fallo" "."
		for (State s : statistics) {
			res.append(s.fallos_match).append(p);
			res.append(s.fallos_complete).append(p);
			res.append(s.fallos_write).append(p);
		}
		return res.toString();
	}

	public void clear() {
		for (int i = 0; i < passed.length; i++) {
			passed[i] = false;
			State e = statistics.get(i);
			e.fallos_match = 0;
			e.fallos_complete = 0;
			e.fallos_write = 0;
		}
	}

	public void nextStat() {
		for (int i = 0; i < passed.length; i++) {
			passed[i] = false;
		}
	}

	public boolean isSaved() {
		if (id == -1) {
			return false;
		}
		return true;
	}
}
