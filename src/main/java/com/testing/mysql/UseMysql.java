package com.testing.mysql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UseMysql {
	// 数据库连接对象
	private Connection ct;

	// 构造函数中实例化这个数据库连接对象成员变量，与建立连接的类一起使用
	public UseMysql(Connection conn) {
		ct = conn;
	}

	/**
	 * 获取用户信息的方法，同学们可以参考这个方法去获取数据库中的数据
	 * 
	 * @param name登录用户名
	 * @return 返回查询信息的结果集合
	 */
	public Map<String, String> getUserInfo(String name) {
		String sql = "SELECT * FROM userinfo where username='" + name + "';";
		System.out.println(sql);
		// 保存查询结果的集合
		ResultSet rs = null;
		// 声明statement对象，通过这个对象查询数据库
		Statement sm;
		try {
			// 通过数据库连接实例化statement对象
			sm = ct.createStatement();
			// 执行查询
			rs = sm.executeQuery(sql);
			// 创建map存储表中信息
			Map<String, String> map = new HashMap<String, String>();
			//设置读取结果的循环控制变量，代表获取的数据的行数
			/* rs!=null说明sql语句执行查找成功，有内容返回。
			 * rs.next()代表着集合当中还有下一个元素（一行的数据），并且读取该行的值。
			 * 如果sql查询不止一条语句，则可以用while循环取这些值
			 */
			if (rs != null && rs.next()) {
				// 元组数据代表数据库查询结果中的一行,通过rsmd来获取数据的列数
				ResultSetMetaData rsmd = rs.getMetaData();
				// 注意sql中的列从1开始，遍历一行数据中的每列内容，并以键值对形式存储到map中去
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					//展示的信息去除密码和用户名
					if (!(rsmd.getColumnName(i).equals("pwd") || rsmd.getColumnName(i).equals("username")))
						// 将每一列的名称和数据作为键值对存放到map当中，将行数拼接到键里
						map.put(rsmd.getColumnName(i), rs.getString(i));
				}
				System.out.println(map.toString());
			}
			// 关闭statement对象和查询结果集合对象，释放资源
			sm.close();
			rs.close();
			return map;
		} catch (SQLException e) {
		}
		return null;
	}

	/**
	 * 通过查询语句验证登录结果的登录方法，如果能够查询到结果，则说明登录成功
	 * 
	 * @param 登录用户名name
	 * @param 登录密码password
	 * @return 返回登录是否成功的布尔值，成功为true，失败为false
	 */
	public boolean Login(String name, String pwd) {
		//拼接通过输入的用户名和密码查询数据库的sql语句
		String sql = "SELECT * FROM userinfo where username='" + name + "'AND pwd='"+pwd+"';";
		System.out.println(sql);
		// 保存结果集
		ResultSet rs =null;
		// 声明statement对象，通过这个对象查询数据库
		Statement sm;
		try {
			// 通过数据库连接实例化statement对象
			sm = ct.createStatement();
			// 执行查询
			rs = sm.executeQuery(sql);
			//
			List<HashMap<String,String>> rslist=new ArrayList<HashMap<String,String>>();
			
			// rs!=null说明sql语句执行查找成功，有内容返回，rs.next()代表着集合当中还有下一个元素（一行的数据），并且读取该行的值。
			while (rs != null && rs.next()) {
				// 元组数据代表数据库查询结果中的一行。
				ResultSetMetaData rsmd = rs.getMetaData();
				//创建一个map用于存储每一行数据中对应的内容。
				HashMap<String, String> map = new HashMap<String, String>();
				// 注意sql中的列从1开始，遍历一行数据中的每列内容，并以键值对形式存储到map中去
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					// 从第一列开始遍历一行数据中的每一列，将对应的键值对存储到map当中
					map.put(rsmd.getColumnName(i), rs.getString(i));
				}
				rslist.add(map);
//				// 关闭statement对象和查询结果集合对象，释放资源
//				sm.close();
//				rs.close();
//				// 如果查询结果不为空，就返回登录成功
//				return true;
			}
			//如果查询结果为空，也要关闭对象释放资源
			System.out.println(rslist.toString());
			sm.close();
			rs.close();
			if(!rslist.isEmpty()) {
				return true;
			}
		} catch (SQLException e) {
		}
		//try建立查询失败或者查询结果为空都会执行到这里，返回false
		return false;
	}

	/**
	 * 通过存储过程验证登录结果的登录方法，如果能够查询到结果，则说明登录成功 
	 * 使用存储过程进行验证时，sql语句不再重新编译，可以防止sql注入
	 * 
	 * @param 登录用户名name
	 * @param 登录密码password
	 * @return 返回登录是否成功的布尔值，成功为true，失败为false
	 */
	public boolean PLogin(String name, String pwd) {
		try {
			// 创建调用存储过程的对象，参数用？号代替，不要直接传递参数
			CallableStatement cm = ct.prepareCall("{call login(?,?)}");
			// 通过set方法传递存储过程用到的参数，1和2代表第几个参数，name和pwd代表参数值
			cm.setString(1, name);
			// cm.setInt(1, 1);
			cm.setString(2, pwd);
			// 获取查询结果
			ResultSet rs = cm.executeQuery();
			// 处理查询结果，与之前的方法一致，不再重复添加注释了
			if (rs != null && rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				HashMap<String, String> map = new HashMap<String, String>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					map.put(rsmd.getColumnName(i), rs.getString(i));
				}
				System.out.println(map.toString());
				cm.close();
				rs.close();
				return true;
			}
			cm.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return false;
	}
}
