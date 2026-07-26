<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="form"
           uri="http://www.springframework.org/tags/form" %>

<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>

<title>Transaction Report</title>

<style>

.error{
    color:red;
}

table{
    border-collapse:collapse;
}

td{
    padding:5px;
}

</style>

</head>

<body>

<h2>Transaction Report</h2>

<form:form
        method="post"
        modelAttribute="searchForm">

    <!-- class-level validation -->
    <form:errors
            cssClass="error"
            element="div"/>

    <table>

        <tr>

            <td>Start Date</td>

            <td>

                <form:input
                        path="startDate"
                        type="datetime-local"/>

            </td>

            <td>

                <form:errors
                        path="startDate"
                        cssClass="error"/>

            </td>

        </tr>

        <tr>

            <td>End Date</td>

            <td>

                <form:input
                        path="endDate"
                        type="datetime-local"/>

            </td>

            <td>

                <form:errors
                        path="endDate"
                        cssClass="error"/>

            </td>

        </tr>

        <tr>

            <td>Account ID</td>

            <td>

                <form:input
                        path="accountId"/>

            </td>

            <td></td>

        </tr>

        <tr>

            <td colspan="2">

                <button type="submit">

                    Generate Report

                </button>

            </td>

        </tr>

    </table>

</form:form>
<c:if test="${empty transactions}">
    <h2>No Data Found</h2>
</c:if>
<c:if test="${not empty transactions}">

    <hr/>

    <table border="1">

        <tr>
            <th>Id</th>
            <th>Account Id</th>
            <th>Date Time</th>
            <th>Tran Type</th>
            <th>Platform Id</th>
            <th>Game Transaction Id</th>
            <th>Game Id</th>
            <th>Amount</th>
            <th>Balance</th>
        </tr>

        <c:forEach
            items="${transactions}"
            var="transaction">

        <tr>

            <td>${transaction.id}</td>

            <td>${transaction.accountId}</td>

            <td>${transaction.datetime}</td>

            <td>${transaction.tranType}</td>

            <td>${transaction.platformTranId}</td>

            <td>${transaction.gameTranId}</td>

            <td>${transaction.gameId}</td>

            <td>${transaction.amount}</td>

            <td>${transaction.balance}</td>

        </tr>

        </c:forEach>

    </table>

</c:if>

</body>
</html>