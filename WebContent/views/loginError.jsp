<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; UTF-8">
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script type="text/javascript"
	src="../../javascripts/jquery.tools.min.js"></script>
<%@ taglib prefix="s" uri="/struts-tags"%>
<title>Nathan CHEN</title>
</head>
<body>
	<s:include value="headerTitle.jsp"></s:include>
	<div id="notepad">
		<ul class="nav nav-list">
			<li class="nav-header">文章分类</li>
			<li class="active"><a href="#"> <i class="icon-leaf"></i>&nbsp;Home</a></li>
			<li><a href="#"> <i class="icon-fire"></i>&nbsp;Library</a></li>
		</ul>
	</div>

	<div id="main">
		<s:if test="isError eq 0">
			<p class="success">谢谢您留下的评论</p>
		</s:if>
		<div class="post">
			<h2 class="post-title">
				<a href="#"> <s:property value="eachBlog.title" /></a>
			</h2>

			<div class="post-metadata">
				<span class="post-author">by <s:property
						value="eachBlog.author" /></span> <span class="post-date"><s:property
						value="eachBlog.date" /></span> <span class="post-comments">
					&nbsp;|&nbsp; <s:property value="eachBlog.numberOfComments" /> 条评论
				</span>
			</div>
			<div class="post-content">
				<s:property escape="false" value="eachBlog.articleBody" />
			</div>
		</div>
		<div class="comments">
			<h3>
				<s:property value="eachBlog.numberOfComments" />
				条评论
			</h3>
			<s:if test="eachBlog.numberOfComments gt 0">
				<s:iterator value="eachBlogComment">
					<div class="comment">
						<div class="comment-metadata">
							<span class="comment-author">by <s:property value="name" />
								,
							</span> <span class="comment-date"> <s:property value="date" />
							</span>
						</div>
						<div class="comment-content">
							<div class="about">Detail:</div>
							<s:property value="message" />
						</div>
					</div>
				</s:iterator>
			</s:if>
			<s:else>
				<div class="comment-content">
					<div class="about">Detail:</div>
					"快来留言吧！"
				</div>
			</s:else>
		</div>
		<h3>给作者留言</h3>

		<s:form action="postComment" validate="true" theme="simple"
			method="post" namespace="/struts">
			<s:actionerror />
			<s:if test="isError eq 2">
				<p class="error">All fields are required!</p>
			</s:if>
			<input type="hidden" name="blogComment.articleId"
				value="<s:property
						value="eachBlog.articleId" />" />
			<br />
			<label>您的名字: </label>
			<s:textfield name="blogComment.name"></s:textfield>
			<label>您的留言: </label>
			<s:textarea name="blogComment.message"></s:textarea>
			<p>
				<s:submit value="提交您的评论"></s:submit>
				<s:reset value="重置"></s:reset>
			</p>
		</s:form>
	</div>
	<s:include value="footer.jsp"></s:include>
</body>

</html>