package com.demo.lli.mydemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by liyong on 2016/7/27.
 */
public class MySlideLinearlayout extends LinearLayout {
    private static final String TAG = "APP";
    private View contentView, actionView;
    private int mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    private Scroller mScroller;
    private int dragDistance;
    private int downX, downY, moveX, moveY, mStartX;
    //处理是否是横向滑动，拦截事件
    private int mMostRecentX,mMostRecentY;

    public MySlideLinearlayout(Context context) {
        this(context, null);
    }

    public MySlideLinearlayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MySlideLinearlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        actionView = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        dragDistance = actionView.getMeasuredWidth();
        Log.d(TAG, "dragDistance is " + dragDistance + "::mTouchSlop is " + mTouchSlop);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent running...");
        boolean intercepted = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMostRecentX = (int) ev.getX();
                mMostRecentY= (int) ev.getY();
                mStartX=mMostRecentX;
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int diff = moveX - mMostRecentX;
                Log.i(TAG, "diff is " + diff);
                if (Math.abs(diff) > mTouchSlop) {
                    intercepted = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
        }
        Log.i(TAG, "onInterceptTouchEvent return = " + intercepted);
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                downX = (int) event.getX();
//                downY = (int) event.getY();
//                Log.i(TAG,"onTouchEvent ACTION_DOWN ");
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = x;
                moveY = y;
                int dx = moveX - mMostRecentX;
                int dy = moveY - mMostRecentY;
                if (Math.abs(dy) < mTouchSlop * 2 && Math.abs(dx) > mTouchSlop) {
                    int scrollX = getScrollX();
                    int newScroll = mStartX - x;
                    if (newScroll < 0 && scrollX <= 0) {
                        newScroll = 0;
                    } else if (newScroll > 0 && scrollX >= dragDistance) {
                        newScroll = 0;
                    }
                    scrollBy(newScroll, 0);
                }

                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                int newScrollX;
                if (scrollX >= dragDistance / 2) {
                    newScrollX = dragDistance - scrollX;
                } else {
                    newScrollX = -scrollX;

                }
                mScroller.startScroll(scrollX, 0, newScrollX, 0);
                invalidate();
                break;
        }
        mStartX = x;
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}
