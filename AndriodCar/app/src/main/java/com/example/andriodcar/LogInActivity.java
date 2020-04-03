package com.example.andriodcar;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.example.andriodcar.Bean.Userbean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import utils.javautils;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_Confirm,btn_Agree,btn_fanhui,btn_Quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
        setContentView(R.layout.activity_login);
        btn_Agree = findViewById(R.id.Login_AgreeButton);
        btn_Confirm = findViewById(R.id.Login_ConfirmButton);
        btn_fanhui = findViewById(R.id.Login_fanhui);
        btn_Quit = findViewById(R.id.QuitLogin_Button);
        btn_Agree.setOnClickListener(this);
        btn_Confirm.setOnClickListener(this);
        btn_fanhui.setOnClickListener(this);
        btn_Quit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Login_ConfirmButton:{
                login();
                break;
            }
            case R.id.Login_AgreeButton:{
                break;
            }
            case R.id.Login_fanhui:{
                finish();
                break;
            }
            case R.id.QuitLogin_Button:{
                finish();
                break;
            }
            default:
                break;
        }
    }

    private void login(){
        //4.3：完全来自MainActivity的奇妙登陆方法，需要全部替换为connect类中的方法
        // Handle the camera action。弹出登陆窗口
        DialogUIUtils.showAlert(this, "登录", "", "请输入用户名", "请输入密码", "登录", "取消", false, true, true, new DialogUIListener() {
            @Override
            public void onPositive() {
                Toast.makeText(getApplicationContext(),"11",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNegative() {

            }

            @Override
            //获取输入信息方法，参数1为账号，参数2为密码
            public void onGetInput(CharSequence loginname, CharSequence password) {
                if (loginname.length()<=0||password.length()<=0){
                    Toast.makeText(getApplicationContext(),"账户或密码为空",Toast.LENGTH_SHORT).show();
                }else{
                    //转为字符串赋值
                    final     String uid=loginname.toString().trim();
                    final    String upwd=password.toString().trim();
                    //验证显示账号密码
                    Toast.makeText(getApplicationContext(),uid+"  "+upwd,Toast.LENGTH_SHORT).show();
                    //数据库连接判断判断
                    new Thread(new Runnable() {

                        @Override
                        public void run() {

                            //与Tomcat服务器进行连接，传输账号信息-----------------------------------------------------------------------------------------------

                            String path="http://120.79.87.21/TomcatTest/HelloServlet?loginname="+uid+"&password="+upwd;
                            try {
                                try{
                                    URL url = new URL(path); //新建url并实例化
                                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                    connection.setRequestMethod("GET");//获取服务器数据
                                    connection.setReadTimeout(8000);//设置读取超时的毫秒数
                                    connection.setConnectTimeout(8000);//设置连接超时的毫秒数
                                    connection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接

                                    InputStream in = connection.getInputStream();
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                    String result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                                    StringBuffer sb=new StringBuffer();

                                    sb.append(result);
                                    //result中的数据为服务器端页面显示内容，显示的信息量有限

                                    Log.e("MainActivity","run: "+result);

                                    if (result.equals("can not login!")){//读取的文本内容对比
                                        Looper.prepare();
                                        Toast.makeText(getApplicationContext(),"账号或密码错误",Toast.LENGTH_LONG).show();
                                        Looper.loop();
                                    }else{


                                        Log.d("MainActivity","run2: "+result);


                                        Looper.prepare();

                                        Userbean userbean= javautils.parseJSONWithJSONObject(result);
                                        MainActivity.logflag = true;

                                        Looper.loop();
                                    }
                                }catch (MalformedURLException e){}
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }

            }
        }).show();

        //--------------------------------------------------------------------------------------------------------------------------



    }
}

