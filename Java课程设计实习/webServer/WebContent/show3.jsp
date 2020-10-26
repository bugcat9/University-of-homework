<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>Testing for Servlet-MVC</title>
<body> <h1>Recommended Pet - Testing for Web-MVC</h1> 
<p>
You want a <%=request.getParameter("legs")%>-legged pet weighing <%=request.getParameter("weight")%>lbs.
</p>
<p> We recommend getting <b><%=request.getAttribute("pet")%></b>
</p>
</body> 

</html>