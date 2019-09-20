package com.runoob.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import dao.Userbean;
import dao.userdao;

/**
 * Servlet implementation class HelloServlet
 */
@WebServlet("/HelloServlet")
public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HelloServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("UTF-8");//ʹ��GBK��������������ʾ
		response.setContentType("text/html;charset=utf-8");//�������
	//	response.getWriter().write("ʵ���è��ʾ<br/>");
	//	response.getWriter().append("Served at: ").append(request.getContextPath());
		//�������ݿ�����
	/*	
		userdao dao = new userdao();
		try {
			Userbean ubean = dao.queryuser("1", "123456");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		PrintWriter out = response.getWriter();
		//String loginame = request.getParameter("loginname").trim();
		//String password = request.getParameter("password").trim();
		String loginame = getNewString(request.getParameter("loginname"));//��ȡweb�������Ϣ��������������ʾ����
		String password = getNewString(request.getParameter("password"));
		userdao dao = new userdao();//����userdao����
		//out.println("loginname:" + loginame + "     " + "password:" + password+"<br/>");
		try {

			Userbean ubean = dao.queryuser(loginame, password);//����ע���������ݿⷽ��
			response.getWriter().write(ubean.getUid());//ҳ��������
			System.out.println(ubean.getUid());//����̨������

			if (ubean.getUid() != null) {
				JsonObject js = new JsonObject();
				js.addProperty("flag", true);
				js.addProperty("uid", ubean.getUid());
				js.addProperty("upwd", ubean.getUpwd());
				js.addProperty("uname", ubean.getUname());
				js.addProperty("umoney", ubean.getMoney());
				out.println(js);
				out.write("�������");
			
			} else {
				out.println("can not login!");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		out.flush();
		out.close();

		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	//ת��Ϊ���ı���������ʾ
	 public static String getNewString(String str) throws UnsupportedEncodingException {	       
		 
		 return new String(str.getBytes("ISO-8859-1"),"UTF-8");	    
		 }
	
	

}
