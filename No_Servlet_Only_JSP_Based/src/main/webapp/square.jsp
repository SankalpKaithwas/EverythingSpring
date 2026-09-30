<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	
	int i = Integer.parseInt(request.getParameter("number"));

	int k = i * i;

	out.println("(JSP)square result is " + k);

	%>

</body>
</html>