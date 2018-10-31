package com.example.codoon.myapplication.Record;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.example.codoon.myapplication.DbFlow.Location;
import com.example.codoon.myapplication.DbFlow.MyDataBase;
import com.example.codoon.myapplication.R;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者：Sunday
 * 项目名：coddonGMap
 * 包名：com.example.codoon.myapplication.Record
 * 文件名：RecordShowActivity
 * 创建时间：2018/10/22 下午4:15
 * 描述：历史详情页面
 */
public class RecordShowActivity extends AppCompatActivity {
    private MapView mapView;
    private AMap aMap;
    private Location location;
    private List<MyDataBase> userList = new ArrayList<>();
    private TextView sportDistance, averageSpeed, time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historicaldetails_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
        mapView = findViewById(R.id.recordMapView);
        initView();
        initData();
        mapView.onCreate(savedInstanceState);// 调用地图所必须重写

    }

    private void initView() {
        sportDistance = findViewById(R.id.sportDistance);
        averageSpeed = findViewById(R.id.aveSpeed);
        time = findViewById(R.id.sportTime);
        getDataFromDB();
        int i = 0;
        sportDistance.setText(String.valueOf(userList.get(i).duration));
        time.setText(String.valueOf(userList.get(i).duration));
        i++;
    }

    private void initData() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setMapTextZIndex(5);
        location = new Location();
        LatLng latLng = new LatLng(30.538326, 104.075369);
        //设置显示比例
        aMap.moveCamera(CameraUpdateFactory.zoomTo(20));
        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("Sun").snippet("DefaultMarker"));
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));//这个是关键  如果不设置的话中心点是北京，汇出现目标点在地图上显示不了
    }

    private void getDataFromDB() {
        List<MyDataBase> datas = SQLite.select().from(MyDataBase.class).queryList();
        userList.addAll(datas);
    }
}
