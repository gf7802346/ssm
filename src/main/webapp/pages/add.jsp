<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>添加帐号</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/account/add" method="post">
    帐户名：<input type="text" name="name"><br>
    余额：<input type="text" name="money"><br>
    <input type="submit" value="添加帐号">
</form>
</body>
</html>