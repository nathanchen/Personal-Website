package com.nathanchen.dao;



public class DaoFactory 
{
	private static DaoFactory df;
	private UserDaoBlog blogUserDao;
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
	
	public UserDaoBlog getBlogUserDao()
	{
		if(blogUserDao == null)
			blogUserDao = new UserDaoBlogImpl();
		return blogUserDao;
	}
	
}
