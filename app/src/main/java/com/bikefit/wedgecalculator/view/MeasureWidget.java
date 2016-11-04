package com.bikefit.wedgecalculator.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MeasureWidget extends View {
    //region INJECTED CLASSES ----------------------------------------------------------------------
    //endregion

    //region INJECTED VIEWS ------------------------------------------------------------------------
    //endregion

    //region PUBLIC INTERFACES ---------------------------------------------------------------------
    //endregion

    //region STATIC LOCAL CONSTANTS ----------------------------------------------------------------

    private enum MoveMode {
        NONE,
        ROTATE,
        TRANSLATE,
    }

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    // Constants
    private PointF mScreenSize = new PointF();
    private PointF mHalfScreenSize = new PointF();
    private Paint mLinePaint;
    private Paint mDebugPaint;

    // Transform state
    private Matrix mMainTransform = new Matrix();
    private float mAngle;

    // Input
    private PointF mPrevMovePoint = new PointF();
    private PointF mLastTouchPoint = new PointF();
    private MoveMode mMoveMode = MoveMode.NONE;

    // Misc
    private Matrix mDebugScratchMatrix = new Matrix();

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public MeasureWidget(Context context) {
        super(context);
        init(null);
    }

    public MeasureWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MeasureWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        float screenHeight = dm.heightPixels;   // STATUS_BAR_HEIGHT
        float screenWidth = dm.widthPixels;

        mScreenSize.set(screenWidth, screenHeight);
        mHalfScreenSize.set(screenWidth * 0.5f, screenHeight * 0.5f);

        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
        mLinePaint.setStrokeCap(Paint.Cap.SQUARE);
        mLinePaint.setDither(true);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(5);

        mDebugPaint = new Paint();
        mDebugPaint.setStyle(Paint.Style.STROKE);
        mDebugPaint.setStrokeJoin(Paint.Join.ROUND);
        mDebugPaint.setStrokeCap(Paint.Cap.SQUARE);
        mDebugPaint.setDither(true);
        mDebugPaint.setAntiAlias(true);
        mDebugPaint.setStrokeWidth(3);

        // Initial transform
        mMainTransform.setTranslate(screenWidth / 2, screenHeight / 2);
    }


    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDebugScratchMatrix.reset();

        drawDebugAxisCanvasTransform(canvas);
        drawDebugAxisWorldSpace(canvas);
        debugDrawInput(canvas);
        debugDrawAngle(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        PointF input = new PointF(event.getX(), event.getY());

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                touchStart(input);
                break;
//            case MotionEvent.ACTION_UP:
//                mMoveMode = MeasureLine.MoveMode.NONE;
//                break;

            case MotionEvent.ACTION_MOVE:
                if (mMoveMode == MoveMode.TRANSLATE)
                    moveVertical(input);
                else if (mMoveMode == MoveMode.ROTATE)
                    moveRotate(input);
                break;
        }

        /*updateRotateIconMatrix(getWidth());
        updateUpDownIconMatrix(getWidth());*/

        invalidate();
        return true;
    }

    //endregion

    //region LISTENERS -----------------------------------------------------------------------------
    //endregion

    //region ACCESSORS -----------------------------------------------------------------------------
    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------
    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------

    private void touchStart(PointF input) {
        mLastTouchPoint.set(input);
        mPrevMovePoint.set(input);

        // TODO - TEMP!!
        if (input.x < mScreenSize.x / 2) {
            mMoveMode = MoveMode.TRANSLATE;
        } else {
            mMoveMode = MoveMode.ROTATE;
        }
    }

    private void moveVertical(PointF input) {
        PointF delta = pointSubtract(input, mPrevMovePoint);

        mPrevMovePoint.set(input);

        mMainTransform.postTranslate(0, delta.y);
    }

    private void moveRotate(PointF input) {
        float[] center = {0, 0};
        mMainTransform.mapPoints(center);

        float anglePrev = getAngleBetweenPoints(center[0], center[1], mPrevMovePoint.x, mPrevMovePoint.y);
        float angleCurrent = getAngleBetweenPoints(center[0], center[1], input.x, input.y);

        float angleDelta = angleCurrent - anglePrev;
        mAngle += angleDelta;

        Log.d("MeasureWidget", "Rotate: prev : " + anglePrev + ", current : " + angleCurrent + ", delta : " + angleDelta + " finalAngle : " + mAngle);
        mMainTransform.preRotate(-angleDelta);
        mPrevMovePoint.set(input);
    }

    private float getAngleBetweenPoints(float startX, float startY, float endX, float endY) {
        float angle = (float) Math.toDegrees(Math.atan2(endY - startY, endX - startX));
        angle *= -1;
        return angle;
    }

    private void drawDebugAxisCanvasTransform(Canvas canvas) {
        final float axisLength = 200;
        canvas.save();
        canvas.setMatrix(mMainTransform);
        mDebugPaint.setColor(Color.RED);
        canvas.drawLine(0, 0, axisLength, 0, mDebugPaint);
        mDebugPaint.setColor(Color.GREEN);
        canvas.drawLine(0, 0, 0, axisLength, mDebugPaint);
        canvas.restore();
    }

    private void debugDrawAngle(Canvas canvas) {
        canvas.save();

        mDebugScratchMatrix.reset();
        mDebugScratchMatrix.setRotate(mAngle);
        mDebugScratchMatrix.postTranslate(mHalfScreenSize.x, mHalfScreenSize.y);
        canvas.setMatrix(mDebugScratchMatrix);

        mDebugPaint.setColor(Color.YELLOW);
        canvas.drawLine(0, 0, 400, 0, mDebugPaint);

        canvas.restore();
    }

    private void drawDebugAxisWorldSpace(Canvas canvas) {
        final float axisLength = 200;
        float[] center = {0, 0};
        float[] xAxis = {axisLength, 0};
        float[] yAxis = {0, axisLength};
        mMainTransform.mapPoints(center);
        mMainTransform.mapPoints(xAxis);
        mMainTransform.mapPoints(yAxis);
        mDebugPaint.setColor(Color.MAGENTA);
        canvas.drawLine(center[0], center[1], xAxis[0], xAxis[1], mDebugPaint);
        mDebugPaint.setColor(Color.CYAN);
        canvas.drawLine(center[0], center[1], yAxis[0], yAxis[1], mDebugPaint);
    }

    private void debugDrawInput(Canvas canvas) {
        float radius = 10;
        mDebugPaint.setColor(Color.YELLOW);
        canvas.drawCircle(mLastTouchPoint.x, mLastTouchPoint.y, radius, mDebugPaint);

        mDebugPaint.setColor(Color.CYAN);
        canvas.drawCircle(mPrevMovePoint.x, mPrevMovePoint.y, radius, mDebugPaint);
    }

    private PointF pointSubtract(PointF a, PointF b) {
        return new PointF(a.x - b.x, a.y - b.y);
    }

    //endregion

    //region OBSERVERS -----------------------------------------------------------------------------
    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------
    //endregion
}
