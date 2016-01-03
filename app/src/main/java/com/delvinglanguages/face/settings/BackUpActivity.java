package com.delvinglanguages.face.settings;

import android.app.Activity;
import android.os.Bundle;

import com.delvinglanguages.R;
import com.delvinglanguages.data.BackUpManager;
import com.delvinglanguages.net.internal.Messages;
import com.delvinglanguages.net.internal.TaskHandler;

public class BackUpActivity extends Activity implements TaskHandler, Messages {

	public static final int	BACKUP_CREATION = 0;
	public static final int BACKUP_RECOVER = 1;
	
	
	
	private int task;
	private BackUpManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_back_up);
		
		task = getIntent().getExtras().getInt(SEND_TASK);
		
		manager = new BackUpManager(this);
	}

	@Override
	public void onTaskStart() {
	}

	@Override
	public void onTaskMessage(int message_id, Object message) {
		
	}

	@Override
	public void onTaskDone(int codePetition, TaskState state, Object result) {
		
	}

}
