package com.nathanchen.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nathan.model.Article;
import com.nathan.model.Comment;
import com.nathan.model.Tag;

public class BlogUserDaoImpl implements BlogUserDao
{
	private DataSource ds;
	Log log = LogFactory.getLog(BlogUserDaoImpl.class);
	
	
	/******* SQL statements go to here *************************************************************************/
	
	String getAllArticles = "select * from nathan_articles where author_name = ? order by publish_date desc";
	String getNumberOfComments = "select count(*) from nathan_comments where article_id = ? order by publish_date desc"; 
			
	
	/***********************************************************************************************************/
	
	public BlogUserDaoImpl()
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
	public void updateComments(Comment comment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createComment(Comment comment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Comment> getComments(String articleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateArticle(Article article, String articleId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Article> getArticles(String author) 
	{
		List<Article> articles = new ArrayList<Article>();
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getAllArticles);
			
			// For testing
			stmt.setString(1, "nathan");
			
//			// Will be modified in the future
//			stmt.setString(1, author);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				String tagInfo = rs.getString("tags");
				String[] arr = tagInfo.split(",");
				List<Tag> tags = new ArrayList<Tag>();
				for(int i = 0; i < arr.length; i ++)
				{
					tags.add(new Tag(arr[i]));
				}
				
				Article article = new Article(rs.getString("article_id"), 
						rs.getString("author_name"), rs.getDate("publish_date"), 
						rs.getString("article_body"), tags);
				articles.add(article);
			}
			log.debug("** getArticles success");
			stmt.close();
			conn.close();
		}
		catch(Exception e)
		{
			log.error("** getArticles fails");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Article getArticle(String articleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNumberOfComments(String articleId) 
	{
		String result = "";
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getNumberOfComments);
			
			stmt.setString(1, articleId);
			
			ResultSet rs = stmt.executeQuery();
			int num = 0;
			while(rs.next())
			{
				num = rs.getInt(1);
			}
			try
			{
				result = num + "";
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			rs.close();
			stmt.close();
			conn.close();
			log.debug("** getNumberOfComments success");
			return result;
		}
		catch(Exception e)
		{
			log.error("** getNumberOfComments fails");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateTags(List<Tag> tags, String articleId) 
	{
		
	}

	@Override
	public List<Tag> getTags(String articleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Article> getTagDistribution(String tagName) {
		// TODO Auto-generated method stub
		return null;
	}
}
