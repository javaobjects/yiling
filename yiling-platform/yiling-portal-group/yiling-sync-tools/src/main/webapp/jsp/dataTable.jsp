<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
%>
<%@ include file="pathConfig.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>商家ERP对接平台</title>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE"/>
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <link rel="icon" href="<%=path%>/static/images/favicon.ico"
          type="image/x-icon"/>
    <link rel="shortcut icon" href="<%=path%>/static/images/favicon.ico"
          type="image/x-icon"/>
    <link href="<%=path%>/static/css/bootstrap.min.css"
          type="text/css" rel="stylesheet"/>
    <link href="<%=path%>/static/css/common.css" rel="stylesheet"/>
</head>
<body>
<input type="hidden" id="basePath" value="<%=path%>"/>
<div id="container" class="container">
    <div id="header" class="header clearfix">
        <a class="fl margin-r-20" href="#" title="商家ERP对接平台"><img width="165" height="45"
                src="<%=path%>/static/images/logo.png"/></a> <a class="fl margin-t-5"
                                                                href="#" title="商家ERP对接平台"><img
            src="<%=path%>/static/images/l3.png"/></a>
        <ul class="fr">
            <li class="red"><span>尊敬的:${sessionScope.userInfo.userName}，您好！</span></li>
            <li><i class="fa fa-home"></i><a href="https://pop.59yi.com/" target="_blank">以岭商城首页</a>
            </li>
            <li><i class="fa fa-power-off"></i><a title="logout"
                                                  href="javascript:loginOut()">退出</a></li>
        </ul>
    </div>
    <!-- 菜单信息 -->
    <jsp:include page="common/menu.jsp" flush="true"/>

    <div id="main-content" class="main-content">
        <div class="wrapper">
            <div class="qy_basenews">
                <div class="row choseuser border-gray">
                    <div class="form-horizontal padding-t-10">
                        <div class="form-group">
                            <div class="col-xs-7"></div>
                            <div class="col-xs-3 text-right">
                                <button type="button" class="btn btn-info" onclick="getTableNameInfo();">
                                    &nbsp;搜索&nbsp;
                                </button>
                            </div>
                            <div class="col-xs-1"></div>
                        </div>
                    </div>
                </div>
                <div class="row margin-t-10">
                    <div class="col-xs-12">
                        <div class="panel">
                            <div class="panel-body">
                                <div class="row">
                                    <table class="table table-box">
                                        <colgroup>
                                            <col style="width: 10%;"/>
                                            <col style="width: 10%;"/>
                                            <col style="width: 10%;"/>
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th>表名</th>
                                            <th>注释</th>
                                            <th>表结构</th>
                                        </tr>
                                        </thead>
                                        <tbody id="tabledivhtml">
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 表结构弹出框  begin -->
    <div class="modal fade" id="myModal1" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" style="width: 750px;">

                <div class="modal-body">
                    <div class="form-horizontal">
                        <div class="row">
                            <table class="table table-box">
                                <colgroup>
                                    <col style="width: 10%;"/>
                                    <col style="width: 10%;"/>
                                    <col style="width: 10%;"/>
                                    <col style="width: 10%;"/>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>表名</th>
                                    <th>字段名</th>
                                    <th>类型</th>
                                    <th>注释</th>
                                </tr>
                                </thead>
                                <tbody id="columndivhtml">
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal"
                            onclick="$('#myModal1').modal('hide');">关闭
                    </button>
                </div>
            </div>
        </div>
    </div>
    <!--  表结构弹出框  end -->
</div>
</body>
</html>
<script src="<%=path%>/static/js/jquery-3.5.1.min.js" type="text/javascript"></script>
<script src="<%=path%>/static/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=path%>/static/js/b_common.js"></script>
<script type="text/javascript"
        src="<%=path%>/static/js/client/getDBInfo.js"></script>
<script type="text/javascript">
    function loginOut() {
        var basePath = document.getElementById("basePath").value;
        window.document.location.href = basePath + "/login/logout.htm";
    }
</script>