package com.example.andriodcar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.example.andriodcar.Bean.UserOrdinary;
import com.example.andriodcar.Map.MapActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import image.ImageCarousel;
import image.ImageInfo;
import com.example.andriodcar.Bean.Userbean;
import utils.javautils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , View.OnClickListener{

    //日志提示信息
    private final static String log = "我是日志信息：";

    //登录标记
    public static boolean logflag=false;

    //扫码结果集
    String result=null;

    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;

    //动态申请权限
    private static final int BAIDU_READ_PHONE_STATE = 100;

    // 图片轮播控件
    private ViewPager mViewPager;
    private TextView mTvPagerTitle;
    private LinearLayout mLineLayoutDot;
    private ImageCarousel imageCarousel;
    private List<View> dots;//小点

    // 图片数据，包括图片标题、图片链接、数据、点击要打开的网站（点击打开的网页或一些提示指令）
    private List<ImageInfo> imageInfoList;



    //二维码扫描
    private Button btn_sacn;
   //缴费
    private Button btn_money;
    //地图
    private  Button btn_carmap;
    //帮助
    private Button  btn_help;

    //显示信息
    private RelativeLayout relativeLayoutOne;
    private RelativeLayout relativeLayoutTwo;
    private RelativeLayout relativeLayoutThree;

    //线程池
    public final static ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 3 + 2);

    //网络连接操作对象
    static Connect ct;

    //数据持久化操作对象
    public SharedPreferences sp_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //标题为空
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //初始化权限
        judgePermission();
      /*
      首页信封图标点击事件

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "客服在线帮助", Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener(){
                            public void onClick(View ang){
                                //跳转到客服帮助界面，连接qq

                            }
                        }).show();
            }
        });
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);//显示菜单图标
        navigationView.setItemTextColor(null);
        navigationView.setNavigationItemSelectedListener(this);

        /*图片方法初始化*/
        initView();
        initEvent();
        imageStart();

        DialogUIUtils.init(this);

        ZXingLibrary.initDisplayOpinion(this);
        //二维码扫描
        btn_sacn=findViewById(R.id.scan);
        btn_sacn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(logflag){//未登录logflag为假
                    Toast.makeText(MainActivity.this,"请登录",Toast.LENGTH_SHORT).show();

                }else{
                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }

            }
        });


        //空闲车位的发布
        btn_money=findViewById(R.id.money);
        btn_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到发布停车位
            //    Toast.makeText(MainActivity.this,"功能还在开发中····",Toast.LENGTH_SHORT).show();
                Log.i(log,"进入空闲车位表单页面");
                Intent intent = new Intent(MainActivity.this,FromCar.class);
                startActivity(intent);
            }
        });


        //地图实现
        btn_carmap=findViewById(R.id.carmap);
        btn_carmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });


        //帮助实现
        btn_help = findViewById(R.id.help);
        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,HelpActivity.class);
                startActivity(intent);
            }
        });

        //创建储存数据对象
        sp_user = getSharedPreferences("user", Context.MODE_PRIVATE);

        //与服务器建立连接，并且自动登陆
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ct = new Connect();
                ct.SetUserSharePreference(sp_user);
                if(sp_user.getBoolean("logflag",false)){
                    ct.login(String.valueOf(sp_user.getInt("PhoneNum",123)),sp_user.getString("Password","123"));
                    Looper.prepare();
                    showToast("自动登录成功");
                    logflag=true;
                    Looper.loop();
                }else{
                    Looper.prepare();
                    showToast("用户尚未登陆");
                    Looper.loop();
                }
            }
        });

    }
    /**
     * 首页和我的点击方法
     * 点击后分别显示首页界面和我的信息界面
     * 同时更换按钮活动图标
     *
     */
    public void indexAndmyMessage(View view){
        //获得首页布局id
        LinearLayout linearLayout = findViewById(R.id.indexLayout);
        //获取信息页面布局id
        LinearLayout linearLayout1 = findViewById(R.id.myMessageLayout);
        //导航栏的文字控件
        TextView textView =findViewById(R.id.toolbar_title);
        switch (view.getId()){
            case R.id.index_1 : {
                //默认进入软件首先展示首页
                Log.i(log,"点击了首页");
                //设置图标,把首页设置为活跃状态
                drawIndexAndMyMessageIcon(view.getId(),R.drawable.index_activity);
                //把我的设置为停止状态，更改图标
                drawIndexAndMyMessageIcon(R.id.my,R.drawable.mymessage);
                //设置首页信息可见，占据空间
                linearLayout.setVisibility(View.VISIBLE);
                //隐藏我的信息页面
                linearLayout1.setVisibility(View.GONE);
                textView.setText("首页");
                break;
            }
            case R.id.my :{
                //点击了我的
                Log.i(log,"点击了我的");
                //设置图标,把首页设置为停止状态
                drawIndexAndMyMessageIcon(R.id.index_1,R.drawable.index);
                //把我的设置为活跃状态，更改图标
                drawIndexAndMyMessageIcon(view.getId(),R.drawable.mymessage_activity);
                //设置首页不显示,留出空间
                linearLayout.setVisibility(View.GONE);
                //显示我的信息页面
                linearLayout1.setVisibility(View.VISIBLE);
                textView.setText("我的");
                break;
            }
        }

    }

    /**
     * 改变按钮图标
     * @param age1 按钮对象对应的int值
     * @param age 图标对应的int值
     */
    public void drawIndexAndMyMessageIcon(int age1,int age){
        Button buttonIndexMy = findViewById(age1);
        Drawable drawable = getResources().getDrawable(age);
        drawable.setBounds(0,0,100,100);
        buttonIndexMy.setCompoundDrawables(null,drawable,null,null);
    }

    /**
     * 首页信息展示卡片的点击事件
     * 点击后显示详细信息
     * @param view 点击的view
     */
    public void ShowMessage(View view){
        //获取到三个信息卡片的id
        relativeLayoutOne = findViewById(R.id.one);
        relativeLayoutTwo = findViewById(R.id.two);
        relativeLayoutThree = findViewById(R.id.three);
        switch(view.getId()){
            case R.id.one:{
                //点击了第一个卡片信息
                Log.i(log,"点击了第一个卡片信息");
                /**
                 * 点击信息卡片之后，跳转到详情页面
                 * 再详情页面重新加载
                 * 传递当前点击信息卡片展示的信息id
                 * 在详情信息页面进行重新查询展示
                 * 在bean中获取新闻信息id,
                 * 此处需要一个新闻信息bean
                 *
                 */
                //数据携带对象
                Bundle bundle = new Bundle();
                //假定第一个信息卡片显示的信息的id为1
                bundle.putInt("messageId",1);
                Intent intent =  new Intent(MainActivity.this,ParticularActivity.class);
                //放入跳转对象
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
            case R.id.two:{
                //点击了第二个卡片信息
                Bundle bundle = new Bundle();
                //假定第一个信息卡片显示的信息的id为2
                bundle.putInt("messageId",2);
                Intent intent =  new Intent(MainActivity.this,ParticularActivity.class);
                //放入跳转对象
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
            case R.id.three:{
                //点击了第三个卡片信息
                Bundle bundle = new Bundle();
                //假定第一个信息卡片显示的信息的id为3
                bundle.putInt("messageId",3);
                Intent intent =  new Intent(MainActivity.this,ParticularActivity.class);
                //放入跳转对象
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /*处理二维码扫描结果
                */

        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String jieGuo="http://www.feilonkji.xyz/";
                    result = bundle.getString(CodeUtils.RESULT_STRING);//二维码解析结果
                    if(result.equals(jieGuo)){
                        Intent intent=new Intent(MainActivity.this,OpenLack.class);
                        startActivity(intent);
                    }
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //设置传递扫码结果集方法
    public String chuanDi(){
        return result;
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //判断点击了实验选项
        if (id == R.id.action_settings) {
            //点击后的事件
            Toast.makeText(MainActivity.this,"功能还在开发中····",Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    //菜单触发事件方法判断
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
         //判断点击了账户
        if (id == R.id.nav_person) {
            if(!logflag){
                Intent intent=new Intent(MainActivity.this,LogInActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this,"账户已登录",Toast.LENGTH_SHORT).show();
            }

           //判断点击了钱包
        } else if (id == R.id.nav_gallery) {
            if(logflag==true){
                Intent intent=new Intent(MainActivity.this,ChargeActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(MainActivity.this,"请登录",Toast.LENGTH_SHORT).show();
            }
            //判断点击了信息
        } else if (id == R.id.nav_slideshow) {
            if(logflag==true){
                Intent intent=new Intent(MainActivity.this,MessageActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(MainActivity.this,"请登录",Toast.LENGTH_SHORT).show();
            }
            //判断点击了车位
        } else if (id == R.id.nav_manage) {
            if(logflag==true){
                Intent intent=new Intent(MainActivity.this,CarActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(MainActivity.this,"请登录",Toast.LENGTH_SHORT).show();
            }
            //判断点击了注册
        }else if(id == R.id.nav_register){
            if(logflag==true){
                Toast.makeText(MainActivity.this,"已登录，无法注册,请先退出登陆",Toast.LENGTH_SHORT).show();
            }else{
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        }else if(id == R.id.nav_shezhi){
            Intent intent = new Intent(this,ShezhiActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    //图片方法初始

    /**
     * 初始化事件
     */
    private void initEvent() {
        imageInfoList = new ArrayList<>();
        imageInfoList.add(new ImageInfo(1, "图片1，公告1", "","https://pic.sogou.com/d?query=%B5%C0%C2%B7%CD%BC%C6%AC&mode=1&did=1#did5", "http://www.cnblogs.com/luhuan/"));
        imageInfoList.add(new ImageInfo(1, "图片2，公告2", "", "https://pic.sogou.com/d?query=%BF%C6%BC%BC%CD%BC%C6%AC&mode=1&did=2#did31", "http://www.cnblogs.com/luhuan/"));
        imageInfoList.add(new ImageInfo(1, "图片3，公告3", "", "https://pic.sogou.com/d?query=%B5%C0%C2%B7%CD%BC%C6%AC&mode=1&did=1#did2", "http://www.cnblogs.com/luhuan/"));
        imageInfoList.add(new ImageInfo(1, "图片4，公告4", "仅展示", "https://pic.sogou.com/d?query=%CD%A3%B3%B5%CD%BC%C6%AC&mode=1&did=2#did1", ""));
        imageInfoList.add(new ImageInfo(1, "图片5，公告5", "仅展示", "https://pic.sogou.com/d?query=%CD%A3%B3%B5%CD%BC%C6%AC&mode=1&did=2#did3", ""));
    }

    /**
     * 初始化控件
     */
    private void initView() {

        mViewPager = findViewById(R.id.viewPager);
        mTvPagerTitle = findViewById(R.id.tv_pager_title);
        mLineLayoutDot = findViewById(R.id.lineLayout_dot);

    }

    private void imageStart() {
        //设置图片轮播
        int[] imgaeIds = new int[]{R.id.pager_image1, R.id.pager_image2, R.id.pager_image3, R.id.pager_image4,
                R.id.pager_image5, R.id.pager_image6, R.id.pager_image7, R.id.pager_image8};
        String[] titles = new String[imageInfoList.size()];
        List<SimpleDraweeView> simpleDraweeViewList = new ArrayList<>();

        for (int i = 0; i < imageInfoList.size(); i++) {
            titles[i] = imageInfoList.get(i).getTitle();
            SimpleDraweeView simpleDraweeView = new SimpleDraweeView(this);
            simpleDraweeView.setAspectRatio(1.78f);
            // 设置一张默认的图片
            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(this.getResources())
                    .setPlaceholderImage(ContextCompat.getDrawable(this, R.drawable.defult), ScalingUtils.ScaleType.CENTER_CROP).build();
            simpleDraweeView.setHierarchy(hierarchy);
            simpleDraweeView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));

            //加载高分辨率图片;
            ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageInfoList.get(i).getImage()))
                    .setResizeOptions(new ResizeOptions(1280, 720))
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    //.setLowResImageRequest(ImageRequest.fromUri(Uri.parse(listItemBean.test_pic_low))) //在加载高分辨率图片之前加载低分辨率图片
                    .setImageRequest(imageRequest)
                    .setOldController(simpleDraweeView.getController())
                    .build();
            simpleDraweeView.setController(controller);

            simpleDraweeView.setId(imgaeIds[i]);//给view设置id
            simpleDraweeView.setTag(imageInfoList.get(i));
            simpleDraweeView.setOnClickListener(this);
            titles[i] = imageInfoList.get(i).getTitle();
            simpleDraweeViewList.add(simpleDraweeView);

        }

        dots = addDots(mLineLayoutDot, fromResToDrawable(this, R.drawable.ic_dot_focused), simpleDraweeViewList.size());
        imageCarousel = new ImageCarousel(this, mViewPager, mTvPagerTitle, dots, 5000);
        imageCarousel.init(simpleDraweeViewList, titles)
                .startAutoPlay();
        imageCarousel.start();

    }


    /**
     * 动态添加一个点
     *
     * @param linearLayout 添加到LinearLayout布局
     * @param backgount    设置
     * @return 小点的Id
     */
    private int addDot(final LinearLayout linearLayout, Drawable backgount) {
        final View dot = new View(this);
        LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dotParams.width = 16;
        dotParams.height = 16;
        dotParams.setMargins(4, 0, 4, 0);
        dot.setLayoutParams(dotParams);
        dot.setBackground(backgount);
        dot.setId(View.generateViewId());
        ((Activity) this).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                linearLayout.addView(dot);
            }
        });

        return dot.getId();
    }


    /**
     * 资源图片转Drawable
     *
     * @param context 上下文
     * @param resId   资源ID
     * @return 返回Drawable图像
     */
    public static Drawable fromResToDrawable(Context context, int resId) {
        return ContextCompat.getDrawable(context, resId);
        //return context.getResources().getDrawable(resId);
    }

    /**
     * 添加多个轮播小点到横向线性布局
     *
     * @param linearLayout 线性横向布局
     * @param backgount    小点资源图标
     * @param number       数量
     * @return 返回小点View集合
     */
    private List<View> addDots(final LinearLayout linearLayout, Drawable backgount, int number) {
        List<View> dots = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int dotId = addDot(linearLayout, backgount);
            dots.add(findViewById(dotId));

        }
        return dots;
    }


    public static void showToast(CharSequence msg) {
        DialogUIUtils.showToastLong(msg.toString());
    }




    //6.0之后要动态获取权限，重要！！！
    protected void judgePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
        }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝

            // sd卡权限
            String[] SdCardPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, SdCardPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, SdCardPermission, 100);
            }

            //手机状态权限
            String[] readPhoneStatePermission = {Manifest.permission.READ_PHONE_STATE};
            if (ContextCompat.checkSelfPermission(this, readPhoneStatePermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, readPhoneStatePermission, 200);
            }

            //定位权限
            String[] locationPermission = {Manifest.permission.ACCESS_FINE_LOCATION};
            if (ContextCompat.checkSelfPermission(this, locationPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, locationPermission, 300);
            }

            String[] ACCESS_COARSE_LOCATION = {Manifest.permission.ACCESS_COARSE_LOCATION};
            if (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, ACCESS_COARSE_LOCATION, 400);
            }


            String[] READ_EXTERNAL_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, READ_EXTERNAL_STORAGE, 500);
            }

            String[] WRITE_EXTERNAL_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, WRITE_EXTERNAL_STORAGE, 600);
            }




        } else {
            //doSdCardResult();
        }
        //LocationClient.reStart();
    }



}
