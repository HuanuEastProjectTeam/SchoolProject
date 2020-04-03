package com.example.andriodcar;

import com.alibaba.fastjson.JSON;
import com.example.andriodcar.Bean.NewMessage;
import com.example.andriodcar.Bean.PakingSpace;
import com.example.andriodcar.Bean.ParkingOrder;
import com.example.andriodcar.Bean.ResidenialQuarter;
import com.example.andriodcar.Bean.UserOrdinary;
import com.example.andriodcar.Bean.Userbean;
import com.example.andriodcar.Bean.Wallet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Connect {

    /**
     * 2020-4-3 袁铨：所有方法都需要重写
     */

    private Socket socket;
    private DataInputStream instr;
    private DataOutputStream outstr;

    /**
    * 初始化socket
    * @param host 服务器端ip
    * @param port 服务器端监听的端口
    */

    public void InitSocket(String host,int port){
        try {
            socket = new Socket(host, port);
            instr = new DataInputStream(socket.getInputStream());
            outstr = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //关闭socket
    public void CloseSocket(){
        try {
            socket.close();
            instr.close();
            outstr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 将目标javabean编码为fastjson支持的string
     * @param source 目标javabean对象
     * @return 返回编码结束的fastjson-String
     */
    public String Encode(Object source){
        String result = null;
        result = JSON.toJSONString(source);
        return result;
    }

    /**
     * 将服务器端发送来的string解码为javabean对象
     * @param source 服务端传回的string类型的数据
     * @return 返回一个javabean类型
     */
    public Object Decode(String source,String type){
        Object result = null;
        switch (type){
            case "NewMessage": {
                result = JSON.parseObject(source, NewMessage.class);
                break;
            }
            case "PakingSpace":{
                result = JSON.parseObject(source, PakingSpace.class);
                break;
            }
            case "ParkingOrder":{
                result = JSON.parseObject(source, ParkingOrder.class);
                break;
            }
            case "ResidenialQuarter":{
                result = JSON.parseObject(source, ResidenialQuarter.class);
                break;
            }
            case "UserBean":{
                result = JSON.parseObject(source, Userbean.class);
                break;
            }
            case "UserOrdinary":{
                result = JSON.parseObject(source, UserOrdinary.class);
                break;
            }
            case "Wallet":{
                result = JSON.parseObject(source, Wallet.class);
                break;
            }
            default:
                break;
        }
        return result;
    }
}
