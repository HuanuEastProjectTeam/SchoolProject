package com.example.andriodcar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

/**
 * 帮助页面
 * 用于显示软件功能及用途
 */
public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_help);
    }
}
