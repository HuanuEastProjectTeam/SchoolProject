package dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

	
	static String driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";//驱动链接形式
	static String uri="jdbc:sqlserver://localhost:1433;databaseName=teaching";//链接位置
	static String name="sa";//数据库用户名
	static String pass="123456";//数据库登陆密码
	private static Connection con = null;//连接对象
	static Statement stmt=null;//活动对象
	static ResultSet rs=null;//结果记录集
	
	private DBUtil(){};
	/*
	public static void qudon(){
		try {
			//注册驱动
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("注册驱动失败!");
		}
	}
	*/
	public static Connection getCon(){
		if(con==null){
			try {
				Class.forName(driver);
				con=DriverManager.getConnection(uri,name,pass);
				System.out.println("成功");
				stmt=con.createStatement();//创建一个活动对象
			//	findAll();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
		return con;
	}
	
	
	//测试查询数据库数据
	public static void findAll() {
		try {
			String strSql="select * from car";//建立查询语句字符串
			rs=stmt.executeQuery(strSql);//使用活动对象运行数据库语句
			while(rs.next()) {//判断查询下一条
				int id=rs.getInt(1);
				String name=rs.getString(2);
				String pass=rs.getString(3);
				System.out.println(id+" "+name+"  "+pass);
			}
			
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	
	
	public static void closeCon(){
		if(con!=null){
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
}
