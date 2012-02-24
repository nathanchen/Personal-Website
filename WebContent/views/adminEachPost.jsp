<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; UTF-8">
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript" src="../ueditor/editor_config.js"></script>
<script type="text/javascript" src="../ueditor/editor_all.js"></script>
<link rel="stylesheet" href="../ueditor/themes/default/ueditor.css" />
<title>Administration</title>
</head>
<body id="crud">
	<s:include value="adminHeader.jsp"></s:include>

	<div id="crudContent">
		<div id="crudShow">

			<h2 id="crudShowTitle">编辑文章</h2>

			<div class="objectForm">
				<s:form action="postArticle" theme="simple" method="post"
					namespace="/struts">
					<div class="crudField">
						<table>
							<s:if test="oneArticle.articleId gt -1">
								<input type="hidden" name="article.articleId"
									value="<s:property
						value="oneArticle" />" />
							</s:if>
							<tr>
								<label>文章标题: </label>
								<s:textfield name="oneArticle.title" size="50"></s:textfield>
								<br />
								<br />
							</tr>
							<tr>
								<label>发表时间: </label>
								<s:textfield name="oneArticle.date" size="10"></s:textfield>
								<br />
								<br />
							</tr>
							<tr>
								<label>文章作者: </label>
								<s:textfield name="oneArticle.author" size="50"></s:textfield>
								<br />
								<br />
							</tr>
							<tr>
								<label>文章标签: </label>
								<s:textfield name="oneArticle.date" size="10"></s:textfield>
								<br />
								<br />
							</tr>
							<tr width="1000px">
								<label>文章正文: </label>
									<script type="text/plain" id="myEditor"><s:property value="oneArticle.articleBody" /></script>
									<script type="text/javascript">
										var editor = new baidu.editor.ui.Editor();
										
										editor.render("myEditor");
									</script>
								<br />
								<br />
							</tr>
						</table>
					</div>
					<p class="crudButtons">
						<s:submit value="提交您的文章"></s:submit>
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