package com.delvinglanguages.face.settings;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.delvinglanguages.R;
import com.delvinglanguages.face.settings.ColorPickerView.OnColorChangedListener;
import com.delvinglanguages.settings.Configuraciones;

public class MakeColorActivity extends Activity implements
		OnColorChangedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int width = getIntent().getExtras().getInt("width");
		int height = getIntent().getExtras().getInt("height");

		int backgroundcolor = Color.GRAY;
		if (Configuraciones.backgroundType() == Configuraciones.BG_COLOR_ON) {
			backgroundcolor = Configuraciones.getBackgroundColor();
		}
		setContentView(new ColorPickerView(this, this, 0xFFFF0000, backgroundcolor,
				width, height));

		setTitle(R.string.makecolor);
	}

	@Override
	public void colorChanged(int button, int color) {
		if (button == ColorPickerView.OK) {
			Configuraciones.setBackground(null, color, false);
		}
		finish();
	}
}

class ColorPickerView extends View {

	public interface OnColorChangedListener {
		void colorChanged(int button, int color);
	}

	private final static int HUEBAR = 0;
	private final static int MAINFIELD = 1;
	private final static int BUTTONRIGHT = 2;
	private final static int BUTTONLEFT = 3;
	private final int RGB_MAX = 256;

	
	private final int marginLeft = 15;
	private final int marginTop = 10;
	private final int marginRight = 15;
	private final int marginBottom = 10;

	private final int hueHeight = 80;
	private final int mainHeight;
	private final int space = 15;
	private final int buttonHeight = 100;
	private int buttonWidth = 118;
	private final int buttonSpace = 5;
	private final float textsize;
	
	
	private int MODE = -1;
	

	
	
	private Paint mPaint;
	private float mCurrentHue = 0;
	private int mCurrentX = 0, mCurrentY = 0;
	private int mCurrentColor, mDefaultColor;
	private final int[] mHueBarColors;
	private int[] mMainColors;
	private OnColorChangedListener mListener;

	private int wwidth, hheight, hueWidth;

	ColorPickerView(Context c, OnColorChangedListener l, int initialcolor,
			int defaultColor, int width, int height) {
		super(c);
		mListener = l;
		mDefaultColor = defaultColor;

		wwidth = width;
		hheight = height;
		hueWidth = width - marginLeft - marginRight;
		mHueBarColors = new int[hueWidth + 10];
		mMainColors = new int[hueWidth];

		// Initialize the colors of the hue slider bar
		float increment = (float) (6.0 * RGB_MAX / (float) hueWidth);
		int index = 0;
		int offset = (int)(RGB_MAX/increment);
		for (float i = 0; i < RGB_MAX; i += increment) {
			// Red (#f00) to pink (#f0f)
			mHueBarColors[index] = Color.rgb(255, 0, (int) i);
			index++;
		}
		for (float i = 0; i < RGB_MAX; i += increment) {
			// Pink (#f0f) to blue (#00f)
			mHueBarColors[index] = Color.rgb(255 - (int) i, 0, 255);
			index++;
		}
		for (float i = 0; i < RGB_MAX; i += increment) {
			// Blue (#00f) to light blue (#0ff)
			mHueBarColors[index] = Color.rgb(0, (int) i, 255);
			index++;
		}
		for (float i = 0; i < RGB_MAX; i += increment) {
			// Light blue (#0ff) to green (#0f0)
			mHueBarColors[index] = Color.rgb(0, 255, 255 - (int) i);
			index++;
		}
		for (float i = 0; i < RGB_MAX; i += increment) {
			// Green (#0f0) to yellow (#ff0)
			mHueBarColors[index] = Color.rgb((int) i, 255, 0);
			index++;
		}
		for (float i = 0; i < RGB_MAX; i += increment) {
			// Yellow (#ff0) to red (#f00)
			mHueBarColors[index] = Color.rgb(255, 255 - (int) i, 0);
			index++;
		}

		// Get the current hue from the current color and update the main
		// color field
		float[] hsv = new float[3];
		Color.colorToHSV(initialcolor, hsv);
		mCurrentHue = hsv[0];
		updateMainColors();

		mCurrentColor = initialcolor;
		
		textsize = new Button(c).getTextSize();
		// Initializes the Paint that will draw the View
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextAlign(Paint.Align.CENTER);
		mPaint.setTextSize(textsize);
		
		// Configure elements sizes
		mCurrentX = wwidth/2;
		mCurrentY = hheight/2;
		buttonWidth = (wwidth - marginLeft - marginRight - buttonSpace) >> 1;
		mainHeight = hheight - space*2 - marginTop - marginBottom - hueHeight - buttonHeight;
	}

	// Get the current selected color from the hue bar
	private int getCurrentMainColor() {
		int index = hueWidth - 1 - (int) (mCurrentHue * hueWidth / 360);
		if (index < 0 || index > hueWidth -1)
			return mCurrentColor;
		else return mHueBarColors[index];
	}

	// Update the main field colors depending on the current selected hue
	private void updateMainColors() {
		int mainColor = getCurrentMainColor();
		float redincrement = (float) (255 - Color.red(mainColor)) / (float) (hueWidth - 1);
		float greenincrement = (float) (255 - Color.green(mainColor)) / (float) (hueWidth - 1);
		float blueincrement = (float) (255 - Color.blue(mainColor)) / (float) (hueWidth - 1);
		float red = 0, green = 0, blue = 0;
		for (int x = 0; x < hueWidth; x++) {
			mMainColors[x] = Color.rgb(255 - (int) red, 255 - (int) green,
					255 - (int) blue);
			red += redincrement;
			green += greenincrement;
			blue += blueincrement;
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		int translatedHue = hueWidth - 1 - (int) (mCurrentHue * hueWidth / 360);
		// Display all the colors of the hue bar with lines
		for (int x = 0; x < hueWidth; x++) {
			// If this is not the current selected hue, display the actual color
			if (translatedHue != x) {
				mPaint.setColor(mHueBarColors[x]);
				mPaint.setStrokeWidth(1);
			} else // else display a slightly larger black line
			{
				mPaint.setColor(Color.BLACK);
				mPaint.setStrokeWidth(3);
			}
			canvas.drawLine(x + marginLeft, marginTop, x + marginLeft,
					marginTop + hueHeight, mPaint);
		}

		// Display the main field colors using LinearGradient
		int lineTop = marginTop + hueHeight + space;
		int lineBottom = lineTop + mainHeight;
		int[] colors = new int[2];
		colors[1] = Color.BLACK;
		for (int x = 0; x < hueWidth; x++) {
			colors[0] = mMainColors[x];
			mPaint.setShader(new LinearGradient(0, lineTop, 0, lineBottom,
					colors, null, Shader.TileMode.MIRROR));
			canvas.drawLine(x + marginLeft, lineTop, x + marginLeft,
					lineBottom, mPaint);
		}
		mPaint.setShader(null);

		// Display the circle around the currently selected color in the main
		// field
		if (mCurrentX != 0 && mCurrentY != 0) {
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setColor(Color.BLACK);
			canvas.drawCircle(mCurrentX, mCurrentY, 9, mPaint);
		}

		// Draw a 'button' with the currently selected color
		lineTop = lineBottom + space;
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(mCurrentColor);
		canvas.drawRect(marginLeft, lineTop, buttonWidth + marginLeft, lineTop + buttonHeight, mPaint);

		// Set the text color according to the brightness of the color
		if (Color.red(mCurrentColor) + Color.green(mCurrentColor)
				+ Color.blue(mCurrentColor) < 384)
			mPaint.setColor(Color.WHITE);
		else
			mPaint.setColor(Color.BLACK);
		canvas.drawText("Make this up!", marginLeft + (buttonWidth/2), lineTop + (buttonHeight/2)+textsize/4, mPaint);

		// Draw a 'button' with the default color
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(mDefaultColor);
		int lineLeft = marginLeft+ buttonWidth+buttonSpace;
		canvas.drawRect(lineLeft, lineTop, lineLeft + buttonWidth, lineTop + buttonHeight, mPaint);

		// Set the text color according to the brightness of the color
		if (Color.red(mDefaultColor) + Color.green(mDefaultColor)
				+ Color.blue(mDefaultColor) < 384)
			mPaint.setColor(Color.WHITE);
		else
			mPaint.setColor(Color.BLACK);
		canvas.drawText("Cancel", lineLeft + (buttonWidth/2), lineTop + ((buttonHeight+textsize/2)/2), mPaint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(wwidth, hheight);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		
		int rightlimit = wwidth - marginRight - 1;
		int huebottomlimit = marginTop + hueHeight;
		
		int topmain = huebottomlimit + space;
		int bottommain = hheight - marginBottom - space - buttonHeight;
		
		int topbuttons = bottommain + space;
		int bottombuttons = hheight - marginBottom;

		int lineMedium = marginLeft+ buttonWidth+buttonSpace;

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			if (x >= marginLeft && x < rightlimit && y >= marginTop
					&& y < huebottomlimit) {
				MODE = HUEBAR;
			} else if (x >= marginLeft && x < rightlimit && y >= topmain
					&& y < bottommain) {
				MODE = MAINFIELD;
			} else if (x >= marginLeft && x < (buttonWidth + marginLeft)
					&& y >= topbuttons && y < bottombuttons) {
				MODE = BUTTONLEFT;
			} else if (x >= lineMedium && x < (wwidth - marginRight)
					&& y >= topbuttons && y < bottombuttons) {
				MODE = BUTTONRIGHT;
			}		
		
		case MotionEvent.ACTION_MOVE:

			// If the touch event is located in the hue bar
			if (MODE == HUEBAR) {
				if (x < marginLeft) x = marginLeft;
				else if (x > rightlimit) x = rightlimit;
				
				// Update the main field colors
				mCurrentHue = (hueWidth - (x - marginLeft)) * 360 / hueWidth;
				updateMainColors();

				// Update the current selected color
				int transX = mCurrentX - marginLeft;
				int transY = mainHeight - 1 - (mCurrentY - topmain);
				mCurrentColor = Color.rgb(
						transY * Color.red(mMainColors[transX]) / (mainHeight-1),
						transY * Color.green(mMainColors[transX]) / (mainHeight-1),
						transY * Color.blue(mMainColors[transX]) / (mainHeight-1));

				// Force the redraw of the dialog
				invalidate();
			}
			
			// If the touch event is located in the main field
			if (MODE == MAINFIELD) {
				if (y < topmain) mCurrentY = topmain;
				else if (y >= bottommain) mCurrentY = bottommain;
				else mCurrentY = y;
				if (x < marginLeft) mCurrentX = marginLeft;
				else if (x >= rightlimit) mCurrentX = rightlimit;
				else mCurrentX = x;
				
				int transX = mCurrentX - marginLeft;
				int transY = mainHeight - 1 - (mCurrentY - topmain);
				// Update the current color
				mCurrentColor = Color.rgb(
						transY * Color.red(mMainColors[transX]) / (mainHeight-1),
						transY * Color.green(mMainColors[transX]) / (mainHeight-1),
						transY * Color.blue(mMainColors[transX]) / (mainHeight-1));

				// Force the redraw of the dialog
				invalidate();
			}

			
			break;
		case MotionEvent.ACTION_UP:

			// If the touch event is located in the left button, notify the
			// listener with the current color
			if (MODE == BUTTONLEFT)
				mListener.colorChanged(OK, mCurrentColor);

			// If the touch event is located in the right button, notify the
			// listener with the default color
			if (MODE == BUTTONRIGHT)
				mListener.colorChanged(CANCEL,mDefaultColor);

			
		}

		return true;
	}
	
	public static final int OK = 1;
	public static final int CANCEL = 0;
	
}
