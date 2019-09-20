package com.runoob.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * 建立服务器端
 * @author 邹荣
 *
 */
public class fuwuqiduan_serversocket {
	public static void main(String[] args) {
		ServerSocket serverSocket=null;
		Socket socket=null;
		InputStream in=null;
		OutputStream out=null;
		int port=5000;
		try {
			serverSocket=new ServerSocket(port);  //以端口5000建立服务器套接字
			System.out.println("服务器开启，等待客户端连接·····");
			socket=serverSocket.accept();//获取客户端连接
			in=socket.getInputStream();//接收客户端发送的内容
			byte[] b=new byte[1024];
			in.read(b);
			System.out.println("客户端传送的数据是："+new String(b));
			out=socket.getOutputStream();     //反馈信息给客户端
			out.write("我是服务器，我给你发信息了".getBytes());
			in.close();//关闭流
			out.close();
			socket.close();
			serverSocket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
