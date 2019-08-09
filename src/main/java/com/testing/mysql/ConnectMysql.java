package com.testing.mysql;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectMysql {
	//和数据库之间建立连接的对象。
	public Connection conn;

	// 数据库连接的地址、用户名和密码
	// ?之前是数据库的地址端口和数据库名，？后面的是连接的参数。
	public ConnectMysql() {
		try {
			//从properties文件中读取数据库地址、用户名和密码。
			FileInputStream fis = new FileInputStream(this.getClass().getResource("").getPath() + "/inter.properties");
			Properties prop = new Properties();
			//将文件中的内容读取到propertie设置对象中。
			prop.load(fis);
			//从properties中读取对应的选项内容。
			String dbUrl = prop.getProperty("DBURL");
			String dbUser = prop.getProperty("DBUSER");
			String dbPwd = prop.getProperty("DBPWD");
			//连接数据库的操作。
			//加载com.mysql.jdbc.Driver类。
			Class.forName("com.mysql.cj.jdbc.Driver");
			//使用drivermanager建立连接。
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
			// 设置超时时间
			DriverManager.setLoginTimeout(10);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
