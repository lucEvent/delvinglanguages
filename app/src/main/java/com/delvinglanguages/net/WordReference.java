package com.delvinglanguages.net;

import android.os.Handler;

import com.delvinglanguages.AppSettings;
import com.delvinglanguages.kernel.util.AppFormat;
import com.delvinglanguages.net.utils.OnlineDictionary;
import com.delvinglanguages.net.utils.Search;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;

public class WordReference extends OnlineDictionary {

    private final static String CODE_ARABIC = "ar";
    private final static String CODE_CHINESE = "zh";
    private final static String CODE_CZECH = "cz";
    private final static String CODE_ENGLISH = "en";
    private final static String CODE_FRENCH = "fr";
    private final static String CODE_GREEK = "gr";
    private final static String CODE_ITALIAN = "it";
    private final static String CODE_JAPANESE = "ja";
    private final static String CODE_KOREAN = "ko";
    private final static String CODE_POLISH = "pl";
    private final static String CODE_PORTUGUESE = "pt";
    private final static String CODE_ROMANIAN = "ro";
    private final static String CODE_SPANISH = "es";
    private final static String CODE_TURKISH = "tr";

    private final static String[] CODES = new String[]{
            CODE_ARABIC, null, null, CODE_CHINESE, CODE_CZECH, null, null, CODE_ENGLISH, null,
            CODE_FRENCH, null, CODE_GREEK, null, null, null, null, null, CODE_ITALIAN,
            CODE_JAPANESE, CODE_KOREAN, null, CODE_POLISH, CODE_PORTUGUESE, CODE_ROMANIAN, null, CODE_SPANISH,
            null, null, null, CODE_TURKISH, null, null, null
    };

    // http://api.wordreference.com/a51d4/enes/welcome
    // {api_version}/{API_key}/json/{dictionary}/{term}
    private final static String URL_BASE = "http://api.wordreference.com/";
    private final static String API_VERSION = "0.8";
    private final static String API_KEY = "a51d4";

    private String header;

    public WordReference(int from_language_code, int to_language_code, Handler handler)
    {
        super(handler);
        updateLanguages(from_language_code, to_language_code);
    }

    @Override
    public void updateLanguages(int from_language_code, int to_language_code)
    {
        if (isTranslationAvailable(from_language_code, to_language_code))
            header = URL_BASE + API_VERSION + "/" + API_KEY + "/json/" + CODES[from_language_code] + CODES[to_language_code] + "/";
        else
            header = null;
    }

    @Override
    public boolean isTranslationAvailable(int from, int to)
    {
        String sfrom = CODES[from];
        String sto = CODES[to];
        return sfrom != null && sto != null && sfrom != sto && (sfrom == CODE_ENGLISH || sto == CODE_ENGLISH);
    }

    @Override
    public boolean search(Search search)
    {
        if (header == null) {
            return false;
        }
        new WRSearch(search).start();
        return true;
    }

    private class WRSearch extends Thread {

        private final Search search;

        WRSearch(Search search)
        {
            this.search = search;
        }

        @Override
        public void run()
        {
            try {
                URL page = new URL(header + search.searchTerm);
                AppSettings.printlog(header + search.searchTerm);
                //Lectura del contenido de la pagina
                BufferedReader in = new BufferedReader(new InputStreamReader(page.openStream()));

                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                //Tratamiento datos json
                JSONObject alldata = new JSONObject(content.toString());

                String[] terms = new String[]{"term0", "term1", "term2"};
                for (String termi : terms) {
                    if (!alldata.has(termi)) {
                        continue;
                    }
                    JSONObject data = alldata.getJSONObject(termi);

                    String[] sections = new String[]{"Entries", "PrincipalTranslations", "AdditionalTranslations"};
                    for (String section : sections) {
                        if (data.has(section)) {
                            addAll(data.getJSONObject(section));
                        }
                    }
                }
            } catch (Exception e) {
                AppSettings.printerror("[WR] Error in run", e);
            }
            handler.obtainMessage(0, search).sendToTarget();
        }

        void addAll(JSONObject translations) throws JSONException
        {
            String[] KEYS = {"OriginalTerm", "FirstTranslation", "SecondTranslation", "ThirdTranslation", "FourthTranslation"};

            int N = 0;
            while (true) {
                if (translations.has("" + N)) {
                    JSONObject translation = translations.getJSONObject("" + N);

                    int type = getType(translation.getJSONObject(KEYS[0]));

                    add(translation.getJSONObject(KEYS[1]), type);

                    if (translation.has(KEYS[2])) {
                        add(translation.getJSONObject(KEYS[2]), type);

                        if (translation.has(KEYS[3])) {
                            add(translation.getJSONObject(KEYS[3]), type);

                            if (translation.has(KEYS[4])) {
                                add(translation.getJSONObject(KEYS[4]), type);
                            }

                        }
                    }
                    N++;
                } else {
                    break;
                }
            }
        }

        void add(JSONObject data, int type) throws JSONException
        {
            if (type != -1) {
                String name = data.getString("term");
                search.translations[type].addAll(Arrays.asList(AppFormat.formatTranslation(name)));
            }
        }

        int getType(JSONObject data) throws JSONException
        {
            String typeCode = data.getString("POS");
            int type;

            switch (typeCode) {
                case "n":
                case "nf":
                case "nm":
                case "nfpl":
                case "nmpl":
                case "loc nom":
                case "loc nom f":
                case "grupo nom":
                case "loc nom m":
                case "n inv m/f":
                case "n común":
                case "npl":
                    type = 0;
                    break;
                case "vtr":
                case "vi":
                case "v prnl":
                case "vi + prep":
                case "vtr + prep":
                case "v":
                case "v prnl + prep":
                case "vi + adv":
                case "vi + adj":
                case "v aux":
                case "vi + n":
                    type = 1;
                    break;
                case "adj":
                case "adj mf":
                case "loc adj":
                case "participio":
                    type = 2;
                    break;
                case "adv":
                case "loc adv":
                    type = 3;
                    break;
                case "vtr phrasal sep":
                    type = 4;
                    break;
                case "loc verb":
                case "fr hecha":
                case "v expr":
                case "expr":
                    type = 5;
                    break;
                case "prep":
                case "loc prep":
                    type = 6;
                    break;
                case "conj":
                    type = 7;
                    break;
                case "interj":
                case "loc interj":
                case "pron":
                case "prefijo":
                case "loc prnl":
                    type = 8;
                    break;
                default:
                    AppSettings.printerror("HEYYY!!!! new word type on WR:" + typeCode, null);
                    type = -1;
                    break;
            }
            return type;
        }
    }

}

