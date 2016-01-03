package com.delvinglanguages.net.internal;

public interface TaskHandler {
	
	public static enum TaskState {
		TASK_ERROR, TASK_DEBUG, TASK_OK, TASK_START, TASK_DONE
	}

	public TaskState taskstate = TaskState.TASK_DEBUG;
	
	public void onTaskStart();
	
	public void onTaskMessage(int message_id, Object message);

	public void onTaskDone(int codePetition, TaskState state, Object result);
}
