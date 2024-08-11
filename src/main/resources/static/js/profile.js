$(function(){
	$(".follow-btn").click(follow);
});

function follow() {
	var btn = this;
	if($(btn).hasClass("btn-info")) {
		// 关注TA
		$.ajax({
			url: CONTEXT_PATH + "/follow",
			type: 'POST',
			data: JSON.stringify({entityType: 3, entityId: $(btn).prev().val()}),
			success: function (response) {
				if (response.code === 0) {
					window.location.reload()
				} else {
					alert(response.message)
				}

			},
			error: function (xhr, status, error) {

			},
			dataType: "json",
			contentType: "application/json"
		});
		$(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary");
	} else {
		// 取消关注
		$.ajax({
			url: "/community/unFollow",
			type: 'POST',
			data: JSON.stringify({entityType: 3, entityId: $(btn).prev().val()}),
			success: function (response) {
				if (response.code === 0) {
					window.location.reload()
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
}