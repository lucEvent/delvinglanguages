package com.delvinglanguages.net;

import android.os.Handler;

import com.delvinglanguages.AppSettings;
import com.delvinglanguages.net.utils.OnlineDictionary;
import com.delvinglanguages.net.utils.Search;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class YandexTranslator extends OnlineDictionary {

    private static final String APIKEY = "trnsl.1.1.20170707T195606Z.0ab4bdac09468f7a.8129d47c125b6aea47fe073ce7e0e3c80d9f570b";

    private static final String AZERBAIJAN = "az";
    private static final String MALAYALAM = "ml";
    private static final String ALBANIAN = "sq";
    private static final String MALTESE = "mt";
    private static final String AMHARIC = "am";
    private static final String MACEDONIAN = "mk";
    private static final String ENGLISH = "en";
    private static final String MAORI = "mi";
    private static final String ARABIC = "ar";
    private static final String MARATHI = "mr";
    private static final String ARMENIAN = "hy";
    private static final String MARI = "mhr";
    private static final String AFRIKAANS = "af";
    private static final String MONGOLIAN = "mn";
    private static final String BASQUE = "eu";
    private static final String GERMAN = "de";
    private static final String BASHKIR = "ba";
    private static final String NEPALI = "ne";
    private static final String BELARUSIAN = "be";
    private static final String NORWEGIAN = "no";
    private static final String BENGALI = "bn";
    private static final String PUNJABI = "pa";
    private static final String BURMESE = "my";
    private static final String PAPIAMENTO = "pap";
    private static final String BULGARIAN = "bg";
    private static final String PERSIAN = "fa";
    private static final String BOSNIAN = "bs";
    private static final String POLISH = "pl";
    private static final String WELSH = "cy";
    private static final String PORTUGUESE = "pt";
    private static final String HUNGARIAN = "hu";
    private static final String ROMANIAN = "ro";
    private static final String VIETNAMESE = "vi";
    private static final String RUSSIAN = "ru";
    private static final String HAITIAN = "ht";
    private static final String CEBUANO = "ceb";
    private static final String GALICIAN = "gl";
    private static final String SERBIAN = "sr";
    private static final String DUTCH = "nl";
    private static final String SINHALA = "si";
    private static final String HILLMARI = "mrj	";
    private static final String SLOVAKIAN = "sk";
    private static final String GREEK = "el";
    private static final String SLOVENIAN = "sl";
    private static final String GEORGIAN = "ka";
    private static final String SWAHILI = "sw";
    private static final String GUJARATI = "gu";
    private static final String SUNDANESE = "su";
    private static final String DANISH = "da";
    private static final String TAJIK = "tg";
    private static final String HEBREW = "he";
    private static final String THAI = "th";
    private static final String YIDDISH = "yi";
    private static final String TAGALOG = "tl";
    private static final String INDONESIAN = "id";
    private static final String TAMIL = "ta";
    private static final String IRISH = "ga";
    private static final String TATAR = "tt";
    private static final String ITALIAN = "it";
    private static final String TELUGU = "te";
    private static final String ICELANDIC = "is";
    private static final String TURKISH = "tr";
    private static final String SPANISH = "es";
    private static final String UDMURT = "udm";
    private static final String KAZAKH = "kk";
    private static final String UZBEK = "uz";
    private static final String KANNADA = "kn";
    private static final String UKRAINIAN = "uk";
    private static final String CATALAN = "ca";
    private static final String URDU = "ur";
    private static final String KYRGYZ = "ky";
    private static final String FINNISH = "fi";
    private static final String CHINESE = "zh";
    private static final String FRENCH = "fr";
    private static final String KOREAN = "ko";
    private static final String HINDI = "hi";
    private static final String XHOSA = "xh";
    private static final String CROATIAN = "hr";
    private static final String KHMER = "km";
    private static final String CZECH = "cs";
    private static final String LAOTIAN = "lo";
    private static final String SWEDISH = "sv";
    private static final String LATIN = "la";
    private static final String SCOTTISH = "gd";
    private static final String LATVIAN = "lv";
    private static final String ESTONIAN = "et";
    private static final String LITHUANIAN = "lt";
    private static final String ESPERANTO = "eo";
    private static final String LUXEMBOURGISH = "lb";
    private static final String JAVANESE = "jv";
    private static final String MALAGASY = "mg";
    private static final String JAPANESE = "ja";
    private static final String MALAY = "ms";

    private final static String[] CODES = new String[]{
            ARABIC, BASQUE, CATALAN, CHINESE, CZECH, DANISH, DUTCH, ENGLISH, FINNISH, FRENCH,
            GERMAN, GREEK, HEBREW, HINDI, HUNGARIAN, INDONESIAN, IRISH, ITALIAN, JAPANESE,
            KOREAN, NORWEGIAN, POLISH, PORTUGUESE, ROMANIAN, RUSSIAN, SPANISH, SWAHILI, SWEDISH,
            THAI, TURKISH, UKRAINIAN, VIETNAMESE, WELSH
    };

    private String fromLanguage, toLanguage;

    public YandexTranslator(int from_language_code, int to_language_code, Handler handler)
    {
        super(handler);
        updateLanguages(from_language_code, to_language_code);
    }

    @Override
    public void updateLanguages(int fromLanguage, int toLanguage)
    {
        this.fromLanguage = CODES[fromLanguage];
        this.toLanguage = CODES[toLanguage];
    }

    @Override
    public boolean isTranslationAvailable(int fromLanguage, int toLanguage)
    {
        String from = CODES[fromLanguage];
        String to = CODES[toLanguage];
        return from != null && to != null && from != to;
    }

    @Override
    public boolean search(Search search)
    {
        if (fromLanguage != null && toLanguage != null && fromLanguage != toLanguage) {
            new YandexTranslator.YTSearch(search).start();
            return true;
        }

        return false;
    }

    private class YTSearch extends Thread {

        private final Search search;

        YTSearch(Search search)
        {
            this.search = search;
        }

        @Override
        public void run()
        {
            String url_string = new StringBuilder("https://translate.yandex.net/api/v1.5/tr.json/translate?")
                    .append("key=")
                    .append(APIKEY)
                    .append("&text=")
                    .append(search.searchTerm.replace(" ", "%20"))
                    .append("&lang=")
                    .append(fromLanguage)
                    .append("-")
                    .append(toLanguage)
                    .toString();

            AppSettings.printlog(url_string);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url_string).openStream(), "utf-8"))) {

                //Page content reading
                StringBuilder sb_result = new StringBuilder();

                char[] buffer = new char[1024];
                int length;
                while (-1 != (length = in.read(buffer, 0, buffer.length)))
                    sb_result.append(buffer, 0, length);

                in.close();

                //json data management
                JSONObject data = new JSONObject(sb_result.toString());

                if (data.getInt("code") == 200) {

                    String result = data.getString("text");

                    if (!result.isEmpty())
                        result = result.substring(2, result.length() - 2);

                    search.translations[8].add(result);
                }
            } catch (Exception e) {
                AppSettings.printerror("[YT.YTS] Error in run", e);
            }
            handler.obtainMessage(0, search).sendToTarget();
        }

    }

}
