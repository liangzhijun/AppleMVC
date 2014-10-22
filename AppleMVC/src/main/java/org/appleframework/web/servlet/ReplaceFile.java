package org.appleframework.web.servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReplaceFile
{
	public static void main(String[] args) throws Exception
	{
		//初始化时用map储存路由配置文档路由记录
        List<Route> routeList = new ArrayList<Route>();		
        
        try
		{
			//解析路由配置文档（router-cfg.xml），在配置文档里做请求的映射（不使用注释的方法）
			RouterCfgDomParse.domParse(routeList, new HashMap<String, Object>(), "cfg/router-cfg.xml");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		 String routeCode =
				 	"/*begin-------route code-------*/\n" +
				 	"\n" +
			        "		if(\""+ routeList.get(0).path + "\".equals(requestUri))\n" +
			        "        {\n" +
			        "			forwardPath = ((" +  routeList.get(0).className + ")controller)"  + "." +  routeList.get(0).methodName + "(request, response);\n" +	      	    		
			        "        }\n" ;
			        
			        for(int i = 1; i < routeList.size(); i++)
			        {
			        	String path = routeList.get(i).path;
			        	//取得controller实例名
			    		String className = routeList.get(i).className;
			    		
			    		//取得controller实例将要调用的方法名
			    		String methodName = routeList.get(i).methodName;
			    		
			    		routeCode += 
			    		"		else if(\""+ path + "\".equals(requestUri))\n" +
			    		"        {\n" +
			    		"			forwardPath = ((" + className + ")controller)."+ methodName + "(request, response);\n" +      	    		
			    		"        }\n";
			        }
			        
        
       String fileName = "src\\main\\java\\org\\appleframework\\web\\servlet\\DispatcherTemplate.java";
       
       File file = new File(fileName);
       
       try 
       {
           FileReader fis = new FileReader(file);// 创建文件输入流
           char[] data = new char[1024];// 创建缓冲字符数组
           
           int rn = 0;
           StringBuilder sb = new StringBuilder();// 创建字符串构建器
           
           while ((rn = fis.read(data)) > 0) // 读取文件内容到字符串构建器
           {
               String str = String.valueOf(data, 0, rn);
               sb.append(str);
           }
           
           fis.close();// 关闭输入流
           
           // 从构建器中生成字符串，并替换搜索文本
           String str = sb.toString().replace("DispatcherTemplate", "DispatcherServlet");
           str = str.replace("/*begin-------route code-------*/", routeCode);
           
           String fileName2 = "src\\main\\java\\org\\appleframework\\web\\servlet\\DispatcherServlet.java";
           
           File file2 = new File(fileName2);
           
           FileWriter fout = new FileWriter(file2);// 创建文件输出流
           fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
           fout.close();// 关闭输出流
       } 
       catch (FileNotFoundException e) 
       {
           e.printStackTrace();
       }
       catch (IOException e) 
       {
           e.printStackTrace();
       }
	}
}
