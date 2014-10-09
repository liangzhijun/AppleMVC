package com.example.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginController
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
}
