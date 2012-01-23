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
	
	
	public Article()
	{
		
	}
	public Article(String articleId, String author, Date date, String articleBody, List<Tag> tags)
	{
		this.articleBody = articleBody;
		this.author = author;
		this.date = date;
		this.articleId = articleId;
		this.tags = tags;
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
}
