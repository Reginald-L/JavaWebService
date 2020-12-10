package com.reggie.servlet;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.reggie.http.HttpRequest;
import com.reggie.http.HttpResponse;

public class RegisterServlet extends HttpServlet{

	public RegisterServlet() {
		System.out.println("------------  Constructing RegisterServlet  ------------");
	}

	public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
		//1. getting param values from HttpRequest
    	String username = httpRequest.getParamValueByKey("username");
    	String password = httpRequest.getParamValueByKey("password");
    	String telephone = httpRequest.getParamValueByKey("telephone");
    	String email = httpRequest.getParamValueByKey("email");
    	//2. creating an object of raf for write user info to the user.dat, 
    	//   every value places 32.
    	try(RandomAccessFile raf = new RandomAccessFile("user.dat", "rw");){
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
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    	//3. setting regsuc.html as response entity and 200 as status_code
    	forwards("regsuc.html", httpRequest, httpResponse);
	}
}
