<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>longin jsp</title>
</head>
<body>
	<h1>登录页面</h1>
	<form action="/spring/loginCheck" method="post">
		<input type="hidden" value="user" name="id"> 用户名：<input
			name="user_name" value="admin"><br> 密码：<input
			value="123456" name="pass_word" type="password"><br> <input
			type="submit" value="登录"> <input type="reset" value="重置">
	</form>
</body>
</html>