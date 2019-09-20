package com.example.andriodcar;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class OpenLack extends AppCompatActivity implements View.OnClickListener,ActivityCompat.OnRequestPermissionsResultCallback {
    private Button openLack;
    private TextView priceBiaoZhun1,priceBiaoZhun2,priceBiaoZhun3;
    private MainActivity saoMa=new MainActivity();
    private String erWeiMa_result=null;
    private int port=5000;
    private String ip="192.168.43.131";
    private PrintStream out=null;
    private InputStream in=null;
    private String result=null;
    private int biaoZhi=0;
    private Handler handler;
    private final String str="1";//发送1
    private Chronometer chronometer;
    private final int REQUEST_ENABLE_BT = 1;//设置回调返回数据
    private BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;
    private List<Map<String,String>> list=new ArrayList<Map<String,String>>();
    private List<Map<String,String>> list1=new ArrayList<Map<String,String>>();
    private boolean biaoZhi1=true;
    private String mac="20:19:03:28:23:95";
    private static final UUID MY_UUID=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//通用蓝牙串口uuid
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_lack);
        openLack=(Button)findViewById(R.id.openlack);
        priceBiaoZhun1=(TextView)findViewById(R.id.bianhao1);
        priceBiaoZhun2=(TextView)findViewById(R.id.bianhao2);
        priceBiaoZhun3=(TextView)findViewById(R.id.bianhao3);
        openLack.setOnClickListener(this);
        erWeiMa_result=saoMa.chuanDi();//获取扫码结果，然后传输到数据库进行查询比对，获取数据库中的收费标准，调用显示方法
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();//获取蓝牙句柄

        /**
         * 打开蓝牙
         *
         *
         */
        if(!mBluetoothAdapter.isEnabled())
        {
            mBluetoothAdapter.enable();

        }

        //    mBluetoothAdapter.startDiscovery();
        requestPermission();//动态获取权限
        IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver,filter);
        IntentFilter filter2=new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver,filter2);



        /**
         *
         *
         *
         * 调用链接数据库
         * 调用显示收费标准方法
         *
         */

    }

    /***********************************搜索蓝牙****************************************************************************/
    /**
     * 动态处理权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkAccessFinePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkAccessFinePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
                Log.d("tag", "没有权限，请求权限");
                return;
            } else {
                /**
                 * 如果已经同意了该权限则开始搜索设备
                 */
                mBluetoothAdapter.startDiscovery();

            }
            Log.d("tag", "已有定位权限");
        }
    }


    /**
     * 申请权限回调方法 处理用户是否授权
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /**
                     * 用户同意授权开始搜索设备
                     */
                    mBluetoothAdapter.startDiscovery();

                } else {
                    //用户拒绝授权 则给用户提示没有权限功能无法使用，
                    Log.d("tag", "没有定位权限，请先开启!");
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void onDestroy() {

        super.onDestroy();
        //解除注册
        unregisterReceiver(mReceiver);
        Log.e("destory","解除注册");
        if(bluetoothSocket!=null){
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //触发点击事件
    public void onClick(View ang){

        switch(ang.getId()){
            case R.id.openlack:{//判断点击了开锁
                 /**
                 * 新建一个线程
                 * 传递数据信息给硬件端，接收到硬件端的反馈信息在进行跳转
                 */
  /*              handler=new Handler(){//创建handler对象获取子线程传递的变量
                    public void handleMessage(Message msg){
                        super.handleMessage(msg);
                        switch(msg.what){//获取信息对象的约定值进行判断
                            case 0:{
                                if(msg.obj.equals("0")){
                                    Intent intent=new Intent(OpenLack.this,StartTimeActivity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(),"解锁失败，请检查网络连接",Toast.LENGTH_LONG).show();//提示解锁失败
                                }
                                break;
                            }
                            default:{
                                Toast.makeText(getApplicationContext(),"车位异常",Toast.LENGTH_LONG).show();

                            }

                        }
                    }

                };
*/

               /******************************************************************************************/
                /**
                 * 设置wifi模块的连接线程
                 */
 /*               new Thread(new Runnable(){
                    public void run(){
                        try{
                            Socket socket=new Socket(ip,port);//与服务器建立链接建立链接
                            out=new PrintStream(socket.getOutputStream());
                            out.println("1");//传送数字1表示开锁
                            in=socket.getInputStream();//接入输入流
                            byte[] b= new byte[1024];
                            in.read(b);//读取到b中
                            result=new String(b);//转换为字符串
                            Log.e("OpenLack",result.trim()+"cccccccccccccccccccccccccc");
                            Message message=new Message();//创建消息对象，作为线程间的传递对象
                            message.obj=result.trim();
                            handler.sendMessage(message);
                            message.what=0;//线程标记
                            out.close();
                            in.close();
                            socket.close();
                        }catch(Exception e){

                        }
                    }
                }).start();
              /********************************************************************************************/
             /*     if(result.trim().equals("0")){//判断反馈信息为0则进行跳转
                    Toast.makeText(getApplicationContext(),"解锁成功",Toast.LENGTH_LONG).show();//提示解锁成功

                    //              Intent intent=new Intent(OpenLack.this,StartTimeActivity.class);
                    //         startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(),"解锁失败，请检查网络连接",Toast.LENGTH_LONG).show();//提示解锁失败
                }*/


             /**
              * 使用蓝牙连接通信进行开锁
              */



                openLack.setText("开锁中··");
                Log.v("msg",""+list.get(0));
                connect(0);//调用连接方法
     /*           for(int i=0;i<=list1.size();i++){

                /*    if(list.get(i).get("HC-06").equals(mac)){
                     //   connect(i);
                        Toast.makeText(OpenLack.this,"chenggong111",Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(OpenLack.this,"chenggong",Toast.LENGTH_LONG).show();

                }*/
         //       requestPermission();//调用动态申请权限方法并开始蓝牙扫描
                new Handler().postDelayed(new Runnable(){//构建定时器延时2秒跳转
                    public void run(){
                        openLack.setText("开锁成功");
                        Intent intent=new Intent(OpenLack.this,StartTimeActivity.class);
                        startActivity(intent);
                    }
                },4000);




            }
        }



    }



    /**
     * 设置显示收费标准
     * @param san 前三个小时的收费
     * @param qi 3-7小时的收费
     * @param guoYe 过夜的收费
     */
    public void xianShiBiaoZhun(String san,String qi,String guoYe){
        priceBiaoZhun1.setText(san);
        priceBiaoZhun1.setText(qi);
        priceBiaoZhun1.setText(guoYe);

    }
    //定义广播接收
    private BroadcastReceiver mReceiver=new BroadcastReceiver(){



        @Override
        public void onReceive(Context context, Intent intent) {

            String action=intent.getAction();

            Log.e("ywq", action);
            if(action.equals(BluetoothDevice.ACTION_FOUND))
            {
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //  Toast.makeText(context,"dddd",Toast.LENGTH_LONG).show();
                if(device.getBondState()==BluetoothDevice.BOND_BONDED)
                {    //显示已配对设备
                    Map<String,String> map=new HashMap<String,String>();
                    map.put(device.getName(),device.getAddress());
                    list.add(map);
                    Toast.makeText(context,"peidui"+device.getName(),Toast.LENGTH_LONG).show();
                //    souSuo();
                    //   text.append("\n"+device.getName()+"==>"+device.getAddress()+"\n");
                }else if(device.getBondState()!=BluetoothDevice.BOND_BONDED)
                {
                    Map<String,String> map=new HashMap<String,String>();
                    map.put(device.getName(),device.getAddress());
                    list1.add(map);
                    Toast.makeText(context,device.getName(),Toast.LENGTH_LONG).show();
                //    souSuo();//调用适配器实时显示搜索情况
                    // text3.append("\n"+device.getName()+"==>"+device.getAddress()+"\n");
                }

            }else if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){


                Toast.makeText(context,"dddd",Toast.LENGTH_LONG).show();

            }

        }


    };

    /**
     * 连接蓝牙
     * @param i 对应集合中的位置
     */
    public void connect(int i) {

        if(biaoZhi1==true){//点击了已配对的进行连接
            bluetoothDevice=mBluetoothAdapter.getRemoteDevice(list.get(i).get("HC-06"));//获取到目标蓝牙的地址
            Log.v("Tog",list.get(i).get("HC-06"));
            new Thread(new Runnable(){//创建一个线程进行连接
                public void run(){
                    try {
                        //  bluetoothSocket=bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);//建立连接串口
                        //  bluetoothSocket.connect();//连接
                        int sdk = Build.VERSION.SDK_INT;
                        if (sdk >= 10) {
                            bluetoothSocket  = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                            bluetoothSocket.connect();
                        } else {
                            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
                            bluetoothSocket.connect();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else {
            bluetoothDevice=mBluetoothAdapter.getRemoteDevice(list1.get(i).get("HC-06"));//获取到目标蓝牙的地址
            Log.v("Tog",list1.get(i).get("HC-06"));
            new Thread(new Runnable(){//创建一个线程进行连接
                public void run(){
                    try {
                        // bluetoothSocket=bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);//建立连接串口
                        //   bluetoothSocket.connect();//连接

                        int sdk = Build.VERSION.SDK_INT;
                        if (sdk >= 10) {
                            bluetoothSocket  = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                            bluetoothSocket.connect();
                        } else {
                            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
                            bluetoothSocket.connect();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }


}
