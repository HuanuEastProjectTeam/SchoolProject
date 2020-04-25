package com.example.andriodcar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class YonghumingActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bu1,bu2;
    private EditText editText;
    private String yonghuming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_changename);
        bu1=(Button)findViewById(R.id.fanhui);
        bu2=(Button)findViewById(R.id.quedin);
        editText=(EditText)findViewById(R.id.shuru);
        bu1.setOnClickListener(this);
        bu2.setOnClickListener(this);
    }
    public void onClick(View ang3){
        switch(ang3.getId()){
            case R.id.fanhui:{
                finish();
                break;
            }
            case R.id.quedin:{
                yonghuming=editText.getText().toString();
              //  Log.e("YonghumingActivity",yonghuming);//打印输入的用户名
                SharedPreferences sp=getSharedPreferences("yonghuming",MODE_PRIVATE);//创建临时数据,第一个参数为编辑文件名，第二个参数常量0，使得可以使用context模式调用打开
                SharedPreferences.Editor editor=sp.edit();//使得sharedPreferences处于编辑状态
                editor.putString("data",yonghuming);//传递数据
                editor.commit();
                Intent intent1=new Intent(YonghumingActivity.this,MessageActivity.class);//跳转到Message界面
                startActivity(intent1);

            }
        }
    }
    public String getYonghuming(){
        return yonghuming;
    }

}
