$(function () {
    console.log("Document is ready!");

    const $form = $("#searchForm");
    const $sortDirection = $("#sortDirection");
    const  $sortBy = $("#sortBy");
    const $page = $("#page");
    const $size = $("#size");
    const $pageSize =  $("#pageSize");
    const $gotoPageTextbox = $("#gotoPage");
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
});