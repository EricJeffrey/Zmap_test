package com.example.fei.zmap_test;

import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;

public class MainActivity extends AppCompatActivity {
    private LinearLayout top;
    private FrameLayout bottom;
    private String TAG = "MainActivity";
    private MapView mapView;
    private AMap aMap;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);
        getSupportActionBar().hide();   //隐藏标题栏

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

        top = (LinearLayout) findViewById(R.id.top_view);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();

        addListener();
    }
    //为每个Button添加监听器
    public void addListener(){
        //缩放
        ImageButton zoom_in = (ImageButton) findViewById(R.id.zoom_in);
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
        //个人信息
        ImageButton me = (ImageButton) findViewById(R.id.me);
        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO start profile activity
            }
        });
    }
}
