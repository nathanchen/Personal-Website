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
			<h2>请选择要编辑的栏目</h2>
			<table>
				<thead>
					<tr>
						<th>Object Type</th>
						<s:url var="adminShowAllPostsInfo" action="adminShowAllPostsInfo" namespace="/struts">
						</s:url>
						<th width="20%"><s:a href="%{adminShowAllPostsInfo}">Posts</s:a></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><a href=""> </a></td>
						<td class="crudNew">
						<s:url var="adminAddNewPost" action="adminAddNewPost" namespace="/struts">
						</s:url>
						<s:a href="%{adminAddNewPost}">写一篇新文章</s:a>
						</td>
					</tr>
				</tbody>
			</table>

		</div>
	</div>

	<s:include value="adminFooter.jsp"></s:include>
</body>
</html>