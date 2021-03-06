package com.nathanchen.lucene.xml;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
public class ArticleParserXML 
{
	private String articlePath;
	
	private Document response;
	
	private NodeList nl;
	
	public ArticleParserXML(String articleParser)
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
		String result = "";
		int max = element.getElementsByTagName(tag).getLength();
		for(int j = 0; j < max; j ++)
		{
			NodeList nodeList = element.getElementsByTagName(tag).item(j).getChildNodes();
			for(int i = 0; i < nodeList.getLength(); i++)
			{
				Node value = (Node)nodeList.item(i);
				result = result.trim() + " " + value.getNodeValue().trim();
			}
		}
		
		return result.trim();
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
