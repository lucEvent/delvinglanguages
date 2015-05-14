package com.delvinglanguages.kernel.test;

import android.content.Context;

import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.set.DReferences;
import com.delvinglanguages.kernel.set.Tests;

public class TestKernelControl extends KernelControl {

	public static Test runningTest;

	public TestKernelControl(Context context) {
		super(context);
	}

	public Tests getTests() {
		Tests result = currentLanguage.getTests();
		if (result == null) {
			result = database.readTests(currentLanguage.getID());
			currentLanguage.setTests(result);
		}
		return result;
	}

	public void runTest(DReferences words) {
		runningTest = new Test(words);
	}

	public void saveTest(Test test) {
		database.updateTest(test);
	}

	public void saveRunningTest(String name) {
		int tid = addTest(name, runningTest.encapsulate());
		runningTest.id = tid;
		runningTest.name = name;
		currentLanguage.addTest(runningTest);
	}

	public int addTest(String name, String content) {
		return database.insertTest(name, currentLanguage.getID(), content);
	}

	public void removeTest(Test test) {
		currentLanguage.getTests().remove(test);
		database.deleteTest(test.id);
	}

}
