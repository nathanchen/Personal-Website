package com.nathanchen.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DaoFactory 
{
	private static DaoFactory df;
	private BlogUserDao blogUserDao;
	private Log log = LogFactory.getLog(DaoFactory.class);
	String classname = this.getClass().getName();
	
	private DaoFactory()
	{
		
	}
	
	public static DaoFactory getInstance()
	{
		if(df == null)
			df = new DaoFactory();
		return df;
	}
	
	public BlogUserDao getBlogUserDao()
	{
		if(blogUserDao == null)
			blogUserDao = new BlogUserDaoImpl();
		return blogUserDao;
	}
	
}
