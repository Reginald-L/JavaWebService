package com.reggie.servlet;

import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author Reggie
 *
 */
public class ServletContext {
	private static HashMap<String, String> servletMapping = new HashMap<String, String>();
	
	static {
		parseServlet();
	}
	
	public static void parseServlet() {
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read("conf/servlet.xml");
			Element root = doc.getRootElement(); // servlets
			List<Element> servletList = root.elements("servlet");
			for(Element servlet : servletList) {
				String suffix = servlet.element("suffix").getText();
				String className = servlet.element("class").getText();
				servletMapping.put(suffix, className);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public static String getClassnameBySuffix(String suffix) {
		return servletMapping.get(suffix);
	}
	
//	public static void main(String[] args) {
//		System.out.println(ServletContext.getClassnameBySuffix("/login"));
//	}
}
