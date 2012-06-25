package com.nathanchen.lucene;

import java.io.File;
import java.io.IOException;


import net.paoding.analysis.analyzer.PaodingAnalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * if(no bugs){
 *	author = @author NATHAN;
 * }
 * else{
 *	author = "God knows"
 * }
 *
 *	natechen@me.com
 * 	9:59:28 PM
 *	Feb 17, 2012
 */
public class IndexManager 
{
	
	private final String dataDir = "/Users/NATHAN/Programming/ForFun/Website/Article Files_original";
	
	private final String indexDir = "/Users/NATHAN/Programming/ForFun/Website/Article Files_indexed";
	
	public boolean createIndex() throws IOException
	{
		if(true == ifIndexExist())
		{
			return true;
		}
		File dir = new File(dataDir);
		if(!dir.exists())
		{
			return false;
		}
		File[] xmls = dir.listFiles();
		File destination = new File(indexDir);
		Directory fsDirectory = FSDirectory.open(destination);
		Analyzer analyser = new PaodingAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig (Version.LUCENE_33, analyser);
		IndexWriter myWriter = new IndexWriter(fsDirectory, iwc);
		for(int i = 0; i < xmls.length; i++)
		{
			String articlePath = xmls[i].getAbsolutePath();
			if(articlePath.endsWith("xml"))
			{
				addDocument(articlePath, myWriter);
			}
		}
		myWriter.optimize();
		myWriter.close();
		return true;
	}
	
	public void addDocument(String articlePath, IndexWriter indexWriter)
	{
		ArticleParserXML articleParser = new ArticleParserXML(articlePath);
		String title = articleParser.getElement("Title");
		String tags = articleParser.getElement("Tag"); 
		String content = articleParser.getElement("ArticleBody");
		String comments = articleParser.getElement("Comment");
		String path = articleParser.getPath();
		String articleId = articleParser.getElement("ArticleId");
		
		Document document = new Document();
		document.add(new Field("Title", title, Field.Store.YES, Field.Index.ANALYZED));
		document.add(new Field("Tags", tags, Field.Store.YES, Field.Index.ANALYZED));
		document.add(new Field("Content", content, Field.Store.YES, Field.Index.ANALYZED));
		document.add(new Field("Comments", comments, Field.Store.YES, Field.Index.ANALYZED));
		document.add(new Field("Path", path, Field.Store.YES, Field.Index.NO));
		document.add(new Field("ArticleId", articleId, Field.Store.YES, Field.Index.ANALYZED));
		
		try
		{
			indexWriter.addDocument(document);
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
	
	public String getDataDir()
	{
		return this.dataDir;
	}
	
	public String getIndexDir()
	{
		return this.indexDir;
	}

}
