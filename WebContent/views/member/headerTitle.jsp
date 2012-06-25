<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" type="text/css" media="screen"
	href="../stylesheets/main.css" />

<link rel="shortcut icon" type="image/ico" href="../images/favicon.ico"></link>
<script type="text/javascript" src="../javascripts/jquery.tools.min.js"></script>
<script type="text/javascript" src="../javascripts/jquery-1.4.3.min.js"></script>
<script type="text/javascript" src="../javascripts/search_btn.js"></script>
<title>Nathan CHEN</title>
</head>
<body onLoad="tip('keyword', 'for-keyword');">
	<div id="header">
		<div id="logo">
			<a href="../blog/index.html">Nathan CHEN</a>
		</div>
		<ul id="tools">
			<li><a href="../blog/index.html">Blog</a></li>
			<li><a href="#">Projects</a></li>
			<li><a href="#">About Me</a></li>
			<li>
				<div class="search">
					<form action="globalSearch" method="post" name="search" id="search"
						namespace="/struts">
						<input name="keyword" type="text" class="input" id="keyword"
							value="" style="float: left;"> <label
							id="for-keyword" class="label">我要搜索</label> <span class="submit"
							id="frmsearch">搜索</span>
					</form>
				</div>
			</li>
		</ul>
	</div>
	<br />
</body>
</html>