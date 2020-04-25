package com.example.andriodcar;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.andriodcar.MainActivity.threadPoolExecutor;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_fanhui,btn_Comfirm;
    private EditText et_old_password, et_new_password;
    Connect ct;
    SharedPreferences sp_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
        setContentView(R.layout.activity_change_password);
        btn_Comfirm = findViewById(R.id.ChangePassword_Comfirm);
        btn_fanhui = findViewById(R.id.ChangePassword_fanhui);
        et_old_password = findViewById(R.id.ChangePassword_et_old);
        et_new_password = findViewById(R.id.ChangePassword_et_new);
        btn_fanhui.setOnClickListener(this);
        btn_Comfirm.setOnClickListener(this);
        sp_user = getSharedPreferences("user", Context.MODE_PRIVATE);
        ct = Connect.getConncet();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ChangePassword_Comfirm:{

                String old_password = sp_user.getString("Password","");     //获取保存在本地的登陆密码
                if(!et_old_password.getText().toString().equals(et_new_password.getText().toString())){         //判断两个输入框是否相同
                    if(et_old_password.getText().toString().equals(old_password)){              //判断输入的旧密码与保存的登陆密码是否相同
                        threadPoolExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                if(ct.ChangePassword(et_new_password.getText().toString())){
                                    Looper.prepare();       //在子线程中使用toast
                                    Toast.makeText(ChangePasswordActivity.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                                    finish();
                                    Looper.loop();
                                }else{
                                    Looper.prepare();
                                    Toast.makeText(ChangePasswordActivity.this,"修改密码失败",Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(this,"输入旧密码与账号密码不符合",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this,"新密码不能与原密码相同",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.ChangePassword_fanhui:{
                finish();
                break;
            }
            default:
                break;
        }
    }
}


