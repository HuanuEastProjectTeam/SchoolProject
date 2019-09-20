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
		response.setCharacterEncoding("UTF-8");//使用GBK设置中文正常显示
		response.setContentType("text/html;charset=utf-8");//解决乱码
	//	response.getWriter().write("实验丑猫显示<br/>");
	//	response.getWriter().append("Served at: ").append(request.getContextPath());
		//测试数据库链接
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
		String loginame = getNewString(request.getParameter("loginname"));//获取web传入的信息并调用了中文显示方法
		String password = getNewString(request.getParameter("password"));
		userdao dao = new userdao();//创建userdao对象
		//out.println("loginname:" + loginame + "     " + "password:" + password+"<br/>");
		try {

			Userbean ubean = dao.queryuser(loginame, password);//调用注册链接数据库方法
			response.getWriter().write(ubean.getUid());//页面输出编号
			System.out.println(ubean.getUid());//控制台输出编号

			if (ubean.getUid() != null) {
				JsonObject js = new JsonObject();
				js.addProperty("flag", true);
				js.addProperty("uid", ubean.getUid());
				js.addProperty("upwd", ubean.getUpwd());
				js.addProperty("uname", ubean.getUname());
				js.addProperty("umoney", ubean.getMoney());
				out.println(js);
				out.write("有这个人");
			
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
	
	//转化为中文编码正常显示
	 public static String getNewString(String str) throws UnsupportedEncodingException {	       
		 
		 return new String(str.getBytes("ISO-8859-1"),"UTF-8");	    
		 }
	
	

}
