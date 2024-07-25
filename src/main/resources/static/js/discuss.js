function like(btn, entityType, entityId) {
    $.ajax({
        url: "/community/like",
        type: 'POST',
        data: JSON.stringify({entityType: entityType, entityId: entityId}),
        success: function (response) {
            if (response.code === 0) {
                var likeCount = response.data.likeCount;
                var likeStatus = response.data.likeCount;
                $(btn).children("span").text(likeCount);

                if (likeStatus === 1) {
                    $(btn).children("b").text("已赞");
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