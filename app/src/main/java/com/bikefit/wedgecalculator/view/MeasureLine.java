package com.bikefit.wedgecalculator.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.bikefit.wedgecalculator.BikeFitApplication;
import com.bikefit.wedgecalculator.R;


public class MeasureLine extends View {

    //region STATIC LOCAL CONSTANTS ----------------------------------------------------------------

    private static final float DEFAULT_TOUCH_RADIUS = 25;
    private static final int DEFAULT_COLOR = Color.RED;
    private static final float DEFAULT_STROKE_WIDTH = 5;
    private static final boolean DEFAULT_ANTIALIAS = true;
    private static final int DEFAULT_TOUCH_MARGIN = 0;

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private Paint mPaint;

    private float mTouchRadius;
    private float mMiddleScreen;
    private float mLineAngle;
    private float mStartX, mStartY, mEndX, mEndY;
    private float mYDelta;

    private boolean mCanMove = false;

    private float mScreenHeight;
    private float mYMargin;

    private Matrix mRotateIconMatrix = new Matrix();

    private VectorDrawableCompat mUpDownIcon;
    private VectorDrawableCompat mRotateIcon;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public MeasureLine(Context context) {
        super(context);
        init(null);
    }

    public MeasureLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MeasureLine(Context context, AttributeSet attrs, int defstyle) {
        super(context, attrs, defstyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();

        mScreenHeight = dm.heightPixels - getStatusBarHeight();
        mPaint = new Paint();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mPaint.setDither(true);

        if (!isInEditMode() && attrs != null) {
            TypedArray styleValues = BikeFitApplication.getInstance().obtainStyledAttributes(attrs, R.styleable.MeasureLine);
            mPaint.setAntiAlias(styleValues.getBoolean(R.styleable.MeasureLine_antiAlias, DEFAULT_ANTIALIAS));
            mPaint.setColor(styleValues.getColor(R.styleable.MeasureLine_strokeColor, DEFAULT_COLOR));
            mPaint.setStrokeWidth(styleValues.getFloat(R.styleable.MeasureLine_strokeWidth, DEFAULT_STROKE_WIDTH));
            mTouchRadius = styleValues.getFloat(R.styleable.MeasureLine_touchRadius, DEFAULT_TOUCH_RADIUS);
            mYMargin = styleValues.getFloat(R.styleable.MeasureLine_touchMargin, DEFAULT_TOUCH_MARGIN);
        } else {
            //default values
            mPaint.setAntiAlias(DEFAULT_ANTIALIAS);
            mPaint.setColor(DEFAULT_COLOR);
            mPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
            mTouchRadius = DEFAULT_TOUCH_RADIUS;
            mYMargin = DEFAULT_TOUCH_MARGIN;
        }

        // Setup initial values
        mMiddleScreen = Math.round(dm.widthPixels / 2);

        //Setup line horizontally in middle of screen
        mLineAngle = 0;
        mStartX = 0;
        mEndX = dm.widthPixels;
        mStartY = dm.heightPixels / 2;
        mEndY = dm.heightPixels / 2;

        Resources res = getResources();

        mUpDownIcon = VectorDrawableCompat.create(res, R.drawable.up_down_icon, null);
        mUpDownIcon.setBounds(0, 0, mUpDownIcon.getIntrinsicWidth(), mUpDownIcon.getIntrinsicHeight());

        mRotateIcon = VectorDrawableCompat.create(res, R.drawable.rotate_icon, null);
        mRotateIcon.setBounds(0, 0, mRotateIcon.getIntrinsicWidth(), mRotateIcon.getIntrinsicHeight());

        updateRotateIconMatrix(dm.widthPixels);

    }

    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(mStartX, mStartY, mEndX, mEndY, mPaint);

        float margin = 50;

        canvas.save();
        canvas.translate(mStartX + margin, mStartY - mUpDownIcon.getIntrinsicHeight() / 2);
        mUpDownIcon.draw(canvas);
        canvas.restore();

        canvas.save();
        canvas.setMatrix(mRotateIconMatrix);
        mRotateIcon.draw(canvas);
        canvas.restore();

    }

    private void updateRotateIconMatrix(float width) {
        float rotateArrowDistance = width * 0.75f;
        mRotateIconMatrix.reset();
        mRotateIconMatrix.postTranslate(mStartX, mStartY);
        mRotateIconMatrix.preRotate(mLineAngle * -1);
        mRotateIconMatrix.preTranslate(rotateArrowDistance, (-mRotateIcon.getIntrinsicHeight() / 2) + getStatusBarHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                mYDelta = y;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mCanMove) {
                    if (x <= mMiddleScreen) {
                        moveVertical(x, y);
                    } else {
                        changeAngle(x, y);
                    }
                }
                break;
        }

        updateRotateIconMatrix(getWidth());

        invalidate();
        return true;
    }

    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------

    public float getLineAngle() {
        return mLineAngle;
    }

    //endregion


    //region PRIVATE METHODS -----------------------------------------------------------------------

    void touchStart(float x, float y) {
        mCanMove = circleLineIntersect(mStartX, mStartY, mEndX, mEndY, x, y, mTouchRadius);

        //todo: detect click inside coachmarks

        if (mCanMove) {
            Log.i(this.getClass().getSimpleName(), "Touch: YES");
        } else {
            Log.i(this.getClass().getSimpleName(), "Touch : NO");
        }
    }

    void moveVertical(float x, float y) {

        float verticalChange = (y - mYDelta);

        if (isInViewBounds(mStartY + verticalChange, mEndY + verticalChange)) {
            mStartY += verticalChange;
            mEndY += verticalChange;
            mYDelta = y;
            logLine("Vertical Move");
        } else {
            logLine("Vertical Move: BLOCKED");
        }
    }

    void changeAngle(float x, float y) {
        //todo: clamp the view bounds
        mLineAngle = calculateLineAngle(mStartX, mStartY, x, y);
        mEndY = mStartY + ((mEndX - mStartX) * (float) Math.tan(Math.toRadians(-mLineAngle)));
    }

    boolean isInViewBounds(float startY, float endY) {
        //Check bottom of screen
        float checkBottomY = Math.max(startY, endY);
        boolean bottomInBounds = !(checkBottomY > (mScreenHeight - mYMargin));

        //Check top of screen
        float checkTopY = Math.min(startY, endY);
        boolean topInBounds = checkTopY > mYMargin;

        return bottomInBounds && topInBounds;
    }

    boolean circleLineIntersect(float x1, float y1, float x2, float y2, float cx, float cy, float cr) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float a = dx * dx + dy * dy;
        float b = 2 * (dx * (x1 - cx) + dy * (y1 - cy));
        float c = cx * cx + cy * cy;
        c += x1 * x1 + y1 * y1;
        c -= 2 * (cx * x1 + cy * y1);
        c -= cr * cr;
        float bb4ac = b * b - 4 * a * c;

        if (bb4ac < 0) {
            return false;    // No collision
        } else {
            return true;      //Collision
        }
    }


    float calculateLineAngle(float startX, float startY, float endX, float endY) {
        float angle = (float) Math.toDegrees(Math.atan2(endY - startY, endX - startX));
        angle *= -1;
        return angle;
    }

    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    private void logLine(String message) {
        Log.i(this.getClass().getSimpleName(), message + "( X:" + (int) mStartX + "-" + (int) mEndX + "|| Y:" + (int) mStartY + "-" + (int) mEndY + ") ANGLE: " + mLineAngle);
    }

    //endregion

}

