package com.example.fei.zmap_test.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;

/**
 * Created by FEI on 2017/12/20.
 * 搜索得到的结果项
 */

public class SearchResultItem implements Parcelable {
    private LatLng itemLatLng;
    private String itemDetail;

    public SearchResultItem(LatLng itemLatLng, String itemDetail){
        this.itemLatLng = itemLatLng;
        this.itemDetail = itemDetail;
    }

    public LatLng getItemLatLng() {
        return itemLatLng;
    }

    public String getItemDetail() {
        return itemDetail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(itemLatLng, 1);
        dest.writeString(itemDetail);
    }
    public static final Creator<SearchResultItem> CREATOR = new Creator<SearchResultItem>() {
        @Override
        public SearchResultItem createFromParcel(Parcel source) {
            return new SearchResultItem((LatLng)source.readParcelable(SearchResultItem.class.getClassLoader()), source.readString());
        }

        @Override
        public SearchResultItem[] newArray(int size) {
            return new SearchResultItem[size];
        }
    };
}
