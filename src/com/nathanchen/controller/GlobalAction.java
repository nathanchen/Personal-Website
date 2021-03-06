package com.nathanchen.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.nathanchen.dao.UserDaoBlog;
import com.nathanchen.dao.DaoFactory;
import com.nathanchen.lucene.GlobalSearchManager;
import com.nathanchen.lucene.database.GlobalSearchManagerSql;
import com.nathanchen.model.Article;
import com.nathanchen.model.Comment;
import com.nathanchen.model.BlogSearchResult;
import com.opensymphony.xwork2.ActionSupport;

public class GlobalAction extends ActionSupport implements SessionAware {

	private Map session;

	private UserDaoBlog blogUserDao;
	// index.action
	private Article latestArticleOfAll;
	private int numberOfComments;
	private String authorOfLatestComment;
	private List<Article> top10ArticlesOfAll;

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

	public String execute() {
		return INPUT;
	}

	/**
	 * 显示所有blog
	 * 
	 * 第一篇blog加亮，其后缩略呈现 
	 * */
	 
	public String index() {
		try {
			if (blogUserDao == null) {
				blogUserDao = DaoFactory.getInstance().getBlogUserDao();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		latestArticleOfAll = blogUserDao.getLatestArticlesOfAll();
		numberOfComments = Integer.parseInt(blogUserDao
				.getNumberOfCommentsOfOneArticle(latestArticleOfAll
						.getArticleId()));
		authorOfLatestComment = blogUserDao
				.getAuthorOfLatestCommentOfOneArticle(latestArticleOfAll
						.getArticleId());
		top10ArticlesOfAll = blogUserDao.getTop10LatestArticlesOfAll();
		return SUCCESS;
	}

	/**
	 * 进入一篇blog查看详情
	 * 
	 * @param articleId
	 * */
	public String gotoBlog() {
		try {
			if (blogUserDao == null) {
				blogUserDao = DaoFactory.getInstance().getBlogUserDao();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		eachBlog = blogUserDao.getArticle(articleId);

		eachBlogComment = blogUserDao.getCommentsOfOneArticle(articleId);
		hasError = isError;
		return SUCCESS;
	}

	/**
	 * 添加评论 
	 * 
	 * */
	public String postComment() {
		try {
			if (blogUserDao == null) {
				blogUserDao = DaoFactory.getInstance().getBlogUserDao();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String bb = blogComment.getArticleId();
		articleId = bb;
		blogCommentId = Integer.parseInt(blogUserDao
				.getLatestCommentOfOneArticle(bb).getCommentId()) + 1 + "";
		blogUserDao.createComment(blogComment, bb, blogCommentId);
		System.out.println(blogComment.getName() + " "
				+ blogComment.getMessage());
		blogCommentName = blogComment.getName();
		return SUCCESS;
	}

	/**
	 * 搜索
	 * 
	 * */
	public String globalSearch() {
		searchResults = new ArrayList<Article>();
		String searchWord = keyword;
		GlobalSearchManager searchManager = new GlobalSearchManagerSql(searchWord);
		ArrayList<BlogSearchResult> temp = (ArrayList<BlogSearchResult>) searchManager
				.globalSearch();
		for (BlogSearchResult gsr : temp) {
			searchResults.add(assignGlobalSearchResult(gsr.getArticleId()));
		}
		return SUCCESS;
	}

	/**
	 * 根据文章tag找含有相同tag的文章
	 * 
	 * */
	public String similarPosts() {
		similarPosts = new ArrayList<Article>();
		try {
			if (blogUserDao == null) {
				blogUserDao = DaoFactory.getInstance().getBlogUserDao();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (blogUserDao.getArticlesByTag(queryTag) != null) {
			String[] array = blogUserDao.getArticlesByTag(queryTag);
			for (String str : array) {
				Article article = blogUserDao.getArticle(str);
				similarPosts.add(article);
			}
			return SUCCESS;
		}
		return ERROR;
	}

	/**
	 * 
	 * 
	 * */
	public Article assignGlobalSearchResult(String articleId) {
		try {
			if (blogUserDao == null) {
				blogUserDao = DaoFactory.getInstance().getBlogUserDao();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Article article = blogUserDao.getArticle(articleId);
		return article;
	}

	
	/**********************************************************************************************************/
	// Getters and Setters
	/**********************************************************************************************************/
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

	public UserDaoBlog getBlogUserDao() {
		return blogUserDao;
	}

	public void setBlogUserDao(UserDaoBlog blogUserDao) {
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
