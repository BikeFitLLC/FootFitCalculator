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

    private static final float DEFAULT_TOUCH_RADIUS = 30;
    private static final int DEFAULT_COLOR = Color.RED;
    private static final float DEFAULT_STROKE_WIDTH = 5;
    private static final boolean DEFAULT_ANTIALIAS = true;
    private static final int DEFAULT_TOUCH_MARGIN = 0;

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private enum MoveMode {
        NONE,
        ROTATE,
        TRANSLATE,
    }

    private MoveMode mMoveMode = MoveMode.NONE;

    private Paint mPaint;

    private float mTouchRadius;
    private float mMiddleScreen;
    private float mLineAngle;
    private float mStartX, mStartY, mEndX, mEndY;
    private float mYDelta;
    private float mDeltaAngle;

    private boolean mCanMove = false;

    private float mScreenHeight;
    private float mYMargin;

    private Matrix mRotateIconMatrix = new Matrix();
    private Matrix mUpDownIconMatrix = new Matrix();

    private VectorDrawableCompat mUpDownIcon;
    private VectorDrawableCompat mRotateIcon;

    private float mCurrentInputX;
    private float mCurrentInputY;
    private static float MAXIMUM_ANGLE = 30.0f;


    private boolean mShowTouchPoints = false;
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
        updateUpDownIconMatrix(dm.widthPixels);

        //todo: add to style
        mShowTouchPoints = false;
    }

    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(Color.RED);
        canvas.drawLine(mStartX, mStartY, mEndX, mEndY, mPaint);

        canvas.save();
        canvas.setMatrix(mUpDownIconMatrix);
        mUpDownIcon.draw(canvas);
        if (mShowTouchPoints) {
            canvas.drawRect(mUpDownIcon.getBounds(), mPaint);
        }
        canvas.restore();

        canvas.save();
        canvas.setMatrix(mRotateIconMatrix);
        mRotateIcon.draw(canvas);
        if (mShowTouchPoints) {
            canvas.drawRect(mRotateIcon.getBounds(), mPaint);
        }
        canvas.restore();

        mPaint.setColor(Color.YELLOW);
        canvas.drawCircle(mCurrentInputX, mCurrentInputY, 2, mPaint);

        // Input area debugging
//        float cx = mRotateIcon.getIntrinsicWidth()/2;
//        float cy = mRotateIcon.getIntrinsicHeight()/2;
//        canvas.save();
//        float[] center = {cx, cy};
//        mRotateIconMatrix.mapPoints(center);
//        mPaint.setColor(Color.GREEN);
//        canvas.drawCircle(center[0], center[1] - getStatusBarHeight(), cy, mPaint);
//        canvas.drawCircle(center[0], center[1] - getStatusBarHeight(), 2, mPaint);
//        canvas.restore();
    }

    private void updateUpDownIconMatrix(float width) {
        float upDownIconDistance = width * 0.20f - (mUpDownIcon.getIntrinsicWidth());
        mUpDownIconMatrix.reset();
        mUpDownIconMatrix.postTranslate(upDownIconDistance, (mStartY - mUpDownIcon.getIntrinsicHeight() / 2) + getStatusBarHeight());
    }

    private void updateRotateIconMatrix(float width) {
        float rotateArrowDistance = width * 0.80f;
        mRotateIconMatrix.reset();
        mRotateIconMatrix.postTranslate(rotateArrowDistance, (-mRotateIcon.getIntrinsicHeight() / 2));
        mRotateIconMatrix.postRotate(mLineAngle * -1);
        mRotateIconMatrix.postTranslate(mStartX, mStartY + getStatusBarHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        mCurrentInputX = x;
        mCurrentInputY = y;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                break;
            case MotionEvent.ACTION_UP:
                mMoveMode = MoveMode.NONE;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mMoveMode == MoveMode.ROTATE) {
                    changeAngle(x, y);
                } else if (mMoveMode == MoveMode.TRANSLATE) {
                    moveVertical(x, y);
                }
                break;
        }

        updateRotateIconMatrix(getWidth());
        updateUpDownIconMatrix(getWidth());

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

        if (insideRotateIcon(x, y)) {
            Log.d(this.getClass().getSimpleName(), "insideRotateIcon! " + x + ", " + y);
        }

        mCanMove = circleLineIntersect(mStartX, mStartY, mEndX, mEndY, x, y, mTouchRadius);

        if (!mCanMove) {
            mCanMove = insideRotateIcon(x, y);
        }

        if (!mCanMove) {
            mCanMove = insideUpDownIcon(x, y);
        }

        //Log.i(this.getClass().getSimpleName(), "Can Move: " + mCanMove);
        if (mCanMove) {
            mYDelta = y;
            float currentAngle = calculateLineAngle(mStartX, mStartY, x, y);
            mDeltaAngle = currentAngle - mLineAngle;

            if (x <= mMiddleScreen) {
                mMoveMode = MoveMode.TRANSLATE;
            } else {
                mMoveMode = MoveMode.ROTATE;
            }
        } else {
            mMoveMode = MoveMode.NONE;
        }
    }

    boolean insideRotateIcon(float x, float y) {
        float cx = mRotateIcon.getIntrinsicWidth() / 2;
        float cy = mRotateIcon.getIntrinsicHeight() / 2;
        float halfHeight = cy;
        float[] iconCenter = {cx, cy};
        mRotateIconMatrix.mapPoints(iconCenter);
        iconCenter[1] -= getStatusBarHeight();
        float distanceBetweenIconAndPoints = getDistanceBetweenPoints(x, y, iconCenter[0], iconCenter[1]);
        return distanceBetweenIconAndPoints < halfHeight;
    }

    boolean insideUpDownIcon(float x, float y) {
        float[] point = new float[]{x, y};

        Matrix inverse = new Matrix();
        mUpDownIconMatrix.invert(inverse);
        inverse.mapPoints(point);

        int bitmapX = Math.abs((int) point[0]);
        int bitmapY = Math.abs((int) point[1]);


        return mRotateIcon.getBounds().contains(bitmapX, bitmapY);
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
        //logLine("Change Angle : angle : " + mLineAngle);
        mLineAngle = calculateLineAngle(mStartX, mStartY, x, y);
        mLineAngle -= mDeltaAngle;

        mLineAngle = Math.max(Math.min(mLineAngle, MAXIMUM_ANGLE), -MAXIMUM_ANGLE);

        mEndY = mStartY + ((mEndX - mStartX) * (float) Math.tan(Math.toRadians(-mLineAngle)));
    }

    boolean isInViewBounds(float startY, float endY) {
        //Check bottom of screen
        //float checkBottomY = Math.max(startY, endY);
        float checkBottomY = startY;
        boolean bottomInBounds = !(checkBottomY > (mScreenHeight - mYMargin));

        //Check top of screen
        //float checkTopY = Math.min(startY, endY);
        float checkTopY = startY;
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

    private float getDistanceBetweenPoints(float x1, float y1, float x2, float y2) {
        float deltaX = x2 - x1;
        float deltaY = y2 - y1;
        return (float) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
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

