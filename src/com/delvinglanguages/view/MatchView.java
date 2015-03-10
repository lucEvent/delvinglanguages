package com.delvinglanguages.view;

import com.delvinglanguages.net.internal.NetWork;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.View;

public class MatchView extends View implements OnTouchListener {

	private class Coordinate {
		float xOld, yOld;
		float xNew, yNew;
		float movX, movY;

		public Coordinate(float x, float y) {
			xOld = xNew = x;
			yOld = yNew = y;
		}

		public void moveTo(float x, float y) {
			xOld = xNew;
			yOld = yNew;
			xNew = x;
			yNew = y;
			movX = xOld - xNew;
			movY = yOld - yNew;
		}
	}

	private Coordinate coordinate;

	private NetWork network;

	private ShapeDrawable mDrawable;

	public MatchView(Context context) {
		super(context);

		coordinate = new Coordinate(0, 0);

		int x = 10;
		int y = 10;
		int width = 300;
		int height = 50;

		mDrawable = new ShapeDrawable(new OvalShape());
		mDrawable.getPaint().setColor(0xff74AC23);
		mDrawable.setBounds(x, y, x + width, y + height);

		makePaints();
	}

	public void setNetwork(NetWork network) {
		this.network = network;
	}

	protected void onDraw(Canvas canvas) {
		mDrawable.draw(canvas);

		// Background
		// canvas.drawRect(0, 0, width, height, background);

		// Buttons
		// Text

	}

	private Paint buttons, background, text;

	private void makePaints() {
		// Button style

		// Text style
		text = new Paint();
		text.setTextSize(100);
		text.setColor(Color.BLACK);
		text.setTextAlign(Align.CENTER);
		text.setStyle(Paint.Style.FILL);

		// Background style
		background = new Paint();
		background.setStyle(Paint.Style.FILL);
		background.setARGB(255, 1, 101, 2);

	}

	/** ******************* * METODOS TOUCH LISTENER * *********************** **/

	private boolean dragging;

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			coordinate = new Coordinate(event.getX(), event.getY());
			dragging = true;
			break;
		case MotionEvent.ACTION_MOVE:
			coordinate.moveTo(event.getX(), event.getY());
			break;
		case MotionEvent.ACTION_UP:
			coordinate.moveTo(event.getX(), event.getY());
			dragging = false;
			break;
		}
		invalidate();
		return false;
	}

}
