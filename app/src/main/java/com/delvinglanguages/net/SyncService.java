package com.delvinglanguages.net;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.AppSettings;
import com.delvinglanguages.data.SyncDatabaseManager;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DrawerReference;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.LanguageManager;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.Languages;
import com.delvinglanguages.kernel.util.Statistics;
import com.delvinglanguages.kernel.util.ThemePairs;
import com.delvinglanguages.kernel.util.Wrapper;
import com.delvinglanguages.net.utils.SyncWrapper;
import com.delvinglanguages.net.utils.SyncWrappers;
import com.delvinglanguages.server.delvingApi.DelvingApi;
import com.delvinglanguages.server.delvingApi.model.LanguageItem;
import com.delvinglanguages.server.delvingApi.model.UpdateWrapper;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.io.IOException;

public class SyncService extends IntentService {

    public static final String PETITION_KEY = "petition";
    public static final String LANGUAGE_ID_KEY = "lang_id";
    public static final String ITEM_ID_KEY = "item_id";
    public static final String ITEM_TYPE_KEY = "item_type";
    public static final String ITEM_WRAPPER_KEY = "item_wrapper";

    public static final int SYNCHRONIZE = 1;
    public static final int ADD_LANGUAGE = 2;
    public static final int UPDATE_LANGUAGE = 3;
    public static final int DELETE_LANGUAGE = 4;
    public static final int ADD_ITEM = 5;
    public static final int UPDATE_ITEM = 6;
    public static final int DELETE_ITEM = 7;

    public SyncService()
    {
        super("SyncService");
    }

    private static GoogleAccountCredential credential;
    private static DelvingApi apiService;
    private static SyncDatabaseManager syncDatabase;

    @Override
    public void onCreate()
    {
        super.onCreate();

        credential = new CredentialsManager(this).getCredential();
        syncDatabase = new SyncDatabaseManager(this);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i != null && i.isConnected() && i.isAvailable())
            synchronize(intent);
        else
            saveSynchronize(intent);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return super.onBind(intent);
    }

    private void synchronize(Intent intent)
    {
        if (apiService == null) {
            apiService = new DelvingApi
                    .Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), credential)
                    .setRootUrl("https://delving-890219.appspot.com/_ah/api")
                    .build();
        }

        Bundle extras = intent.getExtras();
        int petition = extras.getInt(PETITION_KEY);
        int language_id = extras.getInt(LANGUAGE_ID_KEY);
        LanguageManager dataManager = new LanguageManager(this);
        System.out.println("[SS] Connection with the server for " + petition);
        try {
            switch (petition) {
                case SYNCHRONIZE:
                    sendBroadcast(new Intent(AppCode.ACTION_SYNC).putExtra(AppCode.ACTION_SYNC, AppCode.SYNC_START));

                    long lastSync = AppSettings.getLastSynchronization();

                    // 1. Download changes from the server and store them in DB
                    AppSettings.printlog("1. Download changes from the server and store them in DB (lastSyncTime:" + lastSync + ")");
                    downloadChanges(lastSync);

                    // 2. Update last sync time
                    AppSettings.printlog("2. Update last sync time");
                    AppSettings.setLastSynchronization(System.currentTimeMillis());

                    // 3. Send changes to the server and update app db
                    AppSettings.printlog("3. Send changes to the server");
                    uploadChanges();

                    // 4. Done
                    AppSettings.printlog("4. Sync Done");

                    sendBroadcast(new Intent(AppCode.ACTION_SYNC).putExtra(AppCode.ACTION_SYNC, AppCode.SYNC_OK));
                    break;
                case ADD_LANGUAGE:

                    Language language = dataManager.getLanguages().getLanguageById(language_id);

                    apiService.updateLanguage(
                            parseLanguage(language)
                    ).execute();
                    apiService.updateLanguageItem(
                            parseItem(language_id, language.statistics.id, Wrapper.TYPE_STATISTICS, language.statistics.wrap())
                    ).execute();

                    break;
                case UPDATE_LANGUAGE:

                    apiService.updateLanguage(
                            parseLanguage(dataManager.getLanguages().getLanguageById(language_id))
                    ).execute();

                    break;
                case DELETE_LANGUAGE:

                    apiService.removeLanguage(language_id).execute();

                    break;

                case ADD_ITEM:
                case UPDATE_ITEM:

                    int item_id = extras.getInt(ITEM_ID_KEY);
                    int type = extras.getInt(ITEM_TYPE_KEY);
                    String wrapper = extras.getString(ITEM_WRAPPER_KEY);

                    apiService.updateLanguageItem(
                            parseItem(language_id, item_id, type, wrapper)
                    ).execute();

                    break;
                case DELETE_ITEM:

                    item_id = extras.getInt(ITEM_ID_KEY);
                    type = extras.getInt(ITEM_TYPE_KEY);
                    apiService.removeLanguageItem(item_id, language_id, type).execute();

                    break;
            }
        } catch (Exception e) {
            System.out.println("Exception on server");
            e.printStackTrace();
        }
    }

    private void downloadChanges(long lastSync) throws IOException
    {
        UpdateWrapper update = apiService.getServerUpdate(lastSync).execute();

        sendBroadcast(new Intent(AppCode.ACTION_SYNC).putExtra(AppCode.ACTION_SYNC, AppCode.SYNC_SAVING));


        syncDatabase.openWritableDatabase();

        if (update.getLanguagesToAdd() != null)
            for (com.delvinglanguages.server.delvingApi.model.Language sl : update.getLanguagesToAdd())
                syncDatabase.insertLanguage(sl.getId(), sl.getCode(), sl.getName(), sl.getSettings());

        if (update.getLanguagesToUpdate() != null)
            for (com.delvinglanguages.server.delvingApi.model.Language sl : update.getLanguagesToUpdate())
                syncDatabase.updateLanguage(sl.getId(), sl.getCode(), sl.getName(), sl.getSettings());

        if (update.getLanguagesToRemove() != null)
            for (int id : update.getLanguagesToRemove())
                syncDatabase.deleteLanguage(id);

        DReference refWrapper = DReference.createBait("");
        DrawerReference drefWrapper = new DrawerReference(0, "");
        Theme thAux = new Theme(0, "", new ThemePairs());
        Test tsAux = new Test(0, "", new DReferences(), -1);
        Statistics stsAux = new Statistics(0);

        if (update.getItemsToAdd() != null)
            for (LanguageItem item : update.getItemsToAdd())
                switch (item.getType()) {
                    case Wrapper.TYPE_REFERENCE:
                        DReference ref = refWrapper.unWrap(item.getWrapper());
                        syncDatabase.insertReference(item.getId(), item.getLanguageId(), ref.name, ref.getInflexions().wrap(), ref.pronunciation, ref.priority);
                        break;
                    case Wrapper.TYPE_DRAWER_REFERENCE:
                        DrawerReference dref = drefWrapper.unWrap(item.getWrapper());
                        syncDatabase.insertDrawerReference(item.getId(), item.getLanguageId(), dref.name);
                        break;
                    case Wrapper.TYPE_THEME:
                        Theme theme = thAux.unWrap(item.getWrapper());
                        syncDatabase.insertTheme(item.getId(), item.getLanguageId(), theme.getName(), theme.getPairs());
                        break;
                    case Wrapper.TYPE_TEST:
                        Test test = tsAux.unWrap(item.getWrapper());
                        syncDatabase.insertTest(item.getId(), item.getLanguageId(), test.name, test.getRunTimes(), Test.wrapContent(test), test.theme_id);
                        break;
                }

        if (update.getItemsToUpdate() != null)
            for (LanguageItem item : update.getItemsToUpdate())
                switch (item.getType()) {
                    case Wrapper.TYPE_REFERENCE:
                        syncDatabase.updateReference(refWrapper.unWrap(item.getWrapper()), item.getLanguageId());
                        break;
                    case Wrapper.TYPE_THEME:
                        syncDatabase.updateTheme(thAux.unWrap(item.getWrapper()), item.getLanguageId());
                        break;
                    case Wrapper.TYPE_TEST:
                        syncDatabase.updateTest(tsAux.unWrap(item.getWrapper()), item.getLanguageId());
                        break;
                    case Wrapper.TYPE_STATISTICS:
                        syncDatabase.updateStatistics(stsAux.unWrap(item.getWrapper()));
                        break;
                }

        if (update.getItemsToRemove() != null)
            for (LanguageItem item : update.getItemsToRemove())
                switch (item.getType()) {
                    case Wrapper.TYPE_REFERENCE:
                        syncDatabase.deleteReference(item.getLanguageId(), item.getId());
                        break;
                    case Wrapper.TYPE_DRAWER_REFERENCE:
                        syncDatabase.deleteDrawerReference(item.getLanguageId(), item.getId());
                        break;
                    case Wrapper.TYPE_THEME:
                        syncDatabase.deleteTheme(item.getLanguageId(), item.getId());
                        break;
                    case Wrapper.TYPE_TEST:
                        syncDatabase.deleteTest(item.getLanguageId(), item.getId());
                        break;
                }

        syncDatabase.closeWritableDatabase();
    }

    private void uploadChanges() throws IOException
    {
        sendBroadcast(new Intent(AppCode.ACTION_SYNC).putExtra(AppCode.ACTION_SYNC, AppCode.SYNC_UPLOADING));

        //Opening database
        syncDatabase.openReadableDatabase();

        // uploading languages
        Languages syncLanguages = syncDatabase.readLanguages();
        if (!syncLanguages.isEmpty()) {

            for (Language language : syncLanguages)
                apiService.updateLanguage(parseLanguage(language)).execute();

            syncDatabase.syncLanguages();
        }

        SyncWrappers syncStatistics = syncDatabase.readStatistics();
        if (!syncStatistics.isEmpty()) {
            for (SyncWrapper wrapper : syncStatistics) {
                apiService.updateLanguageItem(
                        parseItem(wrapper.language_id, wrapper.id, Wrapper.TYPE_STATISTICS, wrapper.wrapper)
                ).execute();
            }
            syncDatabase.syncStatistics();
        }

        SyncWrappers refSWrappers = syncDatabase.readReferences();
        if (!refSWrappers.isEmpty()) {
            for (SyncWrapper wrapper : refSWrappers)
                apiService.updateLanguageItem(
                        parseItem(wrapper.language_id, wrapper.id, Wrapper.TYPE_REFERENCE, wrapper.wrapper)
                ).execute();
            syncDatabase.syncReferences();
        }

        SyncWrappers drefSWrappers = syncDatabase.readDrawerReferences();
        if (!drefSWrappers.isEmpty()) {
            for (SyncWrapper wrapper : drefSWrappers)
                apiService.updateLanguageItem(
                        parseItem(wrapper.language_id, wrapper.id, Wrapper.TYPE_DRAWER_REFERENCE, wrapper.wrapper)
                ).execute();
            syncDatabase.syncDrawerReferences();
        }

        SyncWrappers themeSWrappers = syncDatabase.readThemes();
        if (!themeSWrappers.isEmpty()) {
            for (SyncWrapper wrapper : themeSWrappers)
                apiService.updateLanguageItem(
                        parseItem(wrapper.language_id, wrapper.id, Wrapper.TYPE_THEME, wrapper.wrapper)
                ).execute();
            syncDatabase.syncThemes();
        }

        SyncWrappers testSWrappers = syncDatabase.readTests();
        if (!testSWrappers.isEmpty()) {
            for (SyncWrapper wrapper : testSWrappers)
                apiService.updateLanguageItem(
                        parseItem(wrapper.language_id, wrapper.id, Wrapper.TYPE_TEST, wrapper.wrapper)
                ).execute();
            syncDatabase.syncTests();
        }

        SyncWrappers removes = syncDatabase.readRemoves();
        if (!removes.isEmpty()) {
            for (SyncWrapper removedItem : removes) {
                if (removedItem.wrap_type == Wrapper.TYPE_LANGUAGE)
                    apiService.removeLanguage(removedItem.language_id).execute();

                else
                    apiService.removeLanguageItem(
                            removedItem.id, removedItem.language_id, removedItem.wrap_type
                    ).execute();

            }
            syncDatabase.syncRemoves();
        }
        //closing database
        syncDatabase.closeReadableDatabase();
    }

    private LanguageItem parseItem(int language_id, int id, int type, String wrapper)
    {
        return new LanguageItem()
                .setLanguageId(language_id)
                .setId(id)
                .setType(type)
                .setWrapper(wrapper);
    }

    private com.delvinglanguages.server.delvingApi.model.Language parseLanguage(Language l)
    {
        return new com.delvinglanguages.server.delvingApi.model.Language()
                .setId(l.id)
                .setCode(l.code)
                .setSettings(l.settings)
                .setName(l.language_name)
                .setIsPublic(l.getSetting(Language.MASK_PUBLIC));
    }

    private void saveSynchronize(Intent intent)
    {
        Bundle extras = intent.getExtras();
        int petition = extras.getInt(PETITION_KEY);
        int language_id = extras.getInt(LANGUAGE_ID_KEY);

        switch (petition) {
            case SYNCHRONIZE:

                sendBroadcast(new Intent(AppCode.ACTION_SYNC).putExtra(AppCode.ACTION_SYNC, AppCode.SYNC_NO_INTERNET));

                break;
            case DELETE_LANGUAGE:

                syncDatabase.insertSyncItem(language_id, language_id, Wrapper.TYPE_LANGUAGE);

                break;
            case DELETE_ITEM:

                int item_id = extras.getInt(ITEM_ID_KEY);
                int type = extras.getInt(ITEM_TYPE_KEY);
                syncDatabase.insertSyncItem(item_id, language_id, type);

                break;
        }
    }

}
