package com.nathanchen.controller;

import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.nathanchen.dao.BlogUserDao;
import com.nathanchen.dao.DaoFactory;
import com.nathanchen.model.Article;
import com.nathanchen.model.Comment;
import com.opensymphony.xwork2.ActionSupport;

public class AdminAction extends ActionSupport implements SessionAware 
{
	private BlogUserDao blogUserDao;
	private String articleId;
	
	// showAllPostsInfo
	private ArrayList<Article> allPosts;
	
	// adminEditBlog
	// postArticle
	private Article oneArticle;
	private ArrayList<Comment> allComments;
	private String commentId;
	
	// deleteArticle
	
	// deleteComment
	
	
	
	public String adminIndex()
	{
		return SUCCESS;
	}
	
	public String adminShowAllPostsInfo()
	{
		allPosts = new ArrayList<Article>(); 
		try
		{
			if(blogUserDao == null)
			{
				blogUserDao = DaoFactory.getInstance().getBlogUserDao();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		allPosts = (ArrayList<Article>) blogUserDao.getAllArticles();
		return SUCCESS;
	}
	
	public String adminAddNewPost()
	{
		return SUCCESS;
	}
	
	public String adminEditBlog()
	{
		allComments = new ArrayList<Comment>();
		oneArticle = new Article();
		try
		{
			if(blogUserDao == null)
			{
				blogUserDao = DaoFactory.getInstance().getBlogUserDao();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		// edit an existing post
		if(Integer.parseInt(articleId) != -1)
		{
			oneArticle = blogUserDao.getArticle(articleId);
			allComments = (ArrayList<Comment>) blogUserDao.getCommentsOfOneArticle(articleId);
		}
		
		// create a new post
		else
		{
			oneArticle.setArticleId("-1");
		}
		
		return SUCCESS;
	}
	
	public String deleteArticle()
	{
		if(Integer.parseInt(oneArticle.getArticleId()) == -1)
		{
			return SUCCESS;
		}
		else
		{
			try
			{
				if(blogUserDao == null)
				{
					blogUserDao = DaoFactory.getInstance().getBlogUserDao();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			int articleCode = blogUserDao.deleteArticle(oneArticle.getArticleId());
			int commentsCode = blogUserDao.deleteAllComments(oneArticle.getArticleId());
			
			/*
			 * return SUCCESS when
			 * 1. article and comments have been deleted
			 * 2. article has been deleted and no comment has been found
			 * 
			 * */
			if((articleCode * commentsCode == 1) || ((articleCode * commentsCode == 0) && (commentsCode == 0) ))
			{
				return SUCCESS;
			}
			else
				return ERROR;
		}
	}
	public String postArticle()
	{
		int code;
		try
		{
			if(blogUserDao == null)
			{
				blogUserDao = DaoFactory.getInstance().getBlogUserDao();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(Integer.parseInt(oneArticle.getArticleId()) == -1)
		{
			code = blogUserDao.createArticle(oneArticle);
		}
		else
		{
			code = blogUserDao.editArticle(oneArticle.getArticleId(), oneArticle);
		}
		if(code == 1)
			return SUCCESS;
		else
			return ERROR;
	}
	
	public String deleteComment()
	{
		try
		{
			if(blogUserDao == null)
			{
				blogUserDao = DaoFactory.getInstance().getBlogUserDao();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		int code = blogUserDao.deleteOneComment(commentId);
		if(code == 1)
		{
			allComments = (ArrayList<Comment>) blogUserDao.getCommentsOfOneArticle(articleId);
			return SUCCESS;
		}
		else
			return ERROR;
	}
	
	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub

	}
	public BlogUserDao getBlogUserDao() {
		return blogUserDao;
	}
	public void setBlogUserDao(BlogUserDao blogUserDao) {
		this.blogUserDao = blogUserDao;
	}
	public ArrayList<Article> getAllPosts() {
		return allPosts;
	}
	public void setAllPosts(ArrayList<Article> allPosts) {
		this.allPosts = allPosts;
	}
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public Article getOneArticle() {
		return oneArticle;
	}

	public void setOneArticle(Article oneArticle) {
		this.oneArticle = oneArticle;
	}

	public ArrayList<Comment> getAllComments() {
		return allComments;
	}

	public void setAllComments(ArrayList<Comment> allComments) {
		this.allComments = allComments;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
}
