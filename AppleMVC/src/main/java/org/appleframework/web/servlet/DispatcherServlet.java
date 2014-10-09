 package org.appleframework.web.servlet;

import java.io.IOException;
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
	//用map储存所有请求执行方法名的内容。key为请求地址、请求执行的方法名为value;
	private Map<String, String> methodNameMap;
	//用map储存所有请求所调用的控制器类名。请求地址为key、请求执行的方法名为value;
	private Map<String, String> classNameMap;
	private Map<String, Object> attributesMap;
	
	@Override
	public void init()
	{
		methodNameMap = new HashMap<String, String>();
		classNameMap = new HashMap<String, String>();
		attributesMap = new HashMap<String, Object>();
		
		try
		{
			RouterCfgDomParse.domParse(methodNameMap, classNameMap, attributesMap, "cfg/router-cfg.xml");
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
	
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException 
	{
		String requestUri = request.getRequestURI();
		
		String methodName = methodNameMap.get(requestUri);
		String className = classNameMap.get(requestUri);
		Object contrller = attributesMap.get(className);
		
		Class<?> classType = contrller.getClass();
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
			forwardPath = invokeMethod.invoke(contrller, request, response);
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
		
		RequestDispatcher rd = request.getRequestDispatcher((String)forwardPath);
		
		rd.forward(request, response);
	}
	
}

