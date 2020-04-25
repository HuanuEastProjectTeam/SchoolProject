package com.example.andriodcar;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.andriodcar.Bean.UserOrdinary;


import java.io.*;
import java.net.Socket;

public class Connect  {

    private String ip = "120.79.87.21";
    private int imageUploadPort = 5423;
    private int port = 5422;

    private static String TAG = "Connect";

    //普通数据交互接口
    private Socket sc = null;
    //图片交互接口
    private Socket ImageSocket = null;

    private OutputStream dout = null;
    private InputStreamReader din = null;

    private JSONObject jsonData = null;

    //数据持久化操作
    private SharedPreferences sp_user;
    private SharedPreferences.Editor editor_user;

    private static Connect ct = new Connect();      //单例模式，装载类时强制初始化


    /**
     * 构造函数
     */
    private Connect(){          //私有化构造函数

    }

    private void InitConnect(){
        try {
            sc = new Socket(ip,port);       //通过socket连接服务器
            din = new InputStreamReader(sc.getInputStream(),"gb2312");
            dout = sc.getOutputStream();
            if(sc!=null){
                Log.i(TAG,"connect server successful");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connect getConncet(){
        return ct;
    }

    /**
     * 传入sharedpreference对象，用于保存用户数据
     */
    public void SetUserSharePreference(SharedPreferences sharedPreferences){
        if(sharedPreferences != null){
            sp_user = sharedPreferences;
            editor_user = sp_user.edit();
        }else{
            Log.i(TAG,"传入的sharedPreferences为空");
        }
    }

    /**
     * 初始化图片上传接口
     */
    public void InitImageIO(){
        try {
            ImageSocket = new Socket(ip,imageUploadPort);
            if(ImageSocket!=null){
                Log.i(TAG,"ImageUpLoad Ready");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传图片
     * 上传图片前必须传输用户信息，必须包含有name
     */
    public void UploadImage(){

    }

    /**
     * 图片输入
     * @return 如果图片不为null则返回true
     */
    public boolean ReceiveImage(){
        return true;
    }

    /**
     * 发送数据至服务器
     * @param message 要发送至服务器的字符串
     */
    public void sendMessage(String message){
        InitConnect();      //发消息的时候连接服务器，收到消息后关闭
        try {
            if(dout != null || message != null){        //判断输出流或者消息是否为空，为空的话会产生nullpoint错误
                message = message + "\n";       //末尾加上换行让服务器端有消息返回
                byte[] me = message.getBytes();
                dout.write(me);
                dout.flush();
            }else{
                Log.d("Connect","The message to be sent is empty");
            }
            Log.i(TAG,"send message successful");
        } catch (IOException e) {
            Log.i(TAG,"send message to server failed");
            e.printStackTrace();
        }
    }

    public String receiveMessage(){
        String message = "";
        try {
            Log.i(TAG,"开始接收服务端信息");
            char[] inMessage = new char[1024];
            int a =din.read(inMessage);     //a存储返回消息的长度
            Log.i(TAG,"reply length:"+a);
            message = new String(inMessage,0,a);        //必须要用new string来转换
            Log.i("Connect",message);
        } catch (IOException e) {
            Log.i(TAG,"receive message failed");
            e.printStackTrace();
        }
        CloseConnect();     //发消息的时候连接服务器，收到消息后关闭
        return message;
    }


    /**
     * 包装search操作类型json数据
     * @param type 操作类型
     * @param source 包含信息的javabean
     * @return 返回source转换的json格式的字符串
     */
    public String search(String type,Object source){
        JSONObject job = null;
        String jstr = null;
        Log.i(TAG,"开始查找编码操作类型");
        job = (JSONObject) JSON.toJSON(source);
        if(source.getClass() != JSONObject.class){      //判断传入的是否已经是jsonobject类型，是的话直接转为字符串返回，不是的话开始编码
            switch (type){
                case "login":   //登陆
                    Log.i(TAG,"开始编码login内容");
                    job.put("login","1");
                    break;
                case "newMessage":  //新闻信息查询
                    break;
                case "PersonMessage":   //个人信息查询
                    break;
                case "creditScore": //信誉积分查询
                    break;
                case "stall":   //个人车位查询
                    break;
                case "help":    //帮助查询
                    break;
                default:
                    break;
            }
            if (job != null) {
                job.put("type","search");
                jstr = job.toJSONString();
                Log.i(TAG,"编码成功，编码结果为："+jstr);
            }
        }else{
            jstr = JSON.toJSONString(source);
            Log.i(TAG,"检查到已经是目标类型，直接编码，编码结果为："+jstr);
        }
        return jstr;
    }

    /**
     * 登陆方法
     * @param name 用户名
     * @param password  登陆密码
     */
    public boolean login(String name,String password){
        JSONObject job = new JSONObject();  //创建一个json对象存放login信息
        job.put("name",name);
        job.put("password",password);
        job.put("login","1");
        job.put("type","search");

        String Msend = search("login",job); //用本类的search方法将json对象转换为json字符串
        sendMessage(Msend);
        String reply = receiveMessage();    //获取服务器返回信息
        JSONObject replyjob = null;
        if(reply!=null && !reply.equals("")){
            replyjob = JSONObject.parseObject(reply);
        }
        if(replyjob!=null && replyjob.get("sign")!=null && replyjob.get("sign").toString().equals("default")){
            MainActivity.logflag=false;
            return false;
        }else if (reply != null && !reply.trim().equals("")) {
            Log.i("Connect", "login successful");
            Log.i(TAG, "reply:" + reply);
            UserOrdinary uo = JSON.parseObject(reply, UserOrdinary.class);    //将服务器返回的消息解码为user类

            MainActivity.logflag = true;        //设置当前登录状态
            editor_user.putBoolean("logflag", true);
            editor_user.putInt("PhoneNum", uo.getPhoneNum());     //保存登录信息，只保存登陆名和密码，下次开启app重新登陆
            editor_user.putString("Password", uo.getPassword());
            editor_user.putString("userinfo", reply);
            editor_user.apply();

            return true;
        }
        return false;
    }



    /**
     * 包装update操作类型json数据
     * 参数用法同search方法
     */
    public String updata(String type,Object source){
        JSONObject job = null;
        String jstr = null;
        Log.i(TAG,"开始查找编码操作类型");
        job = (JSONObject) JSON.toJSON(source);
        if(source.getClass() != JSONObject.class){      //判断传入的是否已经是jsonobject类型，是的话直接转为字符串返回，不是的话开始编码
            switch (type){
                case "personMessage":   //修改个人信息
                    break;
                case "ediget":  //注册账号
                    job.put("ediget","1");
                    break;
                case "updataPassWord":  //修改密码
                    job.put("updataPassword","1");
                    break;
                case "upHeadPortrait":  //修改头像
                    job.put("upHeadPortrait","1");
                    break;
                case "publishStall":    //停车位信息插入
                    break;
                default:
                    break;
            }
            if (job != null) {
                job.put("type","updata");
                jstr = job.toJSONString();
                Log.i(TAG,"编码成功，编码结果为："+jstr);
            }
        }else{
            jstr = JSON.toJSONString(source);
            Log.i(TAG,"检查到已经是目标类型，直接编码，编码结果为："+jstr);
        }
        return jstr;
    }

    /**
     * 注册账号
     * @param phoneNum  用户手机号
     * @param password  密码
     * @return  是否注册成功
     */
    public boolean Register(String phoneNum,String password){
        UserOrdinary uo = new UserOrdinary();
        uo.setPhoneNum(Integer.parseInt(phoneNum));
        uo.setPassword(password);


        String Msend = updata("ediget",uo);
        sendMessage(Msend);
        String reply = receiveMessage();    //获取服务器返回信息
        Log.i("Connect","message received"+reply);
        JSONObject replyjob = null;
        if(reply!=null && !reply.equals("")){
            replyjob = JSONObject.parseObject(reply);
        }
        if(replyjob!=null && replyjob.get("sign")!=null && replyjob.get("sign").toString().equals("default")){
            MainActivity.logflag=false;
            return false;
        }else{
            Log.i("Connect","register successful");
            Log.i(TAG,"reply:"+reply);
            UserOrdinary replyuo = JSON.parseObject(reply,UserOrdinary.class);    //将服务器返回的消息解码为user类

            MainActivity.logflag = true;        //设置当前登录状态
            editor_user.putBoolean("logflag",true);
            editor_user.putInt("PhoneNum",replyuo.getPhoneNum());     //保存登录信息，只保存登陆名和密码，下次开启app重新登陆
            editor_user.putString("Password",replyuo.getPassword());
            editor_user.putString("userinfo",reply);
            editor_user.apply();

            return true;
        }
    }

    /**
     * 修改密码
     * @param newPassword 新密码
     * @return 修改成功返回true
     */
    public boolean ChangePassword(String newPassword){      //必须要有type:updata,updataPassword:1,name:账号，newUserMessage：新密码
        JSONObject uo = new JSONObject();
        uo.put("name",String.valueOf(sp_user.getInt("PhoneNum",123)));      //获取保存在本地的账户名
        uo.put("newPassword",newPassword);
        uo.put("type","updata");
        uo.put("updataPassword","1");
        String Msend = updata("updataPassword",uo);     //将jsonObject转换为String

        this.sendMessage(Msend);
        String reply = this.receiveMessage();    //获取服务器返回信息
        Log.i("Connect","message received"+reply);
        JSONObject replyjob = null;
        if(reply!=null && !reply.equals("")){
            replyjob = JSONObject.parseObject(reply);
        }
        if(replyjob!=null && replyjob.get("sign")!=null && replyjob.get("sign").toString().equals("default")){
            MainActivity.logflag=false;
            return false;
        }else if(replyjob!=null && replyjob.get("sign")!=null && replyjob.get("sign").toString().equals("success")){
            Log.i("Connect", "Changed Password successful");
            Log.i(TAG, "reply:" + reply);

            //保存修改后的密码
            editor_user.putString("PassWord",newPassword);
            String jstr = sp_user.getString("userinfo","");
            JSONObject job = JSONObject.parseObject(jstr);
            job.put("Password",newPassword);
            jstr = job.toJSONString();
            editor_user.putString("userinfo",jstr);
            return true;
        }
        return false;
    }

    /**
     * 关闭连接
     */
    public void CloseConnect(){
        try {
            if(din != null){
                din.close();
            }
            if(dout != null){
                dout.close();
            }
            if(sc != null) {
                sc.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        Log.i(TAG,"关闭连接");
    }

}
