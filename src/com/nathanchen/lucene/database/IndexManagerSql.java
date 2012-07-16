package com.nathanchen.lucene.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

import com.nathanchen.dao.UtilsDao;
import com.nathanchen.dao.UtilsDaoImpl;
import com.nathanchen.lucene.IndexManager;
import com.nathanchen.model.BlogSearchIndexResult;
import org.apache.log4j.Logger;


public class IndexManagerSql extends IndexManager
{
	public IndexManagerSql()
	{
		logger = Logger.getLogger(IndexManagerSql.class);
	}


	/**
	 * 强制建立索引
	 * 
	 * */ 
	public boolean createGlobalIndex(
			ArrayList<BlogSearchIndexResult> blogSearchIndexResultList,
			boolean overwrite)
	{
		if (ifIndexExist() && !overwrite)
		{
			logger.info("尝试建立索引不成功 ---------- 索引已存在 ---------- createGlobalIndexIfNotExisted(ArrayList<BlogSearchIndexResult> blogSearchIndexResultList)");
			return true;
		}
		
		logger.info("强制建立索引开始 ---------- createGlobalIndexForce( ArrayList<BlogSearchIndexResult> blogSearchIndexResultList, boolean overwrite)");
		try
		{
			return createGlobalIndex(blogSearchIndexResultList);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}


	private boolean createGlobalIndex(
			ArrayList<BlogSearchIndexResult> blogSearchIndexResultList)
			throws IOException, CorruptIndexException,
			LockObtainFailedException
	{
		if ((null == blogSearchIndexResultList))
		{
			return false;
		}

		File indexDestination = new File(indexDir);
		Directory fsDirectory = FSDirectory.open(indexDestination);
		Analyzer analyser = new StandardAnalyzer(Version.LUCENE_33);
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_33,
				analyser);
		IndexWriter myWriter = new IndexWriter(fsDirectory, iwc);

		for (BlogSearchIndexResult indexResult : blogSearchIndexResultList)
		{
			addDocument(indexResult, myWriter);
		}
		myWriter.optimize();
		myWriter.close();
		logger.info("建立索引成功 ---------- createGlobalIndex(ArrayList<BlogSearchIndexResult> blogSearchIndexResultList)");
		return true;
	}

	private ArrayList<BlogSearchIndexResult> parseAllFiles()
	{
		UtilsDao utilDao = new UtilsDaoImpl();
		return (ArrayList<BlogSearchIndexResult>) utilDao.getBlogSearchIndexResultList();
	}

}
