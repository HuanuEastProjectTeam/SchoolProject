package com.example.andriodcar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ChargeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bu1,bu2,bu3;
    private View textView,textView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
        setContentView(R.layout.activity_charge);
        bu1=(Button)findViewById(R.id.fanhui);
        bu2=(Button)findViewById(R.id.tixian);
        bu3=(Button)findViewById(R.id.chongzhi);
        textView=(TextView)findViewById(R.id.bankid);
        textView1=(TextView)findViewById(R.id.shezhi);
        bu1.setOnClickListener(this);
        bu2.setOnClickListener(this);
        bu3.setOnClickListener(this);
        textView.setOnClickListener(this);
        textView1.setOnClickListener(this);
    }


    public void onClick(View ang){
        switch(ang.getId()){
            //判断点击了返回
            case R.id.fanhui:{
                finish();
                break;
            }
            //判断点击了设置
            case R.id.shezhi:{
                Intent intent1=new Intent(ChargeActivity.this,ShezhiActivity.class);
                startActivity(intent1);
                break;
            }
            //判断点击了充值
            case R.id.chongzhi:{
                Toast.makeText(getApplication(),"功能还在开发中···尽情期待", Toast.LENGTH_LONG).show();
                break;
            }
            //判断点击了提现
            case R.id.tixian:{
                Toast.makeText(ChargeActivity.this,"功能还在开发中···尽情期待", Toast.LENGTH_LONG).show();
                break;
            }
            //判断点击了绑定银行卡
            case R.id.bankid:{
                      break;
            }
        }
    }

}
