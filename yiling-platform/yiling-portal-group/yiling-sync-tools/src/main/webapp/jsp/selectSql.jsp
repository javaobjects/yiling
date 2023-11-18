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
                                                                  src="<%=path%>/static/images/logo.png"/></a> <a
            class="fl margin-t-5"
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
                            <div class="col-xs-1">
                                    <select id="sqlCount" class="form-control">
                                        <option value='2000'>查询2000条</option>
                                        <option value='5000'>查询5000条</option>
                                        <option value='10000'>查询10000条</option>
                                        <option value='20000'>查询20000条</option>
                                    </select>
                            </div>
                            <div class="col-xs-1 text-right">
                                <button type="button" class="btn btn-info" onclick="getSqlData();">
                                    执行sql
                                </button>
                            </div>
                            <div class="col-xs-1"></div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <textarea rows="15" class="form-control" cols="50" id="sqlContext_sql" name="sqlContext_sql"
                              wrap="soft" placeholder="请输入SQL语句"></textarea>
                </div>

                <!-- 表结构弹出框  begin -->
                <div class="form-group">
                    <div style="overflow:scroll;">
                        <table id="divhtml" class="table table-bordered">
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <!--  表结构弹出框  end -->

    </div>
</div>
</div>

<!-- 模态框（Modal） -->
<div class="modal fade" id="sysModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <div id='modal_message' style="text-align: center"><h2>正在连接中.....</h2></div>
                <div class="progress progress-striped active">
                    <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="60"
                         aria-valuemin="0" aria-valuemax="100" style="width: 100%;">
                        <span class="sr-only">100% 完成</span>
                    </div>
                </div>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
</div>
</body>
</html>
<script src="<%=path%>/static/js/jquery-3.5.1.min.js" type="text/javascript"></script>
<script src="<%=path%>/static/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=path%>/static/js/b_common.js"></script>
<script type="text/javascript"
        src="<%=path%>/static/js/rk/selectSql.js"></script>
<script type="text/javascript">
    function loginOut() {
        var basePath = document.getElementById("basePath").value;
        window.document.location.href = basePath + "/login/logout.htm";
    }
</script>