package com.example.andriodcar.Map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.andriodcar.R;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {


    /**
     * 地图上边的功能图标控件
     */
    private ImageButton imageButton_trafficSit;
    private Boolean trafficSit_biaoZhi=true;

    //动态申请权限
    private static final int BAIDU_READ_PHONE_STATE = 100;

    /*
     * 地图控件
     */
    private MapView mMapView = null;


    private BaiduMap mBaiduMap;
    private BaiduMapOptions baiduMapOptions;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private MyLocationConfiguration.LocationMode locationMode;//定位模式对象
    BitmapDescriptor bitmapDescriptor;//自定义图标对象
    boolean isFirstLoc = true;// 是否首次定位
    private int yan1,yan2;//精度圈颜色值
    private MyOrientationListener myOrientationListener;//方向传感数据实现类
    //地图搜索
   private SuggestionSearch mSuggestionSearch;
   private Button btnmap;
   private EditText et;
   private float lastX;//旋转的x方向值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SDKInitializer.initialize(getApplicationContext());//初始化百度地图sdk
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;


        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);// 设置发起定位请求的间隔时间为1000ms
        option.setIsNeedAddress(true);
        //设置locationClientOption
        mLocClient.setLocOption(option);
        //注册LocationListener监听器
        mLocClient.registerLocationListener(myListener);
        //开启地图定位图层
        mLocClient.start();
         //改变定位图标
        myLocationConfiguration();
        orientationLoc();//调用方向传感数据实现方法

        //定义Maker坐标点,两个参数为湖南农业大学停车场的坐标
        LatLng point = new LatLng(28.1880748409, 113.0906765898);
       //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.loc1);


       //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions opt = new MarkerOptions()
                .position(point)
                .icon(bitmap);

        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(opt);


        //文字覆盖物位置坐标
        LatLng llText = new LatLng(28.1880748409, 113.0906765898);

        //构建TextOptions对象
        OverlayOptions mTextOptions = new TextOptions()
                .text("停车场") //文字内容
                .fontSize(24) //字号
                .position(llText);

        //在地图上显示文字覆盖物
        Overlay mText = mBaiduMap.addOverlay(mTextOptions);

        //标记点击事件
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MapActivity.this, "点击了", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        //创建Sug检索实例
         mSuggestionSearch = SuggestionSearch.newInstance();
         btnmap=findViewById(R.id.btn_map);
         et=findViewById(R.id.et_map);
         btnmap.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                String location=et.getText().toString();
                 //创建Sug检索监听器
                 mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
                     @Override
                     public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                         //处理检索结果

                     }
                 });
                 /**
                  * 在您的项目中，keyword为随您的输入变化的值
                  */
                 mSuggestionSearch.requestSuggestion(new SuggestionSearchOption()
                         .city("长沙")
                         .keyword(location));

             }
         });

        monitorImp(this);//图层图标添加监听实现方法


    }

    /**
     * 注册监听实现方法
     */
    private void monitorImp(View.OnClickListener onclickListener) {
        imageButton_trafficSit=findViewById(R.id.luKuang);//获取实况交通图标控件
        imageButton_trafficSit.setOnClickListener(onclickListener);

    }
    //点击监听方法的实现
    public void onClick(View view){
        switch(view.getId()){
            case R.id.luKuang:{//点击了实况交通图标
                //map图层切换到实况交通图

                Log.i("tuBiaoMessage","点击了切换实时路况交通图");
                if(trafficSit_biaoZhi==true){
                    mBaiduMap.setTrafficEnabled(true);//开启实时交通路况图
                    trafficSit_biaoZhi=false;
                }else{
                    mBaiduMap.setTrafficEnabled(false);//关闭实时交通路况图
                    trafficSit_biaoZhi=true;
                }

            }
            /**
             * 后面接图层右边图标触发事件类型
             */

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {


        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;


        super.onDestroy();
    }


    /**
     * 定位SDK监听函数，接受定位数据传给mapview
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(lastX).latitude(location.getLatitude())//lastX为旋转的方向值
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                // MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                // 设置缩放比例,更新地图状态
                float f = mBaiduMap.getMaxZoomLevel();// 19.0
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,
                        f - 2);
                mBaiduMap.animateMapStatus(u);
                //地图位置显示
                Toast.makeText(MapActivity.this, location.getAddrStr(),
                        Toast.LENGTH_SHORT).show();
            }

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    /**
     * 自定义定位图标，精度范围实现方法
     */
   public void myLocationConfiguration() {

       locationMode = MyLocationConfiguration.LocationMode.NORMAL;//定位为普通态
     //  bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.zhizheng);//自定义定位图标
       yan1 = 0xAAFFFF55;//精度圈填充颜色
       yan2 = 0xAA11FF11;//精度圈边框颜色
       mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(locationMode, true, bitmapDescriptor, yan1, yan2));//设置实现
   }

    /**
     * 随定位图标随方向旋转方法实现
     */
    public void orientationLoc(){
        //调用方向传感器数据实现类构建对象
        Log.i("dd","开始调用方向传感数据实现方法");
        myOrientationListener=new MyOrientationListener(this);
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                lastX=x;//x旋转方向值赋值给全局变量
                Log.i("dd",""+lastX);
            }
        });
    }

}
