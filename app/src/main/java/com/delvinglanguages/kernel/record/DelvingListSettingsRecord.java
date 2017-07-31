package com.delvinglanguages.kernel.record;

public class DelvingListSettingsRecord<T> extends Record {

    public final int list_id;
    public final int language_code;
    public final T oldValue;
    public final T newValue;

    public DelvingListSettingsRecord(int type, int list_id, int language_code, T oldValue, T newValue, long time)
    {
        super(type, time);

        this.list_id = list_id;
        this.language_code = language_code;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

}
