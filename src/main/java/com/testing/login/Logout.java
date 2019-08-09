package com.testing.login;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8");
		// 收到的参数编码
		request.setCharacterEncoding("UTF-8");
		//使当前session过期失效
		if(request.getSession().getAttribute("loginName")!=null){
			//通过让session失效，达到注销的目的。
			request.getSession().invalidate();
			response.getWriter().append("{\"status\":0,\"msg\":\"注销成功!\"}");
		}else{
			request.getSession().invalidate();
			response.getWriter().append("{\"status\":0,\"msg\":\"您还没有登录！\"}");
		}
//		通过sendRedirect进行重定向。
//response.sendRedirect("index.html");
		
	}

}
