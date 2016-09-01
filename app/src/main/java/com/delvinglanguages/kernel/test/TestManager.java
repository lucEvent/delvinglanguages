package com.delvinglanguages.kernel.test;

import android.content.Context;

import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.kernel.Language;
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
        Language language = getCurrentLanguage();
        if (language.tests == null)
            language.setTests(dbManager.readTests(language.id));

        return language.tests;
    }

    public Test createTest(String test_name, int numberOfWords, int type)
    {
        Language language = getCurrentLanguage();

        DReferences references = new TestGame(language.getReferences()).getRandomReferences(numberOfWords, type);
        if (references.isEmpty())
            return null;

        Test test = dbManager.insertTest(language.id, test_name, references, -1);
        language.tests.add(test);

        synchronizeNewItem(language.id, test.id, test);
        return test;
    }

    public void updateTest(Test test)
    {
        Language language = getCurrentLanguage();
        dbManager.updateTest(test, language.id);

        synchronizeUpdateItem(language.id, test.id, test);
    }

    public void deleteTest(Test test)
    {
        Language language = getCurrentLanguage();
        language.tests.remove(test);
        dbManager.deleteTest(test.id, language.id);

        synchronizeDeleteItem(language.id, test.id, Wrapper.TYPE_TEST);
    }

}
