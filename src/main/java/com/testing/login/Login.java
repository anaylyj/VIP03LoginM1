package com.testing.login;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.testing.mysql.ConnectMysql;
import com.testing.mysql.UseMysql;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	// 构造方法
	public Login() {
		// 调用父类构造方法
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 设置返回内容的编码格式
		response.setContentType("text/html;charset=UTF-8");
		// 请求参数的编码
		request.setCharacterEncoding("UTF-8");
		// 从请求中获取loginName和passWord参数。
		String user = request.getParameter("loginName");
		String pwd = request.getParameter("passWord");
		// 调用login的方法进行登录验证
//		LoginSample loginClass = new LoginSample();
//		boolean loginResult = loginClass.login(user, pwd);
		ConnectMysql conSql =new ConnectMysql();
		UseMysql mySql=new UseMysql(conSql.conn);
		boolean loginResult=mySql.Login(user, pwd);
		String responseResult = "{";
		if (loginResult) {
			responseResult += "\"msg\":\"恭喜您，登录成功!\"}";
		} else {
			responseResult += "\"msg\":\"登录失败，用户名密码错误！\"}";
		}
		System.out.println(responseResult);
		response.getWriter().append(responseResult);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 *      request 和 reponse 引用指向实现了HttpServletRequest和HttpServletResponse接口的Request和Response类的对象
	 *      而Request和Response类的对象是由tomcat容器创建的
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8");
		// 请求参数的编码  重写此请求主体中使用的字符编码的名称。必须在读取请求参数或使用getReader()读取输入之前调用此方法。否则，它没有任何效果。
		request.setCharacterEncoding("UTF-8");
		// 创建或获取请求对应的session,并获取对应的sessionId
        //具体如何创建session,如何生成sessionId,这些是在tomcat容器中实现的
		String sessionId = request.getSession().getId();
		System.out.println("sessionId是：" + sessionId);
		//设置session有效期
		//request.getSession().setMaxInactiveInterval(20);
		
		System.out.println("session创建时间："+request.getSession().getCreationTime());
		//默认的Session超时时间为1800s,即30 min
		System.out.println("session超时时间："+request.getSession().getMaxInactiveInterval());
		System.out.println("session最后一次访问时间："+request.getSession().getLastAccessedTime());
		// 从请求中获取loginName和passWord参数。
		String user = request.getParameter("loginName");
		String pwd = request.getParameter("passWord");
		String responseResult = "{";
		String regex = "[^0-9a-zA-Z_]";
		Pattern p = Pattern.compile(regex);
		Matcher mu = p.matcher(user);
		Matcher mp = p.matcher(pwd);

		if (user != null && pwd != null && !user.equals("") && !pwd.equals("")) {
			if (user.length() > 2 && user.length() <16 && pwd.length() > 2 && pwd.length() <16) {
				if (!mu.find() && !mp.find()) {
					if (request.getSession().getAttribute("loginName") == null) {
						ConnectMysql conSql = new ConnectMysql();
						UseMysql mySql = new UseMysql(conSql.conn);
						boolean loginResult = mySql.PLogin(user, pwd);
						if (loginResult) {
							responseResult += "\"msg\":\"恭喜您，登录成功!\"}";
							//返回给客户端一个cookie，记录的是本次的sessionID（也就是房间号），
							// 名称和servlet默认返回的cookie名一致
//							Cookie ssID=new Cookie("JSessionID",sessionId);
//							ssID.setMaxAge(20);
//							response.addCookie(ssID);
							// 如果登录成功通过了校验，就在服务端记录用户名的信息
							request.getSession().setAttribute("loginName", user);
						} else {
							responseResult += "\"msg\":\"登录失败，用户名密码错误！\"}";
						}
					} // 如果session有记录，分为两种情况：第一种 同一个用户第二次登录；第二种 不同用户登录
					else {
						if (request.getSession().getAttribute("loginName").equals(user)) {
							responseResult += "\"msg\":\"用户已登录，无法再次登录！\"}";
						} else {
							responseResult += "\"msg\":\"已经有其他用户登录，无法再次登录！\"}";
						}

					}

				
				  } // 用户名密码有特殊字符处理 
				else { responseResult += "\"msg\":\"输入有误，不能包含特殊字符！\"}"; }
				 

			} // 用户名密码长度过长过短处理
			else {
				responseResult += "\"msg\":\"输入有误，用户名密码必须3到15位！\"}";
			}

		} // 用户名密码为空处理
		else {
			responseResult += "\"msg\":\"输入有误，用户名密码不能为空！\"}";
		}

		System.out.println(responseResult);
		response.getWriter().append(responseResult);
	}

}
