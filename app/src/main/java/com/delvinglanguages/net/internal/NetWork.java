package com.delvinglanguages.net.internal;

public interface NetWork {

    int FAILED = 0;
    int OK = 1;
    int ERROR = 2;
    int ACTION = 3;
    int DATA = 4;

    void datagram(int code, String message, Object packet);

}
