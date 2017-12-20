package com.example.fei.zmap_test;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by FEI on 2017/12/20.
 * 不能够左右滑动的view pager
 */

public class ViewPagerNoSlide extends ViewPager {

    public ViewPagerNoSlide(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
