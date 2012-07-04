package com.nathanchen.dao;

import java.util.List;

import com.nathanchen.model.BlogSearchIndexResult;

public interface UtilsDao 
{
	/**
	 * 获取blog搜索的准备材料
	 * @return List<BlogSearchIndexResult>
	 * */
	public List<BlogSearchIndexResult> getBlogSearchIndexResultList();
	
}
