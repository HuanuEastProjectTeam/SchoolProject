package com.example.andriodcar;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andriodcar.Bean.NewMessage;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class ParticularActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消默认顶部导航栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_particular);
        ShowMessage();
    }

    /**
     * 点击方法
     * @param view
     */
    public void Back(View view){
        switch (view.getId()){
            case R.id.back_message:{
                //返回
                finish();
                break;
            }
        }
    }
    public void ShowMessage(){
        //获取详情页面的众多组件
        TextView textViewTitle = findViewById(R.id.messageTitle);
        ImageView imageViewImage = findViewById(R.id.messageImage);
        TextView textViewMessage = findViewById(R.id.messageParticular);
        /**
         * 根据传递过来的新闻信息的id
         * 来查询数据库中的全部信息
         * 并分类处理标题，图片，主要新闻信息
         *
         */
        //获取传递过来的信息id
        int messageId = getIntent().getIntExtra("messageId",1);
        //根据id连接数据库进行查询信息
        /**
         * 此处查询信息处理
         *
         * 然后通过处理完的信息数据对像
         * 最终显示到页面
         */
        NewMessage newMessage = MainActivity.newMessageList.get(messageId-1);
        textViewTitle.setText(newMessage.getMessageTitle());
        textViewMessage.setText(newMessage.getMessageImage());          //因为服务端错误导致内容在image变量中
    }
}
