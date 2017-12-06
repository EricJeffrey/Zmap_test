package com.example.fei.zmap_test;

import android.content.Intent;

import android.graphics.Color;
import android.location.Location;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import com.amap.api.maps.model.AMapCameraInfo;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.traffic.TrafficSearch;
import com.amap.api.services.traffic.TrafficStatusResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AMapLocationListener,LocationSource,PoiSearch.OnPoiSearchListener {
    private LinearLayout top;
    private static final String TAG = "MainActivity";
    private MapView mapView;
    private AMap aMap;
    private UiSettings mUiSettings;

    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private OnLocationChangedListener mListener = null;//定位监听器
    private AMapLocation MyAmapLocation=null;

    private DrawerLayout drawerLayout;
    private ImageButton map_mode_normal_button;
    private ImageButton map_mode_satellite_button;
    private ImageButton map_mode_bus_button;
    private ImageButton traffic_accident_button;
    private ImageButton my_collection_button;
    private int traffic_accident_button_status;
    private int my_collection_button_status;

    Marker marker =null;
    LatLng POILatlng =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();   //隐藏标题栏

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        });
        map_mode_normal_button =(ImageButton) findViewById(R.id.map_mode_normal_button);
        map_mode_bus_button = (ImageButton) findViewById(R.id.map_mode_bus_button);
        map_mode_satellite_button = (ImageButton) findViewById(R.id.map_mode_satellite_button);
        traffic_accident_button = (ImageButton) findViewById(R.id.traffic_accident_button);
        my_collection_button = (ImageButton) findViewById(R.id.my_collection_button);
        traffic_accident_button_status = 1;
        my_collection_button_status = 1;

        AddListener();      //为每个Button添加监听器
        Init();             //初始化地图view
        Information();      //个人信息
        InitLocIcon();      //定义蓝色按钮
        InitLoc();          //定位服务
        SetEdgeBar();       //显示&隐藏地图功能按钮
        SetUI();            //地图原始UI布局设置
        SetTraffic();       //设置交通态势信息
        SetPOIMarker();     //设置点击地图POI
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

    //定位服务
    private void InitLoc() {
        mLocationClient =new AMapLocationClient(getApplicationContext());               //初始化定位
        mLocationClient.setLocationListener(this);                                      //设置定位回调监听
        mLocationOption = new AMapLocationClientOption();                               //初始化定位参数
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy); //设置高精度模式
        mLocationOption.setNeedAddress(true);                                           //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setOnceLocation(true);                                          //设置是否只定位一次,默认为false;
        mLocationOption.setMockEnable(false);                                           //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setInterval(2000);                                              //设置定位间隔,单位毫秒,默认为2000ms
        mLocationClient.setLocationOption(mLocationOption);                             //给定位客户端对象设置定位参数
        mLocationClient.startLocation();                                                //启动定位
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        MyAmapLocation =amapLocation;
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                aMap.moveCamera(CameraUpdateFactory.zoomTo(17)); //设置缩放级别
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));//将地图移动到定位点
                mListener.onLocationChanged(amapLocation);//点击定位按钮 能够将地图的中心移动到定位点
                //aMap.addMarker(getMarkerOptions(amapLocation));//添加图钉
                StringBuffer buffer = new StringBuffer();//获取定位信息
                buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
                Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());

                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mLocationClient == null) {
            Toast.makeText(getApplicationContext(), "mLocationClient == null", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }


    //初始化地图view
    private void Init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }

    //定义显示定位蓝色按钮
    private void InitLocIcon() {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));  //设置缩放级别
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        myLocationStyle.strokeColor(Color.argb(80,00,00,205));//设置定位蓝点精度圆圈的边框颜色的方法。
        myLocationStyle.radiusFillColor(Color.argb(50,00,191,255));//设置定位蓝点精度圆圈的填充颜色的方法。
        BitmapDescriptorFactory mBipmapFactory =new BitmapDescriptorFactory();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location64));//设置定位蓝点的icon图标方法，需要用到BitmapDescriptor类对象作为参数。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
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

    //地图原始UI布局设置
    public void SetUI(){
        mUiSettings = aMap.getUiSettings();                     //实例化UiSettings类对象
        mUiSettings.setZoomControlsEnabled(false);              //不显示原始缩放按钮
        mUiSettings.setCompassEnabled(true);                    //显示指南针
        mUiSettings.setScaleControlsEnabled(true);              //显示比例尺，默认右下角显示
        mUiSettings.setMyLocationButtonEnabled(false);          //隐藏默认的定位按钮

        aMap.showIndoorMap(true);                            //设置显示室内地图，默认为不显示

    }
    //为每个Button添加监听器
    public void AddListener(){
        AddListenerById(R.id.zoom_in);
        AddListenerById(R.id.zoom_out);
        AddListenerById(R.id.location);
        AddListenerById(R.id.map_mode_bus_button);
        AddListenerById(R.id.map_mode_normal_button);
        AddListenerById(R.id.map_mode_satellite_button);
        AddListenerById(R.id.my_collection_button);
        AddListenerById(R.id.traffic_accident_button);
        AddListenerById(R.id.fog_haze_button);
        AddListenerById(R.id.pond_map);
        AddListenerById(R.id.map_setting);
        AddListenerById(R.id.map_mode_button);
        AddListenerById(R.id.search_box);
        AddListenerById(R.id.voice_search_button);
        AddListenerById(R.id.msg_center_button);
        AddListenerById(R.id.instant_route_status_button);
        AddListenerById(R.id.take_taxi_button);
        AddListenerById(R.id.shared_bike_button);
        AddListenerById(R.id.team_button);
        AddListenerById(R.id.home_workplace_set_button);
        AddListenerById(R.id.route_plan_button);
        AddListenerById(R.id.near_search_box);
        AddListenerById(R.id.report_button);
    }
    //依照ID添加监听器
    public void AddListenerById(final int resId){
        findViewById(resId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (resId){
                    case R.id.zoom_in:
                        aMap.animateCamera(CameraUpdateFactory.zoomIn());
                        break;
                    case R.id.zoom_out:
                        aMap.animateCamera(CameraUpdateFactory.zoomOut());
                        break;
                    case R.id.location:
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(17)); //设置缩放级别
                        aMap.moveCamera(CameraUpdateFactory.changeBearing(0));
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(MyAmapLocation.getLatitude(), MyAmapLocation.getLongitude())));
                        break;
                    case R.id.map_mode_normal_button:
                        map_mode_normal_button.setImageResource(R.drawable.map_mode_normal_selector);
                        map_mode_bus_button.setImageResource(R.drawable.map_mode_bus);
                        map_mode_satellite_button.setImageResource(R.drawable.map_mode_satellite);
                        aMap.setMapType(aMap.MAP_TYPE_NORMAL);
                        break;
                    case R.id.map_mode_satellite_button:
                        map_mode_normal_button.setImageResource(R.drawable.map_mode_normal);
                        map_mode_bus_button.setImageResource(R.drawable.map_mode_bus);
                        map_mode_satellite_button.setImageResource(R.drawable.map_mode_satellite_selector);
                        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                        break;
                    case R.id.map_mode_bus_button:
                        map_mode_bus_button.setImageResource(R.drawable.map_mode_bus_selector);
                        map_mode_normal_button.setImageResource(R.drawable.map_mode_normal);
                        map_mode_satellite_button.setImageResource(R.drawable.map_mode_satellite);
                        aMap.setMapType(AMap.MAP_TYPE_NAVI);
                        break;
                    case R.id.map_mode_button:
                        drawerLayout.openDrawer(Gravity.END);
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        break;
                    case (R.id.traffic_accident_button):
                        if(traffic_accident_button_status == 1)
                            traffic_accident_button.setImageResource(R.drawable.traffic_accident_checked);
                        else
                            traffic_accident_button.setImageResource(R.drawable.traffic_accident);
                        traffic_accident_button_status = Math.abs(traffic_accident_button_status - 1);
                        break;
                    case R.id.my_collection_button:
                        if(my_collection_button_status == 1)
                            my_collection_button.setImageResource(R.drawable.my_collection_checked);
                        else
                            my_collection_button.setImageResource(R.drawable.my_collection);
                        my_collection_button_status = Math.abs(my_collection_button_status - 1);
                        break;
                    case R.id.fog_haze_button:
                        Toast.makeText(MainActivity.this, "你点击了雾霾地图", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.pond_map:
                        Toast.makeText(MainActivity.this, "你点击了积水地图", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.map_setting:
                        Toast.makeText(MainActivity.this, "你点击了地图设置", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.search_box:
                        Toast.makeText(MainActivity.this, "你点击了搜索", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.voice_search_button:
                        Toast.makeText(MainActivity.this, "你点击了语音搜索", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.msg_center_button:
                        Toast.makeText(MainActivity.this, "你点击了消息中心", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.instant_route_status_button:
                        if(aMap.isTrafficEnabled()){
                            Toast.makeText(MainActivity.this, "实时路况已关闭", Toast.LENGTH_SHORT).show();
                            aMap.setTrafficEnabled(false);       //显示实时路况图层，aMap是地图控制器对象。
                        }
                        else{
                            Toast.makeText(MainActivity.this, "实时路况已开启", Toast.LENGTH_SHORT).show();
                            aMap.setTrafficEnabled(true);        //显示实时路况图层，aMap是地图控制器对象。
                        }
                        break;
                    case R.id.take_taxi_button:
                        Toast.makeText(MainActivity.this, "你点击了叫车", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.shared_bike_button:
                        Toast.makeText(MainActivity.this, "你点击了共享单车", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.team_button:
                        Toast.makeText(MainActivity.this, "你点击了组队出行", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.home_workplace_set_button:
                        Toast.makeText(MainActivity.this, "你点击了家，公司位置设置", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.route_plan_button:
                        Toast.makeText(MainActivity.this, "你点击了路线规划", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.near_search_box:
                        Toast.makeText(MainActivity.this, "你点击了搜索附近", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.report_button:
                        Toast.makeText(MainActivity.this, "你点击了上报", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    //显示&隐藏地图功能按钮
    public void SetEdgeBar(){
        Log.d(TAG, "onCreate: Now Create");
        findViewById(R.id.search_box).clearFocus();
        top = (LinearLayout) findViewById(R.id.top_view);
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(top.getVisibility() == View.VISIBLE) top.setVisibility(View.INVISIBLE);
                else top.setVisibility(View.VISIBLE);
            }
        });
    }

    //设置交通态势信息
    public void SetTraffic(){
        TrafficSearch trafficSearch = new TrafficSearch(this);
        trafficSearch.setTrafficSearchListener(new TrafficSearch.OnTrafficSearchListener() {
            @Override
            public void onRoadTrafficSearched(TrafficStatusResult trafficStatusResult, int i) {

            }
        });
    }

    //设置点击地图POI
    public void SetPOIMarker(){
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                POILatlng =latLng;
                SearchPOI(latLng);
            }
        });
    }

    public void SearchPOI(LatLng latLng){

        PoiSearch.Query query = new PoiSearch.Query("郑州","","");
        query.setPageSize(1);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);//设置查询页码
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch =new PoiSearch(this,query);
        List<LatLonPoint> points =new ArrayList<LatLonPoint>();
        points.add(new LatLonPoint(latLng.latitude+0.0001,latLng.longitude+0.0001));
        points.add(new LatLonPoint(latLng.latitude-0.0001,latLng.longitude-0.0001));
        points.add(new LatLonPoint(latLng.latitude-0.0001,latLng.longitude+0.0001));
        points.add(new LatLonPoint(latLng.latitude+0.0001,latLng.longitude-0.0001));
        poiSearch.setBound(new PoiSearch.SearchBound(points));//设置多边形区域
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();

    }


    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        //Toast.makeText(MainActivity.this,"+"+poiResult.getPois().toString()+poiResult.getPageCount(), Toast.LENGTH_SHORT).show();
        if(poiResult.getPageCount()!=0){
            if(marker==null){
                marker= aMap.addMarker(new MarkerOptions().position(POILatlng).title(poiResult.getPois().toString().substring(1,poiResult.getPois().toString().length()-1)));
            }

            if( marker.getPosition()!=POILatlng) {
                marker.remove();
                marker= aMap.addMarker(new MarkerOptions().position(POILatlng).title(poiResult.getPois().toString().substring(1,poiResult.getPois().toString().length()-1)));
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
        Toast.makeText(MainActivity.this, " ", Toast.LENGTH_SHORT).show();

    }
}
