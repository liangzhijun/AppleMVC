package org.appleframework.web.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet 
{
	//初始化时用List储存路由配置文档路由记录
	private List<Route> routeList;

	@Override
	public void init()
	{
		routeList = new ArrayList<Route>();		
		
		try
		{
			//解析路由配置文档（router-cfg.xml），在配置文档里做请求的映射（不使用注释的方法）
			RouterCfgDomParse.domParse(routeList, "cfg/router-cfg.xml");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Delegate GET requests to doService.
	 * @see #
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doService(request, response);
	}

	/**
	 * Delegate POST requests to {@link #doService}.
	 * @see #
	 */
	@Override
	protected final void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doService(request, response);
	}

	/**
	 * Delegate POST requests to {@link #doService}.
	 * @see #
	 */
	@Override
	protected final void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doService(request, response);
	}

	/**
	 * Delegate POST requests to {@link #doService}.
	 * @see #
	 */
	@Override
	protected final void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        	
		doService(request, response);
	}

	/**
	 * Delegate POST requests to {@link #doService}.
	 * @see #	 */
	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doService(request, response);
	}
	
	/**
	 * Delegate POST requests to {@link #doService}.
	 * @see #
	 */
	@Override
	protected void doTrace(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doService(request, response);
	}

	//通过利用反射为调用contrller实例进行请求映射
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException 
	{
		String requestUri = request.getRequestURI();

		//取得controller实例将要调用方法的返回（输出）类型
		String outputType = null;
		
		for(Route r : routeList)
		{
			if(r.path.equals(requestUri))
			{
				outputType = r.outputType;
				break;
			}
		}

		Object forwardPath = null;

		
		/*begin-------route code-------*/

		if("/AppleMVC/signIn.action".equals(requestUri))
        {
			com.example.controller.Controller controller = new com.example.controller.Controller();

			forwardPath = controller.login(request, response);
        }
		else if("/AppleMVC/exit.action".equals(requestUri))
        {
			com.example.controller.Controller controller = new com.example.controller.Controller();

			forwardPath = controller.exit(request, response);
        }
		else if("/AppleMVC/getText.action".equals(requestUri))
        {
			com.example.controller.Controller controller = new com.example.controller.Controller();

			forwardPath = controller.getText(request, response);
        }
		else if("/AppleMVC/getImage.action".equals(requestUri))
        {
			com.example.controller.Controller controller = new com.example.controller.Controller();

			forwardPath = controller.getImage(request, response);
        }
		else if("/AppleMVC/getFile.action".equals(requestUri))
        {
			com.example.controller.Controller controller = new com.example.controller.Controller();

			forwardPath = controller.getFile(request, response);
        }

		

		/*-------route code-------end*/
		
		
		if(outputType.equals("view"))
		{
			RequestDispatcher rd = request.getRequestDispatcher((String)forwardPath);

			rd.forward(request, response);
		}
		else if(outputType.equals("text"))
		{
			PrintWriter out = response.getWriter();

			out.println(forwardPath);

			out.flush();
		}
		else if(outputType.equals("image"))
		{
			FileInputStream is = new FileInputStream((String)forwardPath);
			response.setContentType("image/*");   
			response.setHeader("Content-Disposition", "attachment; filename=\"photo.jpg\"");
		    OutputStream os = response.getOutputStream();  

		    byte[] b = new byte[1024];
		 	while (is.read(b) != -1)
		 	{
		 		os.write(b);
		 	}

		    os.flush();
		    os.close();
	    is.close();
		}
		else if(outputType.equals("file"))
		{
			FileInputStream is = new FileInputStream((String)forwardPath);
			response.setContentType("file/*");
			response.setHeader("Content-Disposition", "attachment; filename=\"file.txt\"");
		    OutputStream os = response.getOutputStream();  

		    byte[] b = new byte[1024];
		 	while (is.read(b) != -1)
		 	{
		 		os.write(b);
		 	}

		    os.flush();
		    os.close();
		    is.close();
		}
	}
}
