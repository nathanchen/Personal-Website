package com.nathanchen.lucene.xml;


import com.nathanchen.lucene.GlobalSearchManager;


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
