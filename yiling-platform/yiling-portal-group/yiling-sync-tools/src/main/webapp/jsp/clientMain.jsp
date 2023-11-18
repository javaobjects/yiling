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
    <link rel="icon" href="<%=path%>/static/images/64_64.bmp" type="image/x-icon"/>
    <link rel="shortcut icon" href="<%=path%>/static/images/favicon.ico" type="image/x-icon"/>
    <link href="<%=path%>/static/css/bootstrap.min.css"  type="text/css" rel="stylesheet"/>
    <link href="<%=path%>/static/css/common.css" rel="stylesheet"/>
    <link rel="stylesheet" href="<%=path%>/static/css/bootstrap-table.min.css">
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
            <li><span id="businessName" style="font-size:18px"></span></li>
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
                        <ul id="myTab" class="nav nav-tabs">
                        </ul>
                        <div id="myTabContent" class="tab-content">
                            <!-- 数据库信息 -->
                            <jsp:include page="sync/sysConfig.jsp" flush="true"/>

                            <!-- 药品信息同步 -->
                            <jsp:include page="sync/goods.jsp" flush="true"/>

                            <!-- 药品库存同步 -->
                            <jsp:include page="sync/goodsBatch.jsp" flush="true"/>

                            <!-- 发货单同步 -->
                            <jsp:include page="sync/orderSend.jsp" flush="true"/>

                            <!-- 客户同步 -->
                            <jsp:include page="sync/customer.jsp" flush="true"/>

                            <!-- 客户定价同步 -->
                            <jsp:include page="sync/goodsCustomerPrice.jsp" flush="true"/>

                            <!-- 客户分组定价同步 -->
                            <jsp:include page="sync/goodsGroupPrice.jsp" flush="true"/>

                            <!-- 订单下发同步 -->
                            <jsp:include page="sync/order.jsp" flush="true"/>

                            <!-- 开票申请单下发同步 -->
                            <jsp:include page="sync/orderBill.jsp" flush="true"/>

                            <!-- 退货单下发同步 -->
                            <jsp:include page="sync/orderReturn.jsp" flush="true"/>

                            <!-- 返利协议申请单同步 -->
                            <jsp:include page="sync/orderAgreement.jsp" flush="true"/>

                            <!-- 采购订单下发同步 -->
                            <jsp:include page="sync/orderPurchase.jsp" flush="true"/>

                            <!-- 采购发货单下发同步 -->
                            <jsp:include page="sync/orderPurchaseSend.jsp" flush="true"/>

                            <!-- 采购入库单同步 -->
                            <jsp:include page="sync/orderPurchaseDelivery.jsp" flush="true"/>

                            <!-- 采购订单流向 -->
                            <jsp:include page="sync/orderPurchaseFlow.jsp" flush="true"/>

                            <!-- 销售订单流向 -->
                            <jsp:include page="sync/orderSaleFlow.jsp" flush="true"/>

                            <!-- 流向采购单单号回写 -->
                            <jsp:include page="sync/flowPurchaseNo.jsp" flush="true"/>

                            <!-- 流向销售单单号回写 -->
                            <jsp:include page="sync/flowSaleNo.jsp" flush="true"/>

                            <!-- 库存汇总流向 -->
                            <jsp:include page="sync/goodsBatchFlow.jsp" flush="true"/>

                            <!-- 连锁门店销售订单流向 -->
                            <jsp:include page="sync/shopSaleFlow.jsp" flush="true"/>

                            <!-- 日志同步 -->
                            <jsp:include page="sync/log.jsp" flush="true"/>
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
<script src="<%=path%>/static/js/bootstrap-table.min.js"></script>
<script src="<%=path%>/static/js/bootstrap-table-zh-CN.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/b_common.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/rk/rkClient.js"></script>
<script type="text/javascript">
    function loginOut() {
        var basePath = document.getElementById("basePath").value;
        window.document.location.href = basePath + "/login/logout.htm";
    }


    function showdiv(targetid) {
        var clicktext = document.getElementById('contentid');
        if (targetid == 'closeDelayExpressNum_or') {
            clicktext.style.display = "none";
        } else {
            clicktext.style.display = "block";
        }

    }
</script>
</html>