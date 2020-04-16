package com.example.andriodcar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.andriodcar.MainActivity.threadPoolExecutor;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_ConfirmButton,btn_fanhui,btn_ToLogin;
    private EditText et_name,et_password,et_passwordConfirm;

    //网络连接操作对象
    Connect ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
        setContentView(R.layout.activity_register);
        btn_ConfirmButton = findViewById(R.id.register_ConfirmButton);
        btn_fanhui = findViewById(R.id.register_fanhui);
        btn_ToLogin = findViewById(R.id.register_ToLogin);
        et_name = findViewById(R.id.register_name);
        et_password = findViewById(R.id.register_password);
        et_passwordConfirm = findViewById(R.id.register_passwordConfirm);
        btn_ConfirmButton.setOnClickListener(this);
        btn_fanhui.setOnClickListener(this);
        btn_ToLogin.setOnClickListener(this);
        ct = MainActivity.ct;
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.register_ConfirmButton:{
                if(et_name.getText().toString().trim().equals("")){
                    Toast.makeText(this,"用户名为空！",Toast.LENGTH_SHORT).show();
                }else if(et_password.getText().toString().trim().equals("")){
                    Toast.makeText(this,"密码为空！",Toast.LENGTH_SHORT).show();
                }else if(!et_passwordConfirm.getText().toString().equals(et_password.getText().toString())){
                    Toast.makeText(this,"两次输入密码不同",Toast.LENGTH_SHORT).show();
                }else{
                    threadPoolExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            ct.Register(et_name.getText().toString(),et_password.getText().toString());
                        }
                    });
                }
                break;
            }
            case R.id.register_fanhui:{
                finish();
                break;
            }
            case R.id.register_ToLogin:{
                Intent intent = new Intent(this,LogInActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            default:
                break;
        }
    }

}
