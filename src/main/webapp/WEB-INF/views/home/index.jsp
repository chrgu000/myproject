<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- <script type="text/javascript" src="/spring/../../../statics/jquery-1.8.3.js"></script> -->
<script type="text/javascript" src="/spring/statics/jquery-1.8.3.js" ></script>
</head>
<body>
<h2>Welcome to security!</h2>
<strong><a href="/spring/logout">注销</a></strong>
<button onclick="test();">测试</button>

<a href="/spring/home/file">下载</a>

<form action="/spring/home/upload/submit" method="post">
<input type="file" value="请选择" name="path">
<input type="submit" value="提交">
</form>


<script type="text/javascript">
	$(document).ready(function(){
		//alert("Hello World!");
	});
	
	
</script>
</body>
</html>
