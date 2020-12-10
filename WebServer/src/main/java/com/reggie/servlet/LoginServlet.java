package com.reggie.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.reggie.http.HttpRequest;
import com.reggie.http.HttpResponse;

public class LoginServlet extends HttpServlet {

	public LoginServlet() {
		System.out.println("------------  Constructing LoginServlet  ------------");
	}
	
	@Override
	public void service(HttpRequest request, HttpResponse response) {
		// 1. getting request values
		String userName = request.getParamValueByKey("username");
		String password = request.getParamValueByKey("password");
		// 2. reading user data from user.data
		// if there is a value in user.data is similar to the request value,
		// this user can be allowed to login.
		// if not, return reg.html
		try(RandomAccessFile raf = new RandomAccessFile("user.dat", "r");){
			System.out.println("------------  Starting reading user's data  ------------");
			byte[] data = new byte[32];
			int d = -1;
			String name = null;
			String psword = null;
			boolean flag = true;
			for (int i=0; i<(raf.length()/128); i++) {
				raf.seek(i * 128);
				d = raf.read(data);
				name = new String(data, 0, d, "UTF-8").trim();
				if (userName.equals(name)) {
					System.out.println("The userName is correct, starting checking passward");
					d = raf.read(data);
					psword = new String(data, 0, d, "UTF-8").trim();
					if (password.equals(psword)) {
						System.out.println("The password is correct too.");
						// 3. forwards
						forwards("regsuc.html", request, response);
					}else {
						System.out.println("The password is not correct!");
						// 3. forwards
						forwards("login.html", request, response);
					}
					flag = false;
					break;
				}
			}
			if (flag) {
				System.out.println("this user not register yet");
				// 3. forwards
				forwards("reg.html", request, response);
				response.redirected("reg.html");
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
