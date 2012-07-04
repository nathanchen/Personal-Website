package com.nathanchen.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.nathanchen.model.BlogSearchIndexResult;

public class UtilsDaoImpl implements UtilsDao
{
	private DataSource ds;
	
	/******* SQL statements go here *************************************************************************/
	StringBuffer getBlogSearchIndexResultList = new StringBuffer("");
	StringBuffer getTotalNumberOfBlogs = new StringBuffer("select count(*) from Nathan_articles;");
	
	/***********************************************************************************************************/
	
	
	public UtilsDaoImpl()
	{
		try
		{
			Context initCtx = new InitialContext();
			Context envCtx = (Context)initCtx.lookup("java:comp/env");
			ds = (DataSource)envCtx.lookup("jdbc/nathan_test");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public List<BlogSearchIndexResult> getBlogSearchIndexResultList() 
	{
		
		
		return null;
	}
	
	public int getTotalNumberOfBlogs()
	{
		int numberOfBlogs = 0;
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getTotalNumberOfBlogs.toString());
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
			{
				numberOfBlogs = rs.getInt("count(*)");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return numberOfBlogs;
	}
	
}
