<%@ taglib prefix="c"
uri="jakarta.tags.core" %>


<html>

<body>


<h2>Users</h2>


<c:forEach
    var="user"
    items="${users}">


    ${user.name}
    -
    ${user.age}

    <br>


</c:forEach>


</body>

</html>