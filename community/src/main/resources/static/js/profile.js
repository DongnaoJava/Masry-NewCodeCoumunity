$(function () {
    $(".follow-btn").click(follow);
});

function follow() {
    var btn = this;
    const userId = $("#userId").val();
    if ($(btn).hasClass("btn-info")) {
        // 关注TA
        $.post(
            "/follow",
            {"entityType": 0, "entityId": userId},
            function (data) {
                data = $.parseJSON(data);
                if (data.code == 0)
                    $(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary");
                else
                    alert(data.code + ":" + data.msg);
            }
        )
    } else {
        // 取消关注
        $.post(
            "/cancelFollow",
            {"entityType": 0, "entityId": userId},
            function (data) {
                data = $.parseJSON(data);
                if (data.code == 1)
                    $(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");
                else
                    alert(data.code + ":" + data.msg);
            }
        )
    }
}