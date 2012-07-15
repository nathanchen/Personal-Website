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

import com.nathanchen.lucene.GlobalSearchManager;
import com.nathanchen.model.BlogSearchResult;


public class GlobalSearchManagerXML extends GlobalSearchManager
{
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


	
}
