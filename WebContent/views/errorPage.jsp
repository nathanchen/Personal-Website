<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; UTF-8">
<script type="text/javascript"
	src="../../javascripts/jquery.tools.min.js"></script>
<%@ taglib prefix="s" uri="/struts-tags"%>
<title>Nathan CHEN</title>
</head>
<body>
	<div id="rotate">
		<jsp:include page="../views/member/headerTitle.jsp"></jsp:include>

		<div id="notepad">
			<ul class="nav nav-list">
				<li class="nav-header">文章分类</li>
				<li class="active"><a href="#"> <i class="icon-leaf"></i>&nbsp;Home
				</a></li>
				<li><a href="#"> <i class="icon-fire"></i>&nbsp;Library
				</a></li>
			</ul>
		</div>

		<div id="main">
			<div class="post">
				<div class="post-content">
					<div id="fourerror">404</div>
					<div id="fourerrorMessage">Oops...</div>
					<br />
					<div id="fourerrorNote"></div>
				</div>
			</div>
		</div>

		<div id="inbuild">
			<div id="main">
				<div class="post">
					<div class="post-content">还在写还在写还在写还在写还在写还在写<br/>
					/** <br/>
 					* if(no bugs){<br/>
 					*	author = @author NATHAN;<br/>
 					* }<br/>
 					* else{<br/>
 					*	author = "God knows"<br/>
 					* }<br/>
 					*<br/>
 					*/<br/>
					</div>
					
				</div>
			</div>
		</div>
		<jsp:include page="../views/member/footer.jsp"></jsp:include>
	</div>
</body>

</html>