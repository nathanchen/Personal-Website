<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" type="text/css" media="screen"
	href="../stylesheets/main.css" />
<link rel="icon" type="image/ico" href="../images/favicon.ico"></link>
<link rel="shortcut icon" href="../images/favicon.ico"></link>
<script type="text/javascript" src="../javascripts/jquery.tools.min.js"></script>
<script type="text/javascript" src="../javascripts/search_btn.js"></script>
<script type="text/javascript" src="../javascripts/jquery-1.4.3.min.js"></script>
<title>Nathan CHEN</title>
</head>
<body>
	<div id="header">
		<div id="logo">
			<a href="../struts/index.action">Nathan CHEN</a>
		</div>
		<ul id="tools">
			<li><a href="../struts/index.action">Blog</a></li>
			<li><a href="#">Projects</a></li>
			<li><a href="#">About Me</a></li>
			<li>
				<div class="search">
					<form action="globalSearch" method="post" name="search" id="search" namespace="/struts">
						<input name="keyword" type="text" class="input" id="keyword"
							value="" style="float: left;"> <label for="keyword"
							id="for-keyword" class="label">我要搜索</label> <span class="submit"
							id="frmsearch" onclick="return formSubmit('formsearch')">搜索</span>
					</form>
				</div> <script type="text/javascript">
					var $ = function(id) {
						return document.getElementById(id);
					}
					var formSubmit = function(id) {
						document.forms[id].submit();
						return false;
					}
					var tip = function(q, for_q) {
						q = $(q);
						for_q = $(for_q);
						q.onfocus = function() {
							for_q.style.display = 'none';
							q.style.backgroundPosition = "right -17px";
						}
						q.onblur = function() {
							if (!this.value)
								for_q.style.display = 'block';
							q.style.backgroundPosition = "right 0";
						}
						for_q.onclick = function() {
							this.style.display = 'none';
							q.focus();
						}
					};
					tip('keyword', 'for-keyword');
				</script>
			</li>
		</ul>
		<div id="title">
			<span class="about">About this blog</span>
			<h3>
				<a href="#">wewewewe</a>
			</h3>
			<h4>asdvv</h4>
		</div>
	</div>
	<br />
</body>
</html>