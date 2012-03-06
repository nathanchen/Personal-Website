<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Nathan CHEN</title>
</head>
<body>
</head>
<body>
	<s:include value="headerTitle.jsp"></s:include>
	<div id="notepad">
		<ul class="nav nav-list">
			<li class="nav-header">文章分类</li>
			<li class="active"><a href="#"> <i class="icon-leaf"></i>&nbsp;Home
			</a></li>
			<li><a href="#"> <i class="icon-fire"></i>&nbsp;Library
			</a></li>
		</ul>
	</div>
	<div id="main">
		<div class="post">
			<h2 class="post-title">
				<s:url var="gotoBlogUrl" value="/blog/articleId=%{latestArticleOfAll.articleId}&amp;isError=1.html">
				</s:url>
				<s:a href="%{gotoBlogUrl}">
					<s:property value="latestArticleOfAll.title" />
				</s:a>
			</h2>

			<div class="post-metadata">
				<span class="post-author">by <s:property
						value="latestArticleOfAll.author" /></span> <span class="post-date"><s:property
						value="latestArticleOfAll.date" /></span> <span class="post-comments">
					&nbsp;|&nbsp; <s:property
						value="latestArticleOfAll.numberOfComments" /> 条评论 <s:if
						test="latestArticleOfAll.numberOfComments gt 0">
							 ,
							最新一条由 <s:property
							value="latestArticleOfAll.authorOfLatestComment" />  留下
						</s:if> <s:if test="latestArticleOfAll.numberOfTags gt 0">
							- 标签 &nbsp;
								<s:iterator value="latestArticleOfAll.tags">
								<s:url var="testUrl" value="/tagSearch/queryTag=%{tagName}.html">
								</s:url>
								<s:a href="%{testUrl}">
								<s:property value="tagName" />
								</s:a>
								</s:iterator>
					</s:if>
				</span>
			</div>
			<div class="post-content">
				<div class="about">Details:</div>
				<s:property escape="false" value="latestArticleOfAll.articleBody" />
			</div>
		</div>

		<div class="older-posts">
			<h3>
				之前的文章 <span class="from">from this blog</span>
			</h3>
			<s:iterator value="top10ArticlesOfAll">
				<div class="post">
					<h2 class="post-title">
					<s:url var="gotoBlogUrl" value="/blog/articleId=%{articleId}&amp;isError=1.html">
					</s:url>	
						<s:a href="%{gotoBlogUrl}">
							<s:property value="title" />
						</s:a>
					</h2>
					<div class="post-metadata">
						<span class="post-author"> by <s:property value="author" />
						</span> <span class="post-date"> <s:property value="date" />
						</span>
						<div class="post-comments">
							<s:property value="numberOfComments" />
							条评论
							<s:if test="numberOfComments gt 0">
								 - 
								最新一条由 <s:property value="authorOfLatestComment" />  留下
							</s:if>
						</div>
					</div>
				</div>
			</s:iterator>
		</div>
	</div>
	<s:include value="footer.jsp"></s:include>
</body>
</html>