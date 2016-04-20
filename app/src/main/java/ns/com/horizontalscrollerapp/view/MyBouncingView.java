package ns.com.horizontalscrollerapp.view;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * Created by Nicolas on 19/04/2016.
 * (c) Touchnote Ltd., 2015
 */
public class MyBouncingView extends View {
    private Paint                   mPaint;
    private Paint                   mOverlayPaint;
    private Path                    path = new Path();

    private Point                   mTopLeft;
    private Point                   mTopRight;
    private Point                   mBottomRight;
    private Point                   mBottomLeft;
    private Point                   mControlTop;
    private Point                   mControlRight;
    private Point                   mControlBottom;
    private Point                   mControlLeft;
    private Point                   mNoiseTop;
    private Point                   mNoiseRight;
    private Point                   mNoiseBottom;
    private Point                   mNoiseLeft;

    private MyBouncingView          mInstance;

    private int                     mWidth;
    private int                     mHeight;

    private boolean                 mIsAnimationRunning = false;
    private boolean                 mIsInitialized = false;
    private boolean                 mIsChangingPaintColorRunning = false;

    private int                     mPaintColorRadius;
    private int                     mStartRadius;
    private int                     mCurrentRadius;
    private int                     mFinalRadius;
    private Point                   mPaintColorStartingPoint;

    private BouncingViewBehaviour   mBehavior;

    public MyBouncingView(Context context) {
        super(context);
    }

    public MyBouncingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyBouncingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Point getTopLeft() {
        return (mTopLeft);
    }

    public Point getBottomRight() {
        return (mBottomRight);
    }

    /**
     * Get the current paint
     * @return the current paint
     */
    public Paint getPaint() {
        if (mPaint == null) {
            initDefaultPaint();
        }
        return (mPaint);
    }

    /**
     * Set your custom behaviour here. Bare in mind that the {@link BouncingViewInterface} has to call {@link MyBouncingView#reInitAfterAmplitudeChanged()}
     * so that the correct values are being refreshed (internal padding dependant of the amplitude mostly)
     * @param behaviour the behaviour you want to have
     */
    public void setBouncingBehaviour(@NonNull BouncingViewBehaviour behaviour) {
        mBehavior = behaviour;
    }

    /**
     * @return the current behaviour of the view
     */
    public BouncingViewBehaviour getBehaviour() {
        return (mBehavior);
    }

    /**
     * In case you want to specify your own paint
     * @param p the paint to use
     */
    public void setPaint(@NonNull Paint p) {
        mPaint = p;
    }

    private int calculateFinalRadius() {
        int widthLeft = Math.abs(mPaintColorStartingPoint.x - mTopLeft.x);
        int widthRight = Math.abs(mPaintColorStartingPoint.x - mTopRight.x);
        int heightTop = Math.abs(mPaintColorStartingPoint.y - mTopLeft.y);
        int heightBottom = Math.abs(mPaintColorStartingPoint.y - mBottomLeft.y);

        int maxWidth = Math.max(widthLeft, widthRight);
        int maxHeight = Math.max(heightBottom, heightTop);

        return ((int)Math.sqrt((maxHeight * maxHeight) + (maxWidth * maxWidth)));
    }

    /**
     * Change the paint color using a ArgbEvaluator
     * @param colorTo The next color of the paint
     * @param duration the duration of the animation
     */
    public void setPaintColor(@NonNull Integer colorTo, int duration, @NonNull Point startingPoint, int startingRadius) {
        if (mOverlayPaint == null) {
            initOverlayPaint();
        }
        mStartRadius = startingRadius;
        mCurrentRadius = startingRadius;
        mPaintColorStartingPoint = startingPoint;
        mFinalRadius = calculateFinalRadius();
        final int radiusDiff = mFinalRadius - mCurrentRadius;
        if (radiusDiff < 0) {
            mOverlayPaint.setColor(colorTo);
            mPaint.setColor(colorTo);
            return;
        }
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), mOverlayPaint.getColor(), colorTo);
        colorAnimation.setDuration(duration);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mIsChangingPaintColorRunning = true;
                mOverlayPaint.setColor((Integer) animation.getAnimatedValue());
                if (!mIsAnimationRunning) {
                    mInstance.invalidate();
                }
            }
        });
        colorAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsChangingPaintColorRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsChangingPaintColorRunning = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mIsChangingPaintColorRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        colorAnimation.start();

        ValueAnimator radiusAnimation = ValueAnimator.ofInt(mStartRadius, mFinalRadius);
        radiusAnimation.setDuration(duration);
        radiusAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mIsChangingPaintColorRunning = true;
                mCurrentRadius = (Integer)animation.getAnimatedValue();
                if (!mIsAnimationRunning) {
                    mInstance.invalidate();
                }
            }
        });
        radiusAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsChangingPaintColorRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsChangingPaintColorRunning = false;
                mPaint.setColor(mOverlayPaint.getColor());
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mIsChangingPaintColorRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        radiusAnimation.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!mIsInitialized) {
            init(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        }
    }

    private void initOverlayPaint() {
        mOverlayPaint = new Paint();
        if (mPaint != null) {
            mOverlayPaint.setColor(mPaint.getColor());

        } else {
            mOverlayPaint.setColor(Color.RED);
        }
        mOverlayPaint.setStrokeWidth(10);
    }

    private void initDefaultPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);
    }

    private void init(int width, int height) {
        mIsInitialized = true;
        if (mPaint == null) {
            initDefaultPaint();
        }

        mWidth = width;
        mHeight = height;

        if (mBehavior == null) {
            mBehavior = new BouncingViewBehaviour(new BouncingViewInterface() {
                @Override
                public void onAmplitudeChanged() {
                    reInitAfterAmplitudeChanged();
                }
            }) {
                @Override
                public float getInterpolation(float t) {
                    return (float) ((1.0f * Math.cos(t)));
                }

                @Override
                public float getAmplitude(int time) {
                    //Function is F(x) = -x + 1
                    //To map : F(0) = 1
                    //         F(1) = 0
                    return ((float) time / (float) mBehavior.getSteps() + 1);
                }
            };
            mBehavior.setMaxAmplitudeX(width / 20);
            mBehavior.setMaxAmplitudeY(height / 20);
            mBehavior.setDuration(300);
        }

        mInstance = this;
    }

    /**
     * Method to update different values based on the behaviour.
     * If you want to set your custom behaviour, make sure you call this function inside the {@link BouncingViewInterface#onAmplitudeChanged()}
     */
    public void reInitAfterAmplitudeChanged() {
        int paddingLeft = mBehavior.getMaxAmplitudeX();
        int paddingTop = mBehavior.getMaxAmplitudeY();

        mTopLeft = new Point(paddingLeft, paddingTop);
        mTopRight = new Point(mWidth - paddingLeft, paddingTop);
        mBottomRight = new Point(mWidth - paddingLeft, mHeight - paddingTop);
        mBottomLeft = new Point(paddingLeft, mHeight - paddingTop);

        mControlTop = new Point((mTopLeft.x + mTopRight.x) / 2, (mTopLeft.y + mTopRight.y) / 2);
        mControlRight = new Point((mTopRight.x + mBottomRight.x) / 2, (mTopRight.y + mBottomRight.y) / 2);
        mControlBottom = new Point((mBottomRight.x + mBottomLeft.x) / 2, (mBottomRight.y + mBottomLeft.y) / 2);
        mControlLeft = new Point((mBottomLeft.x + mTopLeft.x) / 2, (mBottomLeft.y + mTopLeft.y) / 2);

        mNoiseTop = new Point(mControlTop);
        mNoiseRight = new Point(mControlRight);
        mNoiseBottom = new Point(mControlBottom);
        mNoiseLeft = new Point(mControlLeft);
    }

    /**
     * Starts the animation if not already running.
     * The animation will get the values from the {@link MyBouncingView#mBehavior} behaviour
     */
    public void startAnim() {
        if (mIsAnimationRunning) {
            return;
        }
        new Thread(new Runnable() {
            int         currentStep = 0;
            float       turbulence = 0.0f;
            @Override
            public void run() {
                mIsAnimationRunning = true;
                while (currentStep <= mBehavior.getSteps()) {
                    if (currentStep == mBehavior.getSteps()) {
                        mNoiseTop.y = mControlTop.y;
                        mNoiseRight.x = mControlRight.x;
                        mNoiseBottom.y = mControlBottom.y;
                        mNoiseLeft.x = mControlLeft.x;
                    } else {
                        turbulence = mBehavior.getInterpolation((float) Math.toRadians(currentStep * 2 - 90)) * mBehavior.getAmplitude(currentStep * 2);

                        mNoiseTop.y = (int) (mControlTop.y - (mBehavior.getMaxAmplitudeY() * turbulence));
                        mNoiseRight.x = (int) (mControlRight.x + (mBehavior.getMaxAmplitudeX() * turbulence));
                        mNoiseBottom.y = (int) (mControlBottom.y + (mBehavior.getMaxAmplitudeY() * turbulence));
                        mNoiseLeft.x = (int) (mControlLeft.x - (mBehavior.getMaxAmplitudeX() * turbulence));
                    }

                    mInstance.postInvalidate();
                    try {
                        Thread.sleep(mBehavior.getDuration() / mBehavior.getSteps());
                        currentStep++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mIsAnimationRunning = false;
            }
        }).start();
    }

    /*
                 300, 50
        100,100          500,100
             +++++++++++++++
             +             +
     50, 300 +             + 550, 300
             +             +
             +             +
             +             +
             +++++++++++++++
        100,500          500,500
                 300, 550
     */

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.TRANSPARENT);

        path.reset();

        path.moveTo(mTopLeft.x, mTopLeft.y);
        path.quadTo(mNoiseTop.x, mNoiseTop.y, mTopRight.x, mTopRight.y);
        path.quadTo(mNoiseRight.x, mNoiseRight.y, mBottomRight.x, mBottomRight.y);
        path.quadTo(mNoiseBottom.x, mNoiseBottom.y, mBottomLeft.x, mBottomLeft.y);
        path.quadTo(mNoiseLeft.x, mNoiseLeft.y, mTopLeft.x, mTopLeft.y);

        canvas.drawPath(path, mPaint);

        if (mIsChangingPaintColorRunning) {
            canvas.clipPath(path);
            canvas.drawCircle(mPaintColorStartingPoint.x, mPaintColorStartingPoint.y, mCurrentRadius, mOverlayPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new CustomOutline(w, h));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class CustomOutline extends ViewOutlineProvider {
        int width;
        int height;
        CustomOutline(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            if (mBehavior != null) {
                outline.setRect(mBehavior.getMaxAmplitudeX(),
                        mBehavior.getMaxAmplitudeY(),
                        width - mBehavior.getMaxAmplitudeX(),
                        height - mBehavior.getMaxAmplitudeY());
            }
        }
    }

    /**
     * This class serves as a holder for the {@link MyBouncingView} animation behaviour.
     * It holds the animation duration, the amplitude X and Y
     * and a {@link BouncingViewInterface} interface to update drawing values whenever amplitude has changed (for inner padding in the {@link Canvas} purposes)
     */
    public abstract class BouncingViewBehaviour {
        private int                         mDuration = 500;
        private int                         mSteps = 180;
        private int                         mMaxAmplitudeX = 100;
        private int                         mMaxAmplitudeY = 100;
        private BouncingViewInterface       mInterface;

        public BouncingViewBehaviour(BouncingViewInterface i) {
            mInterface = i;
        }

        public abstract float getInterpolation(float t);
        public abstract float getAmplitude(int time);
        public void setDuration(int duration) {
            if (duration < mSteps) {
                duration = mSteps;
            }
            mDuration = duration;
        }
        public int getDuration() {
            return (mDuration);
        }
        public int getSteps() {
            return (mSteps);
        }
        public void setMaxAmplitude(int amplitude) {
            mMaxAmplitudeX = mMaxAmplitudeY = amplitude;
            if (mInterface != null) {
                mInterface.onAmplitudeChanged();
            }
        }
        public int getMaxAmplitude() {
            return (mMaxAmplitudeX);
        }
        public void setMaxAmplitudeX(int amplitude) {
            mMaxAmplitudeX = amplitude;
            if (mInterface != null) {
                mInterface.onAmplitudeChanged();
            }
        }
        public int getMaxAmplitudeX() {
            return (mMaxAmplitudeX);
        }
        public void setMaxAmplitudeY(int amplitude) {
            mMaxAmplitudeY = amplitude;
            if (mInterface != null) {
                mInterface.onAmplitudeChanged();
            }
        }
        public int getMaxAmplitudeY() {
            return (mMaxAmplitudeY);
        }
    }

    private interface BouncingViewInterface {
        void onAmplitudeChanged();
    }
}
