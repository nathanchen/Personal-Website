package com.nathanchen.controller;

import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.nathanchen.dao.BlogUserDao;
import com.nathanchen.dao.DaoFactory;
import com.nathanchen.model.Article;
import com.opensymphony.xwork2.ActionSupport;

public class AdminAction extends ActionSupport implements SessionAware 
{
	private BlogUserDao blogUserDao;
	
	// showAllPostsInfo
	private ArrayList<Article> allPosts;
	
	// adminEditBlog
	private String articleId;
	private Article oneArticle;
	
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
		}
		
		// create a new post
		else
		{
			oneArticle.setArticleId("-1");
		}
		
		return SUCCESS;
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
}
