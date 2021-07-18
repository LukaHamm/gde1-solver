package com.lukh.gde1_solver.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;


import androidx.annotation.Nullable;

import com.google.android.material.card.MaterialCardView;
import com.lukh.gde1_solver.Listeners.OnCircuitComponentTouchedListener;
import com.lukh.gde1_solver.Listeners.OnCircuitDiagramScale;

import java.util.ArrayList;
import java.util.List;

public class ZoomableMaterialCardView extends MaterialCardView {


    private Context context;
    private ScaleGestureDetector scaleGestureDetector;
    private OnCircuitDiagramScale onCircuitDiagramScale;
    private Float scaleFactor;
    private float mPosX;
    private float mPosY;
    private float mLastTouchX;
    private float mLastTouchY;
    private static final int INVALID_POINTER_ID = 1;
    private int mActivePointerId = INVALID_POINTER_ID;
    private float mFocusX;
    private float mFocusY;



    private OnCircuitComponentTouchedListener onCircuitComponentTouchedListener;



    private ImageView tagForDraggedComponent;


    private Integer viewWidth;
    private Integer viewHeight;



    public ZoomableMaterialCardView(Context context) {
        super(context);


    }





    public ZoomableMaterialCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        scaleFactor = 1.0f;
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        setOnTouchListener(this.onTouchListener);

    }

    public ZoomableMaterialCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    /*
    @Override
    public void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mPosX,mPosY);
        canvas.scale(scaleFactor, scaleFactor, mFocusX, mFocusY);
        super.onDraw(canvas);
        canvas.restore();


        //canvas.restore();


    }
    */

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
        super.onSizeChanged(xNew, yNew, xOld, yOld);

        viewWidth = xNew;
        viewHeight = yNew;
        Integer viewPortLeft = this.getLeft();
        Integer viewPortRight = this.getRight();
        Integer viewPortTop = this.getTop();
        Integer viewPortBottom = this.getBottom();

    }


    private ImageView getImageViewWithinBounds(float x, float y){
        List<ImageView> components = findAllChild();
        ImageView searchedImageView = null;
       for(ImageView imageView: components){
           RectF rect = new RectF();
           Float left = imageView.getX();
           Float top = imageView.getY();
           Float right = left + imageView.getWidth();
           Float bottom = top+ imageView.getHeight();
           rect.set(left,top,right,bottom);
           if(rect.contains(x,y)){
               searchedImageView = imageView;
           }

       }

       return searchedImageView;
    }

    private List<ImageView> findAllChild(){
        List<ImageView> components = new ArrayList<>();
        final int childCount = this.getChildCount();
        for(int i = 0;i<childCount;i++ ){
            final View child = this.getChildAt(i);
                components.add((ImageView) child);

        }

        return components;

    }

   private OnLongClickListener onLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            ZoomableMaterialCardView.this.setOnTouchListener(ZoomableMaterialCardView.this.onTouchListener);


            return true;
        }
    };

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mPosX, mPosY);
        canvas.scale(scaleFactor, scaleFactor, mFocusX, mFocusY);
        super.dispatchDraw(canvas);
        canvas.restore();
    }



    //DispatchTouchEvent,InterceptTouchEvent
    private OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int pointerCount = event.getPointerCount();
            ImageView component = getImageViewWithinBounds(event.getX(),event.getY());
            if (component != null){
                component.onTouchEvent(event);
            }
            scaleGestureDetector.onTouchEvent(event);
            final int action = event.getAction();
        /*ImageView sourceComponent = getImageViewWithinBounds(event.getX(),event.getY());
        boolean imageViewOncoordinates = sourceComponent!=null;
         */
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: {
                    final float x = event.getX();
                    final float y = event.getY();

                    mLastTouchX = x;
                    mLastTouchY = y;

                    // Save the ID of this pointer
                    mActivePointerId = event.getPointerId(0);
                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    // Find the index of the active pointer and fetch its position
                    if(mActivePointerId == INVALID_POINTER_ID){
                        mActivePointerId = event.getPointerId(0);
                    }
                    final int pointerIndex = event.findPointerIndex(mActivePointerId);
                    final float x = event.getX(pointerIndex);
                    final float y = event.getY(pointerIndex);

                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;


                    mPosX += dx;
                    mPosY += dy;


                    mLastTouchX = x;
                    mLastTouchY = y;

                   invalidate();
                    break;
                }

                case MotionEvent.ACTION_UP: {
                    mActivePointerId = INVALID_POINTER_ID;

                    break;
                }

                case MotionEvent.ACTION_CANCEL: {
                    mActivePointerId = INVALID_POINTER_ID;

                    break;
                }

                case MotionEvent.ACTION_POINTER_UP: {
                    // Extract the index of the pointer that left the touch sensor
                    final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    final int pointerId = event.getPointerId(pointerIndex);
                    if (pointerId == mActivePointerId) {
                        // This was our active pointer going up. Choose a new
                        // active pointer and adjust accordingly.
                        final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        mLastTouchX = event.getX(newPointerIndex);
                        mLastTouchY = event.getY(newPointerIndex);
                        mActivePointerId = event.getPointerId(newPointerIndex);


                    }
                    break;
                }
            }

            return true;
        }

    };









    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            if (detector.isInProgress()) {
                mFocusX = detector.getFocusX();
                mFocusY = detector.getFocusY();

            }
            if ((mFocusX>=0 && mFocusX<=viewWidth)&&(mFocusY>=0 &&mFocusY<=viewHeight)) {
                // Don't let the object get too small or too large.
                scaleFactor *= detector.getScaleFactor();
                scaleFactor = Math.max(0.75f, Math.min(scaleFactor, 5.0f));
                Integer left = ZoomableMaterialCardView.this.getLeft();
                Integer right = ZoomableMaterialCardView.this.getRight();
                Integer top = ZoomableMaterialCardView.this.getTop();
                Integer bottom = ZoomableMaterialCardView.this.getBottom();
                System.out.println("left: " + left);
                System.out.println("right: " + right);
                System.out.println("top: " + top);
                System.out.println("bottom: " + bottom);

                invalidate();
            }
            return true;
        }


    }




    public ImageView getTagForDraggedComponent() {
        return tagForDraggedComponent;
    }

    public void setTagForDraggedComponent(ImageView tagForDraggedComponent) {
        this.tagForDraggedComponent = tagForDraggedComponent;
    }


    public void initScaleGestureDetector() {
        onCircuitDiagramScale = new OnCircuitDiagramScale(this);
        scaleGestureDetector = new ScaleGestureDetector(context, onCircuitDiagramScale);
    }


    public OnCircuitComponentTouchedListener getOnCircuitComponentTouchedListener() {
        return onCircuitComponentTouchedListener;
    }

    public void setOnCircuitComponentTouchedListener(OnCircuitComponentTouchedListener onCircuitComponentTouchedListener) {
        this.onCircuitComponentTouchedListener = onCircuitComponentTouchedListener;
    }

}

