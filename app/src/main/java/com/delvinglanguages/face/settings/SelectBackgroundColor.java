package com.delvinglanguages.face.settings;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.delvinglanguages.R;
import com.delvinglanguages.settings.Settings;

public class SelectBackgroundColor extends Activity {

    private static final int[] SEL_COLOR = new int[]{
            /** Red ,   Pink,      Purple,      Deep Purple, Indigo,    Blue,       Light Blue, Cyan,       Teal,       Green **/
            0xFFF44336, 0xFFE91E63, 0xFF9C27B0, 0xFF673AB7, 0xFF3F51B5, 0xFF2196F3, 0xFF03A9F4, 0xFF00BCD4, 0xFF009688, 0xFF4CAF50,
            /**Light Green, Lime,   Yellow,     Amber,      Orange,     Deep Orange,Brown,      Grey,       Blue Grey **/
            0xFF8BC34A, 0xFFCDDC39, 0xFFFFEB3B, 0xFFFFC107, 0xFFFF9800, 0xFFFF5722, 0xFF795548, 0xFF9E9E9E, 0xFF607D8B
    };

    private static final int[] PALETTE_ID = new int[]{R.id.color_1, R.id.color_2, R.id.color_3,
            R.id.color_4, R.id.color_5, R.id.color_6, R.id.color_7, R.id.color_8, R.id.color_9,
            R.id.color_10, R.id.color_11, R.id.color_12, R.id.color_13, R.id.color_14};

    private static Button[] palette;

    private static final int[][] PALETTE_COLOR = new int[][]{
            // Red
            {0xFFFFEBEE, 0xFFFFCDD2, 0xFFEF9A9A, 0xFFE57373, 0xFFEF5350, 0xFFF44336, 0xFFE53935, 0xFFD32F2F, 0xFFC62828, 0xFFB71C1C, 0xFFFF8A80, 0xFFFF5252, 0xFFFF1744, 0xFFD50000},
            // Pink
            {0xFFFCE4EC, 0xFFF8BBD0, 0xFFF48FB1, 0xFFF06292, 0xFFEC407A, 0xFFE91E63, 0xFFD81B60, 0xFFC2185B, 0xFFAD1457, 0xFF880E4F, 0xFFFF80AB, 0xFFFF4081, 0xFFF50057, 0xFFC51162},
            // Purple
            {0xFFF3E5F5, 0xFFE1BEE7, 0xFFCE93D8, 0xFFBA68C8, 0xFFAB47BC, 0xFF9C27B0, 0xFF8E24AA, 0xFF7B1FA2, 0xFF6A1B9A, 0xFF4A148C, 0xFFEA80FC, 0xFFE040FB, 0xFFD500F9, 0xFFAA00FF},
            // Deep Purple
            {0xFFEDE7F6, 0xFFD1C4E9, 0xFFB39DDB, 0xFF9575CD, 0xFF7E57C2, 0xFF673AB7, 0xFF5E35B1, 0xFF512DA8, 0xFF4527A0, 0xFF311B92, 0xFFB388FF, 0xFF7C4DFF, 0xFF651FFF, 0xFF6200EA},
            // Indigo
            {0xFFE8EAF6, 0xFFC5CAE9, 0xFF9FA8DA, 0xFF7986CB, 0xFF5C6BC0, 0xFF3F51B5, 0xFF3949AB, 0xFF303F9F, 0xFF283593, 0xFF1A237E, 0xFF8C9EFF, 0xFF536DFE, 0xFF3D5AFE, 0xFF304FFE},
            // Blue
            {0xFFE3F2FD, 0xFFBBDEFB, 0xFF90CAF9, 0xFF64B5F6, 0xFF42A5F5, 0xFF2196F3, 0xFF1E88E5, 0xFF1976D2, 0xFF1565C0, 0xFF0D47A1, 0xFF82B1FF, 0xFF448AFF, 0xFF2979FF, 0xFF2962FF},
            // Light Blue
            {0xFFE1F5FE, 0xFFB3E5FC, 0xFF81D4FA, 0xFF4FC3F7, 0xFF29B6F6, 0xFF03A9F4, 0xFF039BE5, 0xFF0288D1, 0xFF0277BD, 0xFF01579B, 0xFF80D8FF, 0xFF40C4FF, 0xFF00B0FF, 0xFF0091EA},
            // Cyan
            {0xFFE0F7FA, 0xFFB2EBF2, 0xFF80DEEA, 0xFF4DD0E1, 0xFF26C6DA, 0xFF00BCD4, 0xFF00ACC1, 0xFF0097A7, 0xFF00838F, 0xFF006064, 0xFF84FFFF, 0xFF18FFFF, 0xFF00E5FF, 0xFF00B8D4},
            // Teal
            {0xFFE0F2F1, 0xFFB2DFDB, 0xFF80CBC4, 0xFF4DB6AC, 0xFF26A69A, 0xFF009688, 0xFF00897B, 0xFF00796B, 0xFF00695C, 0xFF004D40, 0xFFA7FFEB, 0xFF64FFDA, 0xFF1DE9B6, 0xFF00BFA5},
            // Green
            {0xFFE8F5E9, 0xFFC8E6C9, 0xFFA5D6A7, 0xFF81C784, 0xFF66BB6A, 0xFF4CAF50, 0xFF43A047, 0xFF388E3C, 0xFF2E7D32, 0xFF1B5E20, 0xFFB9F6CA, 0xFF69F0AE, 0xFF00E676, 0xFF00C853},
            // Light Green
            {0xFFF1F8E9, 0xFFDCEDC8, 0xFFC5E1A5, 0xFFAED581, 0xFF9CCC65, 0xFF8BC34A, 0xFF7CB342, 0xFF689F38, 0xFF558B2F, 0xFF33691E, 0xFFCCFF90, 0xFFB2FF59, 0xFF76FF03, 0xFF64DD17},
            // Lime
            {0xFFF9FBE7, 0xFFF0F4C3, 0xFFE6EE9C, 0xFFDCE775, 0xFFD4E157, 0xFFCDDC39, 0xFFC0CA33, 0xFFAFB42B, 0xFF9E9D24, 0xFF827717, 0xFFF4FF81, 0xFFEEFF41, 0xFFC6FF00, 0xFFAEEA00},
            // Yellow
            {0xFFFFFDE7, 0xFFFFF9C4, 0xFFFFF59D, 0xFFFFF176, 0xFFFFEE58, 0xFFFFEB3B, 0xFFFDD835, 0xFFFBC02D, 0xFFF9A825, 0xFFF57F17, 0xFFFFFF8D, 0xFFFFFF00, 0xFFFFEA00, 0xFFFFD600},
            // Amber
            {0xFFFFF8E1, 0xFFFFECB3, 0xFFFFE082, 0xFFFFD54F, 0xFFFFCA28, 0xFFFFC107, 0xFFFFB300, 0xFFFFA000, 0xFFFF8F00, 0xFFFF6F00, 0xFFFFE57F, 0xFFFFD740, 0xFFFFC400, 0xFFFFAB00},
            // Orange
            {0xFFFFF3E0, 0xFFFFE0B2, 0xFFFFCC80, 0xFFFFB74D, 0xFFFFA726, 0xFFFF9800, 0xFFFB8C00, 0xFFF57C00, 0xFFEF6C00, 0xFFE65100, 0xFFFFD180, 0xFFFFAB40, 0xFFFF9100, 0xFFFF6D00},
            // Deep Orange
            {0xFFFBE9E7, 0xFFFFCCBC, 0xFFFFAB91, 0xFFFF8A65, 0xFFFF7043, 0xFFFF5722, 0xFFF4511E, 0xFFE64A19, 0xFFD84315, 0xFFBF360C, 0xFFFF9E80, 0xFFFF6E40, 0xFFFF3D00, 0xFFDD2C00},
            // Brown
            {0xFFEFEBE9, 0xFFD7CCC8, 0xFFBCAAA4, 0xFFA1887F, 0xFF8D6E63, 0xFF795548, 0xFF6D4C41, 0xFF5D4037, 0xFF4E342E, 0xFF3E2723},
            // Grey
            {0xFFFAFAFA, 0xFFF5F5F5, 0xFFEEEEEE, 0xFFE0E0E0, 0xFFBDBDBD, 0xFF9E9E9E, 0xFF757575, 0xFF616161, 0xFF424242, 0xFF212121},
            // Blue Grey
            {0xFFECEFF1, 0xFFCFD8DC, 0xFFB0BEC5, 0xFF90A4AE, 0xFF78909C, 0xFF607D8B, 0xFF546E7A, 0xFF455A64, 0xFF37474F, 0xFF263238}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_select_background);

        LinearLayout selectors_content = (LinearLayout) findViewById(R.id.palette);
        selectors_content.addView(new PaletteSelector(this));

        palette = new Button[PALETTE_ID.length];
        for (int i = 0; i < palette.length; i++) {
            palette[i] = (Button) findViewById(PALETTE_ID[i]);
        }
    }

    public void onPaletteAction(View v) {
        int color = ((ColorDrawable) v.getBackground()).getColor();
        Settings.setBackground(color);

        setResult(RESULT_OK);
        finish();
    }

    class PaletteSelector extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

        public PaletteSelector(Context context) {
            super(context);

            getHolder().addCallback(this);
            setOnTouchListener(this);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Canvas c = holder.lockCanvas(null);
            draw(c);
            holder.unlockCanvasAndPost(c);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);

            paletteWidth = canvas.getWidth();
            paletteHeight = canvas.getHeight();
            float step = paletteHeight / SEL_COLOR.length;

            for (int i = 0; i < SEL_COLOR.length; i++) {
                int y = Math.round(i * step);

                Rect rect = new Rect(0, y, (int) paletteWidth, Math.round(y + step));
                Paint paint = new Paint();
                paint.setColor(SEL_COLOR[i]);

                canvas.drawRect(rect, paint);
            }
        }

        private int paletteShown = -1;
        private float paletteWidth = -1;
        private float paletteHeight = -1;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAxisValue(MotionEvent.AXIS_X) > paletteWidth) return true;

            int paletteSelected = (int) (event.getAxisValue(MotionEvent.AXIS_Y) / (paletteHeight / SEL_COLOR.length));

            if (paletteSelected != paletteShown && paletteSelected < SEL_COLOR.length) {
                paletteShown = paletteSelected;

                int length = PALETTE_COLOR[paletteShown].length;
                for (int i = 0; i < length; i++) {
                    palette[i].setBackgroundColor(PALETTE_COLOR[paletteShown][i]);
                    palette[i].setVisibility(Button.VISIBLE);
                }
                for (int i = length; i < palette.length; i++) {
                    palette[i].setVisibility(Button.GONE);
                }
            }
            return true;
        }
    }

}
