package com.example.andriodcar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mapframework.commonlib.utils.IO;
import com.baidu.platform.comapi.search.RTBusResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener{
    private View layout1,layout2,layout3,layout4,layout5,layout6;
    private Button bu1,btn_QuitLogin,btn_ChangePassword;
    private EditText edit;
    private TextView tv_name,tv_shiming,tv_xingbie,tv_phonenumber,tv_suozaidi,tv_id;
    private ImageButton touxiang_imagebutton;
    Boolean dianji=false;
    private String userinfo;
    SharedPreferences sp_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_message);
        layout1=findViewById(R.id.touxiang);
        //layout2=findViewById(R.id.yonghu);
        layout3=findViewById(R.id.renzheng);
        layout4=findViewById(R.id.sec);
        layout5=findViewById(R.id.number);
        layout6=findViewById(R.id.address);
        bu1= findViewById(R.id.Msg_fanhui);
        btn_QuitLogin=findViewById(R.id.Msg_QuitLogin_Button);
        btn_ChangePassword = findViewById(R.id.Msg_ChangePassword);
        tv_name =findViewById(R.id.Msg_touxiang_text);      //用户名
        tv_id=findViewById(R.id.Msg_touxiang_id);
        tv_phonenumber=findViewById(R.id.Msg_phonenumber);
        tv_shiming=findViewById(R.id.Msg_shiming);
        tv_suozaidi=findViewById(R.id.Msg_suozaidi);
        tv_xingbie=findViewById(R.id.Msg_xingbie);
        touxiang_imagebutton = findViewById(R.id.Msg_touxiang_image);
        touxiang_imagebutton.setOnClickListener(this);
        layout1.setOnClickListener(this);
      //  layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        layout6.setOnClickListener(this);
        bu1.setOnClickListener(this);
        btn_QuitLogin.setOnClickListener(this);
        btn_ChangePassword.setOnClickListener(this);
        sp_user = getSharedPreferences("user", Context.MODE_PRIVATE);
        UnPack();
    }
    //监听物理返回键实现效果
    public boolean onKeyDown(int keyCode,KeyEvent event){
        Intent intent1=new Intent(MessageActivity.this,MainActivity.class);     //跳转到Message界面
        startActivity(intent1);
        return super.onKeyDown(keyCode,event);
    }
    public void onClick(View ang){
        switch(ang.getId()){
            //判断点击了头像
            case R.id.Msg_touxiang_image:{
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);       //打开图库选择图片
                intent.setType("image/*");
                startActivityForResult(intent, 2);
                break;

            }
            //点击了用户名
            case R.id.Msg_touxiang_text:{
                dianji=true;
                Intent intent1=new Intent(MessageActivity.this,YonghumingActivity.class);
                startActivity(intent1);
                break;
            }
            //判断点击了认证
            case R.id.renzheng:{
                break;
            }
            //判断点击了性别
            case R.id.sec:{
                break;
            }
            //判断点击了电话号码
            case R.id.number:{
                break;
            }
            //判断点击了所在地
            case R.id.address:{
                break;
            }
            //判断点击了返回
            case R.id.Msg_fanhui:{
                finish();
                break;
            }
            case R.id.Msg_QuitLogin_Button:{
                MainActivity.logflag = false;
                sp_user.edit().putBoolean("logflag",false).apply();
                finish();
                break;
            }
            case R.id.Msg_ChangePassword:{
                Intent intent = new Intent(this,ChangePasswordActivity.class);
                startActivity(intent);
                break;
            }
            default:
                break;
        }

    }

    private void UnPack(){
        //获取保存的用户信息
        userinfo = sp_user.getString("userinfo","{\"accountNum\":\"游客\",\"carNum\":\"NULL\",\"id\":5,\"idNum\":\"NULL\",\"iden\":0,\"password\":\"123\",\"phoneNum\":\"123\",\"score\":100}");
        JSONObject job = JSONObject.parseObject(userinfo);
        if(job.get("id")!=null){
            tv_id.setText(job.get("id").toString());
        }else{
            Log.i("Connect","获取用户id失败");
        }
        if(job.get("accountNum")!=null){
            tv_name.setText(job.get("accountNum").toString());
        }else{
            Log.i("Connect","获取用户accountNum失败");
        }
        if(job.get("phoneNum")!=null){
            tv_phonenumber.setText(job.get("phoneNum").toString());
        }else{
            Log.i("Connect","获取用户phoneNum失败");
        }
        if(job.getBoolean("iden")!=null){
            if(job.getBoolean("iden")){
                tv_shiming.setText("是");
            }else{
                tv_shiming.setText("否");
            }
        }else{
            Log.i("Connect","获取用户iden失败");
        }
        if( sp_user.getString("HeadPortraitPath",null)!=null){
            File file = new File((sp_user.getString("HeadPortraitPath", null)));
            touxiang_imagebutton.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
            Log.i("Connect","设置头像成功");
        }else{                                          //没有保存头像就获取
            try {
                MainActivity.HeadPortraitImage = Connect.getConncet().getHeadPortrait();
                touxiang_imagebutton.setImageBitmap(MainActivity.HeadPortraitImage);
                File file = new File(getFilesDir().getPath());          //保存头像
                File f = new File(file, "head.PNG");
                f.createNewFile();
                FileOutputStream fos = new FileOutputStream(f);
                MainActivity.HeadPortraitImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                Log.i("Connect", "save HeadPortrait file successful");
                sp_user.edit().putString("HeadPortraitPath", f.getPath()).apply();       //储存图片储存的路径
                fos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        Log.i("Connect","设置信息成功");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                try {
                    final Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    MainActivity.threadPoolExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Connect.getConncet().upHeadPortrait(bitmap);
                                touxiang_imagebutton.setImageBitmap(bitmap);
                                MainActivity.HeadPortraitImage = bitmap;
                                File file = new File(getFilesDir().getPath());
                                File f = new File(file, "head.PNG");
                                f.createNewFile();
                                FileOutputStream fos = new FileOutputStream(f);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                fos.flush();
                                Log.i("Connect", "save HeadPortrait file successful");
                                sp_user.edit().putString("HeadPortraitPath",f.getPath()).apply();       //储存图片储存的路径
                                fos.close();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
