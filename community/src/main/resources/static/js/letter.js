$(function(){
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});

function send_letter() {
	$("#sendModal").modal("hide");
	const recipientName = $("#recipient-name").val();
	const messageText = $("#message-text").val();
	$.post(
		"/letter-send",
		{"recipientName":recipientName,"messageText":messageText},
		function(data){
			data = $.parseJSON(data);
			$("#hintBody").text(data.code + ":" + data.msg);
			//提示框出现，并在两秒后消失
			$("#hintModal").modal("show");
			setTimeout(function () {
				$("#hintModal").modal("hide");
				//刷新页面
				if (data.code == 0) {
					$(".media").remove();
				}
			}, 2000);
		}
	)
}

function delete_msg() {
	// TODO 删除数据
	const messageId = $("#messageId").val();
	$.post(
		"/delete-message",
		{"messageId":messageId},
		function(data){
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
