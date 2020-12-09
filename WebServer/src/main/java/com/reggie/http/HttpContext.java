package com.reggie.http;

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
public class HttpContext {
	public static HashMap<String, String> MIME_EXTENSION_TYPE = new HashMap<String, String>();
	public static HashMap<String, String> MIME_STATUS_REASONS = new HashMap<String, String>();
	
	static {
		initMimeExtensionType();
		initMimeStatusReasons();
	}
	
	private static void initMimeExtensionType() {
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read("conf/web.xml");
			Element root = doc.getRootElement();
			List<Element> mimeMappingList = root.elements("mime-mapping");
			for(Element e : mimeMappingList) {
				String extension = e.element("extension").getText();
				String type = e.element("mime-type").getText();
				MIME_EXTENSION_TYPE.put(extension, type);
			}
//			for(Entry<String, String> e : MIME_EXTENSION_TYPE.entrySet()) {
//				System.out.println(e.getKey() + "------" + e.getValue());
//			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void initMimeStatusReasons() {
		MIME_STATUS_REASONS.put("200", "OK");
		MIME_STATUS_REASONS.put("302", "REDIRECT TO ANOTHER SITES");
		MIME_STATUS_REASONS.put("404", "PAGE NOT BE FOUND");
		MIME_STATUS_REASONS.put("500", "SERVER THROWS AN EXCEPTION");
	}
	
	public static String getExtensionType(String extension) {
		return MIME_EXTENSION_TYPE.get(extension);
	}
	
	public static String getStatusReason(String status_code) {
		return MIME_STATUS_REASONS.get(status_code);
	}
	
}
