package com.example.fei.zmap_test;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by FEI on 2017/12/20.
 * 路线规划里面每个tab的view的适配器
 */

public class RoutePlanPagerAdapter extends PagerAdapter {
    private ArrayList<View> viewList;
    private ArrayList<String> titleList;

    public RoutePlanPagerAdapter(ArrayList<View> viewList, ArrayList<String> titleList){
        this.viewList = viewList;
        this.titleList = titleList;
    }
    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }
}
