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
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.nathanchen.model.BlogSearchResult;

public class GlobalSearchManager 
{
	private String searchWord;
	
	private IndexManager indexManager;
	
	private Analyzer analyser;
	
	private String searchField;
	
	public GlobalSearchManager(String searchWord)
	{
		this.searchWord = searchWord;
		this.indexManager = new IndexManager();
		this.analyser = new PaodingAnalyzer();
	}
	
	public GlobalSearchManager(String searchField, String searchWord)
	{
		this.searchField = searchField;
		this.searchWord = searchWord;
		this.indexManager = new IndexManager();
		this.analyser = new PaodingAnalyzer();
	}
	
	public List<BlogSearchResult> globalSearch()
	{
		List<BlogSearchResult> searchResult = new ArrayList<BlogSearchResult>();
		Directory index = null;
		IndexReader reader = null;
		int hitsPerPage = 10;
		
		if(false == indexManager.ifIndexExist())
		{
			
			System.out.println("indexManager.ifIndexExist()");
			try
			{
				if(false == indexManager.createIndex())
				{
					return searchResult;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return searchResult;
			}
		}
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
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try 
		{
			Query query = new QueryParser(Version.LUCENE_33, "Title", analyser).parse(searchWord);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
			indexSearcher.search(query, collector);
		    ScoreDoc[] hits = collector.topDocs().scoreDocs;
		    for(int i = 0; i < hits.length; i++) 
		    {
		        int docId = hits[i].doc;
		        Document d = indexSearcher.doc(docId);
		        BlogSearchResult result = new BlogSearchResult();
				result.setArticleId(d.get("ArticleId"));
				result.setTitle(d.get("Title"));
				result.setPath(d.get("Path"));
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
