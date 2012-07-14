package com.nathanchen.lucene.xml;

import java.io.File;
import java.io.IOException;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.nathanchen.lucene.IndexManager;
import com.nathanchen.model.BlogSearchIndexResult;


/**
 * if(no bugs){ author = @author NATHAN; } else{ author = "God knows" }
 * 
 * natechen@me.com 9:59:28 PM Feb 17, 2012
 */
public class IndexManagerXML extends IndexManager
{
	String	dataDir	= "";


	public boolean createIndex() throws IOException
	{
		if (true == ifIndexExist())
		{
			return true;
		}
		File dir = new File(dataDir);
		if (!dir.exists())
		{
			return false;
		}
		File[] xmls = dir.listFiles();
		File destination = new File(indexDir);
		Directory fsDirectory = FSDirectory.open(destination);
		Analyzer analyser = new PaodingAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_33,
				analyser);
		IndexWriter myWriter = new IndexWriter(fsDirectory, iwc);
		for (int i = 0; i < xmls.length; i++)
		{
			String articlePath = xmls[i].getAbsolutePath();
			if (articlePath.endsWith("xml"))
			{
				BlogSearchIndexResult indexResult = parseXmlFile(articlePath);
				addDocument(indexResult, myWriter);
			}
		}
		myWriter.optimize();
		myWriter.close();
		return true;
	}


	private BlogSearchIndexResult parseXmlFile(String articlePath)
	{
		BlogSearchIndexResult indexResult = new BlogSearchIndexResult();
		ArticleParserXML articleParser = new ArticleParserXML(articlePath);

		indexResult.setArticleId(articleParser.getElement("articleId"));
		indexResult.setAuthor(articleParser.getElement("author"));
		indexResult.setArticleBody(articleParser.getElement("articleBody"));
		indexResult.setTitle(articleParser.getElement("title"));
		indexResult.setCommenter(articleParser.getElement("commenter"));
		indexResult.setCommentBody(articleParser.getElement("commentBody"));
		indexResult.setTagName(articleParser.getElement("tag"));
		return indexResult;
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
