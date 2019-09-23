package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * 建立与服务器端的连接工具类
 */
public class socketUtils extends Socket {
    private Socket socket=null;//初始化Socket对象
    private int port=6000;//服务器端口
    private String serverSockIP="127.0.0.1";//服务器端的ip地址
    private ObjectInputStream objectInputStream=null;//初始化对象输入流
    public socketUtils(){//构造方法
        try {
            socket=new Socket(serverSockIP,port);//建立连接
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  构建对象输入流方法
     * @return 返回接收的对象数据
     */
    public Object shuRuInputStream(){
        Object object= null;//读取输入流中的对象数据
        try {
            objectInputStream=new ObjectInputStream(socket.getInputStream());//接入对象输入流
            object = objectInputStream.readObject();//读取输入流对象
            if(object!=null){//判断读取的对象不为空
                return object;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally{
            try {
                objectInputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return object;
    }

}
