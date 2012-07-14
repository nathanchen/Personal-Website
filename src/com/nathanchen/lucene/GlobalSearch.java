package com.nathanchen.lucene;

import java.io.File;
import java.io.IOException;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.nathanchen.lucene.xml.IndexManagerXML;


public abstract class GlobalSearch
{
	protected String	searchWord;
	protected Analyzer	analyser;
	protected String	searchField;


	public GlobalSearch(String searchWord)
	{
		this.searchWord = searchWord;
		this.analyser = new PaodingAnalyzer();
	}


	public GlobalSearch(String searchField, String searchWord)
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

}
