package com.bikefit.wedgecalculator.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
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

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint;
    private Paint mPaint;

    private Matrix mMatrix;

    private float mTouchRadius;
    private float mMiddleScreen;
    private float mLineAngle;
    private float mStartX, mStartY, mEndX, mEndY;
    private float mYDelta;

    private boolean mCanMove = false;

    private float mScreenHeight;
    private float mYMargin;

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
        mBitmap = Bitmap.createBitmap(dm.widthPixels, dm.heightPixels, Bitmap.Config.ARGB_8888);

        mScreenHeight = dm.heightPixels - getStatusBarHeight();

        mMatrix = new Matrix();
        mCanvas = new Canvas(mBitmap);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
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

        mCanvas.drawLine(mStartX, mStartY, mEndX, mEndY, mPaint);

/*
        Resources res = getResources();
        VectorDrawableCompat vector = VectorDrawableCompat.create(res, R.drawable.up_down_icon, null);
        vector.setBounds(0, 0, vector.getIntrinsicWidth(), vector.getIntrinsicHeight());
        mCanvas.translate(0, (dm.heightPixels / 2 - vector.getIntrinsicHeight() /2) );
        vector.draw(mCanvas);

        VectorDrawableCompat vector2 = VectorDrawableCompat.create(res, R.drawable.rotate_icon, null);
        vector2.setBounds(0, 0, vector2.getIntrinsicWidth(), vector2.getIntrinsicHeight());
        mCanvas.translate(dm.widthPixels - 100, 0);
        //mCanvas.translate(dm.widthPixels, (dm.heightPixels / 2 - vector2.getIntrinsicHeight() /2) );
        vector2.draw(mCanvas);
*/

    }

    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, mMatrix, mBitmapPaint);
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

                    mLineAngle = calculateLineAngle();
                }
                break;
        }
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

        if (mCanMove) {
            Log.i(this.getClass().getSimpleName(), "Touch: YES");
        } else {
            Log.i(this.getClass().getSimpleName(), "Touch : NO");
        }
    }

    void moveVertical(float x, float y) {
        mBitmap.eraseColor(Color.TRANSPARENT);

        float verticalChange = (y - mYDelta);

        if (isInViewBounds(mStartY + verticalChange, mEndY + verticalChange)) {
            mStartY += verticalChange;
            mEndY += verticalChange;
            mYDelta = y;
            logLine("Vertical Move");
        } else {
            logLine("Vertical Move: BLOCKED");
        }

        mCanvas.drawLine(mStartX, mStartY, mEndX, mEndY, mPaint);
    }

    void changeAngle(float x, float y) {
        mBitmap.eraseColor(Color.TRANSPARENT);

        if (isInViewBounds(mStartY, y)) {
            mEndY = y;
            logLine("Change Angle");
        } else {
            logLine("Change Angle: BLOCKED");
        }

        mCanvas.drawLine(mStartX, mStartY, mEndX, mEndY, mPaint);
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


    float calculateLineAngle() {
        float angle = (float) Math.toDegrees(Math.atan2(mEndY - mStartY, mEndX - mStartX));
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

