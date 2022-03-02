$(function () {
    $("#publishBtn").click(publish);
});

function publish() {
    //标题和内容输入的狂消失
    $("#publishModal").modal("hide");
    //获取输入的标题和内容，并保存到数据库
    const title = $("#recipient-name").val();
    const content = $("#message-text").val();
    $.post(
        "/discuss/add",
        {"title": title, "content": content},
        function (data) {
            data = $.parseJSON(data);
            $("#hintBody").text(data.code + ":" + data.msg);
            //提示框出现，并在两秒后消失
            $("#hintModal").modal("show");
            setTimeout(function () {
                $("#hintModal").modal("hide");
                //刷新页面
                if (data.code == 0) {
                    window.location.reload();
                }
            }, 2000);
        }
    )
}