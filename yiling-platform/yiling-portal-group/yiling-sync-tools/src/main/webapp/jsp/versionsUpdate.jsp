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
    <link rel="icon" href="<%=path%>/static/images/favicon.ico" type="image/x-icon"/>
    <link rel="shortcut icon" href="<%=path%>/static/images/favicon.ico" type="image/x-icon"/>
    <link href="<%=path%>/static/css/bootstrap.min.css"
          type="text/css" rel="stylesheet"/>
    <link href="<%=path%>/static/css/common.css" rel="stylesheet"/>
</head>
<body>
<input type="hidden" id="hrefUrl" name="hrefUrl"/>
<input type="hidden" id="basePath" value="<%=path%>"/>
<div id="container" class="container">
    <div id="header" class="header clearfix">
        <a class="fl margin-r-5" href="#" title="商家ERP对接平台"><img width="165" height="45" src="<%=path%>/static/images/logo.png"/></a>
        <a class="fl margin-t-5" href="#" title="商家ERP对接平台"><img src="<%=path%>/static/images/l3.png"/></a>
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
                        <div id="myTabContent" class="tab-content">
                            <!-- 系统更新信息 -->
                            <div class="tab-pane fade in active" id="sysUpdate">
                                <div class="form-horizontal padding-t-10">
                                    <!-- 系统更新文描 -->
                                    <div class="form-group">
                                        <label for="scope" class="col-xs-1 control-label">ERP商家对接平台系统更新</label>
                                    </div>
                                    <div class="form-group">
                                        <label for="scope" class="col-xs-1 control-label">升级操作步骤如下：</label>
                                        <div class="col-xs-8 control-label text-left">
                                            1.       先下载升级包文件rk-client-web.war并保存到本地</br>
                                            2.       在执行D:\rkConfig\停止服务.bat,先停止ERP对接平台服务</br>
                                            3.       然后将D:\rkConfig\webapps\目录里面文件清空</br>
                                            4.       在将本地的yiling-client-web.war拷贝到D:\rkConfig\webapps\里面</br>
                                            5.       最后执行D:\rkConfig\启动服务.bat,启动ERP对接平台服务</br>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="scope" class="col-xs-1 control-label"></label>
                                        <div class="col-xs-8 control-label text-left">
                                            点击检查更新按钮，会自动连接远程服务器，获取最新版本更新包，如果有版本更新则会出现下载按钮，点击后更新版本！
                                        </div>
                                        <label for="scope" class="col-xs-1 control-label"></label>
                                        <div class="col-xs-8 control-label">
                                            <button type="button" class="btn btn-info" onclick="checkVersion();">检查更新
                                            </button>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="scope" class="col-xs-1 control-label"
                                               id="description-title"></label>
                                        <div class="col-xs-7 control-label text-left" id="description-context"></div>
                                        <label for="scope" class="col-xs-1 control-label" id="versionCreateTime"></label>
                                        <div class="col-xs-11 control-label" id="download">
                                            <button type="button" class="btn btn-info" onclick="download();">自动更新</button>
                                            <button type="button" class="btn btn-info" onclick="downloadWar();">手动更新下载war</button>
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
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="searchModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-body">
                    <div id='modal_message' style="text-align: center"><h2>正在更新中.....</h2></div>
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
<script type="text/javascript" src="<%=path%>/static/js/rk/sysUpdate.js"></script>
<script type="text/javascript">
    function loginOut() {
        var basePath = document.getElementById("basePath").value;
        window.document.location.href = basePath + "/login/logout.htm";
    }
</script>