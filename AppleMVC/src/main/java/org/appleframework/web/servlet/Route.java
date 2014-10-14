package org.appleframework.web.servlet;

/*存放路由配置文档（router-cfg.xml）数据（路由记录）的实体类*/
public class Route
{
	String path;
	
	String className;
	
	String methodName;
	
	String outputType;
	
	public Route(String path, String className, String methodName, String outputType)
	{
		this.path = path;
		this.className = className;
		this.methodName = methodName;
		this.outputType = outputType;
	}
}
