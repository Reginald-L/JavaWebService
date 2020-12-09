package com.reggie.server;


import java.io.File;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

import com.reggie.exception.HttpEmptyRequestException;
import com.reggie.http.HttpRequest;
import com.reggie.http.HttpResponse;


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
	        System.out.println("uri = " + uri); // /reg
	        if ("/reg".equals(uri)) {
	        	System.out.println("这是一个注册业务");
	        	//1. getting param values from HttpRequest
	        	String username = httpRequest.getParamValueByKey("username");
	        	String password = httpRequest.getParamValueByKey("password");
	        	String telephone = httpRequest.getParamValueByKey("telephone");
	        	String email = httpRequest.getParamValueByKey("email");
	        	//2. creating an object of raf for write user info to the user.dat, 
	        	//   every value places 32.
	        	RandomAccessFile raf = new RandomAccessFile("user.dat", "rw");
	        	raf.seek(raf.length());
	        	byte[] username_data = username.getBytes("UTF-8");
	        	username_data = Arrays.copyOf(username_data, 32);
	        	raf.write(username_data);
	        	byte[] password_data = password.getBytes("UTF-8");
	        	password_data = Arrays.copyOf(password_data, 32);
	        	raf.write(password_data);
	        	byte[] telephone_data = telephone.getBytes("UTF-8");
	        	telephone_data = Arrays.copyOf(telephone_data, 32);
	        	raf.write(telephone_data);
	        	byte[] email_data = email.getBytes("UTF-8");
	        	email_data = Arrays.copyOf(email_data, 32);
	        	raf.write(email_data);
	        	//3. setting regsuc.html as response entity and 200 as status_code
	        	File regsuc = new File("web/regsuc.html");
	        	httpResponse.setEntity(regsuc);
	        	httpResponse.setStatusCode("200");
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
