$(function () {
    $("#topBtn").click(setTop);
    $("#wonderfulBtn").click(setWonderful);
    $("#blackListBtn").click(setBlackList);
});

function like(btn, entityType, entityId, entityUserId) {
    $.ajax({
        url: CONTEXT_PATH + "/like",
        type: 'POST',
        data: JSON.stringify({entityType: entityType, entityId: entityId, entityUserId: entityUserId}),
        success: function (response) {
            if (response.code === 0) {
                var likeCount = response.data.likeCount;
                var likeStatus = response.data.likeStatus;
                $(btn).children("span").text(likeCount);

                if (likeStatus === 1) {
                    $(btn).children("b").text("已赞");
                    console.log($(btn).children("b").text())
                } else {
                    $(btn).children("b").text("赞");
                }

            } else {
                alert(response.message)
            }

        },
        error: function (xhr, status, error) {

        },
        dataType: "json",
        contentType: "application/json"
    });
}

function setTop() {
    var id = $("#post-id").val();
    $.ajax({
        url: CONTEXT_PATH + "/post/top",
        type: 'POST',
        data: JSON.stringify({id: id}),
        success: function (response) {
            if (response.code === 0) {
                $("#topBtn").attr("disabled", "disabled")
            } else {
                alert(response.message)
            }

        },
        error: function (xhr, status, error) {

        },
        dataType: "json",
        contentType: "application/json"
    });
}

function setWonderful() {
    var id = $("#post-id").val();
    $.ajax({
        url: CONTEXT_PATH + "/post/wonderful",
        type: 'POST',
        data: JSON.stringify({id: id}),
        success: function (response) {
            if (response.code === 0) {
                $("#wonderfulBtn").attr("disabled", "disabled")
            } else {
                alert(response.message)
            }

        },
        error: function (xhr, status, error) {

        },
        dataType: "json",
        contentType: "application/json"
    });
}

function setBlackList() {
    var id = $("#post-id").val();
    $.ajax({
        url: CONTEXT_PATH + "/post/blackList",
        type: 'POST',
        data: JSON.stringify({id: id}),
        success: function (response) {
            if (response.code === 0) {
                location.href = CONTEXT_PATH + "/index";
            } else {
                alert(response.message)
            }

        },
        error: function (xhr, status, error) {

        },
        dataType: "json",
        contentType: "application/json"
    });
}