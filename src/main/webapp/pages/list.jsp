<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>帐号列表</title>
</head>
<body>
<table border="1" width="500" align="center">
    <tr>
        <th>序号</th>
        <th>帐号</th>
        <th>金额</th>
        <th>管理</th>
    </tr>
    <c:forEach items="${accounts}" var="account" varStatus="status">
        <tr>
            <td>${status.count}</td>
            <td>${account.name}</td>
            <td>${account.money}</td>
            <td>
                <a href="#">删除</a>
            </td>
        </tr>
    </c:forEach>

</table>
</body>
</html>