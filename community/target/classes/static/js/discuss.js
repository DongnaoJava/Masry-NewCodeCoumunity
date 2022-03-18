function like(btn, entityType, entityId, discussPostId) {
    $.post(
        "/like",
        {"entityId": entityId, "entityType": entityType, "discussPostId":discussPostId},
        function (data) {
            data = $.parseJSON(data);
            if (data.code == 1) {
                $(btn).children("i").text(parseInt($(btn).children("i").text()) + 1);
                $(btn).children("b").text("已赞");
            } else if (data.code == 0) {
                $(btn).children("i").text(parseInt($(btn).children("i").text()) - 1);
                $(btn).children("b").text("赞");
            } else {
                alert(data.code + ":" + data.msg);
            }
        }
    )
}

function findProfile(userId) {
    location.href = "http://localhost:8080/profile/" + userId;
}