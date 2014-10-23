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
import android.widget.ScrollView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.settings.Configuraciones;

public class SelectBackground extends Activity implements OnClickListener {

	private final static int CAMERA_REQUEST = 0;
	private final static int SDCARD_REQUEST = 1;

	private Configuraciones configuraciones;
	private ScrollView background;

	private Button camera, card, makeitup;
	private Button[] colores;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_select_background);

		configuraciones = new Configuraciones();
		background = (ScrollView) findViewById(R.id.sb_bg);

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

		for (int i = 0; i < 9; ++i) {
			colores[i].getBackground().setColorFilter(
					Configuraciones.COLORCODE[i], PorterDuff.Mode.MULTIPLY);
			colores[i].setOnClickListener(this);
			colores[i].setTag((Integer) Configuraciones.COLORCODE[i]);
		}

		makeitup = (Button) findViewById(R.id.sb_makeit);
		makeitup.setOnClickListener(this);

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}
	}

	@Override
	public void onClick(View button) {
		if (button == camera) {
			// From Camera
			Intent capturaimagen = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			capturaimagen.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(Configuraciones.locationForImage()));
			startActivityForResult(capturaimagen, CAMERA_REQUEST);
		} else if (button == card) {
			// From SD
			Intent selectimatge = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(selectimatge, SDCARD_REQUEST);
		} else if (button == makeitup) {
			Intent intent = new Intent(this, MakeColorActivity.class);
			intent.putExtra("width", background.getMeasuredWidth());
			intent.putExtra("height", background.getMeasuredHeight());
			startActivity(intent);
		} else {
			int bgcolor = (Integer) button.getTag();
			Configuraciones.setBackground(null, bgcolor, false);
			background.setBackgroundColor(bgcolor);
		}
	}

	@SuppressWarnings("deprecation")
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			if (requestCode == SDCARD_REQUEST) {
				Configuraciones.copyImage(this, intent);
			}
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
			showMessage(R.string.imageloaded);
		}
	}

	private void showMessage(int text) {
		Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		toast.show();
	}

}
