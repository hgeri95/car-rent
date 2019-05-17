$(document).ready(function () {
    $("#search-form").onsubmit(function (event) {
        console.log("Submit");
        event.preventDefault();
        fire_ajax_submit();
    })
});

function fire_ajax_submit() {
    console.log("Search called");
    var search = {};
    search["city"] = $("#city").val();
    search["start"] = $("#start").val();
    search["end"] = $("#end").val();

    $("#search-submit").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/ajax/car/search",
        data: JSON.stringify(search),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var json = JSON.stringify(data);
            $("#search-result").html(json);

            $("#search-submit").prop("disabled", false);
        },
        error: function (e) {
            var json = "<h4>Search response</h4><pre>"
                + e.responseText + "</pre>";
            $("#search-result").html(json);
        }
    })
}