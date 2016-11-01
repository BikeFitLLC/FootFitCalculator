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
    private float yDelta;

    private boolean mCanMove = false;

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
        } else {
            //default values
            mPaint.setAntiAlias(DEFAULT_ANTIALIAS);
            mPaint.setColor(DEFAULT_COLOR);
            mPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
            mTouchRadius = DEFAULT_TOUCH_RADIUS;
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
                yDelta = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mCanMove) {
                    if (x <= mMiddleScreen) {
                        moveVertical(x, y - yDelta);
                        yDelta = y;
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

        Log.i(this.getClass().getSimpleName(), "==Touch-Start=============================================================================");
        Log.i(this.getClass().getSimpleName(), "Current Line X:" + (int) mStartX + ":" + (int) mEndX + "|| Y:" + (int) mStartY + ":" + (int) mEndY);
        Log.i(this.getClass().getSimpleName(), "Touch at cords X:" + (int) x + " Y:" + (int) y);

        mCanMove = circleLineIntersect(mStartX, mStartY, mEndX, mEndY, x, y, mTouchRadius);

        if (mCanMove) {
            Log.i(this.getClass().getSimpleName(), "MOVE YES");
        } else {
            Log.i(this.getClass().getSimpleName(), "MOVE NO");
        }
    }


    void moveVertical(float x, float y) {
        mBitmap.eraseColor(Color.TRANSPARENT);

        logLine("Move Vertical: Pre ");

        float verticalChange = y;// - mEndY;
        mStartY += verticalChange;
        mEndY += verticalChange;

        logLine("Move Vertical: Post ");

        mCanvas.drawLine(mStartX, mStartY, mEndX, mEndY, mPaint);
    }

    void changeAngle(float x, float y) {
        mBitmap.eraseColor(Color.TRANSPARENT);

        logLine("Change Angle: Pre ");
        mEndY = y;
        logLine("Change Angle: Post ");

        mCanvas.drawLine(mStartX, mStartY, mEndX, mEndY, mPaint);
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
        Log.i(this.getClass().getSimpleName(), "lineAngle:" + angle);
        return angle;
    }


    private void logLine(String message) {
        Log.i(this.getClass().getSimpleName(), message + "( X:" + (int) mStartX + "-" + (int) mEndX + "|| Y:" + (int) mStartY + "-" + (int) mEndY + ")");
    }

    //endregion

}

