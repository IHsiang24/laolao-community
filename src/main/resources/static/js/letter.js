$(function(){
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});

function send_letter() {
	// 获取消息接收者和正文
	var toUserName = $("#recipient-name").val()
	var content = $("#message-text").val()

	// 发送异步请求
	$.ajax({
		url: CONTEXT_PATH + "/message/send",
		type: 'POST',
		data: JSON.stringify({toUserName: toUserName, content: content}),
		success: function (response) {
			console.log(response.code)
			console.log(response.message)
			// 在提示框中显示返回消息
			if (response.code === 0) {
				$("#hintBody").text("消息已发送成功！")
			} else {
				$("#hintBody").text(response.message)
			}

			console.log("提示消息:" + $("#hintModal").text())

			// 显示提示框
			$("#hintModal").modal("show");

			// 2秒后自动隐藏提示框
			setTimeout(function () {
				$("#hintModal").modal("hide");

				// 发布成功后，刷新页面
				if (response.code === 0) {
					location.reload()
				}

			}, 2000);
		},
		error: function (xhr, status, error) {

		},
		dataType: "json",
		contentType: "application/json"
	});
}

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}