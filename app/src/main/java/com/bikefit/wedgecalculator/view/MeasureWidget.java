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
    private float mStatusBarHeight;
    private static final boolean DEBUG_DRAWING = false;
    private static final float MAXIMUM_ANGLE = 30.0f;

    // Transform state
    private Matrix mMainTransform = new Matrix();
    private Matrix mRotateIconOffsetTransform = new Matrix();
    private Matrix mRotateIconTransform = new Matrix();
    private Matrix mUpDownIconTransform = new Matrix();
    private Matrix mScratchMatrix = new Matrix();
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
    private float mInputTouchRadius;

    // Listener
    private AngleListener mAngleListener;

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
        float screenHeight = dm.heightPixels;
        float screenWidth = dm.widthPixels;

        mScreenSize.set(screenWidth, screenHeight);
        mHalfScreenSize.set(screenWidth * 0.5f, screenHeight * 0.5f);

        mUpDownIcon = VectorDrawableCompat.create(getResources(), R.drawable.up_down_icon, null);
        mUpDownIcon.setBounds(0, 0, mUpDownIcon.getIntrinsicWidth(), mUpDownIcon.getIntrinsicHeight());
        mUpDownIconSize = new PointF(mUpDownIcon.getIntrinsicWidth(), mUpDownIcon.getIntrinsicHeight());
        mRotateIcon = VectorDrawableCompat.create(getResources(), R.drawable.rotate_icon, null);
        mRotateIcon.setBounds(0, 0, mRotateIcon.getIntrinsicWidth(), mRotateIcon.getIntrinsicHeight());
        mRotateIconSize = new PointF(mRotateIcon.getIntrinsicWidth(), mRotateIcon.getIntrinsicHeight());

        mInputTouchRadius = 1.33f * (Math.max(mRotateIconSize.y, mUpDownIconSize.y) * 0.5f);

        mStatusBarHeight = getStatusBarHeight();

        setupLinePaint();

        setupDebugLinePaint();

        // Initial transforms
        mDistanceToIconsFromCenter = screenWidth / 3;
        mMainTransform.setTranslate(screenWidth / 2, screenHeight / 2);
        mRotateIconOffsetTransform.setTranslate(mDistanceToIconsFromCenter, 0);
        mRotateIconTransform.setTranslate(-mRotateIconSize.x * 0.5f, -mRotateIconSize.y * 0.5f);
        mUpDownIconTransform.setTranslate(-mUpDownIconSize.x * 0.5f, -mUpDownIconSize.y * 0.5f);

        // Main left/right transform
        //mMainTransform.preRotate(180);
    }

    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mScratchMatrix.reset();

        drawVerticalCenterLine(canvas);
        drawHorizontalLine(canvas);
        drawRotateIcon(canvas);
        drawUpDownIcon(canvas);

        if (DEBUG_DRAWING) {
            drawDebugAxisCanvasTransform(canvas);
            drawDebugAxisInWorldSpace(canvas, mMainTransform, 200);
            debugDrawInput(canvas);
        }
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

    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
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
        mScratchMatrix.reset();
        mScratchMatrix.set(mMainTransform);
        mScratchMatrix.preConcat(mRotateIconOffsetTransform);
        mScratchMatrix.preConcat(mRotateIconTransform);

        canvas.save();
        canvas.setMatrix(mScratchMatrix);
        if (DEBUG_DRAWING) {
            mDebugPaint.setColor(Color.MAGENTA);
            canvas.drawRect(mRotateIcon.getBounds(), mDebugPaint);
        }
        mRotateIcon.draw(canvas);
        canvas.restore();
    }

    private void drawUpDownIcon(Canvas canvas) {
        float[] iconAnchorPoint = getUpDownIconWorldSpaceCenter();

        mScratchMatrix.reset();
        mScratchMatrix.setTranslate(iconAnchorPoint[0], iconAnchorPoint[1]);
        mScratchMatrix.postTranslate(0, mStatusBarHeight);
        mScratchMatrix.postConcat(mUpDownIconTransform);

        canvas.save();
        canvas.setMatrix(mScratchMatrix);
        if (DEBUG_DRAWING) {
            mDebugPaint.setColor(Color.MAGENTA);
            canvas.drawRect(mUpDownIcon.getBounds(), mDebugPaint);
        }
        mUpDownIcon.draw(canvas);
        canvas.restore();
    }

    //endregion

    //region INPUT

    private void touchStart(PointF input) {
        mLastTouchPoint.set(input);
        mPrevMovePoint.set(input);

        float[] upDownIconCenter = getUpDownIconWorldSpaceCenter();
        float[] rotateIconCenter = getRotateIconWorldSpaceCenter();

        boolean touchUpDownIcon = touchIconRadiusCheck(input, new PointF(upDownIconCenter[0], upDownIconCenter[1]), mInputTouchRadius);
        boolean touchRotate = touchIconRadiusCheck(input, new PointF(rotateIconCenter[0], rotateIconCenter[1]), mInputTouchRadius);

        if (touchUpDownIcon) {
            mMoveMode = MoveMode.TRANSLATE;
        } else if (touchRotate) {
            mMoveMode = MoveMode.ROTATE;
        }
    }

    private boolean touchIconRadiusCheck(PointF input, PointF iconCenter, float radius) {
        PointF delta = pointSubtract(input, iconCenter);
        return delta.length() <= radius;
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
        float angleDelta = clampAngle(angleCurrent - anglePrev);

        float newAngle = clampAngle(mAngle + angleDelta);
        float clampedAngle = clamp(-MAXIMUM_ANGLE, MAXIMUM_ANGLE, newAngle);

        angleDelta += clampedAngle - newAngle;
        mAngle = clampedAngle;

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
        return clampAngle(angle);
    }

    private PointF pointSubtract(PointF a, PointF b) {
        return new PointF(a.x - b.x, a.y - b.y);
    }

    /**
     * @return the rotate icon's center position in the world / input space, relative to the window
     * without the status bar height.
     */
    private float[] getRotateIconWorldSpaceCenter() {
        // Concatenate the main transform with the offset, accounting for the status bar height
        mScratchMatrix.reset();
        mScratchMatrix.set(mMainTransform);
        mScratchMatrix.preConcat(mRotateIconOffsetTransform);
        mScratchMatrix.postTranslate(0, -mStatusBarHeight);

        float[] position = {0, 0};
        mScratchMatrix.mapPoints(position);
        return position;
    }

    /**
     * @return the up-down icon's center position in the world / input space, relative to the window
     * without the status bar height.
     */
    private float[] getUpDownIconWorldSpaceCenter() {
        mScratchMatrix.reset();
        mScratchMatrix.set(mMainTransform);

        float[] forwardAxis = {10, 0};
        mScratchMatrix.mapVectors(forwardAxis);
        float directionMultiplier = forwardAxis[0] > 0.0f ? -1.0f : 1.0f;

        float distOfIconFromOrigin = mDistanceToIconsFromCenter * directionMultiplier;
        float distanceAlongRotatedLine = distOfIconFromOrigin / (float) Math.cos(Math.toRadians(mAngle));

        float[] centerWorldSpace = {0, 0};
        mScratchMatrix.mapPoints(centerWorldSpace);

        mScratchMatrix.reset();
        mScratchMatrix.setTranslate(distanceAlongRotatedLine, 0);
        mScratchMatrix.postRotate(-mAngle);
        mScratchMatrix.postTranslate(centerWorldSpace[0], centerWorldSpace[1]);
        mScratchMatrix.postTranslate(0, -mStatusBarHeight);

        float[] iconAnchorPoint = {0, 0};
        mScratchMatrix.mapPoints(iconAnchorPoint);
        return iconAnchorPoint;
    }

    private float clamp(float min, float max, float value) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Clamp a degrees angle value between [-180, 180].
     *
     * @param angle an angle in degrees to constrain within the bounds.
     * @return an angle between [-180, 180].
     */
    private float clampAngle(float angle) {
        while (angle > 180.0f)
            angle -= 360.0f;
        while (angle < -180.0f)
            angle += 360.0f;
        return angle;
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

        float[] upDownIconPos = getUpDownIconWorldSpaceCenter();
        mDebugPaint.setColor(Color.GRAY);
        canvas.drawCircle(upDownIconPos[0], upDownIconPos[1], mInputTouchRadius, mDebugPaint);

        float[] rotateIconPos = getRotateIconWorldSpaceCenter();
        mDebugPaint.setColor(Color.GRAY);
        canvas.drawCircle(rotateIconPos[0], rotateIconPos[1], mInputTouchRadius, mDebugPaint);
    }

    //endregion

    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------
    //endregion
}
