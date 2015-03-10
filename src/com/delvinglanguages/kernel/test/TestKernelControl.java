package com.delvinglanguages.kernel.test;

import java.util.ArrayList;

import android.content.Context;

import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.DReference;
import com.delvinglanguages.kernel.set.Tests;

public class TestKernelControl extends ControlCore {

	public static Test runningTest;

	public TestKernelControl(Context context) {
		super(context);
	}

	public Tests getTests() {
		Tests result = actualLang.getTests();
		if (result == null) {
			result = database.readTests(actualLang.getID());
			actualLang.setTests(result);
		}
		return result;
	}

	public void runTest(ArrayList<DReference> words) {
		runningTest = new Test(words);
	}

	public void saveTest(Test test) {
		database.updateTest(test);
	}

	public void saveRunningTest(String name) {
		int tid = database.insertTest(name, actualLang.getID(),
				runningTest.encapsulate());
		runningTest.id = tid;
		runningTest.name = name;
		actualLang.addTest(runningTest);
	}

	public void removeTest(Test test) {
		actualLang.getTests().remove(test);
		database.removeTest(test.id);
	}

}
