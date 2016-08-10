<%@page import="java.util.Enumeration"%>
<%@page import="java.util.logging.LogManager"%>
<%@page import="java.util.logging.Logger"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
Server info: <%= application.getServerInfo() %><br>
JSP version: <%= JspFactory.getDefaultFactory().getEngineInfo().getSpecificationVersion() %><br>
Java version: <%= System.getProperty("java.version") %><br>


<%
LogManager logManagre = LogManager.getLogManager();
Enumeration<String> names = logManagre.getLoggerNames();

while(names.hasMoreElements()){
	String name = names.nextElement();
%>
<BR/><%=name%>
<%
}
%>

</body>
</html>
