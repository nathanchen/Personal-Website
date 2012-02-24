package com.nathanchen.dao;

import java.util.Date;
import java.util.List;

import com.nathanchen.model.Article;
import com.nathanchen.model.Comment;
import com.nathanchen.model.Tag;

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
	public void createComment(Comment comment, String articleId, String commentId);
	
	/**
	 * @param articleId
	 * 
	 * @return ONE article's all comments
	 * 
	 * */
	public List<Comment> getCommentsOfOneArticle(String articleId);
	
	/**
	 * @param articleId
	 * 
	 * @return author's name who published the latest comment under one article
	 * 
	 * */
	public String getAuthorOfLatestCommentOfOneArticle(String articleId);
	
	/**
	 * @param articleId
	 * 
	 * @return number of comments belong to an article
	 * 
	 * */
	public String getNumberOfCommentsOfOneArticle(String articleId);
	
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
	public List<Article> getOneAuthorArticles(String author);
	
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
	 * @param tag name
	 * 
	 * @return all articles contain this tag
	 * 
	 * */
	public List<Article> getTagDistribution(String tagName);
	
	/**
	 * @param author name
	 * 
	 * @return one author's latest article
	 * 
	 * */
	public Article getOneAuthorLatestArticle(String author);
	
	/**
	 * @param author name
	 * 
	 * @return one author's top10 latest articles
	 * 
	 * */
	public List<Article> getOneAuthorTop10LatestArticles(String author);
	
	/**
	 * @return one latest article of all
	 * 
	 * */
	public Article getLatestArticlesOfAll();
	
	/**
	 * @return top10 latest articles of all
	 * 
	 * */
	public List<Article> getTop10LatestArticlesOfAll();
	
	/**
	 * 
	 * search for the tag named accordingly
	 * 
	 * @param tag name
	 * 
	 * @return Tag object with this name 
	 * 			
	 * */
	public Tag findOrCreateByNameAndArticleId(String name, String articleId);
	
	/**
	 * only consider top 5 tags
	 * 
	 * @param articleId
	 * 
	 * @return List<Tag> -> a list of tags belong to one article
	 * 
	 * */
	public List<Tag> getTagsOfOneArticle(String articleId);
	
	/**
	 * 
	 * @param tag name
	 * 
	 * @return String[] -> an array of articleIds reflecting article contains the tag
	 * 
	 * */
	public String[] getArticlesByTag(String tagName);
	
	/**
	 * @return List<Article> -> all articles in the database
	 * 
	 * */
	public List<Article> getAllArticles();
	
	/**
	 * 
	 * @return String latestArticleId -> the articleId of the latest published article
	 * 
	 * */
	public String getLatestArticleId();
	
	/**
	 * @param articleId & commentId, which form the primary key for table nathan_comment
	 * 
	 * @return publish date of one comment
	 * */
	public Date getDateOfComment(String articleId, String commentId);
	
	/**
	 * @param articleId
	 * 
	 * @return latest comment of one article
	 * */
	public Comment getLatestCommentOfOneArticle(String articleId);
	
	/**
	 * @param articleId
	 * 
	 * @return 1: the article specified has been successfully deleted
	 * 		   0: nothing has been deleted
	 * 		  -1: nothing has been deleted and errors have been made 
	 * */
	public int deleteArticle(String articleId);
	
	/**
	 * @param articleId
	 * 
	 * @return 1: the article specified has been successfully created
	 * 		   0: nothing has been created
	 * 		  -1: nothing has been created and errors have been made 
	 * */
	public int createArticle(String articleId);
	
	/**
	 * @param articleId
	 * 
	 * @return 1: the article specified has been successfully edited
	 * 		   0: nothing has been edited
	 * 		  -1: nothing has been edited and errors have been made 
	 * */
	public int editArticle(String articleId);
}
