package com.example.andriodcar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShezhiActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textView1,textView2;
    Button fanhui;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shezhi);
        textView1=(TextView)findViewById(R.id.xiugaimima);
        textView2=(TextView)findViewById(R.id.wangjimima);
        fanhui=(Button)findViewById(R.id.fanhui);
        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
        fanhui.setOnClickListener(this);
    }
    public void onClick(View ang){
        switch(ang.getId()){
            //判断点击了忘记密码
            case R.id.wangjimima:{
                Toast.makeText(this,"还没搞出来", Toast.LENGTH_LONG).show();
            }
            //判断点击了修改密码
            case R.id.xiugaimima:{
                Toast.makeText(this,"我也还没搞出来", Toast.LENGTH_LONG).show();
            }
            case R.id.fanhui:{
                finish();
            }
        }
    }
}
