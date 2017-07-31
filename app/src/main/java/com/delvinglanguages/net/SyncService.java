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
import com.delvinglanguages.Main;
import com.delvinglanguages.data.SyncDatabaseManager;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DelvingList;
import com.delvinglanguages.kernel.DelvingListManager;
import com.delvinglanguages.kernel.DrawerReference;
import com.delvinglanguages.kernel.subject.Subject;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.util.DelvingLists;
import com.delvinglanguages.kernel.util.Statistics;
import com.delvinglanguages.kernel.util.Wrapper;
import com.delvinglanguages.net.utils.SyncWrapper;
import com.delvinglanguages.net.utils.SyncWrappers;
import com.delvinglanguages.server.delvingApi.DelvingApi;
import com.delvinglanguages.server.delvingApi.model.LanguageItem;
import com.delvinglanguages.server.delvingApi.model.UpdateWrapper;
import com.delvinglanguages.view.utils.DelvingListListener;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.io.IOException;

public class SyncService extends IntentService {

    public static final String PETITION_KEY = "petition";
    public static final String LIST_ID_KEY = "list_id";
    public static final String ITEM_ID_KEY = "item_id";
    public static final String ITEM_TYPE_KEY = "item_type";
    public static final String ITEM_WRAPPER_KEY = "item_wrapper";

    public static final int SYNCHRONIZE = 1;
    public static final int ADD_LIST = 2;
    public static final int UPDATE_LIST = 3;
    public static final int DELETE_LIST = 4;
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
        int list_id = extras.getInt(LIST_ID_KEY);
        DelvingListManager dataManager = new DelvingListManager(this);
        AppSettings.printlog("[SS] Connection with the server for " + petition);
        try {
            switch (petition) {
                case SYNCHRONIZE:
                    sendBroadcast(new Intent(AppCode.ACTION_SYNC).putExtra(AppCode.ACTION_SYNC, AppCode.SYNC_START));

                    long lastSync = AppSettings.getLastSynchronization();

                    // 1. Download changes from the server and store them in DB
                    AppSettings.printlog("1. Download changes from the server and store them in DB (lastSyncTime:" + lastSync + ")");
                    boolean changed = downloadChanges(lastSync);

                    if (changed)
                        Main.handler.obtainMessage(DelvingListListener.SYNC_DATA_CHANGED).sendToTarget();

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
                case ADD_LIST:

                    DelvingList delvingList = dataManager.getDelvingLists().getListById(list_id);

                    apiService.updateLanguage(
                            parseLanguage(delvingList)
                    ).execute();
                    apiService.updateLanguageItem(
                            parseItem(list_id, delvingList.statistics.id, Wrapper.TYPE_STATISTICS, delvingList.statistics.wrap())
                    ).execute();

                    break;
                case UPDATE_LIST:

                    apiService.updateLanguage(
                            parseLanguage(dataManager.getDelvingLists().getListById(list_id))
                    ).execute();

                    break;
                case DELETE_LIST:

                    apiService.removeLanguage(list_id).execute();

                    break;

                case ADD_ITEM:
                case UPDATE_ITEM:

                    int item_id = extras.getInt(ITEM_ID_KEY);
                    int type = extras.getInt(ITEM_TYPE_KEY);
                    String wrapper = extras.getString(ITEM_WRAPPER_KEY);

                    apiService.updateLanguageItem(
                            parseItem(list_id, item_id, type, wrapper)
                    ).execute();

                    break;
                case DELETE_ITEM:

                    item_id = extras.getInt(ITEM_ID_KEY);
                    type = extras.getInt(ITEM_TYPE_KEY);
                    apiService.removeLanguageItem(item_id, list_id, type).execute();

                    break;
            }
        } catch (Exception e) {
            AppSettings.printerror("Exception on server", e);
            sendBroadcast(new Intent(AppCode.ACTION_SYNC).putExtra(AppCode.ACTION_SYNC, AppCode.SYNC_ERROR));
        }
    }

    private boolean downloadChanges(long lastSync) throws IOException
    {
        UpdateWrapper update = apiService.getServerUpdate(lastSync).execute();

        sendBroadcast(new Intent(AppCode.ACTION_SYNC).putExtra(AppCode.ACTION_SYNC, AppCode.SYNC_SAVING));

        boolean changes = false;

        syncDatabase.openWritableDatabase();

        if (update.getLanguagesToAdd() != null) {
            changes = true;
            for (com.delvinglanguages.server.delvingApi.model.Language sl : update.getLanguagesToAdd())
                syncDatabase.insertLanguage(sl.getId(), sl.getCode() & 0xFF, sl.getCode() >> 16, sl.getName(), sl.getSettings());
        }

        if (update.getLanguagesToUpdate() != null) {
            changes = true;
            for (com.delvinglanguages.server.delvingApi.model.Language sl : update.getLanguagesToUpdate())
                syncDatabase.updateLanguage(sl.getId(), sl.getCode() & 0xFF, sl.getCode() >> 16, sl.getName(), sl.getSettings());
        }

        if (update.getLanguagesToRemove() != null) {
            changes = true;
            for (int id : update.getLanguagesToRemove())
                syncDatabase.deleteDelvingList(id);
        }

        if (update.getItemsToAdd() != null)
            for (LanguageItem item : update.getItemsToAdd())
                switch (item.getType()) {
                    case Wrapper.TYPE_REFERENCE:
                        DReference ref = DReference.fromWrapper(item.getId(), item.getWrapper());
                        syncDatabase.insertReference(ref.id, item.getLanguageId(), ref.name, ref.getInflexions().wrap(), ref.pronunciation, ref.priority);
                        break;
                    case Wrapper.TYPE_DRAWER_REFERENCE:
                        DrawerReference dref = DrawerReference.fromWrapper(item.getId(), item.getWrapper());
                        syncDatabase.insertDrawerReference(item.getId(), item.getLanguageId(), dref.name);
                        break;
                    case Wrapper.TYPE_SUBJECT:
                        Subject subject = Subject.fromWrapper(item.getId(), item.getWrapper());
                        syncDatabase.insertSubject(subject.id, item.getLanguageId(), subject.getName(), subject.getPairs());
                        break;
                    case Wrapper.TYPE_TEST:
                        Test test = Test.fromWrapper(item.getId(), item.getWrapper());
                        syncDatabase.insertTest(test.id, item.getLanguageId(), test.name, test.getRunTimes(), Test.wrapContent(test), test.subject_id);
                        break;
                }

        if (update.getItemsToUpdate() != null)
            for (LanguageItem item : update.getItemsToUpdate())
                switch (item.getType()) {
                    case Wrapper.TYPE_REFERENCE:
                        syncDatabase.updateReference(DReference.fromWrapper(item.getId(), item.getWrapper()), item.getLanguageId());
                        break;
                    case Wrapper.TYPE_SUBJECT:
                        syncDatabase.updateSubject(Subject.fromWrapper(item.getId(), item.getWrapper()), item.getLanguageId());
                        break;
                    case Wrapper.TYPE_TEST:
                        syncDatabase.updateTest(Test.fromWrapper(item.getId(), item.getWrapper()), item.getLanguageId());
                        break;
                    case Wrapper.TYPE_STATISTICS:
                        syncDatabase.updateStatistics(Statistics.fromWrapper(item.getId(), item.getWrapper()));
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
                    case Wrapper.TYPE_SUBJECT:
                        syncDatabase.deleteSubject(item.getLanguageId(), item.getId());
                        break;
                    case Wrapper.TYPE_TEST:
                        syncDatabase.deleteTest(item.getLanguageId(), item.getId());
                        break;
                }

        syncDatabase.closeWritableDatabase();

        return changes;
    }

    private void uploadChanges() throws IOException
    {
        sendBroadcast(new Intent(AppCode.ACTION_SYNC).putExtra(AppCode.ACTION_SYNC, AppCode.SYNC_UPLOADING));

        //Opening database
        syncDatabase.openReadableDatabase();

        // uploading lists
        DelvingLists syncDelvingLists = syncDatabase.readLists();
        if (!syncDelvingLists.isEmpty()) {

            for (DelvingList delvingList : syncDelvingLists)
                apiService.updateLanguage(parseLanguage(delvingList)).execute();

            syncDatabase.syncLanguages();
        }

        SyncWrappers syncStatistics = syncDatabase.readStatistics();
        if (!syncStatistics.isEmpty()) {
            for (SyncWrapper wrapper : syncStatistics) {
                apiService.updateLanguageItem(
                        parseItem(wrapper.list_id, wrapper.id, Wrapper.TYPE_STATISTICS, wrapper.wrapper)
                ).execute();
            }
            syncDatabase.syncStatistics();
        }

        SyncWrappers refSWrappers = syncDatabase.readReferences();
        if (!refSWrappers.isEmpty()) {
            for (SyncWrapper wrapper : refSWrappers)
                apiService.updateLanguageItem(
                        parseItem(wrapper.list_id, wrapper.id, Wrapper.TYPE_REFERENCE, wrapper.wrapper)
                ).execute();
            syncDatabase.syncReferences();
        }

        SyncWrappers drefSWrappers = syncDatabase.readDrawerReferences();
        if (!drefSWrappers.isEmpty()) {
            for (SyncWrapper wrapper : drefSWrappers)
                apiService.updateLanguageItem(
                        parseItem(wrapper.list_id, wrapper.id, Wrapper.TYPE_DRAWER_REFERENCE, wrapper.wrapper)
                ).execute();
            syncDatabase.syncDrawerReferences();
        }

        SyncWrappers subjectSWrappers = syncDatabase.readSubjects();
        if (!subjectSWrappers.isEmpty()) {
            for (SyncWrapper wrapper : subjectSWrappers)
                apiService.updateLanguageItem(
                        parseItem(wrapper.list_id, wrapper.id, Wrapper.TYPE_SUBJECT, wrapper.wrapper)
                ).execute();
            syncDatabase.syncSubjects();
        }

        SyncWrappers testSWrappers = syncDatabase.readTests();
        if (!testSWrappers.isEmpty()) {
            for (SyncWrapper wrapper : testSWrappers)
                apiService.updateLanguageItem(
                        parseItem(wrapper.list_id, wrapper.id, Wrapper.TYPE_TEST, wrapper.wrapper)
                ).execute();
            syncDatabase.syncTests();
        }
//// TODO: 12/07/2017  Falta sincronizar los nuevos RemovedItems
        SyncWrappers removes = syncDatabase.readDeletes();
        if (!removes.isEmpty()) {
            for (SyncWrapper removedItem : removes) {
                if (removedItem.wrap_type == Wrapper.TYPE_LANGUAGE)
                    apiService.removeLanguage(removedItem.list_id).execute();

                else
                    apiService.removeLanguageItem(
                            removedItem.id, removedItem.list_id, removedItem.wrap_type
                    ).execute();

            }
            syncDatabase.syncRemoves();
        }
        //closing database
        syncDatabase.closeReadableDatabase();
    }

    private LanguageItem parseItem(int list_id, int id, int type, String wrapper)
    {
        return new LanguageItem()
                .setLanguageId(list_id)
                .setId(id)
                .setType(type)
                .setWrapper(wrapper);
    }

    private com.delvinglanguages.server.delvingApi.model.Language parseLanguage(DelvingList l)
    {
        return new com.delvinglanguages.server.delvingApi.model.Language()
                .setId(l.id)
                .setCode(l.getCodes())
                .setSettings(l.settings)
                .setName(l.name)
                .setIsPublic(l.isPublic());
    }

    private void saveSynchronize(Intent intent)
    {
        Bundle extras = intent.getExtras();
        int petition = extras.getInt(PETITION_KEY);
        int list_id = extras.getInt(LIST_ID_KEY);

        switch (petition) {
            case SYNCHRONIZE:

                sendBroadcast(new Intent(AppCode.ACTION_SYNC).putExtra(AppCode.ACTION_SYNC, AppCode.SYNC_NO_INTERNET));

                break;
            case DELETE_LIST:

                syncDatabase.openWritableDatabase();
                syncDatabase.insertSyncItem(list_id, list_id, Wrapper.TYPE_LANGUAGE);
                syncDatabase.closeWritableDatabase();

                break;
            case DELETE_ITEM:

                int item_id = extras.getInt(ITEM_ID_KEY);
                int type = extras.getInt(ITEM_TYPE_KEY);
                syncDatabase.openWritableDatabase();
                syncDatabase.insertSyncItem(item_id, list_id, type);
                syncDatabase.closeWritableDatabase();

                break;
        }
    }

}
