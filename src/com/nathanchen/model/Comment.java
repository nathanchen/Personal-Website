package com.nathanchen.model;

import java.util.Date;


public class Comment
{
	private String	articleId;	// articleId is the primary key
	private String	name;		// guests are not allowed to make comments if
								// they haven't logged in
	private Date	date;
	private String	message;
	private String	commentId;


	public Comment()
	{

	}


	public Comment(String articleId, String name, Date date, String message,
			String commentId)
	{
		this.articleId = articleId;
		this.name = name;
		this.date = date;
		this.message = message;
		this.commentId = commentId;
	}


	public String getArticleId()
	{
		return articleId;
	}


	public void setArticleId(String articleId)
	{
		this.articleId = articleId;
	}


	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}


	public Date getDate()
	{
		return date;
	}


	public void setDate(Date date)
	{
		this.date = date;
	}


	public String getMessage()
	{
		return message;
	}


	public void setMessage(String message)
	{
		this.message = message;
	}


	public String getCommentId()
	{
		return commentId;
	}


	public void setCommentId(String commentId)
	{
		this.commentId = commentId;
	}

}
