<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; UTF-8">
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" type="text/css" media="screen"
	href="../stylesheets/crud.css" />
<link rel="icon" type="image/ico" href="../images/favicon.ico"></link>
<link rel="shortcut icon" href="../images/favicon.ico"></link>
<title>Administration</title>
</head>
<body>
	<s:include value="adminHeader.jsp"></s:include>
	<div id="crudContent">
		<div id="crudIndex">
			<h2>请选择要编辑的栏目</h2>
			<table>
				<thead>
					<tr>
						<th>Object Type</th>
						<th width="20%"></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<s:url var="adminShowAllPostsInfo" action="adminShowAllPostsInfo"
							namespace="/struts">
						</s:url>
						<td><s:a href="%{adminShowAllPostsInfo}">Posts</s:a></td>
						<td class="crudNew"><s:url var="adminEditBlog"
								action="adminEditBlog" namespace="/struts">
								<s:param name="articleId">
							-1
						</s:param>
							</s:url> <s:a href="%{adminEditBlog}">
						编写新文章
					</s:a></td>
					</tr>
				</tbody>
			</table>

		</div>
	</div>

	<s:include value="adminFooter.jsp"></s:include>
</body>
</html>