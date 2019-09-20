package com.example.andriodcar;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class StartTimeActivity extends AppCompatActivity implements View.OnClickListener{
    Button time,pay;
    TextView money;
    int a=0;//设置初始时间
    Runnable runnable;
    Handler handler;//用作定时器
    int port=5000;
    String ip="192.168.43.131";
    Message message;
    Handler handler1;//用作连接
    Boolean biaoZhi=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_time);
        time=(Button)findViewById(R.id.time);
        pay=(Button)findViewById(R.id.pay);
        pay.setOnClickListener(this);
        money=(TextView)findViewById(R.id.money1);
        handler=new Handler();
        handler1=new Handler(){//重写handleMessage方法获得子线程传递信息
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                switch(msg.what){
                    case 0:{//表示完成停车
                        Log.e("StartTimeActivity","jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");

                        if(msg.obj.equals("2")){

                            int shiTime=a/60/60;//a表示小时数
                            if(shiTime<=3){
                                double jinEr=(a/60/60*5)+((a/60)%60*0.08);//计算停车收费总额
                                DecimalFormat df   =new DecimalFormat("#.0");
                                df.format(jinEr);//设置保留一位小数
                                money.setText("需支付的金额为："+jinEr);
                                pay.setVisibility(View.VISIBLE);//设置时间停止后界面刷新显示支付按钮
                            }else if(shiTime>3&&shiTime<=9){
                                double jinEr=(a/60/60*4)+((a/60)%60*0.08);
                                DecimalFormat df   =new DecimalFormat("#.0");
                                df.format(jinEr);//设置保留一位小数
                                money.setText("需支付的金额为："+jinEr);
                                pay.setVisibility(View.VISIBLE);
                            }else if(shiTime>9&&shiTime<=12){
                                double jinEr=50;
                                DecimalFormat df   =new DecimalFormat("#.0");
                                df.format(jinEr);//设置保留一位小数
                                money.setText("需支付的金额为："+jinEr);
                                pay.setVisibility(View.VISIBLE);
                            }
                        }
                        break;


                    }
                    case 1:{//表示出现异常
                        break;

                    }
                }

            }


        };
        new Thread(new Runnable(){
            public void run(){
                try{

                        Socket socket=new Socket(ip,port);
                        InputStream in=socket.getInputStream();//接入输入流
                        byte[] ok=new byte[1024];
                        in.read(ok);//读取数据到ok
                        String result=new String (ok);//实例为字符串型
                        Log.e("StartTimeActivity",result.trim()+"ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
                        in.close();
                        socket.close();
                        Message message=new Message();//创建消息对象，作为线程间的传递对象
                        message.obj=result.trim();
                        handler1.sendMessage(message);
                        message.what=0;//线程标记

                        in.close();
                        socket.close();
                        if(result.trim().equals("2")){//判断停车完成标志
                            biaoZhi=false;//作为停止计时的标志
                        }



                }catch(Exception e){

                }


            }
        }).start();
        runnable=new Runnable(){//创建一个线程对象
            public void run(){
                if(a<60){
                    time.setText(a+"秒");
                    a++;
                }else if(a>=60){
                    time.setText(a/60/60+"小时"+a/60+"分钟"+a%60+"秒");
                    a++;
                }

                handler.postDelayed(runnable,1000);//设置每一秒执行一次
                if(!biaoZhi){
                handler.removeCallbacks(runnable);//关闭计时器
            }
            }
        };
        handler.postDelayed(runnable,1000);//设置每一秒执行一次
    }

    public void onClick(View ang){
        //进行支付跳转，调用支付接口
     //  Intent intent=new Intent(StartTimeActivity.this,);
      // startActivity(intent);

    }



}


