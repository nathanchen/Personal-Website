package com.nathanchen.lucene.database;

import java.util.ArrayList;
import java.util.List;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;

import com.nathanchen.model.BlogSearchResult;


public class GlobalSearchManagerSql
{
	private String			searchWord;
	private IndexManagerSql	indexManager;
	private Analyzer		analyser;
	private String			searchField;


	public GlobalSearchManagerSql(String searchWord)
	{
		this.searchWord = searchWord;
		this.indexManager = new IndexManagerSql();
		this.analyser = new PaodingAnalyzer();
	}


	public GlobalSearchManagerSql(String searchField, String searchWord)
	{
		this.searchField = searchField;
		this.searchWord = searchWord;
		this.indexManager = new IndexManagerSql();
		this.analyser = new PaodingAnalyzer();
	}

	
	public List<BlogSearchResult> globalSearch()
	{
		List<BlogSearchResult> searchResult = new ArrayList<BlogSearchResult>();
		Directory index = null;
		
	}
}
