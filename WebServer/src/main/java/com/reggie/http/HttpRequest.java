package com.reggie.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;

import com.reggie.exception.HttpEmptyRequestException;

/**
 * @ClassName HttpRequest
 * @Description processing http request
 * @Author Reggie
 * @Date 08/12/2020 18:37
 * @Version 1.0
 */

public class HttpRequest {
    private Socket socket;
    private InputStream in;

    // requestLine
    private String method;
    private String url;
    
    private String requestUri;
    private HashMap<String, String> queryParam = new HashMap<String, String>();
    
    private String protocol;

    private HashMap<String, String> headerInfo = new HashMap<String, String>();


    public HttpRequest(Socket socket) {
        try {
            this.socket = socket;
            in = this.socket.getInputStream();
            // parsing request line
            parseRequestLine();
            // parsing request headers
            parseHeaderInfo();
            // parsing request content
            parseRequestContent();
        } catch(HttpEmptyRequestException e) {
        	throw e;
        } catch (IOException e) {
        	e.printStackTrace();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    private void parseRequestLine() {
        System.out.println("------------  Starting parsing requestLine  ------------");
        String line = readLine();
        String[] requestLineData = line.split("\\s");
        if (requestLineData.length == 3) {
            method = requestLineData[0];
            url = requestLineData[1];
			parsingUrl();
            protocol = requestLineData[2];
        } else {
            System.err.println("There is an error when parsing requestLine");
            throw new HttpEmptyRequestException();
        }
        System.out.println("------------  Parsing requestLine is finished ------------");
    }

    private void parsingUrl() {
    	if(url.contains("?")) {
    		// /index.html?username=reggie
    		// /index.html?
    		String[] urlData = url.split("\\?");
    		requestUri = urlData[0];
    		if (urlData.length > 1) {
    			// -1 username=reggie&password=123456
    			// -2 username=reggie (username=  username) ====> 1
    			String queryString = urlData[1];
    			queryString = decodingLine(queryString);
    			for(String str : queryString.split("&")) {
    				String[] queryData = str.split("=");
    				if (queryData.length == 2) {
    					queryParam.put(queryData[0], queryData[1]);
    				}else {
    					queryParam.put(queryData[0], null);
    				}
    			}
    		}
    	}else {
    		// /index.html
    		requestUri = url;
    	}
//    	for(Entry<String, String> e : queryParam.entrySet()) {
//    		System.out.println(e.getKey() + " ====== " + e.getValue());
//    	}
    }
    
    /**
     * translate ISO8859-1 to UTF-8
     * @param line
     * @return
     */
    private String decodingLine(String line) {
    	String newLine = null;
    	try {
			newLine =  URLDecoder.decode(line, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.err.println("There is an exception of coding set");
			e.printStackTrace();
		}
    	return newLine;
    }
    
    private void parseHeaderInfo() {
        System.out.println("------------  Starting parsing headers  ------------");
        String line = null;
        String[] headersData = null;
//        while (true) {
//            String line = readLine();
//            if ("".equals(line)){
//                break;
//            }
//            headersData = line.split(":\\s");
//            System.out.println(headersData.length);
//        }
        while (!"".equals(line = readLine())) {
            if ("".equals(line)) {
                break;
            }
            headersData = line.split(":\\s");
            headerInfo.put(headersData[0], headersData[1]);
        }

//        for(Entry<String, String> e : headerInfo.entrySet()){
//            System.out.println(e.getKey() + "-------" + e.getValue());
//        }
        System.out.println("------------  Parsing headers is finished  ------------");
    }

    /**
     * parsing request content
     */
    private void parseRequestContent() {
        System.out.println("------------  Starting parsing request content  ------------");

        System.out.println("------------  Parsing request content is finished  ------------");
    }

    /**
     * readLine()
     *
     * @param
     * @return
     */
    private String readLine() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            char pre = 'a', cur = 'a';
            int d = -1;
            while ((d = in.read()) != -1) {
                cur = (char) d;
                if (pre == 13 && cur == 10) {
                    break;
                }
                stringBuilder.append(cur);
                pre = cur;
            }
            return stringBuilder.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * getting http method
     * @return
     */
    public String getMethod() {
        return method;
    }

    /**
     * getting http url
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * getting http protocol
     * @return
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * getRequestUri
     * @return
     */
    public String getRequestUri() {
    	return requestUri;
    }
    
    /**
     * getting the value of the request headers by using key
     * @param key
     * @return
     */
    public String getHeaderValue(String key) {
        return headerInfo.get(key);
    }
    
    /**
     * getting param value by passing key
     * @param key
     * @return
     */
    public String getParamValueByKey(String key) {
    	return queryParam.get(key);
    }
}
