package com.delvinglanguages.net.external;

import android.os.Handler;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public final class MicrosoftTranslator extends OnlineDictionary {

    protected static final String ENCODING = "UTF-8";

    private static final String DatamarketAccessUri = "https://datamarket.accesscontrol.windows.net/v2/OAuth2-13";
    private static final String clientId = "delving-890219";
    private static final String clientSecret = "delvingpassword912098";
    private static String token;
    private static long tokenExpiration = 0;
    private static final String contentType = "text/plain";

    private static final String PARAM_APP_ID = "appId=";
    private static final String PARAM_TO = "&to=";
    private static final String PARAM_FROM = "&from=";
    private static final String PARAM_TEXT = "&text=";

    private static final String SERVICE_URL = "http://api.microsofttranslator.com/V2/Ajax.svc/Translate?";
    private static final String ARRAY_JSON_OBJECT_PROPERTY = "TranslatedText";

    /**
     * Language codes
     */
    private final static String CODE_ARABIC = "ar";
    private final static String CODE_BULGARIAN = "bg";
    private final static String CODE_CATALAN = "ca";
    private final static String CODE_CHINESE = "zh-CHS";
    private final static String CODE_CROATIAN = "hr";
    private final static String CODE_CZECH = "cz";
    private final static String CODE_DANISH = "da";
    private final static String CODE_DUTCH = "nl";
    private final static String CODE_ENGLISH = "en";
    private final static String CODE_ESTONIAN = "et";
    private final static String CODE_FINNISH = "fi";
    private final static String CODE_FRENCH = "fr";
    private final static String CODE_GERMAN = "de";
    private final static String CODE_GREEK = "el";
    private final static String CODE_HEBREW = "he";
    private final static String CODE_HINDI = "hi";
    private final static String CODE_HUNGARIAN = "hu";
    private final static String CODE_ITALIAN = "it";
    private final static String CODE_JAPANESE = "ja";
    private final static String CODE_KLINGON = "tlh";
    private final static String CODE_KOREAN = "ko";
    private final static String CODE_LATVIAN = "lv";
    private final static String CODE_LITHUANIAN = "lt";
    private final static String CODE_MALAY = "ms";
    private final static String CODE_MALTESE = "mt";
    private final static String CODE_NORWEGIAN = "no";
    private final static String CODE_PERSIAN = "fa";
    private final static String CODE_POLISH = "pl";
    private final static String CODE_PORTUGUESE = "pt";
    private final static String CODE_ROMANIAN = "ro";
    private final static String CODE_RUSSIAN = "ru";
    private final static String CODE_SLOVAK = "sk";
    private final static String CODE_SLOVENIAN = "sl";
    private final static String CODE_SPANISH = "es";
    private final static String CODE_SWEDISH = "sv";
    private final static String CODE_THAI = "th";
    private final static String CODE_TURKISH = "tr";
    private final static String CODE_UKRAINIAN = "uk";
    private final static String CODE_URDU = "ur";
    private final static String CODE_VIETNAMESE = "vi";
    private final static String CODE_WELSH = "cy";

    private final static String[] CODES = new String[]{
            CODE_ENGLISH, CODE_ENGLISH, CODE_SWEDISH, CODE_FINNISH, CODE_SPANISH, CODE_CATALAN,
            null, CODE_CZECH, CODE_DANISH, CODE_DUTCH, CODE_ESTONIAN, CODE_FRENCH, CODE_GERMAN,
            CODE_GREEK, CODE_ITALIAN, CODE_NORWEGIAN, CODE_PORTUGUESE, CODE_ROMANIAN
    };

    private String header;

    public MicrosoftTranslator(int from_language_code, int to_language_code, Handler handler)
    {
        super(handler);
        updateLanguages(from_language_code, to_language_code);
    }

    @Override
    public void updateLanguages(int from_language_code, int to_language_code)
    {
        if (isTranslationAvailable(from_language_code, to_language_code))
            header = SERVICE_URL + PARAM_FROM + CODES[from_language_code] + PARAM_TO + CODES[to_language_code] + PARAM_TEXT;
        else
            header = null;
    }

    @Override
    public boolean isTranslationAvailable(int from, int to)
    {
        String sfrom = CODES[from];
        String sto = CODES[to];
        return sfrom != null && sto != null && sfrom != sto;
    }

    @Override
    public boolean search(Search search)
    {
        if (header == null) {
            return false;
        }
        new MTSearch(search).start();
        return true;
    }

    private class MTSearch extends Thread {

        private final Search search;

        MTSearch(Search search)
        {
            this.search = search;
        }

        @Override
        public void run()
        {
            String result = "";
            try {
                URL url = new URL(header + URLEncoder.encode(search.searchTerm, ENCODING));
                result = retrieveResponse(url);
            } catch (Exception e) {
                System.out.println("Exception en MTSearch.run");
                e.printStackTrace();
            }

            if (!result.isEmpty())
                search.translations[8].add(result);

            handler.obtainMessage(0, search).sendToTarget();
        }

    }

    /**
     * Forms an HTTP request, sends it using GET method and returns the result of the request as a String.
     *
     * @param url The URL to query for a String response.
     * @return The translated String.
     */
    private String retrieveResponse(URL url)
    {
        HttpURLConnection uc = null;
        try {
            if (System.currentTimeMillis() > tokenExpiration) {
                JSONObject tokenJson = new JSONObject(getToken());
                Integer expiresIn = Integer.parseInt((String) tokenJson.get("expires_in"));
                tokenExpiration = System.currentTimeMillis() + ((expiresIn * 1000) - 1);
                token = "Bearer " + (String) tokenJson.get("access_token");
            }

            uc = (HttpURLConnection) url.openConnection();
            uc.setRequestProperty("Content-Type", contentType + "; charset=" + ENCODING);
            uc.setRequestProperty("Accept-Charset", ENCODING);
            if (token != null) {
                uc.setRequestProperty("Authorization", token);
            }
            uc.setRequestMethod("GET");
            uc.setDoOutput(true);

            int responseCode = uc.getResponseCode();
            if (responseCode != 200) {
                throw new Exception("responseCode not valid: " + responseCode);
            }
            return inputStreamToString(uc.getInputStream());
        } catch (Exception e) {
            System.out.println("Exception en retrieveResponse");
            e.printStackTrace();
        } finally {
            if (uc != null)
                uc.disconnect();
        }
        return "";
    }

    /**
     * Reads an InputStream and returns its contents as a String.
     * Also effects rate control.
     *
     * @param inputStream The InputStream to read from.
     * @return The contents of the InputStream as a String.
     */
    private String inputStreamToString(InputStream inputStream)
    {
        StringBuilder outputBuilder = new StringBuilder();

        try {
            String string;
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
                while (null != (string = reader.readLine())) {
                    // Need to strip the Unicode Zero-width Non-breaking Space. For some reason, the Microsoft AJAX
                    // services prepend this to every response
                    outputBuilder.append(string.replaceAll("\uFEFF", ""));
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception en inputStreamToString");
            ex.printStackTrace();
        }
        return outputBuilder.toString();
    }

    /**
     * Gets the OAuth access token.
     */
    private String getToken()
    {
        String params = "grant_type=client_credentials&scope=http://api.microsofttranslator.com"
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret;

        HttpURLConnection uc = null;
        try {
            URL url = new URL(DatamarketAccessUri);
            uc = (HttpURLConnection) url.openConnection();
            uc.setRequestMethod("POST");
            uc.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(uc.getOutputStream());
            wr.write(params);
            wr.flush();
            wr.close();

            int responseCode = uc.getResponseCode();
            if (responseCode != 200)
                throw new Exception("Invalid response code " + responseCode);

            return inputStreamToString(uc.getInputStream());
        } catch (Exception e) {
            System.out.println("Exception en getToken");
            e.printStackTrace();
        } finally {
            if (uc != null)
                uc.disconnect();
        }
        return "";
    }

}
