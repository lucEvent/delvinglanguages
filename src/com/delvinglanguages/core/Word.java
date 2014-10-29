package com.delvinglanguages.core;

import java.util.ArrayList;

import android.util.Log;

public class Word implements Comparable<Word> {

	public static final int INITIAL_PRIORITY = 100;

	private static final String DEBUG = "##Palabra##";

	public final int id;

	private String nombre;
	private StringBuilder traducciones;
	private String pronunciacion;
	private int tipo;
	private boolean enPapelera;
	private int prioridad;

	// Constructoras
	public Word(int id, String name, String trad, String pron, int type,
			boolean thrown, int priority) {
		this(id, name, new StringBuilder(trad), pron, type, thrown, priority);
	}

	public Word(int id, String name, StringBuilder trad, String pron,
			int type, boolean thrown, int priority) {
		this.id = id;
		nombre = name;
		traducciones = trad;
		pronunciacion = pron;
		tipo = type;
		enPapelera = thrown;
		prioridad = priority;
	}

	// Consultoras
	public String getName() {
		return nombre;
	}

	public String getTranslation() {
		return traducciones.toString();
	}

	public ArrayList<String> getTranslationArray(ArrayList<String> list) {
		return formatArray(list, traducciones.toString());
	}

	public String getTranslationFormated() {
		return format(traducciones.toString());
	}

	public static ArrayList<String> formatArray(ArrayList<String> list,
			String string) {
		if (list == null) {
			list = new ArrayList<String>();
		} else {
			list.clear();
		}
		int indi = 0, caps = 0;
		int indf;
		for (indf = 0; indf < string.length(); ++indf) {
			char car = string.charAt(indf);
			if (!Character.isLetter(car)) {
				if (car == ',') {
					if (caps > 0)
						continue;
					while (string.charAt(indi) == ' ') {
						indi++;
					}
					list.add(string.substring(indi, indf));
					indi = indf + 1;
				} else if (car == '(' || car == '[' || car == '{') {
					caps++;
				} else if (car == ')' || car == ']' || car == '}') {
					caps--;
				}
			}
		}
		if (caps == 0) {
			try {
				while (string.charAt(indi) == ' ') {
					indi++;
				}
			} catch (IndexOutOfBoundsException e) {
				Log.d(DEBUG, "##Error por:" + string + "##");
			}
			list.add(string.substring(indi, indf));
		}
		return list;
	}

	public static String format(String s) {
		//Funcion a revisar y rehacer de 0
		StringBuilder res = new StringBuilder(s);
		int index = res.indexOf(",");
		while (index != -1) {
			try {
				if (res.charAt(index + 1) != ' ') {
					res.insert(index + 1, " ");
				} else {
					while (res.charAt(index + 2) == ' ') {
						res.delete(index + 2, index + 3);
					}
				}
			} catch (StringIndexOutOfBoundsException e) {
				Log.d(DEBUG, "StringIndexOutOfBoundsException con:"+s);
				char car;
				int size = res.length();
				do {
					res.delete(size, size+1);
					res.charAt(index);
					car = res.charAt(size-1);
				} while (car == ',' || car == ' ');
			}
			index = res.indexOf(",", index + 1);
		}
		return res.toString();
	}

	public String getPronunciation() {
		return pronunciacion;
	}

	public int getType() {
		return tipo;
	}

	public int getPriority() {
		return prioridad;
	}

	public Character getCap() {
		return nombre.charAt(0);
	}

	public Character getNativeCap() {
		return traducciones.charAt(0);
	}

	public boolean isThrown() {
		return enPapelera;
	}

	public boolean isNoun() {
		return (tipo & 0x1) != 0;
	}

	public boolean isVerb() {
		return (tipo & 0x2) != 0;
	}

	public boolean isAdjective() {
		return (tipo & 0x4) != 0;
	}

	public boolean isAdverb() {
		return (tipo & 0x8) != 0;
	}

	public boolean isPhrasalVerb() {
		return (tipo & 0x10) != 0;
	}

	public boolean isExpression() {
		return (tipo & 0x20) != 0;
	}

	public boolean isOther() {
		return (tipo & 0x40) != 0;
	}

	// Modificadoras
	public void setChanges(String name, String trad, String pron, int type) {
		nombre = name;
		traducciones = new StringBuilder(trad);
		pronunciacion = pron;
		tipo = type;
	}

	public void updatePriority(int value) {
		prioridad += value;
	}

	public Word cloneReverse() {
		return new Word(id, traducciones.toString(), nombre, pronunciacion,
				tipo, enPapelera, prioridad);
	}

	/** ************** COMPARABLE ********************** **/

	@Override
	public int compareTo(Word another) {
		return nombre.compareToIgnoreCase(another.nombre);
	}

	@Override
	public boolean equals(Object obj) {
		Word temp = (Word) obj;
		return this.id == temp.id;
	}

}
