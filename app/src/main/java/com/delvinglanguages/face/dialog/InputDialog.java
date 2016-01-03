package com.delvinglanguages.face.dialog;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.test.TestKernelControl;

public class InputDialog extends Builder {

	private Context context;

	public InputDialog(Context context) {
		super(context);
		this.context = context;

		final EditText input = new EditText(context);
		input.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		input.setPadding(10, 10, 10, 10);
		input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
		input.requestFocus();

		setTitle(R.string.savingtest);
		setMessage(R.string.questionsavetest);
		setView(input);
		setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				String s = input.getText().toString();
				if (s.length() == 0) {
					showMessage(R.string.nonametest);
				} else {
					new TestKernelControl().saveRunningTest(s);
					showMessage(R.string.testsaved);
				}
			}
		});
		setNegativeButton(R.string.cancel, null);
	}

	private void showMessage(int text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

}
