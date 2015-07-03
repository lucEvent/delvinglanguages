package com.delvinglanguages.net.external;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeSet;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;

import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.net.internal.NetWork;
import com.delvinglanguages.settings.Settings;

public class WordReference implements Runnable {

	private final static String CODE_ENGLISH = "en";
	private final static String CODE_SPANISH = "es";
	/*
	 * private final static String CODE_FRENCH = "fr";
	 * 
	 * private final static String CODE_ITALIAN = "it";
	 * 
	 * private final static String CODE_CZECH = "cz";
	 * 
	 * private final static String CODE_GREEK = "gr";
	 * 
	 * private final static String CODE_PORTUGUESE = "pt";
	 */
	// http://api.wordreference.com/a51d4/enes/welcome

	// {api_version}/{API_key}/json/{dictionary}/{term}

	private final static String URL_BASE = "http://api.wordreference.com/";
	private final static String API_VERSION = "0.8";
	private final static String API_KEY = "a51d4";
	private String lang_from;
	private String lang_to;

	private String address;
	private URL pagina;

	private Handler handler = new Handler();

	private TreeSet<WRItem> res;

	private NetManager net;

	private String wordName;

	private NetWork network;

	public WordReference(NetWork network) {
		Language idiomaAct = KernelControl.getCurrentLanguage();
		if (idiomaAct.isNativeLanguage()) {
			lang_from = getCode(Settings.NativeLanguage);
			lang_to = getCode(idiomaAct.getName());
		} else {
			lang_from = getCode(idiomaAct.getName());
			lang_to = getCode(Settings.NativeLanguage);
		}
		this.network = network;
		address = URL_BASE + API_VERSION + "/" + API_KEY + "/json/" + lang_from + lang_to + "/";
		net = new NetManager(null);
	}

	private String getCode(String language) {
		String code = "";
		if (language.equals("English")) {
			code = CODE_ENGLISH;
		} else if (language.equals("Inglés")) {
			code = CODE_ENGLISH;
		} else if (language.equals("Español")) {
			code = CODE_SPANISH;
		} else if (language.equals("Spanish")) {
			code = CODE_SPANISH;
		} else {
			// Solo para debuggar
			code = CODE_ENGLISH;
		}
		return code;
	}

	public void getContent(String search) {
		try {
			wordName = search;
			pagina = new URL(address + search);
		} catch (MalformedURLException e) {
			debug("Error en getContent");
			e.printStackTrace();
		}
		new Thread(this).start();
	}

	@Override
	public void run() {
		StringBuilder content = net.getPage(pagina);

		res = new TreeSet<WRItem>();

		// tratando JSON
		try {
			JSONObject data = new JSONObject(content.toString());

			String[] keys = { "term0", "PrincipalTranslations", "AdditionalTranslations" };
			String[] subkeys = { "OriginalTerm", "FirstTranslation", "SecondTranslation", "ThirdTranslation", "FourthTranslation" };

			JSONObject allData = data.getJSONObject(keys[0]);
			JSONObject translations = allData.getJSONObject(keys[1]);
			int ntran = 0;
			// Copiando traducciones normales
			while (true) {
				if (translations.has("" + ntran)) {
					JSONObject tmpdata = translations.getJSONObject("" + ntran);

					add(tmpdata.getJSONObject(subkeys[1]));

					if (tmpdata.has(subkeys[2])) {
						add(tmpdata.getJSONObject(subkeys[2]));
						if (tmpdata.has(subkeys[3])) {
							add(tmpdata.getJSONObject(subkeys[3]));
							if (tmpdata.has(subkeys[4])) {
								add(tmpdata.getJSONObject(subkeys[3]));
							}
						}
					}
					ntran++;
				} else {
					break;
				}
			}
		} catch (Exception e) {
			debug("Exception: " + e.toString());

		}
		handler.post(new Runnable() {
			@Override
			public void run() {
				network.datagram(NetWork.OK, wordName, new ArrayList<WRItem>(res));
			}
		});

	}

	public void add(JSONObject data) throws JSONException {
		String name = data.getString("term");
		String typeCode = data.getString("POS");
		int type;

		if (typeCode.equals(typeN) || typeCode.equals(typeNF) || typeCode.equals(typeNM) || typeCode.equals(typeNFPL) || typeCode.equals(typeNMPL)
				|| typeCode.equals(typeLOCNM) || typeCode.equals(typeLOCNOMF) || typeCode.equals(typeGRUPONOM) || typeCode.equals(typeLOCNOMM)
				|| typeCode.equals(typeNINVM_F) || typeCode.equals(typeNCOMUN) || typeCode.equals(typeNPL)) {
			type = 1;
		} else if (typeCode.equals(typeVTR) || typeCode.equals(typeVI) || typeCode.equals(typeVPRNL) || typeCode.equals(typeVI_PREP)
				|| typeCode.equals(typeV) || typeCode.equals(typeVTR_PREP) || typeCode.equals(typeVPRNL_PREP) || typeCode.equals(typeVI_ADV)
				|| typeCode.equals(typeVI_ADJ) || typeCode.equals(typeVAUX) || typeCode.equals(typeVI_N)) {
			type = 2;
		} else if (typeCode.equals(typeADJ) || typeCode.equals(typeADJMF) || typeCode.equals(typeLOCADJ) || typeCode.equals(typePARTICIPIO)) {
			type = 4;
		} else if (typeCode.equals(typeADV) || typeCode.equals(typeLOCADV)) {
			type = 8;
		} else if (typeCode.equals(typeVTRPHRASALSEP)) {
			type = 16;
		} else if (typeCode.equals(typeLOCVB) || typeCode.equals(typeFR) || typeCode.equals(typeVEXPR) || typeCode.equals(typeEXPR)) { // EXP
			type = 32;
		} else if (typeCode.equals(typeINT) || typeCode.equals(typeLOCINT) || typeCode.equals(typePRON) || typeCode.equals(typeLOCPREP)
				|| typeCode.equals(typePREFIJO) || typeCode.equals(typeCONJ) || typeCode.equals(typeLOCPRNL) || typeCode.equals(typePREP)) {
			type = 64;
		} else {
			debug("HEYYY!!!! no tengo este tipo:" + typeCode);
			type = 0;
		}
		String[] words = name.split(",");
		for (int i = 0; i < words.length; i++) {
			res.add(new WRItem(words[i], type));
		}
	}

	// NN
	private static final String typeN = "n";
	private static final String typeNF = "nf";
	private static final String typeNM = "nm";
	private static final String typeNFPL = "nfpl";
	private static final String typeNMPL = "nmpl";
	private static final String typeLOCNM = "loc nom";
	private static final String typeLOCNOMF = "loc nom f";
	private static final String typeGRUPONOM = "grupo nom";
	private static final String typeLOCNOMM = "loc nom m";
	private static final String typeNINVM_F = "n inv m/f";
	private static final String typeNCOMUN = "n común";
	private static final String typeNPL = "npl";

	// VB
	private static final String typeVTR = "vtr";
	private static final String typeVI = "vi";
	private static final String typeVPRNL = "v prnl";
	private static final String typeVI_PREP = "vi + prep";
	private static final String typeVTR_PREP = "vtr + prep";
	private static final String typeV = "v";
	private static final String typeVPRNL_PREP = "v prnl + prep";
	private static final String typeVI_ADV = "vi + adv";
	private static final String typeVI_ADJ = "vi + adj";
	private static final String typeVAUX = "v aux";
	private static final String typeVI_N = "vi + n";

	// ADJ
	private static final String typeADJ = "adj";
	private static final String typeADJMF = "adj mf";
	private static final String typeLOCADJ = "loc adj";
	private static final String typePARTICIPIO = "participio";
	// ADV
	private static final String typeADV = "adv";
	private static final String typeLOCADV = "loc adv";
	// EXP
	private static final String typeLOCVB = "loc verb";
	private static final String typeFR = "fr hecha";
	private static final String typeVEXPR = "v expr";
	private static final String typeEXPR = "expr";
	// PHRV
	private static final String typeVTRPHRASALSEP = "vtr phrasal sep";
	// OTH
	private static final String typeINT = "interj";
	private static final String typeLOCINT = "loc interj";
	private static final String typePRON = "pron";
	private static final String typeLOCPREP = "loc prep";
	private static final String typePREFIJO = "prefijo";
	private static final String typeLOCPRNL = "loc prnl";
	private static final String typeCONJ = "conj";
	private static final String typePREP = "prep";

	public class WRItem implements Comparable<WRItem> {

		public String name;
		public int type;

		public WRItem(String name, int type) {
			char c = name.charAt(0);
			if (c == ' ') {
				name = name.substring(1);
				c = name.charAt(0);
			}
			if (Character.isLowerCase(c)) {
				name = name.replaceFirst(c + "", "" + Character.toUpperCase(c));
			}
			this.name = name;
			this.type = type;
		}

		@Override
		public int compareTo(WRItem another) {
			return name.compareTo(another.name);
		}

	}

	private void debug(String text) {
		if (Settings.DEBUG)
			android.util.Log.d("##WordReference##", text);
	}

}
