<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" media="screen"
	href="../stylesheets/secure.css" />
<link rel="icon" type="image/ico" href="../images/favicon.ico"></link>
<link rel="shortcut icon" href="../images/favicon.ico"></link>
<title>Login Please</title>
</head>
<body>
	<div id="login">
		<form method="post" action="j_security_check">

			<p id="username-field">
				<label for="username">User Name:</label> <input type="text"
					name="j_username" id="j_username" />
			</p>
			<p id="password-field">
				<label for="password">Password:</label> <input type="password"
					name="j_password" id="j_password" />
			</p>
			<!-- 
		<p id="remember-field">
			<input type="checkbox" name="remember" id="remember" value="true" ${flash.remember ? 'checked="true"' : ''} />
			<label for="remember">&{'secure.remember'}</label>
		</p>
		 -->

			<p id="signin-field">
				<input name="Button" type="submit" id="Button" value="Log In" />
			</p>
		</form>
	</div>
</body>
</html>