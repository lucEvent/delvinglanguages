package com.delvinglanguages.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import android.util.Log;

public class Cerebro extends Random implements Runnable {

	private static final String DEBUG = "##CEREBRO##";

	private static int MARGEN = 5; // 0 to 20% listaDReferences.size()

	public class QuestionModel {
		public DReference reference;
		public String[] answers;
		public Boolean[] correct;
	}

	private ArrayList<DReference> references;
	private QuestionModel preguntaActual;
	private HashMap<Integer, ArrayList<DReference>> priorMap;
	private boolean running;

	public Cerebro(ArrayList<DReference> references) {
		this.references = references;
		createPriorityMap();
	}

	private void createPriorityMap() {
		running = true;
		new Thread(this).start();
	}

	public QuestionModel nextQuestion(int nAnswers) {
		preguntaActual = new QuestionModel();

		DReference p = nextReference();
		preguntaActual.reference = p;
		preguntaActual.answers = new String[nAnswers];
		preguntaActual.correct = new Boolean[nAnswers];

		int randompos = nextInt(nAnswers);
		preguntaActual.answers[randompos] = p.getTranslation();
		preguntaActual.correct[randompos] = true;
		for (int i = 0; i < nAnswers; ++i) {
			if (i != randompos) {
				DReference resp = references.get(nextInt(references.size()));
				if (resp == p) {
					preguntaActual.correct[i] = true;
				} else {
					preguntaActual.correct[i] = false;
				}
				preguntaActual.answers[i] = resp.getTranslation();
			}
		}
		return preguntaActual;
	}

	public QuestionModel nextWord(boolean[] aciertos, int nAnswers) {
		preguntaActual = new QuestionModel();

		int cand = nextInt(references.size());

		while (aciertos[cand]) {
			cand = nextInt(aciertos.length);
		}
		DReference p = references.get(cand);
		preguntaActual.reference = p;
		preguntaActual.answers = new String[nAnswers];
		preguntaActual.correct = new Boolean[nAnswers];

		int cand2 = nextInt(nAnswers);
		preguntaActual.answers[cand2] = p.getTranslation();
		preguntaActual.correct[cand2] = true;
		for (int i = 0; i < nAnswers; ++i) {
			if (i != cand2) {
				DReference resp = references.get(nextInt(aciertos.length));
				if (resp == p) {
					preguntaActual.correct[i] = true;
				} else {
					preguntaActual.correct[i] = false;
				}
				preguntaActual.answers[i] = resp.getTranslation();
			}
		}
		return preguntaActual;
	}

	public DReference nextReference() {
		while (running) {
		}

		Integer index = priorMap.keySet().iterator().next();
		ArrayList<DReference> set = priorMap.get(index);

		DReference p = set.remove(nextInt(set.size()));
		if (set.isEmpty()) {
			priorMap.remove(index);
			if (priorMap.size() == 0) {
				createPriorityMap();
			}
		}
		return p;
	}

	public int nextPosition(boolean[] aciertos) {
		int cand;
		do {
			cand = nextInt(aciertos.length);
		} while (aciertos[cand]);
		return cand;
	}

	private Action ischarinto(Action host, Action guest) {
		if (host.replaceBy != null) {
			return ischarinto(host.replaceBy, guest);
		}
		if (host.letter == guest.letter) {
			return host;
		}
		return null;
	}

	private Action getyoungeraction(Action action) {
		if (action.replaceBy != null) {
			return getyoungeraction(action.replaceBy);
		}
		return action;
	}

	public class Action {

		public int position;
		public char letter;
		public String string;
		public Action replaceBy;
		public int visibleUntil;

		public Action(int pos, char let, String act) {
			position = pos;
			letter = let;
			string = act;
			visibleUntil = pos;// Al menos tiene que ser visible hasta su
								// posicion
		}
	}

	public Action[] char_merger(String word, int size) {
		StringBuilder temp = new StringBuilder(word.toUpperCase());
		boolean dictionary[] = new boolean['z' - 'a' + 1];
		for (int i = 0; i < dictionary.length; i++) {
			dictionary[i] = false;
		}
		// Aplicando un filtros y moldeando
		ArrayList<Action> validChars = new ArrayList<Action>();
		int position = 0;
		while (temp.length() > 0) {
			char c = temp.charAt(0);
			if (!Character.isLetter(c)) {
				Action act = validChars.get(position - 1);
				char fin;
				if (c == '(') {
					fin = ')';
				} else if (c == '[') {
					fin = ']';
				} else if (c == '{') {
					fin = '}';
				} else {
					act.string = act.string + c;
					temp.deleteCharAt(0);
					continue;
				}
				int ifin = temp.indexOf("" + fin);
				if (ifin != -1) {
					act.string = act.string + temp.substring(0, ifin);
					temp.delete(0, ifin);
				} else {
					act.string = act.string + c;
					temp.deleteCharAt(0);
				}
			} else {
				try {
					validChars.add(new Action(position, c, "" + c));
					position++;
					temp.deleteCharAt(0);
					if (!dictionary[c - 'A']) {
						dictionary[c - 'A'] = true;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("AIOOBException por: " + c + " ("
							+ (int) c + ")");
				}
			}
		}

		// Comprovamos que las (size o validChars.size) primeras Acciones no
		// estan repetidas
		int minsize = validChars.size() < size ? validChars.size() : size;
		for (int i = 1; i < minsize; i++) {
			Action act1 = validChars.get(i);
			for (int j = 0; j < i; j++) {
				Action act2 = validChars.get(j);
				if (act1.letter == act2.letter) {
					getyoungeraction(act2).replaceBy = act1;
					validChars.remove(i);
					i--;
					minsize = validChars.size() < size ? validChars.size()
							: size;
					break;
				}
			}
		}

		// Creamos lo que será el teclado a devolver
		Action teclado[] = new Action[size];
		// Metemos las (size o validChars.size) primeras Acciones
		minsize = validChars.size() < size ? validChars.size() : size;
		for (int i = 0; i < minsize; i++) {
			int pos;
			do { // Elejimos una posicion libre al azar
				pos = nextInt(size);
			} while (teclado[pos] != null);
			Action act = validChars.remove(0);
			teclado[pos] = act;
		}
		// Si todavia quedan letras por meter:
		position = minsize + 1;
		while (!validChars.isEmpty()) {
			Action next = validChars.remove(0);
			// Miramos si la letra actual esta (necesitamos recursividad)
			boolean isit = false;
			for (int j = 0; j < teclado.length; j++) {
				Action old = ischarinto(teclado[j], next);
				if (old != null) {
					old.replaceBy = next;
					isit = true;
					break;
				}
			}
			// Si no esta buscamos la letra mas joven para reemplazar
			if (isit == false) {
				Action old = getyoungeraction(teclado[0]);
				for (int j = 1; j < teclado.length; j++) {
					Action tmpA = getyoungeraction(teclado[j]);
					if (tmpA.visibleUntil < old.visibleUntil) {
						old = tmpA;
					}
				}
				// y reemplazamos
				old.replaceBy = next;
			}
		}
		// Miramos si alguna posicion del teclado ha quedado vacia
		for (int i = 0; i < teclado.length; i++) {
			if (teclado[i] == null) {
				char c = nextLetter(true);
				for (int j = 0; j < teclado.length; j++) {
					if (teclado[j] != null && teclado[j].letter == c) {
						c = nextLetter(true);
						j = -1;
					}
				}
				teclado[i] = new Action(-1, c, "");
			}
		}
		return teclado;
	}

	public char nextLetter(boolean upperCase) {
		if (upperCase) {
			return (char) (nextInt('Z' - 'A' + 1) + 'A');
		} else {
			return (char) (nextInt('z' - 'a' + 1) + 'a');
		}
	}

	public ArrayList<DReference> getWords(int numero, int types) {
		ArrayList<DReference> res = new ArrayList<DReference>(numero);
		while (running) {
		}

		Iterator<Integer> it = new TreeSet<Integer>(priorMap.keySet()).descendingIterator();
		Main: while (it.hasNext()) {
			//debug
			Integer iiii = it.next();
			Log.d(DEBUG, "prior ->"+iiii);
			//end
			ArrayList<DReference> set = priorMap.get(iiii);
			for (DReference p : set) {
				if ((p.type & types) != 0) {
					res.add(p);
					if (numero == res.size()) {
						break Main;
					}
				}
			}
		}
		
		//debug
		while(it.hasNext()) {
			Integer iiii = it.next();
			Log.d(DEBUG, "prior ->"+iiii);
		}
		//end
		
		return res;
	}

	@Override
	public void run() {
		priorMap = new HashMap<Integer, ArrayList<DReference>>();
		for (int i = 0; i < references.size(); i++) {
			DReference p = references.get(i);
			Integer priority = p.priority / MARGEN;

			ArrayList<DReference> set = priorMap.get(priority);
			if (set == null) {
				set = new ArrayList<DReference>();
				priorMap.put(priority, set);
			}
			set.add(p);
		}
		running = false;
	}
}
