package com.example.fei.zmap_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;

public class MainActivity extends AppCompatActivity{
    private LinearLayout top;
    private FrameLayout bottom;
    private String TAG = "MainActivity";
    private MapView mapView;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener = null;
    public AMapLocationClient mapLocationClient = null;
    public AMapLocationClientOption mapLocationClientOption = null;
    private boolean IsFirstLoc = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();   //隐藏标题栏

        Information();      //个人信息

        /*
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();

        AddListener();      //为每个Button添加监听器

        Init();             //初始化地图view
        initLoc();          //定位服务
        SetEdgeBar();       //显示&隐藏地图功能按钮
*/
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
    /*
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

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    //停止定位
    @Override
    public void deactivate() {
        mListener = null;
    }


    //初始化地图view
    private void Init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }

    //定位服务
    private void initLoc() {


//        UiSettings settings = aMap.getUiSettings();//设置显示定位按钮 并且可以点击
//        aMap.setLocationSource(this);//设置定位监听
//        settings.setMyLocationButtonEnabled(true);// 是否显示定位按钮
//        aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层

        if (mapLocationClient == null) {
            mapLocationClient = new AMapLocationClient(getApplicationContext());//初始化AMapLocationClient，并绑定监听
        }
        mapLocationClient.setLocationListener(this);//绑定监听
        {//设置定位参数
            mapLocationClientOption = new AMapLocationClientOption();//初始化AMapLocationClientOption对象
            mapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式
            mapLocationClientOption.setOnceLocationLatest(false);//获取最近3s内精度最高的一次定位结果
            mapLocationClientOption.setInterval(2000);//设置定位间隔,单位毫秒,默认为2000ms
        }
        mapLocationClient.setLocationOption(mapLocationClientOption);//给定位客户端对象设置定位参数
        mapLocationClient.startLocation();//启动定位
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation!=null){
            if (aMapLocation.getErrorCode()==0){
                if(IsFirstLoc) {
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));  //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
//                    界面下部显示定位信息
//                    StringBuilder stringBuilder = new StringBuilder();
//                    int type = aMapLocation.getLocationType();//定位成功回调信息，设置相关消息
//                    String address = aMapLocation.getAddress();
//                    stringBuilder.append(type+address);
//                    Toast.makeText(this,stringBuilder.toString(),Toast.LENGTH_SHORT).show();

                    IsFirstLoc=false;
                }
            }else {
                Log.i("erro info：",aMapLocation.getErrorCode()+"---"+aMapLocation.getErrorInfo());//显示错误信息ErrCode是错误码，errInfo是错误信息
            }
        }
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
    */
}
