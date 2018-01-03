package com.example.fei.zmap_test.common;

/**
 * Created by FEI on 2018/1/3.
 * 城市选择列表
 */

public class CityListItem {
    public String title;
    public boolean showTitle;

    public CityListItem(String title, boolean showTitle){
        this.title = title;
        this.showTitle = showTitle;
    }

    public String getTitle() {
        return title;
    }

    public boolean isShowTitle() {
        return showTitle;
    }
}
