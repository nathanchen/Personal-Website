package com.nathan.model;

import java.util.Date;
import java.util.List;

public class Article 
{
	private String articleId;
	private String author;
	private Date date;
	private String articleBody;
	private List<Tag> tags;
	private String title;
	private int numberOfComments;
	private String authorOfLatestComment;
	private int numberOfTags;
	
	
	public Article()
	{
		
	}
	public Article(String articleId, String author, Date date, String articleBody, List<Tag> tags, String title, int numberOfTags)
	{
		this.articleBody = articleBody;
		this.author = author;
		this.date = date;
		this.articleId = articleId;
		this.tags = tags;
		this.title = title;
		this.numberOfTags = numberOfTags;
	}
	public Article(String articleId, String author, Date date, 
			String articleBody, List<Tag> tags, String title, 
			int numberOfComments, String authorOfLatestComment, int numberOfTags)
	{
		this.articleBody = articleBody;
		this.author = author;
		this.date = date;
		this.articleId = articleId;
		this.tags = tags;
		this.title = title;
		this.numberOfComments = numberOfComments;
		this.authorOfLatestComment = authorOfLatestComment;
		this.numberOfTags = numberOfTags;
	}
	
	
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getArticleBody() {
		return articleBody;
	}
	public void setArticleBody(String articleBody) {
		this.articleBody = articleBody;
	}
	public List<Tag> getTags() {
		return tags;
	}
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getNumberOfComments() {
		return numberOfComments;
	}
	public void setNumberOfComments(int numberOfComments) {
		this.numberOfComments = numberOfComments;
	}
	public String getAuthorOfLatestComment() {
		return authorOfLatestComment;
	}
	public void setAuthorOfLatestComment(String authorOfLatestComment) {
		this.authorOfLatestComment = authorOfLatestComment;
	}
	
	public int getNumberOfTags() {
		return numberOfTags;
	}
	public void setNumberOfTags(int numberOfTags) {
		this.numberOfTags = numberOfTags;
	}
	boolean isEmpty()
	{
		boolean result = false;
		if(this.getArticleBody().trim().equalsIgnoreCase("") || this.getArticleBody().isEmpty())
		{
			result = true;
		}
		return result;
	}
}
