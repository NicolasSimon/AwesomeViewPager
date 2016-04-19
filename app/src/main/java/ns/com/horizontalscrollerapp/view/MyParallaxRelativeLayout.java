package ns.com.horizontalscrollerapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import ns.com.horizontalscrollerapp.R;

/**
 * Created by Nicolas on 18/04/2016.
 * (c) Touchnote Ltd., 2015
 */
public class MyParallaxRelativeLayout extends RelativeLayout {
    private Paint                       mPaint;
    private int                         mPhoneWidth;
    private int                         mTranslation = 0;

    public MyParallaxRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public MyParallaxRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyParallaxRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        int[] colors = new int[3];
        colors[0] = ContextCompat.getColor(context, R.color.color1);
        colors[1] = ContextCompat.getColor(context, R.color.color2);
        colors[2] = ContextCompat.getColor(context, R.color.color3);

        mPhoneWidth = size.x;

        Shader shader = new LinearGradient(0, 0, size.x * 3, 0, Color.WHITE, Color.WHITE, Shader.TileMode.CLAMP);
        //Shader shader = new LinearGradient(0, 0, size.x * 3, 0, colors, null, Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setShader(shader);
    }

    public void setNewOffset(int position, float offset) {
        mTranslation = - (position * mPhoneWidth);
        mTranslation -= (int) (offset * mPhoneWidth);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(mTranslation, 0);
        canvas.drawPaint(mPaint);
        canvas.restore();
    }
}
