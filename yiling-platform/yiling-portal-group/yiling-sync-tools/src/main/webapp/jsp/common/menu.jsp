<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	response.setHeader("Pragma","No-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setDateHeader("Expires", -10);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
		<div id="sidebar">
			<ul>
				<li><a href="<%=path %>/erp/clientMain.htm"><i class="fa fa-home"></i>抓取数据配置</a></li>
				<li><a href="<%=path %>/erp/createOrderTable.htm"><i class="fa fa-database fa-fw"></i>推送数据配置</a></li>
				<li><a href="<%=path %>/erp/tabView.htm"><i class="fa fa-cog fa-fw"></i>对接项配置</a></li>
				<li><a href="<%=path %>/erp/versionsUpdate.htm"><i class="fa fa-exchange"></i>系统更新</a></li>
				<li><a href="<%=path %>/erp/datatable.htm"><i class="fa fa-exchange"></i>表结构查询</a></li>
				<li><a href="<%=path %>/erp/selectSql.htm"><i class="fa fa-exchange"></i>数据查询</a></li>
			</ul>
		</div>
</body>
</html>