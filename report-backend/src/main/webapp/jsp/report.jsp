<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="form"
           uri="http://www.springframework.org/tags/form" %>

<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>


<html>

<head>

    <title>Transaction Report</title>

    <link rel="stylesheet"
          href="<c:url value='/resources/css/style.css'/>">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="<c:url value='/resources/js/app.js'/>"></script>

</head>


<body>


<h2>Transaction Report</h2>


<form:form
        id="searchForm1"
        method="get"
        action="/report/list"
        modelAttribute="searchForm">


    <form:errors
            cssClass="error"
            element="div"/>

    <form:errors
            path="page"
            cssClass="error"/>


    <table>


        <tr>

            <td>
                Start Date
            </td>


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

            <td>
                End Date
            </td>


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

            <td>
                Account ID
            </td>


            <td>

                <form:input
                        path="accountId"/>

            </td>


        </tr>



        <tr>

            <td colspan="2">

                <button method='submit' id="generateReportBtn">
                    Generate Report
                </button>

            </td>

        </tr>


    </table>


</form:form>



<c:if test="${not empty transactions}">
<form:form
        id="searchForm"
        method="get"
        action="/report/list"
        modelAttribute="searchForm">


    <input type="hidden"
           id="sortBy"
           name="sortBy"
           value="${searchForm.sortBy}"/>


    <input type="hidden"
           id="sortDirection"
           name="sortDirection"
           value="${searchForm.sortDirection}"/>


    <input type="hidden"
           id="page"
           name="page"
           value="${searchForm.page}"/>

    <input type="hidden"
               id="startDate"
               name="startDate"
               value="${searchForm.startDate}"/>

    <input type="hidden"
                   id="endDate"
                   name="endDate"
                   value="${searchForm.endDate}"/>

    <input type="hidden"
                       id="size"
                       name="size"
                       value="${searchForm.size}"/>

<div>
    <table id="report-table">


        <thead>


        <tr>


            <th>
                <a href="#" class="sort-link" data-column="id">
                    ID
                    <c:choose>
                        <c:when test="${searchForm.sortBy == 'id' && searchForm.sortDirection == 'ASC'}">
                            ↑
                        </c:when>
                        <c:when test="${searchForm.sortBy == 'id' && searchForm.sortDirection == 'DESC'}">
                            ↓
                        </c:when>
                        <c:otherwise>
                            ↕
                        </c:otherwise>
                    </c:choose>
                </a>
            </th>


            <th>
                <a href="#" class="sort-link" data-column="accountId">
                    Account ID
                    <c:choose>
                        <c:when test="${searchForm.sortBy == 'accountId' && searchForm.sortDirection == 'ASC'}">
                            ↑
                        </c:when>
                        <c:when test="${searchForm.sortBy == 'accountId' && searchForm.sortDirection == 'DESC'}">
                            ↓
                        </c:when>
                        <c:otherwise>
                            ↕
                        </c:otherwise>
                    </c:choose>
                </a>


            </th>


            <th>
                <a href="#" class="sort-link" data-column="dateTime">
                    Datetime
                    <c:choose>
                        <c:when test="${searchForm.sortBy == 'dateTime' && searchForm.sortDirection == 'ASC'}">
                            ↑
                        </c:when>
                        <c:when test="${searchForm.sortBy == 'dateTime' && searchForm.sortDirection == 'DESC'}">
                            ↓
                        </c:when>
                        <c:otherwise>
                            ↕
                        </c:otherwise>
                     </c:choose>
                </a>


            </th>


            <th>
                <a href="#" class="sort-link" data-column="tranType">
                    Tran Type
                    <c:choose>
                        <c:when test="${searchForm.sortBy == 'tranType' && searchForm.sortDirection == 'ASC'}">
                            ↑
                        </c:when>
                        <c:when test="${searchForm.sortBy == 'tranType' && searchForm.sortDirection == 'DESC'}">
                            ↓
                        </c:when>
                        <c:otherwise>
                            ↕
                        </c:otherwise>
                     </c:choose>
                </a>

            </th>


            <th>
                <a href="#" class="sort-link" data-column="platformTranId">
                    Platform Tran ID
                    <c:choose>
                        <c:when test="${searchForm.sortBy == 'platformTranId' && searchForm.sortDirection == 'ASC'}">
                            ↑
                        </c:when>
                        <c:when test="${searchForm.sortBy == 'platformTranId' && searchForm.sortDirection == 'DESC'}">
                            ↓
                        </c:when>
                        <c:otherwise>
                            ↕
                        </c:otherwise>
                     </c:choose>
                </a>
            </th>


            <th>
                <a href="#" class="sort-link" data-column="gameTranId">
                    Game Tran ID
                    <c:choose>
                        <c:when test="${searchForm.sortBy == 'gameTranId' && searchForm.sortDirection == 'ASC'}">
                            ↑
                        </c:when>
                        <c:when test="${searchForm.sortBy == 'gameTranId' && searchForm.sortDirection == 'DESC'}">
                            ↓
                        </c:when>
                        <c:otherwise>
                            ↕
                        </c:otherwise>
                     </c:choose>
                </a>

            </th>


            <th>
                <a href="#" class="sort-link" data-column="gameId">
                    Game ID
                     <c:choose>
                        <c:when test="${searchForm.sortBy == 'gameId' && searchForm.sortDirection == 'ASC'}">
                            ↑
                        </c:when>
                        <c:when test="${searchForm.sortBy == 'gameId' && searchForm.sortDirection == 'DESC'}">
                            ↓
                        </c:when>
                        <c:otherwise>
                            ↕
                        </c:otherwise>
                     </c:choose>
                </a>
            </th>


            <th>
                <a href="#" class="sort-link" data-column="amount">
                    Amount
                    <c:choose>
                        <c:when test="${searchForm.sortBy == 'amount' && searchForm.sortDirection == 'ASC'}">
                            ↑
                        </c:when>
                        <c:when test="${searchForm.sortBy == 'amount' && searchForm.sortDirection == 'DESC'}">
                            ↓
                        </c:when>
                        <c:otherwise>
                            ↕
                        </c:otherwise>
                     </c:choose>
                </a>
            </th>


            <th>
                <a href="#" class="sort-link" data-column="balance">
                    Balance
                     <c:choose>
                        <c:when test="${searchForm.sortBy == 'balance' && searchForm.sortDirection == 'ASC'}">
                            ↑
                        </c:when>
                        <c:when test="${searchForm.sortBy == 'balance' && searchForm.sortDirection == 'DESC'}">
                            ↓
                        </c:when>
                        <c:otherwise>
                            ↕
                        </c:otherwise>
                     </c:choose>
                </a>
            </th>


        </tr>



        <tr>


            <td></td>


            <td>

                <form:input
                        id="accountId"
                        path="accountId"
                        cssClass="filter-input"/>

            </td>


            <td></td>


            <td>

                <form:input
                        path="tranType"
                        cssClass="filter-input"/>

            </td>


            <td>

                <form:input
                        path="platformTranId"
                        cssClass="filter-input"/>

            </td>


            <td>

                <form:input
                        path="gameTranId"
                        cssClass="filter-input"/>

            </td>


            <td>

                <form:input
                        path="gameId"
                        cssClass="filter-input"/>

            </td>


            <td></td>


            <td></td>


        </tr>


        </thead>



        <tbody>


        <c:forEach
                items="${transactions.data}"
                var="tran">


            <tr>


                <td>
                    ${tran.id}
                </td>


                <td>
                    ${tran.accountId}
                </td>


                <td>
                    ${tran.dateTime}
                </td>


                <td>
                    ${tran.tranType}
                </td>


                <td>
                    ${tran.platformTranId}
                </td>


                <td>
                    ${tran.gameTranId}
                </td>


                <td>
                    ${tran.gameId}
                </td>


                <td>
                    ${tran.amount}
                </td>


                <td>
                    ${tran.balance}
                </td>


            </tr>


        </c:forEach>


        </tbody>


    </table>

</div>

    <br>


    <button type="submit" id="searchBtn">
        Search
    </button>

    <button id="downloadBtn">
        Download CSV
    </button>

    <br>
    <br>



    Page Size:


    <select
            id="pageSize">



        <option value="25"
                <c:if test="${transactions.pageSize == 25}">
                    selected
                </c:if>>
            25
        </option>



        <option value="50"
                <c:if test="${transactions.pageSize == 50}">
                    selected
                </c:if>>
            50
        </option>

    </select>

    <span>
            Go to page:
            <input type="number"
                   id="gotoPage"
                   value="${transactions.currentPage}"
                   min="${transactions.totalPages < 1 ? 0 : 1}"
                   max="${transactions.totalPages}"
                   style="width:60px;">

            <button type="button" class='gotoPageSubmitBtn'>
                Go
            </button>
        </span>



    <p>
        Total Records:
        ${transactions.totalRecords}


    </p>

    <p>
        Total Page:
        ${transactions.totalPages}
    </p>

    <p>
        Current Page:
        ${transactions.currentPage}
    </p>



    <div class="pagination">


        <c:if test="${transactions.hasPrevious()}">

            <a href="#" href="#" class="page-link" data-column="${transactions.currentPage - 1}">
                Previous
            </a>

        </c:if>




        <c:if test="${transactions.hasNext()}">

            <a href="#" href="#" class="page-link" data-column="${transactions.currentPage + 1}">
                Next
            </a>

        </c:if>


    </div>


</form:form>
</c:if>

<div id="summarySection">
    <h3>Summary Session</h3>

    <table id="summaryTable">
        <thead>
            <tr>
                <th>Account ID</th>
                <th>Bet Sum</th>
                <th>Win Sum</th>
                <th>Net</th>
            </tr>
        </thead>
        <tbody>

        </tbody>
    </table>
</div>

</body>

</html>