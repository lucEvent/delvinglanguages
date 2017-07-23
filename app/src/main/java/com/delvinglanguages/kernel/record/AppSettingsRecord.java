package com.delvinglanguages.kernel.record;

public class AppSettingsRecord<T> extends Record {

    public final T value;

    public AppSettingsRecord(int type, T value, long time)
    {
        super(type, time);
        this.value = value;
    }

}