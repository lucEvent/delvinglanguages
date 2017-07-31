package com.delvinglanguages.kernel.test;

import android.content.Context;

import com.delvinglanguages.kernel.DelvingList;
import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.kernel.RecordManager;
import com.delvinglanguages.kernel.game.TestGame;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.Tests;
import com.delvinglanguages.kernel.util.Wrapper;

public class TestManager extends KernelManager {

    public TestManager(Context context)
    {
        super(context);
    }

    public Tests getTests()
    {
        DelvingList delvingList = getCurrentList();
        if (delvingList.tests == null)
            delvingList.setTests(dbManager.readTests(delvingList.id));

        return delvingList.tests;
    }

    public Test createTest(String test_name, int numberOfWords, int type)
    {
        DelvingList delvingList = getCurrentList();

        DReferences references = new TestGame(delvingList.getReferences()).getRandomReferences(numberOfWords, type);
        if (references.isEmpty())
            return null;

        Test test = dbManager.insertTest(delvingList.id, test_name, references, -1);
        delvingList.tests.add(test);

        RecordManager.testAdded(delvingList.id, delvingList.from_code, test.id);
        synchronizeNewItem(delvingList.id, test.id, test);
        return test;
    }

    public void updateTest(Test test)
    {
        DelvingList delvingList = getCurrentList();
        dbManager.updateTest(test, delvingList.id);

        synchronizeUpdateItem(delvingList.id, test.id, test);
    }

    public void removeTest(Test test)
    {
        DelvingList delvingList = getCurrentList();
        delvingList.removeTest(test);
        dbManager.removeTest(delvingList.id, test);

        RecordManager.testRemoved(delvingList.id, delvingList.from_code, test.id);
        synchronizeDeleteItem(delvingList.id, test.id, Wrapper.TYPE_TEST);
    }

}
