package com.example.andriodcar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

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
}

