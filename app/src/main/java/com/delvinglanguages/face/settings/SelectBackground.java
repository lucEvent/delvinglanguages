package com.delvinglanguages.face.settings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.settings.Settings;

public class SelectBackground extends Activity implements OnClickListener {

	private final static int CAMERA_REQUEST = 0;
	private final static int SDCARD_REQUEST = 1;

	private View background;

	private Button camera, card, makeitup;
	private Button[] colores;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_select_background);

		camera = (Button) findViewById(R.id.fromcamera);
		card = (Button) findViewById(R.id.fromsd);
		camera.setOnClickListener(this);
		card.setOnClickListener(this);
		// Matriz de colores
		colores = new Button[9];
		colores[0] = (Button) findViewById(R.id.sb_red);
		colores[1] = (Button) findViewById(R.id.sb_orange);
		colores[2] = (Button) findViewById(R.id.sb_yellow);
		colores[3] = (Button) findViewById(R.id.sb_pink);
		colores[4] = (Button) findViewById(R.id.sb_white);
		colores[5] = (Button) findViewById(R.id.sb_green);
		colores[6] = (Button) findViewById(R.id.sb_purple);
		colores[7] = (Button) findViewById(R.id.sb_blue);
		colores[8] = (Button) findViewById(R.id.sb_cyan);

		int[] COLORCODE = {
		/** RED , ORANGE, YELLOW, PINK, WHITE, GREEN, PURPLE, DARK BLUE, CYAN **/
		0xFFFF0000, 0xFFFF8000, 0xFFFFFF00, 0xFFFF00FF, 0xFFFFFFFF, 0xFF00FF00, 0xFF8000FF, 0xFF0000FF, 0xFF00FFFF };

		for (int i = 0; i < 9; ++i) {
			colores[i].getBackground().setColorFilter(COLORCODE[i], PorterDuff.Mode.MULTIPLY);
			colores[i].setOnClickListener(this);
			colores[i].setTag((Integer) COLORCODE[i]);
		}

		makeitup = (Button) findViewById(R.id.sb_makeit);
		makeitup.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();

		background = findViewById(android.R.id.content);
		Settings.setBackgroundTo(background);
	}

	@Override
	public void onClick(View button) {
		if (button == camera) {
			// From Camera
			Intent capturaimagen = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			capturaimagen.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(Settings.locationForImage()));
			startActivityForResult(capturaimagen, CAMERA_REQUEST);
		} else if (button == card) {
			// From SD
			Intent selectimatge = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(selectimatge, SDCARD_REQUEST);
		} else if (button == makeitup) {
			Intent intent = new Intent(this, MakeColorActivity.class);
			intent.putExtra("width", background.getMeasuredWidth());
			intent.putExtra("height", background.getMeasuredHeight());
			startActivity(intent);
		} else {
			int bgcolor = (Integer) button.getTag();
			Settings.setBackground(null, bgcolor, false);
			background.setBackgroundColor(bgcolor);
		}
	}

	@SuppressWarnings("deprecation")
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			if (requestCode == SDCARD_REQUEST) {
				Settings.copyImage(this, intent);
			}
			background.setBackgroundDrawable(Settings.getBackgroundImage());
			showMessage(R.string.imageloaded);
		}
	}

	private void showMessage(int text) {
		Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		toast.show();
	}

}
