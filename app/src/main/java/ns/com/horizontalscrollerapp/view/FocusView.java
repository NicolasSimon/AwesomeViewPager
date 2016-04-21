package ns.com.horizontalscrollerapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Nicolas on 21/04/2016.
 * (c) Touchnote Ltd., 2015
 */
public class FocusView extends View implements View.OnTouchListener {
    private Paint               mTransparentPaint;
    private Paint               mSemiBlackPaint;
    private Path                mPath = new Path();

    private Point               mTransparentPosition;

    public FocusView(Context context) {
        super(context);
        initPaints();
    }

    public FocusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints();
    }

    public FocusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
    }

    private void initPaints() {
        mTransparentPaint = new Paint();
        mTransparentPaint.setColor(Color.TRANSPARENT);
        mTransparentPaint.setStrokeWidth(10);

        mSemiBlackPaint = new Paint();
        mSemiBlackPaint.setColor(Color.BLACK);
        mTransparentPaint.setStrokeWidth(10);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
            case MotionEvent.ACTION_MOVE:
                mTransparentPosition.set((int)event.getX(), (int)event.getY());
                invalidate();
                return (true);
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mTransparentPosition == null) {
            mTransparentPosition = new Point(canvas.getWidth() / 2, canvas.getHeight() / 2);
        }

        mPath.reset();

        mPath.addCircle(mTransparentPosition.x, mTransparentPosition.y, canvas.getWidth() / 2, Path.Direction.CW);
        mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);

        canvas.drawCircle(mTransparentPosition.x, mTransparentPosition.y, canvas.getWidth() / 2, mTransparentPaint);

        canvas.drawPath(mPath, mSemiBlackPaint);
        canvas.clipPath(mPath);
        canvas.drawColor(Color.BLACK);
    }
}
