var job3 =null;
$(function() {
	$("#download").hide();
});

function checkVersion(){
	var requestUrl = ctx + "/erp/checkVersion.htm";
	$.ajax({
		url : requestUrl,
		dataType:'json',
		type : 'POST',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		success : function(data) {
			if(data!="") {
				$('#description-title').html(data.version+" 版本实现功能如下：");
				$('#description-context').html(data.description);

				var data = new Date(data.createTime);
				var year = data.getFullYear();  //获取年
				var month = data.getMonth() + 1;    //获取月
				var day = data.getDate(); //获取日
				var hours = data.getHours();
				var minutes = data.getMinutes();
				var seconds = data.getSeconds();
				var time = year + "年" + month + "月" + day + "日" + " " + hours + ":" + minutes+":"+seconds;

				$('#versionCreateTime').html("发布时间:"+time);
				$("#hrefUrl").val(data.packageUrl);
				$("#download").show();
			}else{
				$('#description-title').html("已经是最新版本");
			}
		},
		error : function(data) {
			if(data.responseText != ''){
				$('#description-title').html(data.responseText);
			}else{
				$('#description-title').html("已经是最新版本");
			}
		}
	});
}

function download(){
	$("#searchModal").modal("show");//显示“正在更新”字样的模态框
	job3 = setInterval("isAlive()", "3000");
	var requestParam = {
		downUrl: $("#hrefUrl").val()
	};
	if($("#hrefUrl").val() != ""){
		var requestUrl = ctx + "/erp/upgrade.htm";
		$.ajax({
			url : requestUrl,
			data: requestParam,
			dataType:'json',
			type : 'POST',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			success : function(data) {
			},
			error : function(data) {
			}
		});
	}
}

function downloadWar(){
	var hrefUrl=$("#hrefUrl").val();
	window.open(ctx+"/erp/download.htm?downUrl="+hrefUrl);
}

function isAlive(){
	var requestUrl = ctx + "/system/getSystemConfig.htm";
	$.ajax({
		url: requestUrl,
		dataType: 'json',
		type: 'POST',
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		success: function (data) {
			if (data != null) {
				$('#searchModal').modal('hide');
				clearInterval(job3);
				location.reload();
			}
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			// alertModalb("获取数据库配置信息失败！");
		}
	});
}