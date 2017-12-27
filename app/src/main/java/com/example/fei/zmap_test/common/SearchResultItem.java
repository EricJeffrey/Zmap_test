package com.example.fei.zmap_test.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;

/**
 * Created by FEI on 2017/12/20.
 * 搜索得到的结果项
 */

public class SearchResultItem implements Parcelable {
    private LatLng poiItemLatLng;
    private String poiItemName;
    private PoiItem item;

    public SearchResultItem(PoiItem item){
        if(item != null) {
            this.item = item;
            LatLonPoint point = item.getLatLonPoint();
            this.poiItemLatLng = new LatLng(point.getLatitude(), point.getLongitude());
            this.poiItemName = item.getTitle();
        }
        else poiItemName = "NONE";
    }

    public LatLng getPoiItemLatLng() {
        return poiItemLatLng;
    }
    public String getPoiItemLatLngDesription(){
        return "经度：" + poiItemLatLng.longitude + ", 纬度：" + poiItemLatLng.latitude;
    }
    public String getPoiItemName() {
        return poiItemName;
    }

    public PoiItem getItem() {
        return item;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(item, 1);
        dest.writeParcelable(poiItemLatLng, 2);
        dest.writeString(poiItemName);
    }
    public static final Creator<SearchResultItem> CREATOR = new Creator<SearchResultItem>() {
        @Override
        public SearchResultItem createFromParcel(Parcel source) {
            return new SearchResultItem((PoiItem)source.readParcelable(PoiItem.class.getClassLoader()));
        }

        @Override
        public SearchResultItem[] newArray(int size) {
            return new SearchResultItem[size];
        }
    };
}
