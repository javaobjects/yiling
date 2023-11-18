<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", -10);
%>
<%@ include file="pathConfig.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>商家ERP对接平台</title>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE"/>
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="-1">
    <link rel="icon" href="<%=path%>/static/images/favicon.ico" type="image/x-icon"/>
    <link rel="shortcut icon" href="<%=path%>/static/images/logo32_32.ico" type="image/x-icon"/>
    <link href="<%=path%>/static/css/bootstrap.min.css"
          type="text/css" rel="stylesheet"/>
    <link href="<%=path%>/static/css/common.css" rel="stylesheet"/>
</head>
<body>
<input type="hidden" id="basePath" value="<%=path%>"/>
<div id="container" class="container">
    <div id="header" class="header clearfix">
        <a class="fl margin-r-5 " href="#" title="商家ERP对接平台"><img width="165" height="45"
                                                                  src="<%=path%>/static/images/logo.png"/></a>
        <a class="fl margin-t-5" href="#"
           title="商家ERP对接平台"><img src="<%=path%>/static/images/l3.png"/></a>
        <ul class="fr">
            <li class="red"><span>尊敬的:${sessionScope.userInfo.userName}，您好！</span></li>
            <li>
                <i class="fa fa-home"></i><a href="https://pop.59yi.com/" target="_blank">以岭商城首页</a>
            </li>
            <li>
                <i class="fa fa-power-off"></i><a title="logout" href="javascript:loginOut()">退出</a>
            </li>
        </ul>
    </div>
    <!-- 菜单信息 -->
    <jsp:include page="common/menu.jsp" flush="true"/>
    <div id="main-content" class="main-content">
        <div class="wrapper">
            <div class="qy_basenews">
                <div class="row margin-t-10">
                    <div class="col-xs-12">
                        <div class="tab-pane fade in active" id="clientViewConfig">
                            <div class="form-horizontal padding-t-10">
                                <div id="myTabContent" class="tab-content">
                                    <div class="form-group">
                                        <label for="scope" class="col-xs-1 control-label"><em>如果需要改写sql请注意：</em></label>
                                        <div class="col-xs-10 control-label text-left" style="color: #3342FF">
                                            1.sql中只有CREATE table 、ALTER table、创建序列、创建触发器 可以使用 大写的 CREATE、ALTER，其他的地方不要使用<br/>
                                            2.目前只是支持 oracle、 sqlserver、 mysql数据库的类型
                                                 <span style="font-size:15px;color: red; " id="createResult"></span><br/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="scope" class="col-xs-1 control-label"><em>*</em>数据库名称:</label>
                                        <div class="col-xs-3">
                                            <input type="text" class="form-control" id="dbName_oi" name="dbName"
                                                   placeholder="数据库名称">
                                        </div>
                                        <label for="scope" class="col-xs-1 control-label"><em>*</em>数据库类型:</label>
                                        <div class="col-xs-3">
                                            <select class="form-control" id="dbType_oi" name="dbType"
                                                    onclick="showOracleInfo();">
                                                <option value="Mysql">Mysql</option>
                                                <option value="Oracle">Oracle</option>
                                                <option value="SQL Server">SQL Server</option>
                                                <option value="SQL Server2000">SQL Server2000</option>
                                                <option value="DB2">DB2</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="scope" class="col-xs-1 control-label">
                                            <div id="dbOracleTypeLabel_oi"><em>*</em>Oracle类型:</div>
                                        </label>
                                        <div class="col-xs-3">
                                            <select class="form-control" id="dbOracleType_oi" name="dbOracleType">
                                                <option value="sidName">SIDName</option>
                                                <option value="serviceName">ServiceName</option>
                                            </select>
                                        </div>
                                        <label for="scope" class="col-xs-1 control-label">
                                            <div id="dbOracleSidLabel_oi"><em>*</em>Oracle实例(SIDName):</div>
                                        </label>
                                        <div class="col-xs-3">
                                            <input type="text" class="form-control" id="dbOracleSid_oi" name="dbOracleSid" placeholder="Oracle实例名称">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="scope" class="col-xs-1 control-label"><em>*</em>字符集:</label>
                                        <div class="col-xs-3">
                                            <select class="form-control" id="dbCharacter_oi" name="dbCharacter">
                                                <option value="UTF-8">UTF-8</option>
                                                <option value="GBK">GBK</option>
                                                <option value="gb2312">gb2312</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="scope" class="col-xs-1 control-label"><em>*</em>登录名:</label>
                                        <div class="col-xs-3">
                                            <input type="text" class="form-control" id="dbLoginName_oi"
                                                   name="dbLoginName" placeholder="登录名">
                                        </div>
                                        <label for="scope" class="col-xs-1 control-label"><em>*</em>密码:</label>
                                        <div class="col-xs-3">
                                            <input type="password" class="form-control" id="dbLoginPW_oi" name="dbLoginPW"
                                                   placeholder="密码">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="scope" class="col-xs-1 control-label"><em>*</em>数据库IP:</label>
                                        <div class="col-xs-3">
                                            <input type="text" class="form-control" id="dbIP_oi" name="dbIP"
                                                   placeholder="数据库IP">
                                        </div>
                                        <label for="scope" class="col-xs-1 control-label"><em>*</em>数据库端口:</label>
                                        <div class="col-xs-3">
                                            <input type="text" class="form-control" id="dbPort_oi" name="dbPort"
                                                   placeholder="数据库端口">
                                        </div>
                                    </div>
<%--                                    <div class="form-group">--%>
<%--                                        <label for="scope" class="col-xs-1 control-label"><em>*</em>创建 订单中间表:</label>--%>
<%--                                        <div class="col-xs-6 control-label text-left">--%>
<%--                                            <textarea rows="25" class="form-control" style="width: 730"--%>
<%--                                                      id="sqlContext_Order_oi" name="sqlContext_cust" wrap="soft"--%>
<%--                                                      placeholder="请输入创建订单表SQL语句"></textarea>--%>
<%--                                        </div>--%>
<%--                                    </div>--%>
<%--                                    <div class="form-group">--%>
<%--                                        <label for="scope" class="col-xs-1 control-label"><em>*</em>创建 订单明细中间表:</label>--%>
<%--                                        <div class="col-xs-6 control-label text-left">--%>
<%--                                            <textarea rows="20" class="form-control" style="width: 730"--%>
<%--                                                      id="sqlContext_OrderDetail_oi" name="sqlContext_cust" wrap="soft"--%>
<%--                                                      placeholder="请输入创建订单明细表SQL语句"></textarea>--%>
<%--                                        </div>--%>
<%--                                    </div>--%>
                                    <div class="form-group">
                                        <label for="scope" class="col-xs-1 control-label"></label>
                                        <div class="col-xs-3">
                                            <button type="button" class="btn btn-info" onclick="testButton();">测试数据库连接
                                            </button>
                                        </div>
                                        <label for="scope" class="col-xs-1 control-label"></label>
                                        <div class="col-xs-3">
                                            <button id="_oiInfoLock" type="button" class="btn btn-info"
                                                    onclick="lockButton('_oi');">解锁
                                            </button>
                                            <button id="_oiSaveInfo" type="button" class="btn btn-info"
                                                    onclick="alertModal('是否保存连接信息?',function(){saveConfig()});">
                                                保存连接信息
                                            </button>
<%--                                            <button id="_oiSave" type="button" class="btn btn-info"--%>
<%--                                                    onclick="alertModal('是否创建中间库?',function(){saveBaseTable()});">--%>
<%--                                                创建中间库--%>
<%--                                            </button>--%>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
<script src="<%=path%>/static/js/jquery-3.5.1.min.js" type="text/javascript"></script>
<script src="<%=path%>/static/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=path%>/static/js/b_common.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/rk/creMidTabAuto.js"></script>
<script type="text/javascript">
    function loginOut() {
        var basePath = document.getElementById("basePath").value;
        window.document.location.href = basePath + "/login/logout.htm";
    }
</script>
</html>