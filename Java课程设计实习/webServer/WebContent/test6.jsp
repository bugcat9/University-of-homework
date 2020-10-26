<html> <body>
<b>Login to System</B>

Current user is:<%=request.getSession().getAttribute("username")%>
 </br>
 </hr>

<form  action="Login" method="post"> 

   <h4> User Name: </h4>
        <input type="text"  name="username"  size="10">

   <h4> Password: </h4>
   <input type="text"  name="password"  size="10">
        <p>
    <input type="submit" value="Login" >
    </p></body>
</form>
