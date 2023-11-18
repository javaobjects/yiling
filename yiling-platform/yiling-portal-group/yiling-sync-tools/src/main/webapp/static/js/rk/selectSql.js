
function getSqlData(){
    $("#sysModal").modal("show");//显示“正在更新”字样的模态框
	var requestUrl = ctx + "/erp/getSqlData.htm";
	$.ajax({
		url:requestUrl,
		type: "post",
		dataType: "json",
		data: {
			sqlContext: formatSql($("#sqlContext_sql").val()),
			sqlCount: formatSql($("#sqlCount").val())
		},
		success: function (res) {
            $("#sysModal").modal("hide");//显示“正在更新”字样的模态框
			if (res.code != 200) {
				alertModalb("sql执行失败，" + res.rows);
				return;
			}
			get_contaion(res.rows);
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
            $("#sysModal").modal("hide");//显示“正在更新”字样的模态框
			alertModalb("保存失败，请检查配置信息！");
		}
	});
}

function get_contaion(name){
	var html="<caption>查询结果条数为："+name.length+"</caption>";
	$.each(name, function (index, item) {
		if (index === 0) {
			html += "<thead>";
			html += "<tr>";
			$.each(item, function (vlaIndex) {
				html += "<td>";
				html += vlaIndex;
				html += "</td>";
			});
			html += "</tr>";
			html += "</thead>";
			html += "<tbody>";
		}
		html += "<tr>";
		$.each(item, function (vlaIndex, valItem) {
			html += "<td>";
			html += valItem;
			html += "</td>";
		});
		html += "</tr>";
	});
	html += "</tbody>";
	$("#divhtml").html(html);
}


/**
 * sql去除制表符，换行等
 * @param sqlContext
 * @returns {*}
 */
function formatSql(sqlContext) {
	if (sqlContext) {
		return sqlContext.replace(/\s+/g, ' ');
	}
	return sqlContext;
}