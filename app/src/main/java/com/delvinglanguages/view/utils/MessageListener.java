package com.delvinglanguages.view.utils;

public interface MessageListener {

    int MESSAGE_INT = 100;
    int MESSAGE = 101;
    int ERROR = 102;

    void onMessage(int res_id);

    void onMessage(String msg);

    void onError();

}
