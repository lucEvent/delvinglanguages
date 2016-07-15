package com.delvinglanguages.view.utils;

public interface TestListener {

    int TEST_CREATED = 500;
    int TEST_DELETED = 501;
    int TEST_START = 502;
    int TEST_ROUND_PASSED = 503;
    int TEST_ROUND_SKIPPED = 504;

    void onTestDeleted();

    void onTestStart();

    void onTestRoundPassed();

    void onTestRoundSkipped();

}
