package com.example.andriodcar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_ConfirmButton,btn_ExitButton,btn_fanhui;
    private EditText et_name,et_password,et_passwordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
        setContentView(R.layout.activity_register);
        btn_ConfirmButton = findViewById(R.id.register_ConfirmButton);
        btn_ExitButton = findViewById(R.id.register_ExitButton);
        btn_fanhui = findViewById(R.id.register_fanhui);
        et_name = findViewById(R.id.register_name);
        et_password = findViewById(R.id.register_password);
        et_passwordConfirm = findViewById(R.id.register_passwordConfirm);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.register_ConfirmButton:{
                break;
            }
            case R.id.register_ExitButton:{
                finish();
                break;
            }
            case R.id.register_fanhui:{
                finish();
                break;
            }
            default:
                break;
        }
    }

}
