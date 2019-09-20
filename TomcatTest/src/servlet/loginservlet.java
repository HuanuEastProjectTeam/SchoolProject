package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import dao.Userbean;
import dao.userdao;
import dbutil.DBUtil;

public class loginservlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();

		String loginame = req.getParameter("loginname").trim();
		String password = req.getParameter("password").trim();
		userdao dao = new userdao();
		System.out.println("loginname:" + loginame + "  " + "password:" + password);
		try {

			Userbean ubean = dao.queryuser(loginame, password);
			System.out.println(ubean.getUid());

			if (ubean.getUid() != null) {
				JsonObject js = new JsonObject();
				js.addProperty("flag", true);
				js.addProperty("uid", ubean.getUid());
				js.addProperty("upwd", ubean.getUpwd());
				js.addProperty("uname", ubean.getUname());
				js.addProperty("umoney", ubean.getMoney());
				out.println(js);
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

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
