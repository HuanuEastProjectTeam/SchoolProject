package com.example.andriodcar.Map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.andriodcar.MainActivity;
import com.example.andriodcar.R;

import java.util.ArrayList;
import java.util.List;

import com.example.andriodcar.Bean.ResidentialQuarter_Bean;
import com.example.andriodcar.ShezhiActivity;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {


    private String LOG="tog";
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
    private float lastX;//旋转的x方向值
    //地图搜索
   private SuggestionSearch mSuggestionSearch;
   private Button btnmap;
   private EditText et;
   private String location=null;//获取输入数据
   private List<SuggestionResult.SuggestionInfo> searchResult;
   private LinearLayout layout;//搜索视图

   //地图覆盖物
    private List<ResidentialQuarter_Bean> longiLatiTude;//设置覆盖物的经纬度集合
    private List<LatLng> pointMap;
    private List<OverlayOptions> overlayOptions,overlayOptionsText;

    //覆盖物事件
    private TextView textView_Name,textView_Num;
    private boolean textViewBiaoZhi=false;//消息框显示判断标志
    private RelativeLayout messageLayout;
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
         //调用改变定位图标
        myLocationConfiguration();
        orientationLoc();//调用方向传感数据实现方法
         //初始化集合对象
        startArrayList();
        //调用创建覆盖物图标方法
        overlayMapADdd(longiLatiTude);//longiLatiTude为小区地图覆盖物对象的集合对象




        //覆盖物点击事件
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                /**
                 * 点击了车库显示图标触发事件，然后显示该地区的车位使用情况
                 */

                Bundle bundle=marker.getExtraInfo();//获取携带的信息对象
                int id=bundle.getInt("id");//获取覆盖物对应id
                Log.i(LOG,""+id);
                for(int i=0;i<longiLatiTude.size();i++){
                    if(id==longiLatiTude.get(i).getId()){
                        messageLayout=findViewById(R.id.MessageQu);
                        textView_Name=findViewById(R.id.MessageKuang);//获取信息框控件
                        textView_Num=findViewById(R.id.carNum);//获取车位余数控件
                        textView_Num.setText("剩余空闲车位："+longiLatiTude.get(i).getTotalNumPaks()+"个");//显示余数
                        textView_Name.setText(longiLatiTude.get(i).getCommunityName()+longiLatiTude.get(i).getCommunityAddress());//显示小区名字地址
                        messageLayout.setVisibility(View.VISIBLE);//显示信息框
                        textViewBiaoZhi=true;
                        textView_Name.setOnClickListener(new View.OnClickListener() {//设置信息框的点击事件
                            @Override
                            public void onClick(View view) {
                                /**
                                 * 出现导航，指引用户前往地点
                                 */


                            }
                        });
                    }
                }


              //  Toast.makeText(MapActivity.this, "点击了", Toast.LENGTH_LONG).show();

                return false;
            }
        });

        //地图触摸事件
        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if(textViewBiaoZhi==true){
                    messageLayout.setVisibility(View.GONE);//设置消息框消失
                }
            }
        });

        //创建Sug检索实例
         mSuggestionSearch = SuggestionSearch.newInstance();
       //  btnmap=findViewById(R.id.btn_map);
         et=findViewById(R.id.search);
         et.setOnClickListener(new View.OnClickListener() {//给输入框设置点击事件跳转到新的activity处理搜索结果
             @Override
             public void onClick(View view) {
                 Log.i(LOG,"搜索框点击效果，准备跳转");
               //  searchResultDeal();//调用搜索结果处理方法,返回结果,不能直接这样调用
                 layout=findViewById(R.id.layout);//获取视图控件
                 layout.setVisibility(View.VISIBLE);//设置显示
                 et.setFocusable(true);
                 et.setFocusableInTouchMode(true);

             }
         });

        monitorImp(this);//图层图标添加监听实现方法
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {//设置获取焦点触发方法
            @Override
            public void onFocusChange(View view, boolean b) {
                final EditText editText=(EditText)view;//获取焦点变化视图控件
                if(b){
                    Log.i(LOG,"焦点事件触发");

                    editText.addTextChangedListener(new TextWatcher() {//获取焦点后添加文本变化监听事件
                        @Override
                        //输入前的监听方法
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        //输入文本变化的监听方法,
                        // charSequence  输入框中改变前的字符串信息
                        // i 输入框中改变前的字符串的起始位置
                        // i1 输入框中改变前后的字符串改变数量一般为0
                        // i2 输入框中改变后的字符串与起始位置的偏移量
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            new Thread(new Runnable() {//开启一个线程，进行搜索查询
                                @Override
                                public void run() {
                                    searchResultDeal(editText);//调用搜索结果处理方法
                                }
                            }).start();

                            Log.i("gg","文本改变监听方法开始调用");
                        }

                        @Override
                        //输入后的监听
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }else{
                    editText.setHint("搜索");
                }
            }
        });

    }

    /**
     *  搜索结果处理方法
     * @return 返回搜索结果
     */
    public List<SuggestionResult.SuggestionInfo> searchResultDeal(EditText et) {

        location=et.getText().toString();
        Log.i(LOG,"准备检索");
        if(!location.equals("")){
            Log.i(LOG,"开始检索");
            mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
                @Override
                public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                    //处理检索结果
                    searchResult=suggestionResult.getAllSuggestions();//获取搜索结果集
                    for(int i=0;i<searchResult.size();i++){
                        Log.i("gg","结果："+searchResult.get(i));

                    }

                }
            });
            Log.i(LOG,"输入的搜索结果："+location);
            //创建Sug检索监听器

            /**
             * 在您的项目中，keyword为随您的输入变化的值
             */
            mSuggestionSearch.requestSuggestion(new SuggestionSearchOption()
                    .city("长沙")
                    .keyword(location));
        }

        return searchResult;
    }



    /**
     * 初始化集合对象
     */
    private void startArrayList() {
        Log.i(LOG,"进入集合初始化");
        longiLatiTude=new ArrayList<ResidentialQuarter_Bean>();//初始化小区对象集合
        pointMap=new ArrayList<LatLng>();
        overlayOptions=new ArrayList<OverlayOptions>();
        overlayOptionsText=new ArrayList<OverlayOptions>();
        searchResult=new ArrayList<SuggestionResult.SuggestionInfo>();//搜索结果集合
    }


    /**
     * 覆盖物小区图标实现方法
     * @param longiLatiTude 各小区转换对象集合
     */
    private void overlayMapADdd(List<ResidentialQuarter_Bean> longiLatiTude) {

        //创建Maker坐标点
        LatLng point;
        OverlayOptions options,options1Text;//创建覆盖物对象

/******************************测试数据*************************************************************/
        ResidentialQuarter_Bean s1=new ResidentialQuarter_Bean();
        s1.setLongitude(28.18934);
        s1.setLatitude(113.088875);
        s1.setCommunityName("天霸地霸小区");
        s1.setCommunityAddress("天王路一号");
        s1.setId(1);
        longiLatiTude.add(s1);
        ResidentialQuarter_Bean s2=new ResidentialQuarter_Bean();
        s2.setLongitude(28.189658);
        s2.setLatitude(113.090564);
        s2.setCommunityName("舞蹈小区");
        s2.setCommunityAddress("蛇皮路二号");
        s2.setId(2);
        longiLatiTude.add(s2);
        ResidentialQuarter_Bean s3=new ResidentialQuarter_Bean();
        s3.setLongitude(28.188895);
        s3.setLatitude(113.093486);
        s3.setCommunityName("天天小区");
        s3.setCommunityAddress("鄂武商路三号");
        s3.setId(3);
        longiLatiTude.add(s3);

   /***************************************************************************************/


   /*************************************添加接收的小区对象到集合****************************************************/
        /**
         *
         *
         *
         *
         *
         *
         */

        Log.i(LOG,"进入覆盖物创建方法");
        for(int i=0;i<longiLatiTude.size();i++){
            point=new LatLng(longiLatiTude.get(i).getLongitude(),longiLatiTude.get(i).getLatitude());//参数1，2为经纬度，创建坐标点
            pointMap.add(point);//添加到坐标点集合
            Log.i(LOG,"添加坐标集合"+longiLatiTude.get(i).getLongitude());
        }
        BitmapDescriptor bitmapDescriptor=BitmapDescriptorFactory
                .fromResource(R.drawable.loc1);//创建Marker图标
        Bundle mBundle;//创建携带对象
        for(int i=0;i<pointMap.size();i++){
          //构建MarkOption,可以作为地图上添加Marker
            mBundle=new Bundle();
            mBundle.putInt("id",longiLatiTude.get(i).getId());//给bundle添加信息
            options=new MarkerOptions()
                    .position(pointMap.get(i))
                    .extraInfo(mBundle)//携带id信息便于点击事件的区分
                    .icon(bitmapDescriptor);
            //构建文本覆盖物
            options1Text=new TextOptions()
                    .position(pointMap.get(i))
                    .text("停车场")
                    .fontSize(24);
            //加入overlayOptions集合
            overlayOptions.add(options);
            overlayOptionsText.add(options1Text);
            Log.i(LOG,"添加覆盖物集合");
        }
        mBaiduMap.addOverlays(overlayOptions);//在地图上添加marker,显示出来
        mBaiduMap.addOverlays(overlayOptionsText);//添加
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
        myOrientationListener.stop();//关闭传感器
        mSuggestionSearch.destroy();
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
