package dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

	
	static String driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";//����������ʽ
	static String uri="jdbc:sqlserver://localhost:1433;databaseName=teaching";//����λ��
	static String name="sa";//���ݿ��û���
	static String pass="123456";//���ݿ��½����
	private static Connection con = null;//���Ӷ���
	static Statement stmt=null;//�����
	static ResultSet rs=null;//�����¼��
	
	private DBUtil(){};
	/*
	public static void qudon(){
		try {
			//ע������
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("ע������ʧ��!");
		}
	}
	*/
	public static Connection getCon(){
		if(con==null){
			try {
				Class.forName(driver);
				con=DriverManager.getConnection(uri,name,pass);
				System.out.println("�ɹ�");
				stmt=con.createStatement();//����һ�������
			//	findAll();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
		return con;
	}
	
	
	//���Բ�ѯ���ݿ�����
	public static void findAll() {
		try {
			String strSql="select * from car";//������ѯ����ַ���
			rs=stmt.executeQuery(strSql);//ʹ�û�����������ݿ����
			while(rs.next()) {//�жϲ�ѯ��һ��
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
