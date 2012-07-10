package com.nathanchen.lucene.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.nathanchen.model.Article;
import com.nathanchen.model.BlogSearchIndexResult;
import com.nathanchen.model.Comment;
import com.nathanchen.model.Tag;

public class IndexManagerSql 
{
	String indexDir = "";
	
	public boolean createGlobalIndex(ArrayList<BlogSearchIndexResult> blogSearchIndexResultList) throws IOException
	{
		if(true == ifIndexExist())
		{
			return true;
		}
		if((null == blogSearchIndexResultList))
		{
			return false;
		}
		File indexDestination = new File(indexDir);
		Directory fsDirectory = FSDirectory.open(indexDestination);
		Analyzer analyser = new StandardAnalyzer(Version.LUCENE_33);
		IndexWriterConfig iwc = new IndexWriterConfig (Version.LUCENE_33, analyser);
		IndexWriter myWriter = new IndexWriter(fsDirectory, iwc);
		
		if(null != allArticles)
		{
			for(Article eachArticle : allArticles)
			{
				
			}
		}
	}
	
	public boolean ifIndexExist()
	{
		File directory = new File(indexDir);
		if(directory.listFiles().length > 0)
		{
			return true;
		}
		else
			return false;
	}
	
}
