package com.nathanchen.dao;

import java.util.List;

import com.nathan.model.Article;
import com.nathan.model.Comment;
import com.nathan.model.Tag;

public interface BlogUserDao 
{
	/**
	 * 
	 * 
	 * */
	public void updateComments(Comment comment);
	
	/**
	 * @param comment
	 * 
	 * Create a new comment
	 * 
	 * */
	public void createComment(Comment comment);
	
	/**
	 * @param articleId
	 * 
	 * @return ONE article's all comments
	 * 
	 * */
	public List<Comment> getComments(String articleId);
	
	/**
	 * @param articleId
	 * 
	 * @return number of comments belong to an article
	 * 
	 * */
	public String getNumberOfComments(String articleId);
	
	/**
	 * @param article
	 * @param articleId
	 * 
	 * update one specific article
	 * 
	 * */
	public void  updateArticle(Article article, String articleId);
	
	/**
	 * @param author
	 * 
	 * @return ALL articles of an author in the database
	 * 		articles are displayed in descending order according to publish date
	 * 
	 * */
	public List<Article> getArticles(String author);
	
	/**
	 * @param articleId
	 * 
	 * @return ONE article accordingly
	 * 
	 * */
	public Article getArticle(String articleId);
	
	/**
	 * @param tags
	 * @param articleId
	 * 
	 * update one article's tags' information
	 *  
	 * */
	public void updateTags(List<Tag> tags, String articleId);
	
	/**
	 * @param articleId
	 * 
	 * @return one article's all tags 
	 * 
	 * */
	public List<Tag> getTags(String articleId);
	
	/**
	 * @param tag name
	 * 
	 * @return all articles contain this tag
	 * 
	 * */
	public List<Article> getTagDistribution(String tagName);
	
	
}
