package com.runoob.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * ������������
 * @author ����
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
			serverSocket=new ServerSocket(port);  //�Զ˿�5000�����������׽���
			System.out.println("�������������ȴ��ͻ������ӡ���������");
			socket=serverSocket.accept();//��ȡ�ͻ�������
			in=socket.getInputStream();//���տͻ��˷��͵�����
			byte[] b=new byte[1024];
			in.read(b);
			System.out.println("�ͻ��˴��͵������ǣ�"+new String(b));
			out=socket.getOutputStream();     //������Ϣ���ͻ���
			out.write("���Ƿ��������Ҹ��㷢��Ϣ��".getBytes());
			in.close();//�ر���
			out.close();
			socket.close();
			serverSocket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
