<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; UTF-8">
<%@ taglib prefix="s" uri="/struts-tags"%>
<title>Administration</title>
</head>
<body id="crud">
	<s:include value="adminHeader.jsp"></s:include>

	<div id="crudContent">
		<div id="crudIndex">
			<div id="crudList" class="">

				<h2 id="crudListTitle">Posts</h2>

				<div id="crudListSearch">
					<s:include value="adminSearch.jsp"></s:include>
				</div>

				<div id="crudListTable">
					<table>
						<thead>
							<tr>
								<th>标题</th>
								<th>作者</th>
								<th>发表时间</th>
								<th>评论数量</th>
								<th>最新评论作者</th>
								<th>最新评论时间</th>
							</tr>
						</thead>
						<s:if test="allPosts.size gt 0">
							<s:iterator value="allPosts">
								<input type="hidden" name="articleId"
									value="<s:property
						value="articleId" />" />
								<tr>
									<td><s:url var="adminEditBlog" action="adminEditBlog"
											namespace="/struts">
											<s:param name="articleId">
												<s:property value="articleId" />
											</s:param>
										</s:url> <s:a href="%{adminEditBlog}">
											<s:property value="title" />
										</s:a></td>
									<td><s:property value="author" /></td>
									<td><s:property value="date" /></td>
									<td><s:property value="numberOfComments" /></td>
									<td><s:property value="authorOfLatestComment" /></td>
									<td><s:property value="dateOfLatestComment" /></td>
								</tr>
							</s:iterator>
						</s:if>
						<s:else>
							<tr>
								<td>暂时没有文章</td>
							</tr>
						</s:else>
					</table>

				</div>

				<div id="crudListPagination"></div>

				<p id="crudListAdd">
					<s:url var="adminEditBlog" action="adminEditBlog"
						namespace="/struts">
						<s:param name="articleId">
							-1
						</s:param>
					</s:url>
					<s:a href="%{adminEditBlog}">
						编写新文章
					</s:a>
				</p>

			</div>
		</div>
	</div>

	<s:include value="adminFooter.jsp"></s:include>
</body>
</html>