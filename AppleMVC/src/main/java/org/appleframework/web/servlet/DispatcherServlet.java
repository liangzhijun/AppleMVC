 package org.appleframework.web.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet
{
	//初始化时用map储存所有cnotroller控制器的实例（单例）
	private Map<String, Object> attributesMap;
	//初始化时用map储存路由配置文档路由记录
	private Map<String, Route> routeMap;
	
	@Override
	public void init()
	{
		attributesMap = new HashMap<String, Object>();
		
		routeMap = new HashMap<String, Route>();
		
		try
		{
			//解析路由配置文档（router-cfg.xml），在配置文档里做请求的映射（不使用注释的方法）
			RouterCfgDomParse.domParse(routeMap, attributesMap, "cfg/router-cfg.xml");
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
	 * @see #
	 */
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
	
	//通过利用反射为调用controller实例进行请求映射
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException 
	{
		String requestUri = request.getRequestURI();
		
		//取得controller实例名
		String className = routeMap.get(requestUri).className;
		
		//取得controller实例
		Object controller = attributesMap.get(className);
		
		//取得controller实例将要调用的方法名
		String methodName = routeMap.get(requestUri).methodName;
		
		//取得controller实例将要调用方法的返回（输出）类型
		String outputType = routeMap.get(requestUri).outputType;
		
		Class<?> classType = controller.getClass();
		Method invokeMethod = null;
		try
		{
			invokeMethod = classType.getMethod(methodName, new Class[]{HttpServletRequest.class, HttpServletResponse.class});
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		
		Object forwardPath = null;
		try
		{
			
			forwardPath = invokeMethod.invoke(controller, request, response);
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		
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

