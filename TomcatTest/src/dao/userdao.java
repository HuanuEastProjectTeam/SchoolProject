package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dbutil.DBUtil;

public class userdao {
 
	                          //1，123456
	public  Userbean queryuser(String userid,String userpwd) throws SQLException {
		Statement stmt=null;
		Userbean ubean=new Userbean();
		Connection conn=DBUtil.getCon();//返回Connection对象，建立链接数据库成功
		
		PreparedStatement ps = conn.prepareStatement("select * from car where userid=? and userpwd=?");
		ps.setString(1, userid);
		ps.setString(2, userpwd);
		ResultSet rs = ps.executeQuery();
	/*	String sql="select * from car";
	    stmt=conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		*/
		while(rs.next()) {
			
		    ubean.setUid(rs.getString("userid"));
		    ubean.setUpwd(rs.getString("userpwd"));
		    ubean.setUname(rs.getString("username"));
		    ubean.setMoney(rs.getDouble("umoney"));
		    //显示是否连接成功
		    System.out.println(rs.getString(1));
		    System.out.println(rs.getString(2));
		}
		
			
		return ubean;
		
		
		
	}
	
	
	
	
}
