package com.nathanchen.lucene;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;

import com.nathanchen.model.BlogSearchIndexResult;

public abstract class IndexManager
{
	protected String	indexDir	= "/Users/NATHAN/Programming/ForFun/Personal-Website/Article Files_indexed";
	protected Logger	logger	;

	protected void addDocument(BlogSearchIndexResult indexResult,
			IndexWriter indexWriter)
	{
		Document document = new Document();

		String articleId = indexResult.getArticleId();
		String author = indexResult.getAuthor();
		String articleBody = indexResult.getArticleBody();
		String title = indexResult.getTitle();
		String commenter = indexResult.getCommenter();
		String commentBody = indexResult.getCommentBody();
		String tagName = indexResult.getTagName();

		document.add(new Field("articleId", articleId, Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		document.add(new Field("author", author, Field.Store.YES,
				Field.Index.ANALYZED));
		document.add(new Field("articleBody", articleBody, Field.Store.YES,
				Field.Index.ANALYZED));
		document.add(new Field("title", title, Field.Store.YES,
				Field.Index.ANALYZED));
		document.add(new Field("commenter", commenter, Field.Store.YES,
				Field.Index.ANALYZED));
		document.add(new Field("commentBody", commentBody, Field.Store.YES,
				Field.Index.ANALYZED));
		document.add(new Field("tagName", tagName, Field.Store.YES,
				Field.Index.ANALYZED));

		try
		{
			indexWriter.addDocument(document);
		}
		catch (Exception e)
		{
			logger.error("文档添加到indexWriter失败 ---------- " + document.getFields() + " ---------- createGlobalIndex(ArrayList<BlogSearchIndexResult> blogSearchIndexResultList)");
			e.printStackTrace();
		}
	}


	protected boolean ifIndexExist()
	{
		File directory = new File(indexDir);
		if (directory.listFiles().length > 0)
		{
			return true;
		}
		else
			return false;
	}
}
