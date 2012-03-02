package com.nathanchen.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	private Date dateOfLatestComment;
	
	private String published;
	
	public Article()
	{
		
	}
	
	public Article(String articleId, String author, Date date,
			String title, int numberOfComments, String authorOfLatestComment, Date dateOfLatestComment)
	{
		this.author = author;
		this.date = date;
		this.articleId = articleId;
		this.title = title;
		this.numberOfComments = numberOfComments;
		this.authorOfLatestComment = authorOfLatestComment;
		this.dateOfLatestComment = dateOfLatestComment;
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
	public Date getDate() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.parse(sdf.format(date));
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getArticleBody() {
		return articleBody.replaceAll("&nbsp;", " ");
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
	
	public Date getDateOfLatestComment() {
		return dateOfLatestComment;
	}

	public void setDateOfLatestComment(Date dateOfLatestComment) {
		this.dateOfLatestComment = dateOfLatestComment;
	}

	public String toString()
	{
		return this.getArticleId();
	}
	
	/**
	 * @throws ParseException 
	 * @deprecated
	 * 
	 * */
	public String getDateString() throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(this.getDate());
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}
	
//	public static void main (String[] args) throws ParseException
//	{
//		Article article = new Article();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		article.setDate(sdf.parse("2009-06-14"));
//		System.out.println(article.getDate().toString());
//		System.out.println(article.getDateString());
//	}
}
