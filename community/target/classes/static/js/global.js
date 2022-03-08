window.alert = function (message) {
    if (!$(".alert-box").length) {
        $("body").append(
            '<div class="modal alert-box" tabindex="-1" role="dialog">' +
            '<div class="modal-dialog" role="document">' +
            '<div class="modal-content">' +
            '<div class="modal-header">' +
            '<h5 class="modal-title">提示</h5>' +
            '<button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
            '<span aria-hidden="true">&times;</span>' +
            '</button>' +
            '</div>' +
            '<div class="modal-body">' +
            '<p></p>' +
            '</div>' +
            '<div class="modal-footer">' +
            '<button type="button" class="btn btn-secondary" data-dismiss="modal">确定</button>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>'
        );
    }

    var h = $(".alert-box").height();
    var y = h / 2 - 100;
    if (h > 600) y -= 100;
    $(".alert-box .modal-dialog").css("margin", (y < 0 ? 0 : y) + "px auto");

    $(".alert-box .modal-body p").text(message);
    $(".alert-box").modal("show");
}

$(function () {
    $("#commentReplyBtn").click(reply);
});

function reply() {
    //获取输入的内容，并保存到数据库
    const content = $("#commentReplyContent").val();
    const replyEntityId = $("#replyEntityId").val();
    $.post(
        "/comment/addComment",
        {"content": content, "id": replyEntityId},
        function (data) {
            data = $.parseJSON(data);
            //如果正常评论成功，刷新页面
            if (data.code == 0) {
                window.location.reload();
            } else {
                $("#hintBody").text(data.code + ":" + data.msg);
                //否则，错误提示框出现，并在两秒后消失
                $("#hintModal").modal("show");
                setTimeout(function () {
                    $("#hintModal").modal("hide");
                    //刷新页面
                }, 2000);
            }
        }
    )
}


