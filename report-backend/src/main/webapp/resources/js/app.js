$(function () {
    console.log("Document is ready!");
    $("#summarySection").hide();
    const $form = $("#searchForm");
    const $sortDirection = $("#sortDirection");
    const  $sortBy = $("#sortBy");
    const $page = $("#page");
    const $size = $("#size");
    const $pageSize =  $("#pageSize");
    const $gotoPageTextbox = $("#gotoPage");
    const $startDatebox = $("#startDate");
    const $endDatebox = $("#endDate");
    const $accountId = $("#accountId");
    const $tranType = $("#tranType");
    const $platformTranId = $("#platformTranId");
    const $gameTranId = $("#gameTranId");
    const $gameId = $("#gameId");

    loadSummary();
    $form.on("click", ".sort-link", function (e) {
        const column = $(this).data("column");
        sort(column);
    });

    $form.on("click", ".page-link", function(e) {
        const pageNum = $(this).data("column");
        changePage(pageNum);
    });

    $form.on("change", "#pageSize", function(e) {
        const value = $(this).val();
        console.log(value);
        changeSize(value);
    });

    $form.on("click", "#generateReportBtn", function (e) {
        $page.val(1);
    });

    $form.on("click", "#searchBtn", function (e) {
        $page.val(1);
    });

    $form.on("click", ".gotoPageSubmitBtn", function(e) {
        let value = $gotoPageTextbox.val();
        const max = Number($gotoPageTextbox.attr("max"));
        const min = Number($gotoPageTextbox.attr("min"));

        if (value > max) $gotoPageTextbox.val(max);
        if (value < min) $gotoPageTextbox.val(min);
        value = $gotoPageTextbox.val();
        console.log(value);
        changePage(value);
    });

    $form.on("click", "#summaryBtn", function(e) {
        e.preventDefault();
        loadSummary();
    });
    function sort(column) {
        console.log("click on " + column);
        $sortBy.val(column);

        $sortDirection.val(
            $sortDirection.val() === "ASC" ? "DESC" : "ASC"
        );

        $form.submit();
    }

    function changePage(page) {

        $page.val(page);

        $form.submit();
    }



    function changeSize() {

        $page.val(1);
        $size.val($pageSize.val());

        $form.submit();
    }

    function loadSummary() {
        /**
         *  const $startDatebox = $("#startDate");
         *     const $endDatebox = $("#endDate");
         *     const $accountId = $("#accountId");
         *     const tranType = $("#tranType");
         *     const platformTranId = $("#platformTranId");
         *     const gameTranId = $("#gameTranId");
         *     const gameId = $("#gameId");
         * @type {{readyState: number, getResponseHeader: function(*): null|*, getAllResponseHeaders: function(): *|null, setRequestHeader: function(*, *): this, overrideMimeType: function(*): this, statusCode: function(*): this, abort: function(*): this}|jQuery}
         */
        const query = "startDate=" + $startDatebox.prop("defaultValue")
              + "&endDate=" + $endDatebox.prop("defaultValue")
              + "&accountId=" + $accountId.val()
              + "&tranType=" + $tranType.val()
              + "&gameTranId=" + $gameTranId.val()
              + "&gameId=" + $gameId.val()
              + "&platformTranId=" + $platformTranId.val();
        console.log(query);
        $.ajax({
            url: "/api/v1/report/summary?"+query,
            type: "GET",
            dataType: "json",

            success: function (data) {
                $("#summarySection").hide()
                const $tbody = $("#summaryTable tbody");

                $tbody.empty();

                $.each(data, function (index, item) {

                    const row = `
                    <tr>
                        <td>${item.accountId}</td>
                        <td>${item.betSum}</td>
                        <td>${item.winSum}</td>
                        <td>${item.net}</td>
                    </tr>
                `;

                    $tbody.append(row);
                    $("#summarySection").show();
                });

            },

            error: function (xhr, status, error) {
                console.error("Failed to load summary:", error);
            }
        });

    }
});