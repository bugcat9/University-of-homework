package DataServer;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SqlConnection {
	static String JDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";//设置SQL Server数据库引擎
	//static String connectDB = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=CompanyData;?useUnicode=true&characterEncoding=utf-8";//指定数据库
	static String connectDB ="jdbc:sqlserver://database-1.cc9exdqy4dd7.us-east-1.rds.amazonaws.com:1433;DatabaseName=CompanyData";
	static String user = "admin";	//用户
	static String password = "zn19980921";//登陆密码
	 Connection con;//连接
	 PreparedStatement sqlday ;
	 
	 static String []columns = {" [开盘价(元)] ","[收盘价(元)]","[换手率(%)]","[总市值(元)]"};
	 
	 public SqlConnection() {
		 openSqlConnection();
	 }
	 
	/**
	 *连接数据库
	 */
	public  void openSqlConnection() {
		try {
			Class.forName(JDriver);//加载数据库引擎
			 con = DriverManager.getConnection(connectDB, user, password);//连接数据库
			System.out.println("连接数据库成功");
			 sqlday = con.prepareStatement("select  ?  from dbo.上证某公司股市数据 "
					+ "where year([日期]) =year(?) and month ([日期]) = month(?) and day([日期]) = day(?)");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 关闭数据库
	 */
	public  void closeSqlConnection() {
		if(con!=null) {
			try {
				sqlday.close();
				sqlday=null;
				
				con.close();
				con=null;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 查询某天的数据
	 * @param index
	 * @param date
	 * @return
	 */
	public String Select_day(int index,String date) {
		Statement statement;
		String res=null;
		try {
			
			statement = con.createStatement();
	
			String str =String.format("select  %s from dbo.上证某公司股市数据 ",columns[index])
					+ String.format("where year([日期]) =year('%s') and month ([日期]) = month('%s') and day([日期]) = day('%s')",date,date,date);
			
			ResultSet resultSet = statement.executeQuery(str);
			
			while(resultSet.next()) {
				//打印
				res =resultSet.getString(1);
				System.out.println(res);
			}
		
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * 查询某月的数据
	 * @param index
	 * @param date
	 * @return
	 */
	public String Select_month(int index,String date) {
		Statement statement;
		 StringBuilder res = new StringBuilder();
		try {
			
			statement = con.createStatement();
			//查询开盘价
			String str =String.format("select  %s from dbo.上证某公司股市数据 ",columns[index])
					+ String.format("where year([日期]) =year('%s') and month ([日期]) = month('%s') ",date,date);
			
			ResultSet resultSet = statement.executeQuery(str);
			while(resultSet.next()) {
				//打印
				String m = resultSet.getString(1);
				System.out.println(m);
				res.append(m+",");
			}
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res.toString();
	}
	
	
	public static void main(String[] args) {
		SqlConnection connection = new SqlConnection();
		String date = "2000-05-10";
		String msg=connection.Select_month(0,date);
		System.out.println();
		connection.Select_month(1,date);
		System.out.println();
		connection.Select_month(2,date);
		System.out.println();
		connection.Select_month(3,date);
		//System.out.println(msg);
		
		
		connection.closeSqlConnection();
		
	}
	
	
}
