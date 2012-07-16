package com.nathanchen.lucene.database;


import com.nathanchen.lucene.GlobalSearchManager;


public class GlobalSearchManagerSql extends GlobalSearchManager
{

	public GlobalSearchManagerSql(String searchWord)
	{
		super(searchWord);
		this.indexManager = new IndexManagerSql();
	}


	public GlobalSearchManagerSql(String searchField, String searchWord)
	{
		super(searchField, searchWord);
		this.indexManager = new IndexManagerSql();
	}
}
