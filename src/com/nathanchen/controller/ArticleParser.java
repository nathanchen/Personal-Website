package com.nathanchen.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * if(no bugs){
 *	author = @author NATHAN;
 * }
 * else{
 *	author = "God knows"
 * }
 *
 * parse one xml file
 * 
 * return associated tags, content, comments, title
 *
 *
 *	natechen@me.com
 * 	10:42:29 PM
 *	Feb 17, 2012
 */
public class ArticleParser 
{
	private String articlePath;
	
	private Document response;
	
	private NodeList nl;
	
	public ArticleParser(String articleParser)
	{
		this.articlePath = articleParser;
		initDom();
	}
	
	private void initDom()
	{
		InputStream inputStream = null;
		DocumentBuilder db = null;
		try
		{
			inputStream = new FileInputStream(articlePath);
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(null != inputStream && null != db)
		{
			try
			{
				response = db.parse(inputStream);
				nl = response.getElementsByTagName("Article");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private static String getTagValue(String tag, Element element)
	{
		NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node value = (Node)nodeList.item(0);
		return value.getNodeValue();
	}
	
	public String getElement(String tagName)
	{
		String content = "";
		if(null != response && null != nl)
		{
			try
			{
				for(int i = 0; i < nl.getLength(); i ++)
				{
					Node node = nl.item(i);
					Element element = (Element)node;
					try
					{
						if(getTagValue(tagName, element)!= null)
						{
							content = content + getTagValue(tagName, element) + " ";
						}
					}
					catch(Exception e)
					{
						System.out.println("---" + tagName + " tag does not exist");
						e.printStackTrace();
					}
				}
				System.out.println(tagName + " " + content.trim());
				return content.trim();
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage() + tagName);
				e.printStackTrace();
			}
		}
		return null;
	}
	
//	public Reader getContent()
//	{
//		
//		
//	}
	
	public String getPath()
	{
		return this.articlePath;
	}
	
}
