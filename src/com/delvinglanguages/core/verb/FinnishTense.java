package com.delvinglanguages.core.verb;

import com.delvinglanguages.core.Tense;

public class FinnishTense extends Tense {

	private final static String DEBUG = "##FinnishTense##";

	private static final int GROUP_1_2_VOWELS = 100;
	private static final int GROUP_2_ODA_ENDING = 200;
	private static final int GROUP_2_XDA_XVOWEL = 300;
	private static final int GROUP_2_XDA_XCONSONANT = 400;
	private static final int GROUP_3_2_CONSONANT_A = 500;
	private static final int GROUP_4_TA = 600;
	private static final int GROUP_5_ITA = 700;
	private static final int GROUP_6_ETA = 800;

	private static final int GRADATION_K = 1;
	private static final int GRADATION_KK = 2;
	private static final int GRADATION_NK = 3;
	private static final int GRADATION_P = 4;
	private static final int GRADATION_PP = 5;
	private static final int GRADATION_MP = 6;
	private static final int GRADATION_T = 7;
	private static final int GRADATION_TT = 8;
	private static final int GRADATION_NT = 9;
	private static final int GRADATION_LT = 10;
	private static final int GRADATION_RT = 11;

	private String[] sufixs = { "n", "t", "", "mme", "tte", "vat" };
	private String[] sufixsE = { "n", "t", "", "mme", "tte", "vät" };

	/*
	 * Gradations k ~ - kk ~ k nk ~ ng
	 * 
	 * pp ~ p mp ~ mm p ~ v
	 * 
	 * tt ~ t nt ~ nn lt ~ ll rt ~ rr t ~ d
	 */

	private static final String[] GRADATIONS = { "", "", "k", "ng", "v", "p",
			"mm", "d", "t", "nn", "ll", "rr" };
	private static final String[] GRADATIONS_3 = { "", "k", "kk", "nk", "p",
			"pp", "mp", "t", "tt", "nt", "lt", "rt" };

	private final int type;

	public FinnishTense(int tenseID, String verb) {
		super(0, tenseID, verb);
		type = getType();
		System.out.println("Setting type:" + type);
	}

	private int getType() {
		int type = -1;
		int lenght = verbName.length();
		char last = verbName.charAt(lenght - 1);
		char prelast = verbName.charAt(lenght - 2);
		char pre2last = verbName.charAt(lenght - 3);
		char pre3last = verbName.charAt(lenght - 4);
		if (last == 'a' || last == 'ä') {
			if (isVowel(prelast)) {
				// ending in vowel + a/ä (puhua = "to speak", tietää =
				// "to know")
				type = GROUP_1_2_VOWELS + getGradation(pre3last, pre2last);
			} else if (prelast == 'd') {
				if (pre2last == 'o' || pre2last == 'ö') {
					// ending in vowel + da (juoda = 'to drink', syödä = 'to
					// eat')
					type = GROUP_2_ODA_ENDING;
				} else if (isVowel(pre2last)) {
					// (organisoida = 'to organise')
					// (voida = 'to be able/allowed to')
					type = GROUP_2_XDA_XVOWEL;
				} else {
					// ending in two consonants + a (mennä = 'to go')
					type = GROUP_2_XDA_XCONSONANT + GRADATION_K;
				}
			} else if (prelast == 't') {
				if (pre2last == 'a' || pre2last == 'ä' || pre2last == 'o'
						|| pre2last == 'ö' || pre2last == 'u'
						|| pre2last == 'y') {
					// ending in a vowel + ta/tä
					// ata/ätä, ota/ötä, uta/ytä, but other vowels are possible
					// (tavata = "to meet", pilkata = "to mock", vastata =
					// "to answer")
					// (haluta = "to want", tarjota = "to offer")
					type = GROUP_4_TA;
				} else if (pre2last == 'i') {
					// ending in ita/itä. (tarvita = "to need")
					type = GROUP_5_ITA;
				} else if (pre2last == 'e') {
					// ending in 'eta'
					// (exceptions: parata = "to improve/become better" and
					// huonota = "to deteriorate/become worse")
					type = GROUP_6_ETA;
				} else {
					// ending in two consonants + a (mennä = 'to go')
					type = GROUP_3_2_CONSONANT_A;
				}
			} else {
				if (!isVowel(pre2last)) {
					// ending in two consonants + a (mennä = 'to go')
					type = GROUP_3_2_CONSONANT_A;
				}
			}
		}
		return type;
	}

	@Override
	public String[] getConjugations() {
		System.out.println("Taking conjugations");
		if (forms == null) {
			switch (tense) {
			case FI_PRESENT:
				forms = getPresent();
				break;
			case FI_IMPERFECT:
				forms = getPast();
				break;
			case FI_CONDITIONAL:
				forms = getConditional();
				break;
			}
		}
		return forms;
	}

	@Override
	public String[] getPronunciations() {
		return getConjugations();
	}

	private boolean isVowel(char c) {
		boolean res = false;
		if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u'
				|| c == 'ä' || c == 'ö' || c == 'y') {
			res = true;
		}
		return res;
	}

	private boolean isSpecial(char c) {
		boolean res = false;
		if (c == 'ä' || c == 'ö' || c == 'y') {
			res = true;
		}
		return res;
	}

	private int getGradation(char cf, char cs) {
		int subtype = 0;
		switch (cs) {
		case 'k':
			if (cf == 'k') {
				subtype = GRADATION_KK;
			} else if (cf == 'n') {
				subtype = GRADATION_NK;
			} else if (cf != 't') {
				subtype = GRADATION_K;
			}
			break;
		case 'p':
			if (cf == 'p') {
				subtype = GRADATION_PP;
			} else if (cf == 'm') {
				subtype = GRADATION_MP;
			} else if (isVowel(cf)) {
				subtype = GRADATION_P;
			}
			break;
		case 't':
			if (cf == 't') {
				subtype = GRADATION_TT;
			} else if (cf == 'n') {
				subtype = GRADATION_NT;
			} else if (cf == 'l') {
				subtype = GRADATION_LT;
			} else if (cf == 'r') {
				subtype = GRADATION_RT;
			} else if (cf != 's') {
				subtype = GRADATION_T;
			}
		}
		return subtype;
	}

	private String[] getParticipes() {
		String[] res = new String[2];
		switch (type) {
		case GROUP_1_2_VOWELS:

			break;
		case GROUP_2_ODA_ENDING:
		case GROUP_2_XDA_XVOWEL:
		case GROUP_2_XDA_XCONSONANT:
			break;
		case GROUP_3_2_CONSONANT_A:

			break;
		case GROUP_4_TA:

			break;
		case GROUP_5_ITA:

			break;
		case GROUP_6_ETA:

			break;
		default:

		}
		return res;
	}

	private String[] getPresent() {
		String[] res = new String[6];
		int length = verbName.length();
		int gradation = type % 100;
		char vowel1 = verbName.charAt(length - 1);
		char vowel2 = verbName.charAt(length - 2);
		String grad = GRADATIONS[gradation];
		String grad3 = GRADATIONS_3[gradation];
		String root = verbName.substring(0, length - 2 - grad3.length());
		int realtype = type - (type % 100);
		switch (realtype) {
		case GROUP_1_2_VOWELS:
			// Puhua: puhun, puhut, puhuu, puhumme, puhutte, puhuvat
			// Tietää: tiedän, tiedät, tietää, tiedämme, tiedätte, tietävät
			res[0] = root + grad + vowel2 + sufixs[0];
			res[1] = root + grad + vowel2 + sufixs[1];
			res[2] = root + grad3 + vowel2 + vowel2;
			res[3] = root + grad + vowel2 + sufixs[3];
			res[4] = root + grad + vowel2 + sufixs[4];
			res[5] = root + grad3 + vowel2
					+ (isSpecial(vowel1) ? sufixsE[5] : sufixs[5]);
			break;
		case GROUP_2_ODA_ENDING:
		case GROUP_2_XDA_XVOWEL:
			res[0] = root + sufixs[0];
			res[1] = root + sufixs[1];
			res[2] = root;
			res[3] = root + sufixs[3];
			res[4] = root + sufixs[4];
			res[5] = root + (isSpecial(vowel1) ? sufixsE[5] : sufixs[5]);
			break;
		case GROUP_2_XDA_XCONSONANT: // Tehdä
			res[0] = root + 'e' + sufixs[0];
			res[1] = root + 'e' + sufixs[1];
			res[2] = root + grad3 + 'e' + 'e';
			res[3] = root + 'e' + sufixs[3];
			res[4] = root + 'e' + sufixs[4];
			res[5] = root + grad3 + 'e'
					+ (isSpecial(vowel1) ? sufixsE[5] : sufixs[5]);
			break;
		case GROUP_3_2_CONSONANT_A:
			// Mennä: menen, menet, menee, menemme, menette, menevät
			if (verbName.toLowerCase().equals("olla")) {
				String[] olla = { "Olen", "Olet", "On", "Olemme", "Olette",
						"Ovat" };
				res = olla;
			} else {
				res[0] = root + 'e' + sufixs[0];
				res[1] = root + 'e' + sufixs[1];
				res[2] = root + "ee";
				res[3] = root + 'e' + sufixs[3];
				res[4] = root + 'e' + sufixs[4];
				res[5] = root + 'e'
						+ (isSpecial(vowel1) ? sufixsE[5] : sufixs[5]);
			}
			break;
		case GROUP_4_TA:

			break;
		case GROUP_5_ITA:

			break;
		case GROUP_6_ETA:

			break;
		default:

		}
		return res;
	}

	private String[] getPast() {
		String[] res = new String[6];
		int length = verbName.length();
		char vowel = verbName.charAt(length - 1);
		String root;
		switch (type) {
		case GROUP_1_2_VOWELS:
			// Puhua: puhuin, puhuit, puhui, puhuimme, puhuitte, puhuivat
			// Tietää: tiesin, tiesit, tiesi, tiesimme, tiesitte, tiesivät
			root = verbName.substring(0, length - 1) + "i";
			res = isSpecial(vowel) ? sufixsE : sufixs;
			break;
		case GROUP_2_ODA_ENDING:
		case GROUP_2_XDA_XVOWEL:
		case GROUP_2_XDA_XCONSONANT:
			// Juoda: join, joit, joi, joimme, joitte, joivat
			root = verbName.substring(0, length - 4)
					+ verbName.charAt(length - 3) + "i";
			res = isSpecial(vowel) ? sufixsE : sufixs;
			break;
		case GROUP_3_2_CONSONANT_A:// Mennä: menin, menit, meni, menimme,
									// menitte, menivät
			root = verbName.substring(0, length - 2) + "i";
			res = isSpecial(vowel) ? sufixsE : sufixs;
			break;
		case GROUP_4_TA:

			break;
		case GROUP_5_ITA:

			break;
		case GROUP_6_ETA:

			break;
		default:

		}
		return res;
	}

	private String[] getConditional() {
		String[] res = new String[6];
		int length = verbName.length();
		char vowel = verbName.charAt(length - 1);
		String root;
		switch (type) {
		case GROUP_1_2_VOWELS:
			// Puhua: puhuisin, puhuisit, puhuisi, puhuisimme, puhuisitte,
			// puhuisivat
			// Tietää: tietäisin, tietäisit, tietäisi, tietäisimme, tietäisitte,
			// tietäisivät
			root = verbName.substring(0, length - 1) + "isi";
			res = isSpecial(vowel) ? sufixsE : sufixs;
			break;
		case GROUP_2_ODA_ENDING:
		case GROUP_2_XDA_XVOWEL:
		case GROUP_2_XDA_XCONSONANT:
			root = verbName.substring(0, length - 4)
					+ verbName.charAt(length - 3) + "isi";
			res = isSpecial(vowel) ? sufixsE : sufixs;
			break;
		case GROUP_3_2_CONSONANT_A:
			// Mennä: menisin, menisit, menisi, menisimme, menisitte, menisivät
			root = verbName.substring(0, length - 2) + "isi";
			res = isSpecial(vowel) ? sufixsE : sufixs;
			break;
		case GROUP_4_TA:

			break;
		case GROUP_5_ITA:

			break;
		case GROUP_6_ETA:

			break;
		default:

		}
		return res;
	}

}
