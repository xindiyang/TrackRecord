package com.example.codoon.myapplication.SportLines;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.example.codoon.myapplication.Algorithm.LatLngPoint;
import com.example.codoon.myapplication.DbFlow.MyDataBase;
import com.example.codoon.myapplication.OfflineMap.OfflineMapActivity;
import com.example.codoon.myapplication.R;
import com.example.codoon.myapplication.Record.RecordActivity;
import com.example.codoon.myapplication.Util.SportMyView;
import com.example.codoon.myapplication.Util.SuspendedView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 创建者：Sunday
 * 项目名：coddonGMap
 * 包名：com.example.codoon.myapplication.SportLines
 * 文件名：SportActivity
 * 创建时间：2018/10/18 下午4:59
 * 描述：对运动轨迹的记录以及向数据库传递数据
 */
public class SportActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MapView mapView;
    private AMap aMap;
    private TextView offlineMap, record;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocation privLocation;
    private SportMyView sportMyView;
    private SuspendedView suspendedView;
    private List<LatLngPoint> latLngPoints = new ArrayList<>();
    private long beginTime, lastTime, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_avtivity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);// 调用地图所必须重写
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        aMap.setMapTextZIndex(2);
        setUpMap();
        init();
    }

    private void setUpMap() {
        /**
         * 设置一些amap的属性
         */
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setCompassEnabled(true);// 设置指南针是否显示
        uiSettings.setRotateGesturesEnabled(true);// 设置地图旋转是否可用
        uiSettings.setTiltGesturesEnabled(true);// 设置地图倾斜是否可用
        uiSettings.setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示

        /**
         * 自定义系统定位小蓝点
         */
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(mLocationSource);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }

    /**
     * 进入历史详情和地图管理页面
     */
    private void init() {

        offlineMap = findViewById(R.id.offlineMap);
        offlineMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SportActivity.this, OfflineMapActivity.class);
                startActivity(intent);
            }
        });

        //历史记录
        record = findViewById(R.id.record);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SportActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });
    }

    public LocationSource mLocationSource = new LocationSource() {
        //激活定位
        @Override
        public void activate(OnLocationChangedListener onLocationChangedListener) {

            mListener = onLocationChangedListener;
            sportMyView = findViewById(R.id.sportMyView);
            sportMyView.setVisibility(View.VISIBLE);
            suspendedView = findViewById(R.id.suspendedView);
            sportMyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //初始化定位
                    initAmapLocation();
                    sportMyView.setVisibility(View.GONE);
                    suspendedView.setVisibility(View.VISIBLE);
                    beginTime = System.currentTimeMillis();
                    Log.e("BEGINTIME", String.valueOf(beginTime));
                }
            });

            suspendedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLocationClient.stopLocation();//停止定位
                    suspendedView.setVisibility(View.GONE);
                    sportMyView.setVisibility(View.VISIBLE);
                    lastTime = System.currentTimeMillis();
                    Log.e("LASTTIME", String.valueOf(lastTime));
                    insertModel();
                }
            });


        }


        @Override
        public void deactivate() {
            mListener = null;
            if (mLocationClient != null) {
                mLocationClient.stopLocation();
                mLocationClient.onDestroy();
            }
            mLocationClient = null;
        }
    };

    /**
     * 初始化定位
     */
    private void initAmapLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(this);
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        // 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);
        ////设置定位模式为设备模式Device_Sensors，Battery_Saving为低功耗模式，Hight_Accuracy高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(2000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.startLocation();
        }
    }

    private int index = 0;
    /**
     * 定位回调每1秒调用一次
     */
    public AMapLocationListener mAMapLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                mListener.onLocationChanged(amapLocation); //显示系统小蓝点,不写这一句无法显示到当前位置
                amapLocation.getLocationType(); //获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getAccuracy(); //获取精度信息
                amapLocation.getBearing(); //获取方向角信息
                amapLocation.getSpeed(); //获取速度信息  单位：米/秒
                amapLocation.getLocationType(); //查看是什么类型的点
                Log.e(TAG, "获取经纬度集合" + amapLocation);//打Log记录点是否正确
                Log.e(TAG, "获取点的类型" + amapLocation.getLocationType());
                //一边定位一边连线
                drawLines(amapLocation);
                privLocation = amapLocation;
                Log.e("DDDDDDDDD", String.valueOf(Distance(amapLocation)));
                Toast.makeText(SportActivity.this, "距离" + Distance(amapLocation), Toast.LENGTH_SHORT).show();
                //将坐标点存于数组里
                if (amapLocation.getLatitude() != 0 && amapLocation.getLongitude() != 0) {
                    latLngPoints.add(new LatLngPoint(index++, new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                }
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
            }
        }
    };

    /**
     * 绘制运动路线
     *
     * @param curLocation
     */
    public void drawLines(AMapLocation curLocation) {

        if (null == privLocation) {
            return;
        }
        if (curLocation.getLatitude() != 0.0 && curLocation.getLongitude() != 0.0
                && privLocation.getLongitude() != 0.0 && privLocation.getLatitude() != 0.0) {
            PolylineOptions options = new PolylineOptions();
            //上一个点的经纬度
            options.add(new LatLng(privLocation.getLatitude(), privLocation.getLongitude()));
            //当前的经纬度
            options.add(new LatLng(curLocation.getLatitude(), curLocation.getLongitude()));
            options.width(10).geodesic(true).color(Color.GREEN);
            aMap.addPolyline(options);
        }

    }

    private double Distance(AMapLocation curLocation) {
        double distance;
        distance = AMapUtils.calculateLineDistance(new LatLng(privLocation.getLatitude(),
                privLocation.getLongitude()), new LatLng(curLocation.getLatitude(),
                curLocation.getLongitude()));
        distance += distance;
        return distance;
    }

    /**
     * 向数据库中传递数据
     */
    private void insertModel() {
        MyDataBase myDatabase = new MyDataBase();
        time = (lastTime - beginTime)/1000/60;
        myDatabase.duration = time;
        myDatabase.distance = 20.0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        myDatabase.time = sdf.format(date);
        myDatabase.save();
        Log.e("DURATION1", String.valueOf(time));
        Log.e("DURATION", String.valueOf(myDatabase.duration));
        Log.e("TIME", String.valueOf(myDatabase.time));
        Log.e("Sunday", String.valueOf(myDatabase.id));
        Log.e("DISTANCE", String.valueOf(myDatabase.distance));
    }

    /**
     * 必须重写的方法
     */
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    /**
     * 必须重写的方法
     */
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    /**
     * 必须重写的方法
     */
    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }
}
