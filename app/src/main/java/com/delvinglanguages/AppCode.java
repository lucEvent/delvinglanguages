package com.delvinglanguages;

public final class AppCode {

    public static final int ACTION_CREATE = 10;
    public static final int ACTION_MODIFY = 11;
    public static final int ACTION_IMPORT = 12;
    public static final int START_ABORTED = 13;

    public static final int RESULT_LIST_CREATED = 20;
    public static final int RESULT_LIST_CREATED_CANCELED = 21;
    public static final int RESULT_IMPORT_DONE = 22;
    public static final int RESULT_IMPORT_CANCELED = 23;
    public static final int RESULT_SYNC_DONE = 24;

    public static final int IMPORT_SUCCESSFUL = 30;
    public static final int EXPORT_SUCCESSFUL = 31;

    public static final int SUBJECT_CREATED = 40;
    public static final int SUBJECT_MODIFIED = 41;
    public static final int SUBJECT_REMOVED = 42;

    public static final int DREFERENCE_CREATED = 50;
    public static final int DREFERENCE_UPDATED = 51;
    public static final int DREFERENCE_REMOVED = 52;

    public static final int SYNC_START = 60;
    public static final int SYNC_UPLOADING = 61;
    public static final int SYNC_SAVING = 62;
    public static final int SYNC_OK = 63;
    public static final int SYNC_ERROR = 64;
    public static final int SYNC_NO_INTERNET = 65;

    public static final String FRAGMENT = "fragment";
    public static final String ACTION = "action";

    public static final String DREFERENCE_NAME = "dref";
    public static final String DREFERENCE_NAME_INVERSE = "drefinv";
    public static final String INFLEXIONS_WRAPPER = "infwrapper";

    public static final String DRAWER_ID = "did";
    public static final String TEST_ID = "testid";
    public static final String SUBJECT_ID = "subjectid";
    public static final String LIST_ID = "list_id";
    public static final String SEARCH_TERM = "searchterm";

    public static final String ACTION_SYNC = "com.delvinglanguages.sync";

}
