package com.delvinglanguages.kernel;

import java.util.ArrayList;

import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.settings.Settings;

public class Word implements Comparable<Word> {

	public static final int INITIAL_PRIORITY = 100;
	public static final int NOUN = 0;
	public static final int VERB = 1;
	public static final int ADJECTIVE = 2;
	public static final int ADVERB = 3;
	public static final int PHRASAL = 4;
	public static final int EXPRESSION = 5;
	public static final int OTHER = 6;

	public final int id;

	protected String nombre;
	protected Translations traducciones;
	protected String pronunciacion;
	protected int prioridad;

	public Word(int id, String name, Translations translations, String pronunciation, int priority) {
		this.id = id;
		nombre = name;
		traducciones = translations;
		pronunciacion = pronunciation;
		prioridad = priority;
	}

	/** ******************************* Getters ******************************* **/
	public String getName() {
		return nombre;
	}

	public ArrayList<String> getNameArray() {
		return Translation.formatArray(nombre);
	}

	public String getTranslationString() {
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < traducciones.size(); i++) {
			if (i != 0) {
				res.append(", ");
			}
			res.append(traducciones.get(i).name);
		}
		return res.toString();
	}

	public ArrayList<String> getTranslationArray() {
		ArrayList<String> res = new ArrayList<String>();

		for (Translation translation : traducciones) {
			res.addAll(translation.getItems());
		}
		return res;
	}

	public Translations getTranslations() {
		return traducciones;
	}

	public String getPronunciation() {
		return pronunciacion;
	}

	public int getType() {
		int res = 0;
		for (Translation t : traducciones) {
			res |= t.type;
		}
		return res;
	}

	public int getPriority() {
		return prioridad;
	}

	public Character getCap() {
		return nombre.charAt(0);
	}

	public Word getInverse(boolean todelv) {
		StringBuilder name = new StringBuilder();
		Translations translations = new Translations();
		if (todelv) {
			name.append(traducciones.get(0).name);

			ArrayList<String> s_translations = getNameArray();
			for (int i = 0; i < s_translations.size(); i++) {
				translations.add(new Translation(-1, s_translations.get(i), traducciones.get(i).type));
			}
		} else {
			for (int i = 0; i < traducciones.size(); i++) {
				Translation traduccion = traducciones.get(i);
				if (i != 0) {
					name.append(", ");
				}
				name.append(traduccion.name);
				translations.add(new Translation(-1, nombre, traduccion.type));
			}
		}
		return new Word(id, name.toString(), translations, pronunciacion, prioridad);
	}

	/** ******************************* Askers ******************************* **/
	@Override
	public boolean equals(Object word) {
		return this.id == ((Word) word).id;
	}

	/** ******************************* Setters ******************************* **/
	public void setChanges(String name, Translations translations, String pronunciation) {
		nombre = name;
		traducciones = translations;
		pronunciacion = pronunciation;
	}

	public void updatePriority(int value) {
		prioridad = value;
	}

	public void setTranslations(Translations translations) {
		traducciones = translations;
	}

	/** *************************** Interfaces ******************************* **/

	@Override
	public int compareTo(Word another) {
		return nombre.compareToIgnoreCase(another.nombre);
	}

	public static String format(String s) { // Funcion a revisar y rehacer de 0
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
				debug("StringIndexOutOfBoundsException con:" + s);
				char car;
				int size = res.length();
				do {
					res.delete(size, size + 1);
					res.charAt(index);
					car = res.charAt(size - 1);
				} while (car == ',' || car == ' ');
			}
			index = res.indexOf(",", index + 1);
		}
		return res.toString();
	}

	private static void debug(String text) {
		if (Settings.DEBUG)
			android.util.Log.d("##Word##", text);
	}

}
