package com.nathanchen.dao;

import java.util.List;
import java.util.ArrayList;

import com.nathanchen.model.Comment;
import com.nathanchen.model.Article;
import com.nathanchen.model.BlogSearchIndexResult;
import com.nathanchen.model.Tag;


public class UtilsDaoImpl implements UtilsDao
{

	/******* SQL statements go here *************************************************************************/
	StringBuffer	getBlogSearchIndexResultList;

	/***********************************************************************************************************/

	@Override
	public List<BlogSearchIndexResult> getBlogSearchIndexResultList()
	{
		List<BlogSearchIndexResult> blogSearchIndexResultList = new ArrayList<BlogSearchIndexResult>();
		try
		{
			UserDaoBlog userDaoBlog = new UserDaoBlogImpl();
			ArrayList<Article> allArticles = (ArrayList<Article>) userDaoBlog
					.getAllArticles();
			for (int i = 0; i < allArticles.size(); i++)
			{
				Article article = allArticles.get(i);
				String articleId = article.getArticleId();
				BlogSearchIndexResult blogSearchIndexResult = new BlogSearchIndexResult();
				blogSearchIndexResult.setArticleId(articleId);
				blogSearchIndexResult.setArticleBody(article.getArticleBody());
				blogSearchIndexResult.setAuthor(article.getAuthor());
				blogSearchIndexResult.setTitle(article.getTitle());

				blogSearchIndexResult = setCommentsInfoToIndexResult(
						userDaoBlog, articleId, blogSearchIndexResult);

				blogSearchIndexResult = setTagsInfoToIndexResult(userDaoBlog,
						articleId, blogSearchIndexResult);
				blogSearchIndexResultList.add(blogSearchIndexResult);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return blogSearchIndexResultList;
	}


	private BlogSearchIndexResult setTagsInfoToIndexResult(
			UserDaoBlog userDaoBlog, String articleId,
			BlogSearchIndexResult blogSearchIndexResult)
	{
		ArrayList<Tag> tagsOfOneArticle = (ArrayList<Tag>) userDaoBlog
				.getTagsOfOneArticle(articleId);
		String tagName = "";
		if (null != tagsOfOneArticle)
		{
			for (Tag tag : tagsOfOneArticle)
			{
				tagName = tagName + " " + tag.getTagName();
			}
		}
		blogSearchIndexResult.setTagName(tagName);
		return blogSearchIndexResult;
	}


	private BlogSearchIndexResult setCommentsInfoToIndexResult(
			UserDaoBlog userDaoBlog, String articleId,
			BlogSearchIndexResult blogSearchIndexResult)
	{
		ArrayList<Comment> commentsOfOneArticle = (ArrayList<Comment>) userDaoBlog
				.getCommentsOfOneArticle(articleId);
		String commenter = "", commentBody = "";
		if (null != commentsOfOneArticle)
		{
			for (Comment comment : commentsOfOneArticle)
			{
				commenter = commenter + " " + comment.getName();
				commentBody = commentBody + comment.getMessage();
			}
		}
		blogSearchIndexResult.setCommenter(commenter);
		blogSearchIndexResult.setCommentBody(commentBody);
		return blogSearchIndexResult;
	}
}
