package com.delvinglanguages.kernel;

import com.delvinglanguages.R;

import java.util.Locale;

public class LanguageCode {

    // DelvingList codes
    public static final int ARABIC = 0;
    public static final int BASQUE = 1;
    public static final int CATALAN = 2;
    public static final int CHINESE = 3;
    public static final int CZECH = 4;
    public static final int DANISH = 5;
    public static final int DUTCH = 6;
    public static final int ENGLISH = 7;
    public static final int FINNISH = 8;
    public static final int FRENCH = 9;
    public static final int GERMAN = 10;
    public static final int GREEK = 11;
    public static final int HEBREW = 12;
    public static final int HINDI = 13;
    public static final int HUNGARIAN = 14;
    public static final int INDONESIAN = 15;
    public static final int IRISH = 16;
    public static final int ITALIAN = 17;
    public static final int JAPANESE = 18;
    public static final int KOREAN = 19;
    public static final int NORWEGIAN = 20;
    public static final int POLISH = 21;
    public static final int PORTUGUESE = 22;
    public static final int ROMANIAN = 23;
    public static final int RUSSIAN = 24;
    public static final int SPANISH = 25;
    public static final int SWAHILI = 26;
    public static final int SWEDISH = 27;
    public static final int THAI = 28;
    public static final int TURKISH = 29;
    public static final int UKRAINIAN = 30;
    public static final int VIETNAMESE = 31;
    public static final int WELSH = 32;

    /**
     * @param language_code
     * @return the Locale for the language_code given. null if wrong language_code
     */
    public static Locale getLocale(int language_code)
    {
        switch (language_code) {
            case ARABIC:
                return new Locale("ar");
            case BASQUE:
                return new Locale("eu", "ES");
            case CATALAN:
                return new Locale("ca", "ES");
            case CHINESE:
                return new Locale("zh", "CN");
            case CZECH:
                return new Locale("cs", "CZ");
            case DANISH:
                return new Locale("da", "DK");
            case DUTCH:
                return new Locale("nl", "NL");
            case ENGLISH:
                return new Locale("en", "GB");
            case FINNISH:
                return new Locale("fi", "FI");
            case FRENCH:
                return new Locale("fr", "FR");
            case GERMAN:
                return new Locale("de", "DE");
            case GREEK:
                return new Locale("el", "GR");
            case HEBREW:
                return new Locale("iw", "IL");
            case HINDI:
                return new Locale("hi", "IN");
            case HUNGARIAN:
                return new Locale("hu", "HU");
            case INDONESIAN:
                return new Locale("in", "ID");
            case IRISH:
                return new Locale("ga", "IE");
            case ITALIAN:
                return new Locale("it", "IT");
            case JAPANESE:
                return new Locale("ja", "JP");
            case KOREAN:
                return new Locale("ko", "KR");
            case NORWEGIAN:
                return new Locale("no", "NO");
            case POLISH:
                return new Locale("pl", "PL");
            case PORTUGUESE:
                return new Locale("pt", "PT");
            case ROMANIAN:
                return new Locale("ro", "RO");
            case RUSSIAN:
                return new Locale("ru", "RU");
            case SPANISH:
                return new Locale("es", "ES");
            case SWAHILI:
                return new Locale("sw");
            case SWEDISH:
                return new Locale("sv", "SE");
            case THAI:
                return new Locale("th", "TH");
            case TURKISH:
                return new Locale("tr", "TR");
            case UKRAINIAN:
                return new Locale("uk", "UA");
            case VIETNAMESE:
                return new Locale("vi", "VN");
            case WELSH:
                return new Locale("cy");
        }
        return null;
    }

    /**
     * @param language_code
     * @return the Resource id for the language_code given. -1 if wrong language_code
     */
    public static int getFlagResId(int language_code)
    {
        switch (language_code) {
            case ARABIC:
                return R.drawable.flag_ar;
            case BASQUE:
                return R.drawable.flag_ba;
            case CATALAN:
                return R.drawable.flag_ca;
            case CHINESE:
                return R.drawable.flag_cn;
            case CZECH:
                return R.drawable.flag_cz;
            case DANISH:
                return R.drawable.flag_dk;
            case DUTCH:
                return R.drawable.flag_nl;
            case ENGLISH:
                return R.drawable.flag_en;
            case FINNISH:
                return R.drawable.flag_fi;
            case FRENCH:
                return R.drawable.flag_fr;
            case GERMAN:
                return R.drawable.flag_de;
            case GREEK:
                return R.drawable.flag_gr;
            case HEBREW:
                return R.drawable.flag_he;
            case HINDI:
                return R.drawable.flag_hi;
            case HUNGARIAN:
                return R.drawable.flag_hu;
            case INDONESIAN:
                return R.drawable.flag_in;
            case IRISH:
                return R.drawable.flag_ir;
            case ITALIAN:
                return R.drawable.flag_it;
            case JAPANESE:
                return R.drawable.flag_jp;
            case KOREAN:
                return R.drawable.flag_kr;
            case NORWEGIAN:
                return R.drawable.flag_no;
            case POLISH:
                return R.drawable.flag_pl;
            case PORTUGUESE:
                return R.drawable.flag_pt;
            case ROMANIAN:
                return R.drawable.flag_ro;
            case RUSSIAN:
                return R.drawable.flag_ru;
            case SPANISH:
                return R.drawable.flag_es;
            case SWAHILI:
                return R.drawable.flag_sw;
            case SWEDISH:
                return R.drawable.flag_sv;
            case THAI:
                return R.drawable.flag_th;
            case TURKISH:
                return R.drawable.flag_tu;
            case UKRAINIAN:
                return R.drawable.flag_uk;
            case VIETNAMESE:
                return R.drawable.flag_vi;
            case WELSH:
                return R.drawable.flag_wa;
        }
        return -1;
    }

    public static int getTensesArrayResId(int language_code)
    {
        int res;
        switch (language_code) {
            case SPANISH:
                res = R.array.es_tenses;
                break;
            case ENGLISH:
                res = R.array.en_tenses;
                break;
            case SWEDISH:
                res = R.array.sv_tenses;
                break;
            default:
                res = R.array.en_tenses;
                break;
        }
        return res;
    }

    public static int getSubjectArrayResId(int language_code)
    {
        int res;
        switch (language_code) {
            case SPANISH:
                res = R.array.es_subjects;
                break;
            case ENGLISH:
                res = R.array.en_subjects;
                break;
            case SWEDISH:
                res = R.array.sv_subjects;
                break;
            default:
                res = R.array.en_subjects;
                break;
        }
        return res;
    }

}
