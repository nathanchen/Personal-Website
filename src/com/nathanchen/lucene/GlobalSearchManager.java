package com.nathanchen.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.nathanchen.lucene.xml.IndexManagerXML;
import com.nathanchen.model.BlogSearchResult;


public abstract class GlobalSearchManager
{
	protected String	searchWord;
	protected Analyzer	analyser;
	protected String	searchField;
	protected IndexManager	indexManager;

	public GlobalSearchManager(String searchWord)
	{
		this.searchWord = searchWord;
		this.analyser = new PaodingAnalyzer();
	}


	public GlobalSearchManager(String searchField, String searchWord)
	{
		this.searchField = searchField;
		this.searchWord = searchWord;
		this.analyser = new PaodingAnalyzer();
	}



	protected ScoreDoc[] searchHitsCollector(IndexSearcher indexSearcher,
			Query query) throws IOException
	{
		TotalHitCountCollector counter = new TotalHitCountCollector();
		indexSearcher.search(query, counter);

		TopScoreDocCollector collector = TopScoreDocCollector.create(
				counter.getTotalHits(), true);
		indexSearcher.search(query, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		return hits;
	}


	protected IndexSearcher getIndexSearcher()
	{
		Directory index = null;
		IndexReader reader = null;
		try
		{
			index = FSDirectory.open(new File(indexManager.getIndexDir()));
		}
		catch (IOException e2)
		{
			e2.printStackTrace();
		}
		try
		{
			reader = IndexReader.open(index);
		}
		catch (CorruptIndexException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		IndexSearcher indexSearcher = null;
		try
		{
			indexSearcher = new IndexSearcher(reader);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return indexSearcher;
	}
	
	public List<BlogSearchResult> globalSearch()
	{
		List<BlogSearchResult> searchResult = new ArrayList<BlogSearchResult>();

		if (false == indexManager.ifIndexExist())
		{
			System.out.println("indexManager.ifIndexExist()");
			try
			{
				if (false == indexManager.createGlobalIndex())
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
