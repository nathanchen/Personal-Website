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
import org.apache.log4j.Logger;

import com.nathanchen.model.Article;
import com.nathanchen.model.Comment;
import com.nathanchen.model.Tag;

public class UserDaoBlogImpl implements UserDaoBlog
{
	private DataSource ds;
	Logger logger = Logger.getLogger(UserDaoBlogImpl.class);
	
	
	/******* SQL statements go here *************************************************************************/
	StringBuffer getAllArticles = new StringBuffer("select * from Nathan_articles order by publish_date desc");
	StringBuffer getLatestArticlesOfAll = getAllArticles.append(" limit 1");
	StringBuffer getTop10LatestArticlesOfAll = new StringBuffer("select * from Nathan_articles order by publish_date desc limit 1, 10");
	
	StringBuffer getArticle = new StringBuffer("select * from Nathan_articles where article_id = ?");
	StringBuffer getOneAuthorArticles = new StringBuffer("select * from Nathan_articles where author_name = ? order by publish_date desc");
	StringBuffer getOneAuthorLatestArticle = getOneAuthorArticles.append(" limit 1");
	StringBuffer getOneAuthorTop10LatestArticles = new StringBuffer("select * from Nathan_articles where author_name = ? order by publish_date desc limit 10");
	
	StringBuffer getNumberOfCommentsOfOneArticle = new StringBuffer("select count(*) from Nathan_comments where article_id = ? order by publish_date desc"); 
	StringBuffer getAuthorOfLatestCommentOfOneArticle = new StringBuffer("select viewer_name from nathan_comments where article_id = ? order by publish_date desc limit 1");
	StringBuffer getLatestCommentOfOneArticle = new StringBuffer("select * from nathan_comments where article_id = ? order by comment_id desc limit 1");
	
	StringBuffer getCommentsOfOneArticle = new StringBuffer("select * from Nathan_comments where article_id = ? order by publish_date desc");
	
	StringBuffer createComment = new StringBuffer("insert into Nathan_comments values (?, ?, ?, ?, ?)");
	
	StringBuffer findOrCreateByNameAndArticleId = new StringBuffer("select * from nathan_tag_article where tag_name = ? and article_id = ?");
	
	StringBuffer getTagsOfOneArticle = new StringBuffer("select * from nathan_tag_article where article_id = ?");
	
	StringBuffer getArticlesByTag = new StringBuffer("select article_id from Nathan_tag_article where tag_name = ?");
	
	StringBuffer getLatestArticleId = getLatestArticlesOfAll;
	
	StringBuffer getDateOfComment = new StringBuffer("select publish_date from Nathan_comments where article_id = ? and comment_id = ?");
	
	StringBuffer deleteArticle = new StringBuffer("delete from Nathan_articles	where article_id = ?");
	
	StringBuffer deleteAllComments = new StringBuffer("delete from Nathan_comments where article_id = ?");
	
	StringBuffer newArticle = new StringBuffer("insert into Nathan_articles values(?,?,?,?,?)");
	
	StringBuffer deleteOneComment = new StringBuffer("delete from Nathan_comments where comment_id = ?");
	
	StringBuffer setOneArticleTags = new StringBuffer("insert into Nathan_tag_article values(?,?)");
	StringBuffer deleteEntryFromTagTable = new StringBuffer("delete from nathan_tag_article where article_id = ?");
	
	/***********************************************************************************************************/
	
	public UserDaoBlogImpl()
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
			PreparedStatement stmt = conn.prepareStatement(createComment.toString());
			Date date = new Date();
			stmt.setString(1, articleId);
			stmt.setString(2, comment.getName());
			stmt.setString(3, comment.getMessage());
			stmt.setDate(4, new java.sql.Date(date.getTime()));
			stmt.setString(5, commentId);
			
			stmt.executeUpdate();
			logger.debug("** createComment success");
			stmt.close();
			conn.close();
		}
		catch(Exception e)
		{
			logger.error("** createComment fails");
			e.printStackTrace();
		}
	}

	@Override
	public List<Comment> getCommentsOfOneArticle(String articleId) {
		List<Comment> comments = new ArrayList<Comment>();
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getCommentsOfOneArticle.toString());
			
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
			logger.debug("** getCommentsOfOneArticle success");
			stmt.close();
			conn.close();
			return comments;
		}
		catch(Exception e)
		{
			logger.error("** getCommentsOfOneArticle fails");
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
			PreparedStatement stmt = conn.prepareStatement(getOneAuthorArticles.toString());
			
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
			logger.debug("** getOneAuthorArticles success");
			stmt.close();
			conn.close();
			return articles;
		}
		catch(Exception e)
		{
			logger.error("** getOneAuthorArticles fails");
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
			PreparedStatement stmt = conn.prepareStatement(getArticle.toString());
			stmt.setString(1, articleId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				List<Tag> tags = new ArrayList<Tag>();
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
			logger.debug("** getArticle success");
			return result;
		}
		catch(Exception e)
		{
			logger.error("** getArticle success");
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
			PreparedStatement stmt = conn.prepareStatement(getNumberOfCommentsOfOneArticle.toString());
			
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
			logger.debug("** getNumberOfCommentsOfOneArticle success");
			return result;
		}
		catch(Exception e)
		{
			logger.error("** getNumberOfCommentsOfOneArticle fails");
			e.printStackTrace();
		}
		return null;
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
			PreparedStatement stmt = conn.prepareStatement(getOneAuthorLatestArticle.toString());
			
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
			logger.debug("** getOneAuthorLatestArticle success");
			stmt.close();
			conn.close();
			return article;
		}
		catch(Exception e)
		{
			logger.error("** getOneAuthorLatestArticle fails");
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
			PreparedStatement stmt = conn.prepareStatement(getOneAuthorTop10LatestArticles.toString());
			
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
			logger.debug("** getOneAuthorTop10LatestArticles success");
			stmt.close();
			conn.close();
			return articles;
		}
		catch(Exception e)
		{
			logger.error("** getOneAuthorTop10LatestArticles fails");
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
			PreparedStatement stmt = conn.prepareStatement(getLatestArticlesOfAll.toString());
			
			System.out.println("getLatestArticlesOfAll.toString() " + getLatestArticlesOfAll.toString()); 
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
			logger.debug("** getLatestArticlesOfAll success");
			return result;
		}
		catch(Exception e)
		{
			logger.error("** getLatestArticlesOfAll fails");
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
			PreparedStatement stmt = conn.prepareStatement(getTop10LatestArticlesOfAll.toString());
			
			System.out.println("getTop10LatestArticlesOfAll.toString() " + getTop10LatestArticlesOfAll.toString());
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
			logger.debug("** getTop10LatestArticlesOfAll success");
			stmt.close();
			conn.close();
			return articles;
		}
		catch(Exception e)
		{
			logger.error("** getTop10LatestArticlesOfAll fails");
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
			PreparedStatement stmt = conn.prepareStatement(getAuthorOfLatestCommentOfOneArticle.toString());
			
			stmt.setString(1, articleId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				result = rs.getString("viewer_name");
			}
			logger.debug("** getAuthorOfLatestCommentOfOneArticle success");
			stmt.close();
			conn.close();
			return result;
		}
		catch(Exception e)
		{
			logger.error("** getAuthorOfLatestCommentOfOneArticle fails");
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
			PreparedStatement stmt = conn.prepareStatement(getOneAuthorArticles.toString());
			
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
			logger.debug("** findOrCreateByNameAndArticleId success");
			stmt.close();
			conn.close();
			return tag;
		}
		catch(Exception e)
		{
			logger.error("** findOrCreateByNameAndArticleId fails");
			return null;
		}
	}


	public List<Tag> getTagsOfOneArticle(String articleId) 
	{
		List<Tag> tags = new ArrayList<Tag>();
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getTagsOfOneArticle.toString());
			
			stmt.setString(1, articleId);
			ResultSet rs = stmt.executeQuery();
			int i = 0;
			while(rs.next() && i <= 5)
			{
				Tag tag = new Tag();
				tag.setTagName(rs.getString("tag_name"));
				tags.add(tag);
				i ++;
			}
			rs.close();
			stmt.close();
			conn.close();
			logger.debug("** getTagsOfOneArticle success");
			return tags;
		}
		catch(Exception e)
		{
			logger.error("** getTagsOfOneArticle fails");
			e.printStackTrace();
		}
		return null;
	}


	public String[] getArticlesByTag(String tagName) {
		String[] result;
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getArticlesByTag.toString());
			
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
			logger.debug("** getArticlesByTag success");
			return result;
		}
		catch(Exception e)
		{
			logger.error("** getArticlesByTag fails");
			e.printStackTrace();
		}
		return null;	
	}

	
	/**
	 * select * from Nathan_articles order by publish_date desc
	 * 
	 * */
	@Override
	public List<Article> getAllArticles() 
	{
		ArrayList<Article> articles = new ArrayList<Article>();
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getAllArticles.toString());
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				Article article = new Article();
				String articleId = rs.getString("article_id");
				article.setArticleId(articleId);
				article.setAuthor(rs.getString("author_name"));
				article.setDate(rs.getDate("publish_date"));
				article.setTitle(rs.getString("article_title"));
				String numberOfComments = getNumberOfCommentsOfOneArticle(articleId);
				int num = Integer.parseInt(numberOfComments);
				article.setNumberOfComments(num);
				if(num < 1)
				{
					article.setAuthorOfLatestComment(null);
				}
				else
				{
					Comment comment = getLatestCommentOfOneArticle(articleId);
					String authorOfLatestComment = comment.getName();
					article.setAuthorOfLatestComment(authorOfLatestComment);
				}
				articles.add(article);
			}
			rs.close();
			stmt.close();
			conn.close();
			logger.debug("** getAllArticles success");
			return articles;
		}
		catch(Exception e)
		{
			logger.error("** getAllArticles fails");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getLatestArticleId() 
	{
		String articleId = null;
		try
		{
			
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getLatestArticleId.toString());
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				articleId = rs.getString("article_id");
			}
			rs.close();
			stmt.close();
			conn.close();
			logger.debug("** getLatestArticleId success");
			return articleId;
		}
		catch(Exception e)
		{
			logger.error("** getLatestArticleId fails");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Date getDateOfComment(String articleId, String commentId) 
	{
		Date publishDate = null;
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getDateOfComment.toString());
			stmt.setString(1, articleId);
			stmt.setString(2, commentId);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				publishDate = rs.getDate("publish_date");
			}
			rs.close();
			stmt.close();
			conn.close();
			logger.debug("** getDateOfComment success");
			return publishDate;
		}
		catch(Exception e)
		{
			logger.error("** getDateOfComment fails");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Comment getLatestCommentOfOneArticle(String articleId) 
	{
		Comment comment = new Comment();
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(getLatestCommentOfOneArticle.toString());
			stmt.setString(1, articleId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				comment.setCommentId(rs.getString("comment_id"));
				comment.setDate(rs.getDate("publish_date"));
				comment.setName(rs.getString("viewer_name"));
			}
			rs.close();
			stmt.close();
			conn.close();
			logger.debug("** getLatestCommentOfOneArticle success");
			return comment;
		}
		catch(Exception e)
		{
			logger.error("** getLatestCommentOfOneArticle fails");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int deleteArticle(String articleId) 
	{
		int result = -1;
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(deleteArticle.toString());
			stmt.setString(1, articleId);
			result = stmt.executeUpdate();
			stmt.close();
			conn.close();
			logger.debug("** deleteArticle success");
			return result;
		}
		catch(Exception e)
		{
			logger.error("** deleteArticle fails");
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int createArticle(Article article) 
	{
		int result = -1;
		try
		{
			Connection conn = ds.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement(newArticle.toString());
			String articleId = Integer.parseInt(getLatestArticleId()) + 1 + "";
			stmt.setString(1, articleId);
			stmt.setString(2, article.getAuthor());
			stmt.setString(3, article.getArticleBody());
			stmt.setString(4, article.getDateString());
			stmt.setString(5, article.getTitle());
			result = stmt.executeUpdate() * setOneArticleTags(article.getTags(), articleId);
			conn.commit();
			stmt.close();
			conn.close();
			conn.setAutoCommit(true);
			logger.debug("** createArticle success");
			return result;
		}
		catch(Exception e)
		{
			logger.error("** createArticle fails");
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int editArticle(String articleId, Article article) 
	{
		int result = -1;
		try
		{
			Connection conn = ds.getConnection();
			deleteArticle(articleId);
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement(newArticle.toString());
			stmt.setString(1, articleId);
			stmt.setString(2, article.getAuthor());
			stmt.setString(3, article.getArticleBody());
			stmt.setString(4, article.getDateString());
			stmt.setString(5, article.getTitle());
			result = stmt.executeUpdate() * setOneArticleTags(article.getTags(), articleId);
			conn.commit();
			conn.setAutoCommit(true);
			stmt.close();
			conn.close();
			logger.debug("** editArticle success");
			return result;
		}
		catch(Exception e)
		{
			logger.error("** editArticle fails");
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int deleteAllComments(String articleId) 
	{
		int result = -1;
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(deleteAllComments.toString());
			stmt.setString(1, articleId);
			result = stmt.executeUpdate();
			stmt.close();
			conn.close();
			logger.debug("** deleteAllComments success");
			return result;
		}
		catch(Exception e)
		{
			logger.error("** deleteAllComments fails");
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int deleteOneComment(String commentId) 
	{
		int result = -1;
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(deleteOneComment.toString());
			stmt.setString(1, commentId);
			result = stmt.executeUpdate();
			stmt.close();
			conn.close();
			logger.debug("** deleteOneComment success");
			return result;
		}
		catch(Exception e)
		{
			logger.error("** deleteOneComment fails");
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int setOneArticleTags(List<Tag> tags, String articleId) 
	{
		int result = 1;
		try
		{
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(deleteEntryFromTagTable.toString());
			stmt.setString(1, articleId);
			stmt.executeUpdate();
			stmt = conn.prepareStatement(setOneArticleTags.toString());
			for(int i = 0; i < tags.size(); i ++)
			{
				stmt.setString(1, articleId);
				stmt.setString(2, tags.get(i).getTagName());
				result = result * stmt.executeUpdate();
			}
			stmt.close();
			conn.close();
			logger.debug("** setOneArticleTags success");
			return result;
		}
		catch(Exception e)
		{
			logger.error("** setOneArticleTags fails");
			e.printStackTrace();
		}
		return result;
	}
	
	
}
