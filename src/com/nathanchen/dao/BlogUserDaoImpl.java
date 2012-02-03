package com.nathanchen.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
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
	String getAllArticles = "select * from Nathan_articles order by publish_date desc";
	String getLatestArticlesOfAll = getAllArticles + " limit 1";
	String getTop10LatestArticlesOfAll = getAllArticles + " limit 1, 10";
	
	String getArticle = "select * from Nathan_articles where article_id = ?";
	String getOneAuthorArticles = "select * from Nathan_articles where author_name = ? order by publish_date desc";
	String getOneAuthorLatestArticle = getOneAuthorArticles + " limit 1";
	String getOneAuthorTop10LatestArticles = getOneAuthorArticles + " limit 10";
	
	String getNumberOfCommentsOfOneArticle = "select count(*) from Nathan_comments where article_id = ? order by publish_date desc"; 
	String getAuthorOfLatestCommentOfOneArticle = "select viewer_name from nathan_comments where article_id = ? order by publish_date desc limit 1";
	
	String getCommentsOfOneArticle = "select * from Nathan_comments where article_id = ? order by publish_date desc";
	
	String createComment = "insert into Nathan_comments values (?, ?, ?, ?, ?)";
	
	String findOrCreateByNameAndArticleId = "select * from nathan_tag_article where tag_name = ? and article_id = ?";
	
	String getTagsOfOneArticle = "select * from nathan_tag_article where article_id = ? order by tag_appears desc ";
	
	String getArticlesByTag = "select article_id from Nathan_tag_article where tag_name = ?";
	
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
	public void createComment(Comment comment, String articleId, String commentId) 
	{
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(createComment);
			Date date = new Date();
			stmt.setString(1, articleId);
			stmt.setString(2, comment.getName());
			stmt.setString(3, comment.getMessage());
			stmt.setDate(4, new java.sql.Date(date.getTime()));
			stmt.setString(5, commentId);
			
			stmt.executeUpdate();
			log.debug("** createComment success");
			stmt.close();
			conn.close();
		}
		catch(Exception e)
		{
			log.error("** createComment fails");
			e.printStackTrace();
		}
	}

	@Override
	public List<Comment> getCommentsOfOneArticle(String articleId) {
		List<Comment> comments = new ArrayList<Comment>();
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getCommentsOfOneArticle);
			
			stmt.setString(1, articleId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
//				public Comment(String articleId, String name, Date date, String message, String commentId)
				String name = rs.getString("viewer_name");
				Date date = rs.getDate("publish_date");
				String message = rs.getString("message");
				String commentId = rs.getString("comment_id");
				Comment comment = new Comment(articleId, name, date, message, commentId);
				comments.add(comment);
			}
			log.debug("** getCommentsOfOneArticle success");
			stmt.close();
			conn.close();
			return comments;
		}
		catch(Exception e)
		{
			log.error("** getCommentsOfOneArticle fails");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateArticle(Article article, String articleId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Article> getOneAuthorArticles(String author) 
	{
		List<Article> articles = new ArrayList<Article>();
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getOneAuthorArticles);
			
			// For testing
			stmt.setString(1, "nathan");
			
//			// Will be modified in the future
//			stmt.setString(1, author);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				List<Tag> tags = new ArrayList<Tag>();
				String articleId = rs.getString("article_id");
				tags = getTagsOfOneArticle(articleId); 

				
				String numberOfComments = getNumberOfCommentsOfOneArticle(articleId);
				int num = Integer.parseInt(numberOfComments);
				Article article;
				if( num < 1)
				{
					article = new Article(rs.getString("article_id"), 
							rs.getString("author_name"), rs.getDate("publish_date"), 
							rs.getString("article_body"), tags, rs.getString("article_title"), tags.size());
				}
				else
				{
					String authorOfLatestComment = getAuthorOfLatestCommentOfOneArticle(articleId);
					article = new Article(rs.getString("article_id"), 
							rs.getString("author_name"), rs.getDate("publish_date"), 
							rs.getString("article_body"), tags, rs.getString("article_title"),
							num, authorOfLatestComment, tags.size());
				}
				// if the article has been assigned any tags
				articles.add(article);
			}
			log.debug("** getOneAuthorArticles success");
			stmt.close();
			conn.close();
			return articles;
		}
		catch(Exception e)
		{
			log.error("** getOneAuthorArticles fails");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Article getArticle(String articleId) 
	{
		Article result = new Article();
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getArticle);
			stmt.setString(1, articleId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				List<Tag> tags = new ArrayList<Tag>();
				
//				String tagInfo = rs.getString("tags");
//				
//				// if the article has been assigned any tags
//				if(tagInfo.trim() != null)
//				{
//					String[] arr = tagInfo.split(",");
//					for(int i = 0; i < arr.length; i ++)
//					{
//						tags.add(new Tag(arr[i]));
//					}
//				}
				String numberOfComments = getNumberOfCommentsOfOneArticle(articleId);
				int num = Integer.parseInt(numberOfComments);
				if( num < 1)
				{
					result = new Article(rs.getString("article_id"), 
							rs.getString("author_name"), rs.getDate("publish_date"), 
							rs.getString("article_body"), tags, rs.getString("article_title"), tags.size());
				}
				else
				{
					String authorOfLatestComment = getAuthorOfLatestCommentOfOneArticle(articleId);
					result = new Article(rs.getString("article_id"), 
							rs.getString("author_name"), rs.getDate("publish_date"), 
							rs.getString("article_body"), tags, rs.getString("article_title"),
							num, authorOfLatestComment, tags.size());
				}
			}
			rs.close();
			stmt.close();
			conn.close();
			log.debug("** getArticle success");
			return result;
		}
		catch(Exception e)
		{
			log.error("** getArticle success");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getNumberOfCommentsOfOneArticle(String articleId) 
	{
		String result = "";
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getNumberOfCommentsOfOneArticle);
			
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
			log.debug("** getNumberOfCommentsOfOneArticle success");
			return result;
		}
		catch(Exception e)
		{
			log.error("** getNumberOfCommentsOfOneArticle fails");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateTags(List<Tag> tags, String articleId) 
	{
		
	}

	@Override
	public List<Article> getTagDistribution(String tagName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Article getOneAuthorLatestArticle(String author) {
		Article article = new Article();
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getOneAuthorLatestArticle);
			
			// For testing
			stmt.setString(1, "nathan");
			
//			// Will be modified in the future
//			stmt.setString(1, author);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next())
			{
				List<Tag> tags = new ArrayList<Tag>();
				String articleId = rs.getString("article_id");
				tags = getTagsOfOneArticle(articleId); 

				
				String numberOfComments = getNumberOfCommentsOfOneArticle(articleId);
				int num = Integer.parseInt(numberOfComments);
				if( num < 1)
				{
					article = new Article(rs.getString("article_id"), 
							rs.getString("author_name"), rs.getDate("publish_date"), 
							rs.getString("article_body"), tags, rs.getString("article_title"), tags.size());
				}
				else
				{
					String authorOfLatestComment = getAuthorOfLatestCommentOfOneArticle(articleId);
					article = new Article(rs.getString("article_id"), 
							rs.getString("author_name"), rs.getDate("publish_date"), 
							rs.getString("article_body"), tags, rs.getString("article_title"),
							num, authorOfLatestComment, tags.size());
				}
			}
			log.debug("** getOneAuthorLatestArticle success");
			stmt.close();
			conn.close();
			return article;
		}
		catch(Exception e)
		{
			log.error("** getOneAuthorLatestArticle fails");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Article> getOneAuthorTop10LatestArticles(String author) {
		List<Article> articles = new ArrayList<Article>();
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getOneAuthorTop10LatestArticles);
			
			// For testing
			stmt.setString(1, "nathan");
			
//			// Will be modified in the future
//			stmt.setString(1, author);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				List<Tag> tags = new ArrayList<Tag>();
				String articleId = rs.getString("article_id");
				tags = getTagsOfOneArticle(articleId); 

				
				String numberOfComments = getNumberOfCommentsOfOneArticle(articleId);
				int num = Integer.parseInt(numberOfComments);
				Article article;
				if( num < 1)
				{
					article = new Article(rs.getString("article_id"), 
							rs.getString("author_name"), rs.getDate("publish_date"), 
							rs.getString("article_body"), tags, rs.getString("article_title"), tags.size());
				}
				else
				{
					String authorOfLatestComment = getAuthorOfLatestCommentOfOneArticle(articleId);
					article = new Article(rs.getString("article_id"), 
							rs.getString("author_name"), rs.getDate("publish_date"), 
							rs.getString("article_body"), tags, rs.getString("article_title"),
							num, authorOfLatestComment, tags.size());
				}
				articles.add(article);
			}
			log.debug("** getOneAuthorTop10LatestArticles success");
			stmt.close();
			conn.close();
			return articles;
		}
		catch(Exception e)
		{
			log.error("** getOneAuthorTop10LatestArticles fails");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Article getLatestArticlesOfAll() 
	{
		Article result = new Article();
		try
		{
			
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getLatestArticlesOfAll);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				List<Tag> tags = new ArrayList<Tag>();
				
				String articleId = rs.getString("article_id");
				tags = getTagsOfOneArticle(articleId); 
				String numberOfComments = getNumberOfCommentsOfOneArticle(articleId);
				int num = Integer.parseInt(numberOfComments);
				if( num < 1)
				{
					result = new Article(rs.getString("article_id"), 
							rs.getString("author_name"), rs.getDate("publish_date"), 
							rs.getString("article_body"), tags, rs.getString("article_title"), tags.size());
				}
				else
				{
					String authorOfLatestComment = getAuthorOfLatestCommentOfOneArticle(articleId);
					result = new Article(rs.getString("article_id"), 
							rs.getString("author_name"), rs.getDate("publish_date"), 
							rs.getString("article_body"), tags, rs.getString("article_title"),
							num, authorOfLatestComment, tags.size());
				}
			}
			rs.close();
			stmt.close();
			conn.close();
			log.debug("** getLatestArticlesOfAll success");
			return result;
		}
		catch(Exception e)
		{
			log.error("** getLatestArticlesOfAll fails");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Article> getTop10LatestArticlesOfAll() 
	{
		List<Article> articles = new ArrayList<Article>();
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getTop10LatestArticlesOfAll);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				List<Tag> tags = new ArrayList<Tag>();
				String articleId = rs.getString("article_id");
				tags = getTagsOfOneArticle(articleId); 
				String numberOfComments = getNumberOfCommentsOfOneArticle(articleId);
				int num = Integer.parseInt(numberOfComments);
				Article article;
				if( num < 1)
				{
					article = new Article(rs.getString("article_id"), 
							rs.getString("author_name"), rs.getDate("publish_date"), 
							rs.getString("article_body"), tags, rs.getString("article_title"), tags.size());
				}
				else
				{
					String authorOfLatestComment = getAuthorOfLatestCommentOfOneArticle(articleId);
					article = new Article(rs.getString("article_id"), 
							rs.getString("author_name"), rs.getDate("publish_date"), 
							rs.getString("article_body"), tags, rs.getString("article_title"),
							num, authorOfLatestComment, tags.size());
				}
				articles.add(article);
			}
			log.debug("** getTop10LatestArticlesOfAll success");
			stmt.close();
			conn.close();
			return articles;
		}
		catch(Exception e)
		{
			log.error("** getTop10LatestArticlesOfAll fails");
			e.printStackTrace();
		}		
		return null;
	}

	@Override
	public String getAuthorOfLatestCommentOfOneArticle(String articleId) 
	{
		String result = "";
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getAuthorOfLatestCommentOfOneArticle);
			
			stmt.setString(1, articleId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				result = rs.getString("viewer_name");
			}
			log.debug("** getAuthorOfLatestCommentOfOneArticle success");
			stmt.close();
			conn.close();
			return result;
		}
		catch(Exception e)
		{
			log.error("** getAuthorOfLatestCommentOfOneArticle fails");
			e.printStackTrace();
		}
		return null;
	}


	public Tag findOrCreateByNameAndArticleId(String name, String articleId) 
	{
		Tag tag = new Tag();
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getOneAuthorArticles);
			
			// For testing
			stmt.setString(1, name);
			stmt.setString(2, articleId);
			
			ResultSet rs = stmt.executeQuery();
			int number ;
			if(rs != null)
			{
				number = rs.getInt("tag_appears") + 1;
			}
			else
			{
				number = 1;
			}
			tag = new Tag(name, number);
			log.debug("** findOrCreateByNameAndArticleId success");
			stmt.close();
			conn.close();
			return tag;
		}
		catch(Exception e)
		{
			log.error("** findOrCreateByNameAndArticleId fails");
			return null;
		}
	}


	public List<Tag> getTagsOfOneArticle(String articleId) 
	{
		List<Tag> tags = new ArrayList<Tag>();
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getTagsOfOneArticle);
			
			stmt.setString(1, articleId);
			ResultSet rs = stmt.executeQuery();
			int i = 0;
			while(rs.next() && i <= 5)
			{
				Tag tag = new Tag();
				tag.setTagName(rs.getString("tag_name"));
				tag.setNumber(rs.getInt("tag_appears"));
				tags.add(tag);
				i ++;
			}
			rs.close();
			stmt.close();
			conn.close();
			log.debug("** getTagsOfOneArticle success");
			return tags;
		}
		catch(Exception e)
		{
			log.error("** getTagsOfOneArticle fails");
			e.printStackTrace();
		}
		return null;
	}


	public String[] getArticlesByTag(String tagName) {
		String[] result;
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getArticlesByTag);
			
			stmt.setString(1, tagName);
			ResultSet rs = stmt.executeQuery();
			String str = "";
			while(rs.next())
			{
				str = str + " " + rs.getString("article_id");
			}
			if(!str.trim().isEmpty())
			{
				result = str.trim().split(" ");
			}
			else
				return null;
			rs.close();
			stmt.close();
			conn.close();
			log.debug("** getArticlesByTag success");
			return result;
		}
		catch(Exception e)
		{
			log.error("** getArticlesByTag fails");
			e.printStackTrace();
		}
		return null;	
	}
}
