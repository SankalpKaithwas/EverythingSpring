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
	int i = Integer.parseInt(request.getParameter("num1"));
	int j = Integer.parseInt(request.getParameter("num2"));

	int k = i + j;

	out.println("(JSP)result is in page " + k);
	out.println(24 + 56);
	out.println(3 + 4);
	%>

	<%!int coef = 3;%>

	<%
	int a = 9;
	out.println("asd");
	%>

	My lucky number is:
	<%=coef%>
</body>
</html>