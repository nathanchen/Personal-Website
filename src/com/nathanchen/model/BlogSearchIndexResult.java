package com.nathanchen.model;

public class BlogSearchIndexResult
{
	String	articleId;
	String	author;
	String	articleBody;
	String	title;
	String	commenter;
	String	commentBody;
	String	tagName;


	public String getArticleId()
	{
		return articleId;
	}


	public void setArticleId(String articleId)
	{
		this.articleId = articleId;
	}


	public String getAuthor()
	{
		return author;
	}


	public void setAuthor(String author)
	{
		this.author = author;
	}


	public String getArticleBody()
	{
		return articleBody;
	}


	public void setArticleBody(String articleBody)
	{
		this.articleBody = articleBody;
	}


	public String getTitle()
	{
		return title;
	}


	public void setTitle(String title)
	{
		this.title = title;
	}


	public String getCommenter()
	{
		return commenter;
	}


	public void setCommenter(String commenter)
	{
		this.commenter = commenter;
	}


	public String getCommentBody()
	{
		return commentBody;
	}


	public void setCommentBody(String commentBody)
	{
		this.commentBody = commentBody;
	}


	public String getTagName()
	{
		return tagName;
	}


	public void setTagName(String tagName)
	{
		this.tagName = tagName;
	}
}
