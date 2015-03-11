package com.delvinglanguages.core;

public class Tense {

	/*
	 * Spanish tense ids
	 */
	public static final int ES_PRESENT = 0;
	public static final int ES_PAST_SIMPLE_IMPERFECT = 1;
	public static final int ES_PAST_SIMPLE_PERFECT = 2;
	public static final int ES_FUTURE = 3;
	public static final int ES_PAST_PERFECT = 4;
	public static final int ES_PAST_PLUSCUAMPERFECT = 5;
	public static final int ES_FUTURE_PERFECT = 6;
	public static final int ES_SUBJUNTIVE_PRESENT = 7;
	public static final int ES_SUBJUNTIVE_PAST_SIMPLE = 8;
	public static final int ES_SUBJUNTIVE_FUTURE = 9;
	public static final int ES_SUBJUNTIVE_PAST_PERFECT = 10;
	public static final int ES_SUBJUES_IVE_PAST_PLUSCUAMPERFECT = 11;
	public static final int ES_IMPERATIVE_POSITIVE = 12;
	public static final int ES_IMPERATIVE_NEGATIVE = 13;

	/*
	 * English tense ids
	 */
	public static final int EN_PRESENT = 100;
	public static final int EN_PAST_SIMPLE = 101;
	public static final int EN_FUTURE = 102;
	public static final int EN_PRESENT_CONTINUOUS = 103;
	public static final int EN_PRESENT_PERFECT = 104;
	public static final int EN_PRESENT_PERFECT_CONTINUOUS = 105;
	public static final int EN_PAST_CONTINUOUS = 106;
	public static final int EN_PAST_PERFECT_CONTINUOUS = 107;
	public static final int EN_FUTURE_CONTINUOUS = 108;
	public static final int EN_FUTURE_PERFECT = 109;
	public static final int EN_FUTURE_GOING_TO = 110;
	public static final int EN_CONDITIONAL = 111;
	public static final int EN_IMPERATIVE = 112;

	/*
	 * 
	 * Swedish tense ids
	 */
	public static final int SV_PRESENT = 200;
	public static final int SV_SUPINUM = 201;
	public static final int SV_PRETERITUM = 202;
	public static final int SV_FUTURE = 203;
	public static final int SV_IMPERATIV = 204;

	/*
	 * Finnish tense ids
	 */
	public static final int FI_PRESENT = 300;
	public static final int FI_IMPERFECT = 301;
	public static final int FI_CONDITIONAL = 302;

	/*
	 * Tense variables
	 */
	private static final String SEPARATOR = "&";

	public final int id;

	public final int tense;

	public final String verbName;

	protected String forms[];
	protected String pronuntiations[];

	public Tense(int id, int tenseID, String verbName) {
		this.id = id;
		this.tense = tenseID;
		this.verbName = verbName;
		this.forms = null;
		this.pronuntiations = null;
	}

	public Tense(int id, int tenseID, String verbName, String forms,
			String prons) {
		this.id = id;
		this.tense = tenseID;
		this.verbName = verbName;
		this.forms = forms.split(SEPARATOR);
		this.pronuntiations = prons.split(SEPARATOR);
	}

	public int getSize() {
		return 6;
	}

	public String[] getConjugations() {
		return forms;
	}

	public String[] getPronunciations() {
		return pronuntiations;
	}

	public String getFormsString() {
		return queueString(forms);
	}

	public String getPronuntiationsString() {
		return queueString(pronuntiations);
	}

	public static String queueString(String[] data) {
		StringBuilder res = new StringBuilder(data[0]);
		for (int i = 1; i < data.length; i++) {
			res.append(SEPARATOR).append(data[i]);
		}
		return res.toString();
	}

}
