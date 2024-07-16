$(function () {
    $("#publishBtn").click(publish);
});

function publish() {
    $("#publishModal").modal("hide");

    // 获取标题和正文
    var title = $("#recipient-name").val()
    var content = $("#message-text").val()

    // 发送异步请求
    $.ajax({
        url: CONTEXT_PATH + "/post/publish",
        type: 'POST',
        data: JSON.stringify({title: title, content: content}),
        success: function (response) {
            console.log(response.code)
            console.log(response.message)
            // 在提示框中显示返回消息
            if (response.code === 0) {
                $("#hintBody").text("成功发布一篇帖子！")
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
                    window.location.reload()
                }

            }, 2000);
        },
        error: function (xhr, status, error) {

        },
        dataType: "json",
        contentType: "application/json"
    });
}