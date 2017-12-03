package com.example.fei.zmap_test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;

public class MainActivity extends AppCompatActivity{
    private LinearLayout top;
    private FrameLayout bottom;
    private String TAG = "MainActivity";
    private MapView mapView;
    private AMap aMap;
    private boolean IsFirstLoc = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();   //隐藏标题栏

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();

        AddListener();      //为每个Button添加监听器
        Init();             //初始化地图view
        Information();      //个人信息
        InitLoc();          //定位服务
        SetEdgeBar();       //显示&隐藏地图功能按钮

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
    }

    //初始化地图view
    private void Init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }

    //定位服务
    private void InitLoc() {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));  //设置缩放级别
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        myLocationStyle.interval(20000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.strokeColor(Color.argb(00,00,00,00));//设置定位蓝点精度圆圈的边框颜色的方法。
        myLocationStyle.radiusFillColor(Color.argb(00,00,00,00));//设置定位蓝点精度圆圈的填充颜色的方法。
        BitmapDescriptorFactory mBipmapFactory =new BitmapDescriptorFactory();
        myLocationStyle.myLocationIcon(mBipmapFactory.fromResource(R.drawable.location64));//设置定位蓝点的icon图标方法，需要用到BitmapDescriptor类对象作为参数。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }




    //个人信息
    public void Information(){
        ImageButton me = (ImageButton) findViewById(R.id.me);
        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Profile.class);
                startActivity(intent);
            }
        });
    }

    //为每个Button添加监听器
    public void AddListener(){
        ImageButton zoom_in = (ImageButton) findViewById(R.id.zoom_in);//缩放
        zoom_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });
        ImageButton zoom_out = (ImageButton) findViewById(R.id.zoom_out);
        zoom_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });

    }

    //显示&隐藏地图功能按钮
    public void SetEdgeBar(){
        Log.d(TAG, "onCreate: Now Create");
        findViewById(R.id.search_box).clearFocus();
        top = (LinearLayout) findViewById(R.id.top_view);
        bottom = (FrameLayout) findViewById(R.id.frame_layout);
        bottom.setOnClickListener(new FrameLayout.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "click got", Toast.LENGTH_SHORT).show();
                if(!view.equals(top)) {
                    if(top.getVisibility() == View.INVISIBLE) top.setVisibility(View.VISIBLE);
                    else top.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
