package com.nathanchen.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.nathan.model.Article;
import com.nathan.model.Comment;
import com.nathan.model.GlobalSearchResult;
import com.nathan.model.Tag;
import com.nathanchen.dao.BlogUserDao;
import com.nathanchen.dao.DaoFactory;
import com.opensymphony.xwork2.ActionSupport;

public class GlobalAction extends ActionSupport implements SessionAware {

	private Map session;
	
	// index.action
	private Article latestArticleOfAll;
	private int numberOfComments;
	private String authorOfLatestComment;
	private List<Article> top10ArticlesOfAll;
	private BlogUserDao blogUserDao;
	
	// gotoBlog.action
	private Article eachBlog;
	private String articleId;
	private List<Comment> eachBlogComment;
	private int isError;
	private int hasError;
	
	// postComment.action
	private Comment blogComment;
	private String blogId;
	private String blogCommentId;
	private String blogCommentName;
	
	// similarPosts.action
	private List<Article> similarPosts;
	private String queryTag;
	
	// globalSearch.action
	private String keyword;
	private ArrayList<Article> searchResults;
	
	
	public String execute()
	{
		return INPUT;
	}
	
	public String index()
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
		latestArticleOfAll = blogUserDao.getLatestArticlesOfAll();
		numberOfComments = Integer.parseInt(blogUserDao.getNumberOfCommentsOfOneArticle(latestArticleOfAll.getArticleId()));
		authorOfLatestComment = blogUserDao.getAuthorOfLatestCommentOfOneArticle(latestArticleOfAll.getArticleId());
		top10ArticlesOfAll = blogUserDao.getTop10LatestArticlesOfAll();
		return SUCCESS;
	}
	
	public String gotoBlog()
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
		eachBlog = blogUserDao.getArticle(articleId);
		
		eachBlogComment = blogUserDao.getCommentsOfOneArticle(articleId);
		hasError = isError;
		return SUCCESS;
	}
	
	public String postComment()
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
		String bb = blogComment.getArticleId();
		articleId = bb;
		blogCommentId = Integer.parseInt(blogUserDao.getNumberOfCommentsOfOneArticle(bb)) + 1 + "";
		blogUserDao.createComment(blogComment, bb, blogCommentId);
		System.out.println(blogComment.getName() + " " + blogComment.getMessage());
		blogCommentName = blogComment.getName();
		return SUCCESS;
	}
	
	public String globalSearch()
	{
		searchResults = new ArrayList<Article>();
		String searchWord = keyword;
		GlobalSearchManager searchManager = new GlobalSearchManager(searchWord);
		ArrayList<GlobalSearchResult> temp = (ArrayList<GlobalSearchResult>) searchManager.globalSearch();
		for(GlobalSearchResult gsr : temp)
		{
			searchResults.add(assignGlobalSearchResult(gsr.getArticleId()));
		}
		return SUCCESS;
	}
	
	public String similarPosts()
	{
		similarPosts = new ArrayList<Article>();
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
		if(blogUserDao.getArticlesByTag(queryTag) != null)
		{
			String[] array = blogUserDao.getArticlesByTag(queryTag);
			for(String str : array)
			{
				Article article = blogUserDao.getArticle(str);
				similarPosts.add(article);
			}
			return SUCCESS;
		}
		return ERROR;
	}
	
	public Article assignGlobalSearchResult(String articleId)
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
		Article article = blogUserDao.getArticle(articleId);
		return article;
	}

	public Article getLatestArticleOfAll() {
		return latestArticleOfAll;
	}

	public void setLatestArticleOfAll(Article latestArticleOfAll) {
		this.latestArticleOfAll = latestArticleOfAll;
	}

	public List<Article> getTop10ArticlesOfAll() {
		return top10ArticlesOfAll;
	}

	public void setTop10ArticlesOfAll(List<Article> top10ArticlesOfAll) {
		this.top10ArticlesOfAll = top10ArticlesOfAll;
	}

	public BlogUserDao getBlogUserDao() {
		return blogUserDao;
	}

	public void setBlogUserDao(BlogUserDao blogUserDao) {
		this.blogUserDao = blogUserDao;
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
	public Article getEachBlog() {
		return eachBlog;
	}
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public List<Comment> getEachBlogComment() {
		return eachBlogComment;
	}
	public void setEachBlogComment(List<Comment> eachBlogComment) {
		this.eachBlogComment = eachBlogComment;
	}
	public void setEachBlog(Article eachBlog) {
		this.eachBlog = eachBlog;
	}
	public Comment getBlogComment() {
		return blogComment;
	}
	public void setBlogComment(Comment blogComment) {
		this.blogComment = blogComment;
	}
	public String getBlogId() {
		return blogId;
	}
	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}
	public String getBlogCommentId() {
		return blogCommentId;
	}
	public void setBlogCommentId(String blogCommentId) {
		this.blogCommentId = blogCommentId;
	}
	public String getBlogCommentName() {
		return blogCommentName;
	}
	public int getIsError() {
		return isError;
	}
	public void setIsError(int isError) {
		this.isError = isError;
	}
	public int getHasError() {
		return hasError;
	}
	public void setHasError(int hasError) {
		this.hasError = hasError;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setBlogCommentName(String blogCommentName) {
		this.blogCommentName = blogCommentName;
	}

	public String getQueryTag() {
		return queryTag;
	}

	public void setQueryTag(String queryTag) {
		this.queryTag = queryTag;
	}

	public List<Article> getSimilarPosts() {
		return similarPosts;
	}

	public void setSimilarPosts(List<Article> similarPosts) {
		this.similarPosts = similarPosts;
	}

	@Override
	public void setSession(Map<String, Object> value) {
		session = value;
	}

	public ArrayList<Article> getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(ArrayList<Article> searchResults) {
		this.searchResults = searchResults;
	}

}
