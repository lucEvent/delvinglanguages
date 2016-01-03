package com.delvinglanguages.face.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.delvinglanguages.R;
import com.delvinglanguages.face.phrasals.AddPhrasalsActivity;
import com.delvinglanguages.face.phrasals.ListPhrasalActivity;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.phrasals.PhrasalVerbs;
import com.delvinglanguages.listers.OptionLister;
import com.delvinglanguages.net.internal.TaskHandler;
import com.delvinglanguages.net.internal.Messages;
import com.delvinglanguages.settings.Settings;

public class PhrasalsFragment extends ListFragment implements TaskHandler, Messages {

	private PhrasalVerbs phManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.a_simple_list, container, false);

		Settings.setBackgroundTo(view);

		LanguageKernelControl.getPhrasalVerbsManager(this);

		setListAdapter(new OptionLister(getActivity(), getResources().getStringArray(R.array.phv_opt)));

		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Activity activity = getActivity();
		Intent intent = null;
		switch (position) {
		case 0:
			// startActivity(new Intent(activity, Activity.class)); break;
			return;
		case 1:
			while (phManager == null)
				;
			intent = new Intent(activity, AddPhrasalsActivity.class);
			intent.putExtra(SEND_PHRASAL_MANAGER, phManager);
			break;
		case 2:
			intent = new Intent(activity, ListPhrasalActivity.class);
		}
		startActivity(intent);
	}

	@Override
	public void onTaskStart() {
	}

	@Override
	public void onTaskMessage(int message_id, Object message) {
	}

	@Override
	public void onTaskDone(int codePetition, TaskState state, Object result) {
		if (state == TaskState.TASK_DONE) {
			phManager = (PhrasalVerbs) result;
		}
	}

}
