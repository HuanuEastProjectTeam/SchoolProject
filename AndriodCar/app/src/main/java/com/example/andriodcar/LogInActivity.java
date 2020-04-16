package com.example.andriodcar;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.andriodcar.MainActivity.logflag;
import static com.example.andriodcar.MainActivity.threadPoolExecutor;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {



    Button btn_Confirm,btn_Forget,btn_fanhui,btn_ToRegister;

    EditText et_name,et_password;

    //网络连接操作对象
    Connect ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
        setContentView(R.layout.activity_login);
        btn_Confirm = findViewById(R.id.Login_ConfirmButton);
        btn_fanhui = findViewById(R.id.Login_fanhui);
        btn_Forget = findViewById(R.id.Login_Forget);
        btn_ToRegister = findViewById(R.id.Login_ToRegister);
        et_name = findViewById(R.id.Login_name);
        et_password = findViewById(R.id.Login_password);
        btn_Confirm.setOnClickListener(this);
        btn_fanhui.setOnClickListener(this);
        btn_ToRegister.setOnClickListener(this);
        btn_Forget.setOnClickListener(this);
        ct = MainActivity.ct;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Login_Forget:{
                Toast.makeText(this, "功能开发中...", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.Login_fanhui:{
                finish();
                break;
            }
            case R.id.Login_ToRegister:{
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.Login_ConfirmButton:{
                if(!et_name.getText().toString().trim().equals("")&&!et_password.getText().toString().trim().equals("")){   //确认用户名和密码栏不为空
                    threadPoolExecutor.execute(new Runnable() {     //往线程池中加入线程
                        @Override
                        public void run() {
                            if(ct.login(et_name.getText().toString(),et_password.getText().toString())){
                                Looper.prepare();       //在子线程中使用toast
                                Toast.makeText(LogInActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                                finish();
                                Looper.loop();
                            }else{
                                Looper.prepare();
                                Toast.makeText(LogInActivity.this,"登陆失败，用户名不存在或密码错误",Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }
                    });

                }else{
                    Toast.makeText(this,"用户名或密码为空",Toast.LENGTH_SHORT).show();
                }
            }
            default:
                break;
        }
    }



    }


