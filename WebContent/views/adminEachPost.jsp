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
<script language="JavaScript" type="text/javascript">
	var asyncRequest;
	var idid;
	function deleteCommentJS(articleId, commentId) {
		var url = "/Website/struts/deleteComment.action?articleId=" + articleId + "&commentId=" + commentId ;
		idid = articleId;
		try {
			asyncRequest = new XMLHttpRequest();
			asyncRequest.open('GET', url, true);
			asyncRequest.onreadystatechange = processResponse;
			asyncRequest.send(null);
		} catch (exception) {
			alert('Request failed !');
		}
	}
	function processResponse() {
		if (asyncRequest.readyState == 4 && asyncRequest.status == 200) {
			var data = eval("(" + asyncRequest.responseText + ")");
			var tableContent = document.getElementById("deleteCommentAjax");

			while (tableContent.lastChild) {
				tableContent.removeChild(tableContent.lastChild);
			}
			if (data.length < 1) {
				var newRow = document.createElement("tr");
				var newCell = document.createElement("td");
				newCell.innerHTML = "没有评论";
				newRow.appendChild(newCell);
				tableContent.appendChild(newRow);
			} else {
				for ( var i = 0; i < data.length; i++) {
					var newRow = document.createElement("tr");
					
					var input = document.createElement("input");
					input.type = "hidden";
					input.name = "commentId";
					input.value = data[i].commentId;
					newRow.appendChild(input);

					var newCell = document.createElement("td");
					newCell.innerHTML = data[i].message;
					newRow.appendChild(newCell);

					newCell = document.createElement("td");
					newCell.innerHTML = data[i].name;
					newRow.appendChild(newCell);

					newCell = document.createElement("td");
					newCell.innerHTML = data[i].date;
					newRow.appendChild(newCell);

					newCell = document.createElement("td");
					var pp = document.createElement("p");
					pp.id = "crudDeleteComment";
					var inp = document.createElement("input");
					inp.type = "button";
					inp.value = "删除评论";
					inp.onclick = function(){
						deleteCommentJS(this.idid, this.data[i].commentId);
					}
					inp.onclick = deleteCommentJS(this.idid, this.data[i].commentId);
					pp.appendChild(inp);
					newCell.appendChild(pp);
					newRow.appendChild(newCell);

					tableContent.appendChild(newRow);
				}
			}
			asyncRequest = null;
		}
	}
</script>
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
							<input type="hidden" name="oneArticle.articleId"
								value="<s:property
						value="oneArticle.articleId" />" />
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
							<!--  
							<tr>
								<label>文章标签: </label>
								<s:textfield name="oneArticle.date" size="10"></s:textfield>
								<br />
								<br />
							</tr>
							-->
							<tr width="1000px">
								<label>文章正文: </label>
								<script type="text/plain" id="myEditor"
									name="oneArticle.articleBody"><s:property value="oneArticle.articleBody" /></script>
								<script type="text/javascript">
									var editor = new baidu.editor.ui.Editor();
									editor.render("myEditor");
								</script>
								<br />
								<br />
							</tr>
						</table>
						<br /> <label>文章评论：</label> <br />
						<div id="crudListTable">
							<table>
								<thead>
									<tr>
										<th>标题</th>
										<th>作者</th>
										<th>发表时间</th>
										<th></th>
									</tr>
								</thead>
								<tbody id="deleteCommentAjax">
									<s:if test="allComments.size gt 0">
										<s:iterator value="allComments">
											<tr>
												<input type="hidden" name="commentId"
													value="<s:property value="commentId" />" />
												<td><s:property value="message" /></td>
												<td><s:property value="name" /></td>
												<td><s:property value="date" /></td>
												<td>
													<p id="crudDeleteComment">
														<s:url var="deleteCommentUrl" action="deleteComment"
															namespace="/struts">
															<s:param name="commentId">
																<s:property value="commentId" />
															</s:param>
															<s:param name="articleId">
																<s:property value="oneArticle.articleId" />
															</s:param>
														</s:url>
														<input type="button" value="删除评论"
															onclick="javascript:deleteCommentJS('${articleId}','${commentId}')" />
													</p>
												</td>
											</tr>
										</s:iterator>
									</s:if>
									<s:else>
										<tr>
											<td>没有评论</td>
										<tr>
									</s:else>
								</tbody>
							</table>
						</div>
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