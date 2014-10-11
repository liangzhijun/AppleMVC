package org.appleframework.web.servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public  class RouterCfgDomParse
{
		public static void domParse(Map<String, Object> attributesMap, Map<String, String> methodNameMap, Map<String, String> classNameMap, Map<String, String> returnTypeMap, String xml)
			throws Exception
			{
				 DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();

				 try 
				 {
					 DocumentBuilder dombuilder = domfac.newDocumentBuilder();

					 InputStream is = new FileInputStream(xml);

					 Document doc = dombuilder.parse(is);

					 Element root = doc.getDocumentElement();
					 NodeList routerCfgNodeList = root.getChildNodes();

					 if(routerCfgNodeList != null)
					 {
						 for(int i = 0; i < routerCfgNodeList.getLength(); i++)
						 {
							 Node routerCfgNode = routerCfgNodeList.item(i);

							 if(routerCfgNode.getNodeType() == Node.ELEMENT_NODE)
							 {
								 String path = routerCfgNode.getAttributes().getNamedItem("path").getNodeValue();
								 String className = routerCfgNode.getAttributes().getNamedItem("className").getNodeValue();
								 String methodName = routerCfgNode.getAttributes().getNamedItem("method").getNodeValue();
								 String returnType = routerCfgNode.getAttributes().getNamedItem("returnType").getNodeValue();
								 
								 returnTypeMap.put(path, returnType);
								 methodNameMap.put(path, methodName);
								 classNameMap.put(path, className);
								 
								 Class<?> classType = Class.forName(className);
									
								 Object object = classType.newInstance();
								 
								 attributesMap.put(className, object);
								 
								 System.out.println("path: "+path+ "----------" +className+ "----------" +methodName);
							 }
						 }
					 }
				 } 
				 catch (ParserConfigurationException e) {
					 e.printStackTrace();
				 } 
				 catch (FileNotFoundException e) {

					 e.printStackTrace();
				 } 
				 catch (SAXException e) {

					 e.printStackTrace();
				 }
				 catch (IOException e) 
				 {
					 e.printStackTrace();
				 }
			}
}

