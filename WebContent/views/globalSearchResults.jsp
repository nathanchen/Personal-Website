<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib prefix="s" uri="/struts-tags"%>
<title>Nathan CHEN</title>
</head>
<body>
	<s:include value="headerTitle.jsp"></s:include>
	<div id="main">
		<s:if test="searchResults.size gt 0">
			<h3>有 <s:property value="searchResults.size()"/> 篇文章含有关键字“<s:property value="keyword"/>”</h3>
		</s:if>
		<s:else>
			<h3>没有文章含有关键字“<s:property value="keyword"/>”</h3>
		</s:else>
	
		<div class="older-posts">
				<s:iterator value="searchResults">
					<div class="post">
						<h2 class="post-title">
							<s:url var="gotoBlogUrl" action="gotoBlog" namespace="/struts">
		                		<s:param name="articleId"><s:property value ="articleId" /></s:param>
		                		<s:param name="isError">-1</s:param>
			                </s:url>
							<s:a href = "%{gotoBlogUrl}"> <s:property value="title" /></s:a>
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
</body>
</html>