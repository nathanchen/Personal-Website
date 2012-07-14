package com.nathanchen.lucene.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.util.Version;

import com.nathanchen.lucene.GlobalSearch;
import com.nathanchen.model.BlogSearchResult;


public class GlobalSearchManagerXML extends GlobalSearch
{
	protected IndexManagerXML	indexManager;


	public GlobalSearchManagerXML(String searchWord)
	{
		super(searchWord);
		this.indexManager = new IndexManagerXML();
	}


	public GlobalSearchManagerXML(String searchField, String searchWord)
	{
		super(searchField, searchWord);
		this.indexManager = new IndexManagerXML();
	}


	public List<BlogSearchResult> globalSearch()
	{
		List<BlogSearchResult> searchResult = new ArrayList<BlogSearchResult>();

		if (false == indexManager.ifIndexExist())
		{
			System.out.println("indexManager.ifIndexExist()");
			try
			{
				if (false == indexManager.createIndex())
				{
					return searchResult;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return searchResult;
			}
		}

		IndexSearcher indexSearcher = getIndexSearcher();

		try
		{
			String[] fields = { "articleId", "author", "articleBody", "title",
					"commenter", "commentBody", "tagName" };
			BooleanClause.Occur[] flags = { BooleanClause.Occur.SHOULD,
					BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
					BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
					BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD, };

			Query query = MultiFieldQueryParser.parse(Version.LUCENE_33,
					searchWord, fields, flags, analyser);
			ScoreDoc[] hits = searchHitsCollector(indexSearcher, query);
			for (int i = 0; i < hits.length; i++)
			{
				int docId = hits[i].doc;
				Document d = indexSearcher.doc(docId);
				BlogSearchResult result = new BlogSearchResult();
				result.setArticleId(d.get("articleId"));
				result.setTitle(d.get("title"));
				result.setPath(d.get("articleId"));
				searchResult.add(result);
			}
			indexSearcher.close();
		}
		catch (ParseException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return searchResult;
	}
}
