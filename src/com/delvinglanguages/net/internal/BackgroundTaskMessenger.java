package com.delvinglanguages.net.internal;

public interface BackgroundTaskMessenger {

	public static final int TASK_ERROR = -1;
	public static final int TASK_DEBUG = 0;
	public static final int TASK_OK = 1;
	public static final int TASK_MESSAGE = 2;
	public static final int TASK_START = 3;
	public static final int TASK_DONE = 4;

	public void onTaskStart();

	public void onTaskDone(int codePetition, int codeResult, Object result);
}
