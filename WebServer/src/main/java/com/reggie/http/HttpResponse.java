package com.reggie.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;

public class HttpResponse {
	private Socket socket;
	private OutputStream out;
	
	private File entity;
	private String status_code;
	private HashMap<String, String> response_headers = new HashMap<String, String>();
	
	public HttpResponse(Socket socket) {
		try {
			System.out.println("------------  Constracting a httpResponse  ------------");
			this.socket = socket;
			out = this.socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * flush
	 */
	public void flush() {
		sendResponseLine();
		sendResponseHeader();
		sendResponseContent();
	}
	
	/**
	 * sendResponseLine
	 */
	private void sendResponseLine() {
		System.out.println("------------  Starting writting responseLine  ------------");
        try {
        	String status_line = "HTTP/1.1" + " " + status_code + " " + HttpContext.getStatusReason(status_code);
			writeLine(status_line);
		} catch (Exception e) {
			e.printStackTrace();
		}
        System.out.println("------------  Writting responseLine is finished  ------------");
	}
	
	/**
	 * sendResponseHeader
	 */
	private void sendResponseHeader() {
		try {
			System.out.println("------------  Starting writting responseHeaders  ------------");
            for (Entry<String, String> e : response_headers.entrySet()) {
            	String line = e.getKey() + ": " + e.getValue();
            	writeLine(line);
            }
            out.write(13);
            out.write(10);
		}catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("------------  Writting responseHeaders is finished  ------------");
	}
	
	/**
	 * sendResponseContent
	 */
	private void sendResponseContent() {
		System.out.println("------------  Starting writting responseContent  ------------");
		try(FileInputStream fis = new FileInputStream(entity)){
			byte[] file_data = new byte[1024 * 10];
            int d = -1;
            while((d = fis.read(file_data))!=-1) {
//            	System.out.println(new String(file_data, "ISO8859-1"));
            	out.write(file_data, 0, d);
            }
            out.write(13);
            out.write(10);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("------------  Writting responseContent is finished  ------------");
	}
	
	/**
	 * writeLine
	 * @param line
	 */
	private void writeLine(String line) {
		try {
			out.write(line.getBytes("ISO8859-1"));
	    	out.write(13);
	        out.write(10);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setEntity(File entity) {
		this.entity = entity;
		setResponseHeader("Content-Length", String.valueOf(entity.length()));
        String entityName = entity.getName();
		String type = HttpContext.getExtensionType(entityName.substring(entityName.lastIndexOf(".") + 1));
		setResponseHeader("Content-Type", type);
	}
	
	public File getEntity() {
		return this.entity;
	}
	
	public void setStatusCode(String status_code) {
		this.status_code = status_code;
	}
	
	public String getStatusCode() {
		return this.status_code;
	}
	
	private void setResponseHeader(String key, String value) {
		response_headers.put(key, value);
	}
}
