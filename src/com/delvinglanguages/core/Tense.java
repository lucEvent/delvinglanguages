package com.delvinglanguages.core;

public class Tense {

	private static final String SEPARATOR = "&";

	public final int id;

	public String forms[];
	public String pronuntiations[];

	private int options;

	public Tense(int id, String forms, String prons, int options) {
		this.id = id;
		this.forms = forms.split(SEPARATOR);
		this.pronuntiations = prons.split(SEPARATOR);
		this.options = options;
	}

	public int getSize() {
		return forms.length;
	}

	public String getForms() {
		return queueString(forms);
	}

	public String getPronuntiations() {
		return queueString(pronuntiations);
	}

	public int getOptions() {
		return options;
	}

	public static String queueString(String[] data) {
		StringBuilder res = new StringBuilder(data[0]);
		for (int i = 1; i < data.length; i++) {
			res.append(SEPARATOR).append(data[i]);
		}
		return res.toString();
	}

}
