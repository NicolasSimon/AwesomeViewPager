package ns.com.horizontalscrollerapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * Created by Nicolas on 19/04/2016.
 * (c) Touchnote Ltd., 2015
 */
public class MyfunView extends View {
    private Paint                   mPaint;
    private Random                  mRandom;
    private Point                   p0 = new Point();
    private Point                   p1 = new Point();
    private Point                   firstPoint = new Point();
    private Path                    path = new Path();

    public MyfunView(Context context) {
        super(context);
        init();
    }

    public MyfunView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyfunView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);

        mRandom = new Random();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int defaultRadius = canvas.getWidth() / 2;
        int newRadius;
        float rad;

        canvas.save();
        canvas.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);

        //Initialise p0
        if (mRandom.nextFloat() > .5f) {
            newRadius = (int)(defaultRadius * (.5 + mRandom.nextFloat()));
        } else {
            newRadius = (int)(defaultRadius * (1 - mRandom.nextFloat()));
        }
        rad = (float)Math.toRadians(0);
        firstPoint.x = (int)(newRadius * Math.cos(rad));
        firstPoint.y = (int)(newRadius * Math.sin(rad));

        p0.x = firstPoint.x;
        p0.y = firstPoint.y;

        path.reset();

        path.moveTo(firstPoint.x, firstPoint.y);

        for (int i = 5; i <= 360; i += 5) {
            if (mRandom.nextFloat() > .5f) {
                newRadius = (int)(defaultRadius * (1 + mRandom.nextFloat() / 15.0f));
            } else {
                newRadius = (int)(defaultRadius * (1 - mRandom.nextFloat() / 15.0f));
            }
            rad = (float)Math.toRadians(i);
            p1.x = (int)(newRadius * Math.cos(rad));
            p1.y = (int)(newRadius * Math.sin(rad));
            path.quadTo(p0.x, p0.y, p1.x, p1.y);
            //path.lineTo(p1.x, p1.y);
            p0.x = p1.x;
            p0.y = p1.y;
        }
        path.quadTo(p0.x, p0.y, firstPoint.x, firstPoint.y);
        //path.lineTo(firstPoint.x, firstPoint.y);
        canvas.drawPath(path, mPaint);

        canvas.restore();
    }
}
