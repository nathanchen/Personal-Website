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
		<div id="crudShow" class="${type.name}">

			<h2 id="crudShowTitle">编辑文章</h2>

			<div class="objectForm">
				<s:form action="postArticle" theme="simple" method="post"
					namespace="/struts">
					<div class="crudField">
						<s:if test="article.articleId gt -1">
							<input type="hidden" name="article.articleId"
								value="<s:property
						value="article.articleId" />" />
						</s:if>
						<br /> <label>文章标题: </label>
						<s:textfield name="article.title" value="<s:property
						value="article.title"  size="50"/>"></s:textfield>
						<label>发表时间: </label>
						<s:textfield name="article.date" value="<s:property
						value="article.date" size="10" />"></s:textfield>
						<label>文章正文: </label>
						<s:textarea name="article.articleBody" value="<s:property
						value="article.articleBody" size="50"/>"></s:textarea>
						<label>文章作者: </label>
						<s:textarea name="article.author" value="<s:property
						value="article.author" size="50"/>"></s:textarea>
						<label>文章标签: </label>
					</div>
					<p class="crudButtons">
						<s:submit value="提交您的评论"></s:submit>
						<s:reset value="重置"></s:reset>
					</p>
				</s:form>
			</div>

			<s:form action="deleteArticle" theme="simple" method="post"
					namespace="/struts">
			<p class="crudDelete">
				<s:submit value="删除文章"></s:submit>
			</p>
			</s:form>

		</div>
	</div>

	<s:include value="adminFooter.jsp"></s:include>
</body>
</html>