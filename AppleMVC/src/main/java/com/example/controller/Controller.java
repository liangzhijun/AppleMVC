package com.example.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Controller
{
	public String login(HttpServletRequest request, HttpServletResponse response)
	{
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		System.out.println("username=" + username);
		System.out.println("password=" + password);
		
		System.out.println(" Log in successfully.");
		return "logInSuccess.jsp";
	}
	
	public String exit(HttpServletRequest request, HttpServletResponse response)
	{
		//System.out.println(username);
		//System.out.println(password);
		
		System.out.println(" exit successfully.");
		return "exitSuccess.jsp";
	}
	
	public String getText(HttpServletRequest request, HttpServletResponse response)
	{
		String text = request.getParameter("text");
		System.out.println(" output Text successfully.");
		return text;
	}
	
	public String getImage(HttpServletRequest request, HttpServletResponse response)
	{
		String imagePath = request.getParameter("imagePath");
		System.out.println(" output Image successfully.");
		return imagePath;
	}
	
	public String getFile(HttpServletRequest request, HttpServletResponse response)
	{
		String filePath = request.getParameter("filePath");
		System.out.println(" output File successfully.");
		return filePath;
	}
}
