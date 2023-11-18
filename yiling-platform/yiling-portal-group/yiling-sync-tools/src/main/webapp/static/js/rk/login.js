
function login(){
	var requestParam = {
			account:$("#account").val(),
			password: $("#password").val()
	    };
	var requestUrl = ctx + "/login/verify.htm";
	$.ajax({
		url : requestUrl,
		data : requestParam,
		type : 'POST',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		success : function(data) {
			if(data == "success"){
				location.href = ctx + "/erp/clientMain.htm";
			}else{
				alertModalb("账号或密码错误，请重新登录！");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alertModalb(data);
			alertModalb("登录失败，请重新登录！");
		}
	});
}