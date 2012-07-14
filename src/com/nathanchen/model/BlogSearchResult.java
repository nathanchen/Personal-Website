package com.nathanchen.model;

public class BlogSearchResult 
{
	String path;
	String title;
	String articleId;
	
	public String getPath() {
		return path;
	}
	public void setPath(String articleId) {
		this.path = "/blog/articleId=" + articleId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
}
