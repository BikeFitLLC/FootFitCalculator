package com.bikefit.wedgecalculator.view;

import android.content.Context;
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


public class DrawLine extends View {

    //region STATIC LOCAL CONSTANTS ----------------------------------------------------------------
    private static final float RADIUS = 25;
    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint;
    private Paint mPaint;

    private Matrix mMatrix;

    private float mMiddleScreen;
    private float mLineAngle;
    private float mStartX, mStartY, mEndX, mEndY;

    private boolean mCanMove = false;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public DrawLine(Context context, AttributeSet attrs) {
        super(context, attrs);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mBitmap = Bitmap.createBitmap(dm.widthPixels, dm.heightPixels, Bitmap.Config.ARGB_8888);

        mMatrix = new Matrix();
        mCanvas = new Canvas(mBitmap);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mPaint.setStrokeWidth(10);

        // Setup initial values
        mMiddleScreen = Math.round(dm.widthPixels / 2);

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

    private void touchStart(float x, float y) {

        Log.i(this.getClass().getSimpleName(), "==Touch-Start=============================================================================");
        Log.i(this.getClass().getSimpleName(), "Current Line X:" + (int) mStartX + ":" + (int) mEndX + "|| Y:" + (int) mStartY + ":" + (int) mEndY);
        Log.i(this.getClass().getSimpleName(), "Touch at cords X:" + (int) x + " Y:" + (int) y);

        mCanMove = circleLineIntersect(mStartX, mStartY, mEndX, mEndY, x, y, DrawLine.RADIUS);

        if (mCanMove) {
            Log.i(this.getClass().getSimpleName(), "MOVE YES");
        } else {
            Log.i(this.getClass().getSimpleName(), "MOVE NO");
        }
    }


    private void moveVertical(float x, float y) {
        mBitmap.eraseColor(Color.TRANSPARENT);

        logLine("Move Vertical: Pre ");

        float verticalChange = y - mEndY;
        mStartY += verticalChange;
        mEndY += verticalChange;

        logLine("Move Vertical: Post ");

        mCanvas.drawLine(mStartX, mStartY, mEndX, mEndY, mPaint);
    }

    private void changeAngle(float x, float y) {
        mBitmap.eraseColor(Color.TRANSPARENT);

        logLine("Change Angle: Pre ");
        mEndY = y;
        logLine("Change Angle: Post ");

        mCanvas.drawLine(mStartX, mStartY, mEndX, mEndY, mPaint);
    }


    private boolean circleLineIntersect(float x1, float y1, float x2, float y2, float cx, float cy, float cr) {
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


    private float calculateLineAngle() {
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

