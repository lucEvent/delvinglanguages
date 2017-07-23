package com.delvinglanguages.kernel.record;

public class LanguageSettingsRecord<T> extends Record {

    public final int language_id;
    public final int language_code;
    public final T oldValue;
    public final T newValue;

    public LanguageSettingsRecord(int type, int language_id, int language_code, T oldValue, T newValue, long time)
    {
        super(type, time);

        this.language_id = language_id;
        this.language_code = language_code;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

}
