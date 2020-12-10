package com.reggie.server;


import java.io.File;
import java.net.Socket;

import com.reggie.exception.HttpEmptyRequestException;
import com.reggie.http.HttpRequest;
import com.reggie.http.HttpResponse;
import com.reggie.servlet.HttpServlet;
import com.reggie.servlet.RegisterServlet;
import com.reggie.servlet.ServletContext;


/**
 * @ClassName ClientHandler
 * @Description TODO
 * @Author Reggie
 * @Date 08/12/2020 17:53
 * @Version 1.0
 */

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        System.out.println("------------  Constructing a thread of clientHandler  ------------");
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
	        System.out.println("------------  Starting handling a client  ------------");
	        HttpRequest httpRequest = new HttpRequest(socket);
	        HttpResponse httpResponse = new HttpResponse(socket);
	        
	        String uri = httpRequest.getRequestUri();
	        String servletName = ServletContext.getClassnameBySuffix(uri);
	        System.out.println("servletName = " + servletName); // /reg
	        if (servletName != null) {
	        	System.out.println("这是一个业务");
	        	HttpServlet servlet = (HttpServlet) Class.forName(servletName).newInstance();
	        	servlet.service(httpRequest, httpResponse);
	        }else {
	        	File askedFile = new File("web" + uri);
		        
		        if (askedFile.exists()) {
		            System.out.println("File has been already found!!!");
		            httpResponse.setEntity(askedFile);
		            httpResponse.setStatusCode("200");
		        }else{
		            System.out.println("File has not been found!!!");
		            File notFound = new File("web/NotFound.html");
		            httpResponse.setEntity(notFound);
		            httpResponse.setStatusCode("404");
		        }
	        }
	        httpResponse.flush();
        } catch (HttpEmptyRequestException e) {
        	System.err.println("This is a empty request");
        }
        catch (Exception e) {
            e.printStackTrace();
        } 
    }


}
