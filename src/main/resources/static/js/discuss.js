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