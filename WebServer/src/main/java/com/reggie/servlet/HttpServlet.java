package com.reggie.servlet;

import java.io.File;

import com.reggie.http.HttpRequest;
import com.reggie.http.HttpResponse;

public abstract class HttpServlet {

	public abstract void service(HttpRequest request, HttpResponse response);
	
	public void forwards(String fileName, HttpRequest request, HttpResponse response) {
		response.setStatusCode("200");
		response.setEntity(new File("web/" + fileName));
	}
}
