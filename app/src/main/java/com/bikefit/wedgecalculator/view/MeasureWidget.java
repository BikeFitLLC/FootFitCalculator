package com.bikefit.wedgecalculator.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.bikefit.wedgecalculator.R;

public class MeasureWidget extends View {
    //region INJECTED CLASSES ----------------------------------------------------------------------
    //endregion

    //region INJECTED VIEWS ------------------------------------------------------------------------
    //endregion

    //region PUBLIC INTERFACES ---------------------------------------------------------------------

    /**
     * Listener for the current angle. This emits the absolute value of the angle, so it doesn't
     * matter about the rotation, it's always an angle relative to the horizontal.
     */
    public interface AngleListener {
        void onAngleUpdate(float angle);
    }

    //endregion

    //region STATIC LOCAL CONSTANTS ----------------------------------------------------------------

    private enum MoveMode {
        NONE,
        ROTATE,
        TRANSLATE,
    }

    private enum FootSide {
        LEFT,
        RIGHT
    }

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    // Constants
    private PointF mScreenSize = new PointF();
    private PointF mHalfScreenSize = new PointF();
    private Paint mLinePaint;
    private Paint mDebugPaint;
    private float mDistanceToIconsFromCenter;

    // Transform state
    private Matrix mMainTransform = new Matrix();
    private Matrix mRotateIconTransform = new Matrix();
    private Matrix mUpDownIconTransform = new Matrix();
    private float mAngle;

    // Icon Resources
    private VectorDrawableCompat mUpDownIcon;
    private VectorDrawableCompat mRotateIcon;
    private PointF mRotateIconSize;
    private PointF mUpDownIconSize;

    // Input
    private PointF mPrevMovePoint = new PointF();
    private PointF mLastTouchPoint = new PointF();
    private MoveMode mMoveMode = MoveMode.NONE;

    // Listener
    private AngleListener mAngleListener;

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

        mUpDownIcon = VectorDrawableCompat.create(getResources(), R.drawable.up_down_icon, null);
        mUpDownIcon.setBounds(0, 0, mUpDownIcon.getIntrinsicWidth(), mUpDownIcon.getIntrinsicHeight());
        mUpDownIconSize = new PointF(mUpDownIcon.getIntrinsicWidth(), mUpDownIcon.getIntrinsicHeight());
        mRotateIcon = VectorDrawableCompat.create(getResources(), R.drawable.rotate_icon, null);
        mRotateIcon.setBounds(0, 0, mRotateIcon.getIntrinsicWidth(), mRotateIcon.getIntrinsicHeight());
        mRotateIconSize = new PointF(mRotateIcon.getIntrinsicWidth(), mRotateIcon.getIntrinsicHeight());

        setupLinePaint();

        setupDebugLinePaint();

        // Initial transforms
        mDistanceToIconsFromCenter = screenWidth / 3;
        mMainTransform.setTranslate(screenWidth / 2, screenHeight / 2);
        mRotateIconTransform.setTranslate(mDistanceToIconsFromCenter + (-mRotateIconSize.x * 0.5f), -mRotateIconSize.y * 0.5f);
        mUpDownIconTransform.setTranslate(-mUpDownIconSize.x * 0.5f, -mUpDownIconSize.y * 0.5f);

        // Main left/right transform
        //mMainTransform.preRotate(180);
    }


    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDebugScratchMatrix.reset();

        drawVerticalCenterLine(canvas);
        drawHorizontalLine(canvas);


        drawDebugAxisCanvasTransform(canvas);
        drawDebugAxisInWorldSpace(canvas, mMainTransform, 200);
        debugDrawInput(canvas);

        //debugDrawAngle(canvas);
        drawRotateIcon(canvas);
        drawUpDownIcon(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        PointF input = new PointF(event.getX(), event.getY());

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                touchStart(input);
                break;
            case MotionEvent.ACTION_UP:
                mMoveMode = MoveMode.NONE;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mMoveMode == MoveMode.TRANSLATE)
                    moveVertical(input);
                else if (mMoveMode == MoveMode.ROTATE)
                    moveRotate(input);
                break;
        }

        invalidate();
        return true;
    }

    //endregion

    //region LISTENERS -----------------------------------------------------------------------------
    //endregion

    //region ACCESSORS -----------------------------------------------------------------------------
    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------

    /**
     * Set the current angle listener to receive angle events.
     *
     * @param listener listener for receiving the current angle.
     */
    public void setAngleListener(@Nullable AngleListener listener) {
        mAngleListener = listener;
        if (listener != null) {
            listener.onAngleUpdate(0.0f);
        }
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------

    //region SETUP

    private void setupLinePaint() {
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
        mLinePaint.setStrokeCap(Paint.Cap.SQUARE);
        mLinePaint.setColor(Color.RED);
        mLinePaint.setDither(true);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(5);
    }

    private void setupDebugLinePaint() {
        mDebugPaint = new Paint();
        mDebugPaint.setStyle(Paint.Style.STROKE);
        mDebugPaint.setStrokeJoin(Paint.Join.ROUND);
        mDebugPaint.setStrokeCap(Paint.Cap.SQUARE);
        mDebugPaint.setDither(true);
        mDebugPaint.setAntiAlias(true);
        mDebugPaint.setStrokeWidth(3);
    }

    //endregion

    //region DRAW

    private void drawHorizontalLine(Canvas canvas) {
        canvas.save();

        canvas.setMatrix(mMainTransform);
        final float maxExtent = Math.max(mScreenSize.y, mScreenSize.x) * 2;
        canvas.drawLine(-maxExtent, 0, maxExtent, 0, mLinePaint);

        canvas.restore();
    }

    private void drawVerticalCenterLine(Canvas canvas) {
        canvas.drawLine(mHalfScreenSize.x, 0, mHalfScreenSize.x, mScreenSize.y, mLinePaint);
    }

    private void drawRotateIcon(Canvas canvas) {
        mDebugScratchMatrix.reset();
        mDebugScratchMatrix.set(mMainTransform);
        mDebugScratchMatrix.preConcat(mRotateIconTransform);

        drawDebugAxisInWorldSpace(canvas, mDebugScratchMatrix, 100);

        canvas.save();
        mDebugPaint.setColor(Color.MAGENTA);
        canvas.setMatrix(mDebugScratchMatrix);
        canvas.drawRect(mRotateIcon.getBounds(), mDebugPaint);
        mRotateIcon.draw(canvas);
        canvas.restore();
    }

    private void drawUpDownIcon(Canvas canvas) {
        mDebugScratchMatrix.reset();
        mDebugScratchMatrix.set(mMainTransform);

        float[] forwardAxis = {10, 0};
        mDebugScratchMatrix.mapVectors(forwardAxis);
        float directionMultiplier = forwardAxis[0] > 0.0f ? -1.0f : 1.0f;

        float distOfIconFromOrigin = mDistanceToIconsFromCenter * directionMultiplier;
        float distanceAlongRotatedLine = distOfIconFromOrigin / (float) Math.cos(Math.toRadians(mAngle));

        float[] centerWorldSpace = {0, 0};
        mDebugScratchMatrix.mapPoints(centerWorldSpace);

        mDebugScratchMatrix.reset();
        mDebugScratchMatrix.setTranslate(distanceAlongRotatedLine, 0);
        mDebugScratchMatrix.postRotate(-mAngle);
        mDebugScratchMatrix.postTranslate(centerWorldSpace[0], centerWorldSpace[1]);

        float[] iconAnchorPoint = {0, 0};
        mDebugScratchMatrix.mapPoints(iconAnchorPoint);

        canvas.save();
        canvas.setMatrix(mDebugScratchMatrix);
        mDebugPaint.setColor(Color.CYAN);
        canvas.drawCircle(0, 0, 50, mDebugPaint);
        canvas.restore();

        mDebugScratchMatrix.reset();
        mDebugScratchMatrix.setTranslate(iconAnchorPoint[0], iconAnchorPoint[1]);
        mDebugScratchMatrix.postConcat(mUpDownIconTransform);

        canvas.save();
        mDebugPaint.setColor(Color.MAGENTA);
        canvas.setMatrix(mDebugScratchMatrix);
        canvas.drawRect(mUpDownIcon.getBounds(), mDebugPaint);
        mUpDownIcon.draw(canvas);
        canvas.restore();
    }

    //endregion

    //region INPUT

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

        mMainTransform.preRotate(-angleDelta);
        mPrevMovePoint.set(input);

        if (mAngleListener != null)
            mAngleListener.onAngleUpdate(mAngle);
    }

    //endregion

    //region MATH

    private float getAngleBetweenPoints(float startX, float startY, float endX, float endY) {
        float angle = (float) Math.toDegrees(Math.atan2(endY - startY, endX - startX));
        angle *= -1;
        return angle;
    }

    private PointF pointSubtract(PointF a, PointF b) {
        return new PointF(a.x - b.x, a.y - b.y);
    }

    //endregion

    //region DEBUG

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

    private void drawDebugAxisInWorldSpace(Canvas canvas, Matrix transform, float axisLength) {
        float[] center = {0, 0};
        float[] xAxis = {axisLength, 0};
        float[] yAxis = {0, axisLength};
        transform.mapPoints(center);
        transform.mapPoints(xAxis);
        transform.mapPoints(yAxis);
        mDebugPaint.setColor(Color.RED);
        canvas.drawLine(center[0], center[1], xAxis[0], xAxis[1], mDebugPaint);
        mDebugPaint.setColor(Color.GREEN);
        canvas.drawLine(center[0], center[1], yAxis[0], yAxis[1], mDebugPaint);
    }

    private void debugDrawInput(Canvas canvas) {
        float radius = 10;
        mDebugPaint.setColor(Color.YELLOW);
        canvas.drawCircle(mLastTouchPoint.x, mLastTouchPoint.y, radius, mDebugPaint);

        mDebugPaint.setColor(Color.CYAN);
        canvas.drawCircle(mPrevMovePoint.x, mPrevMovePoint.y, radius, mDebugPaint);
    }

    //endregion

    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------
    //endregion
}
