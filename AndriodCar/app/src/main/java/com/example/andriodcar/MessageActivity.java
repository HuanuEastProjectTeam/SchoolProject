package com.example.andriodcar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener{
    private View layout1,layout2,layout3,layout4,layout5,layout6;
    private Button bu1,btn_QuitLogin;
    private EditText edit;
    private TextView textView;
    Boolean dianji=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_message);
        layout1=findViewById(R.id.touxiang);
        //layout2=findViewById(R.id.yonghu);        //YQ:10.16:去掉了用户名一行，只要用头像边上那个应该就可以了
        layout3=findViewById(R.id.renzheng);
        layout4=findViewById(R.id.sec);
        layout5=findViewById(R.id.number);
        layout6=findViewById(R.id.address);
        bu1=(Button)findViewById(R.id.fanhui);
        btn_QuitLogin=findViewById(R.id.QuitLogin_Button);
        textView=findViewById(R.id.touxiang_text);      //YQ:10.16:定位由原来用户名一行改为定位到头像边上的用户名
        layout1.setOnClickListener(this);
      //  layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        layout6.setOnClickListener(this);
        bu1.setOnClickListener(this);
        btn_QuitLogin.setOnClickListener(this);

    }
    //监听物理返回键实现效果
    public boolean onKeyDown(int keyCode,KeyEvent event){
        Intent intent1=new Intent(MessageActivity.this,MainActivity.class);//跳转到Message界面
        startActivity(intent1);
        return super.onKeyDown(keyCode,event);
    }
    ;public void onClick(View ang){
        switch(ang.getId()){
            //判断点击了头像
            case R.id.touxiang:{

                break;

            }
            //点击了用户名
            case R.id.touxiang_text:{
                dianji=true;
                Intent intent1=new Intent(MessageActivity.this,YonghumingActivity.class);
                startActivity(intent1);



                break;
            }
            //判断点击了认证
            case R.id.renzheng:{
                break;
            }
            //判断点击了性别
            case R.id.sec:{
                break;
            }
            //判断点击了电话号码
            case R.id.number:{
                break;
            }
            //判断点击了所在地
            case R.id.address:{
                break;
            }
            //判断点击了返回
            case R.id.fanhui:{
                Intent intent1=new Intent(MessageActivity.this,MainActivity.class);//跳转到Message界面
                startActivity(intent1);
                break;
            }
            case R.id.QuitLogin_Button:{
                MainActivity.logflag = false;
                finish();
                break;
            }
            default:
                break;
        }

    }
}
