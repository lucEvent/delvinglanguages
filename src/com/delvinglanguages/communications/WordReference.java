package com.delvinglanguages.communications;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.util.Log;

import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.settings.Configuraciones;

public class WordReference implements Runnable {

	private final static String CODE_ENGLISH = "en";
	private final static String CODE_SPANISH = "es";
	private final static String CODE_FRENCH = "fr";
	private final static String CODE_ITALIAN = "it";
	private final static String CODE_CZECH = "cz";
	private final static String CODE_GREEK = "gr";
	private final static String CODE_POLISH = "pl";
	private final static String CODE_PORTUGUESE = "pt";

	// http://api.wordreference.com/a51d4/enes/welcome

	// {api_version}/{API_key}/json/{dictionary}/{term}

	private final static String URL_BASE = "http://api.wordreference.com/";
	private final static String API_VERSION = "0.8";
	private final static String API_KEY = "a51d4";
	private String lang_from;
	private String lang_to;

	private String address;

	private NetWork network;

	public WordReference(NetWork network) {
		IDDelved idiomaAct = ControlCore.getIdiomaActual(null);
		if (idiomaAct.isIdiomaNativo()) {
			lang_from = getCode(Configuraciones.IdiomaNativo);
			lang_to = getCode(idiomaAct.getName());
		} else {
			lang_from = getCode(idiomaAct.getName());
			lang_to = getCode(Configuraciones.IdiomaNativo);
		}
		this.network = network;
		address = URL_BASE + API_VERSION + "/" + API_KEY + "/json/" + lang_from
				+ lang_to + "/";
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
			Log.d("##WR##", "Looking at: " + address + search);
			pagina = new URL(address + search);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		new Thread(this).start();
	}

	private URL pagina;

	private Handler handler = new Handler();

	private ArrayList<WRItem> res;

	private NetManager net;

	@Override
	public void run() {
		Log.d("##WR Search1##", "##############LEYENDO####################");
		StringBuilder content = net.getPage(pagina);

		System.out.println(content.toString());

		res = new ArrayList<WRItem>();

		String JSONDEBUG = "##WR.JSON_";
		// tratando JSON
		Log.d("##WR Search3##", "###############TRANSFORMANDO################");
		try {
			Log.d(JSONDEBUG + 0, " ##########################################");
			JSONObject data = new JSONObject(content.toString());

			String[] keys = { "term0", "PrincipalTranslations",
					"AdditionalTranslations" };
			String[] subkeys = { "OriginalTerm", "FirstTranslation",
					"SecondTranslation", "ThirdTranslation",
					"FourthTranslation" };

			JSONObject allData = data.getJSONObject(keys[0]);
			JSONObject translations = allData.getJSONObject(keys[1]);
			int ntran = 0;
			// Copiando traducciones normales
			while (true) {
				if (translations.has("" + ntran)) {
					JSONObject tmpdata = translations.getJSONObject("" + ntran);

					WRItem first = new WRItem(tmpdata.getJSONObject(subkeys[1]));
					res.add(first);

					if (tmpdata.has(subkeys[2])) {
						WRItem second = new WRItem(
								tmpdata.getJSONObject(subkeys[2]));
						res.add(second);
						if (tmpdata.has(subkeys[3])) {
							WRItem third = new WRItem(
									tmpdata.getJSONObject(subkeys[3]));
							res.add(third);

							if (tmpdata.has(subkeys[4])) {
								WRItem forth = new WRItem(
										tmpdata.getJSONObject(subkeys[3]));
								res.add(forth);
							}
						}

						// DEBUG
						Log.d(JSONDEBUG + 'D', "##############################");
						JSONArray deb = tmpdata.names();

						Log.d(JSONDEBUG + "D1", "Keys: " + deb.length());
						for (int i = 0; i < deb.length(); i++) {
							Log.d(JSONDEBUG + "D2", "->" + deb.getString(i));
						}
						// END DEBUG

					}
					ntran++;
				} else {
					break;
				}
			}
			/*
			 * res.add(new StringBuilder(" - Additional Translations -"));
			 * translations = allData.getJSONObject(keys[2]); ntran = 0; while
			 * (true) { if (translations.has("" + ntran)) { JSONObject tmpdata =
			 * translations.getJSONObject("" + ntran);
			 * 
			 * WRItem orig = new WRItem(tmpdata.getJSONObject(subkeys[0]));
			 * WRItem first = new WRItem(tmpdata.getJSONObject(subkeys[1]));
			 * 
			 * StringBuilder sb = new StringBuilder();
			 * sb.append(orig.name).append(" ").append(orig.typeCode)
			 * .append(" - ").append(first.name); if (tmpdata.has(subkeys[2])) {
			 * WRItem second = new WRItem( tmpdata.getJSONObject(subkeys[2]));
			 * sb.append(", ").append(second.name);
			 * 
			 * if (tmpdata.has(subkeys[3])) { WRItem third = new WRItem(
			 * tmpdata.getJSONObject(subkeys[3]));
			 * sb.append(", ").append(third.name); }
			 * 
			 * } res.add(sb);
			 * 
			 * ntran++; } else { break; } }
			 */
		} catch (Exception e) {
			Log.d(JSONDEBUG + 'E', "Exception: " + e.toString());

		}
		Log.d("##WR Search4##", "############ENVIANDO##############");

		handler.post(new Runnable() {
			@Override
			public void run() {
				network.datagram(NetWork.OK, null, res);
			}
		});

	}

	public class WRItem {

		private static final String TERM = "term";
		private static final String POS = "POS";

		private static final String typeN = "n";
		private static final String typeNF = "nf";
		private static final String typeNM = "nm";
		private static final String typeNFPL = "nfpl";
		private static final String typeNMPL = "nmpl";
		private static final String typeLOCNM = "loc nom";
		private static final String typeADJ = "adj";
		private static final String typeVTR = "vtr";
		private static final String typeVI = "vi";
		private static final String typeADV = "adv";
		private static final String typeLOCVB = "loc verb";
		private static final String typeFR = "fr hecha";
		private static final String typeINT = "interj";
		private static final String typeLOCINT = "loc interj";

		public String name;
		public int type;
		public String typeCode;

		WRItem(JSONObject data) throws JSONException {
			name = data.getString(TERM);
			typeCode = data.getString(POS);

			if (typeCode.equals(typeN) || typeCode.equals(typeNF)
					|| typeCode.equals(typeNM) || typeCode.equals(typeNFPL)
					|| typeCode.equals(typeNMPL) || typeCode.equals(typeLOCNM)) {
				type = 1;
			} else if (typeCode.equals(typeVTR) || typeCode.equals(typeVI)) {
				type = 2;
			} else if (typeCode.equals(typeADJ)) {
				type = 4;
			} else if (typeCode.equals(typeADV)) {
				type = 8;
			} else if (typeCode.equals(typeLOCVB) || typeCode.equals(typeFR)) { // EXP
				type = 32;
			} else if (typeCode.equals(typeINT) || typeCode.equals(typeLOCINT)) {
				type = 64;
			}
		}
	}

}
