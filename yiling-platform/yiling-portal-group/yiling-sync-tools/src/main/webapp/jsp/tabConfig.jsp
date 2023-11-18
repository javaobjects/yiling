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
    <link rel="shortcut icon" href="<%=path%>/static/images/logo32_32.ico" type="image/x-icon"/>
    <link href="<%=path%>/static/css/bootstrap.min.css"
          type="text/css" rel="stylesheet"/>
    <link href="<%=path%>/static/css/common.css" rel="stylesheet"/>
</head>
<body>
<input type="hidden" id="hrefUrl" name="hrefUrl"/>
<input type="hidden" id="basePath" value="<%=path%>"/>
<div id="container" class="container">
    <div id="header" class="header clearfix">
        <a class="fl margin-r-5" href="#" title="商家ERP对接平台">
            <img width="165" height="45" src="<%=path%>/static/images/logo.png"/></a>
        <a class="fl margin-t-5" href="#" title="商家ERP对接平台"><img
                src="<%=path%>/static/images/l3.png"/></a>
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
                            <div class="tab-pane fade in active" id="clientViewConfig">
                                <div class="form-horizontal padding-t-10">
                                    <!-- 系统更新文描 -->
                                    <div class="form-group">
                                        <label for="scope" class="col-xs-1 control-label">客户端tab页展示设置:</label>
                                    </div>
                                    <div class="form-group">
                                        <label for="scope" class="col-xs-1 control-label"><em>注意：</em></label>
                                        <div class="col-xs-10 control-label text-left" style="color: #3342FF">
                                            1.如果没有配置过对接选项，在【系统配置管理】菜单页面 会默认展示 商品信息、价格、库存对接项<br/>
                                            2.如果配置，请勾选需要展示的对接选项（商品信息、价格、库存如果需要展示，也要勾选）<br/>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="scope" class="col-xs-1 control-label"/></label>
                                        <div class="col-xs-8 control-label text-left" id="d_viewType_oi">
                                            <input type="checkbox" name="configView_oi" id="goodsPane_oi"
                                                   value="goodsPane" onclick="boxChecked(this);"/>商品信息同步
                                            <input type="checkbox" name="configView_oi" id="goodsBatchPane_oi"
                                                   value="goodsBatchPane" onclick="boxChecked(this);"/>商品库存同步
                                            <input type="checkbox" name="configView_oi" id="orderSendPane_oi"
                                                   value="orderSendPane" onclick="boxChecked(this);"/>发货单同步
                                            <input type="checkbox" name="configView_oi" id="customerPane_oi"
                                                   value="customerPane" onclick="boxChecked(this);"/>终端客户同步
                                            <input type="checkbox" name="configView_oi" id="customerPricePane_oi"
                                                   value="customerPricePane" onclick="boxChecked(this);"/>客户定价同步
                                            <input type="checkbox" name="configView_oi" id="groupPricePane_oi"
                                                   value="groupPricePane" onclick="boxChecked(this);"/>客户分组定价同步
                                            <input type="checkbox" name="configView_oi" id="orderPurchaseDeliveryPane_oi"
                                                   value="orderPurchaseDeliveryPane" onclick="boxChecked(this);"/>采购入库单同步
                                            <input type="checkbox" name="configView_oi" id="orderPurchaseFlowPane_oi"
                                                   value="orderPurchaseFlowPane" onclick="boxChecked(this);"/>采购订单流向
                                            <input type="checkbox" name="configView_oi" id="orderSaleFlowPane_oi"
                                                   value="orderSaleFlowPane" onclick="boxChecked(this);"/>销售订单流向
                                            <input type="checkbox" name="configView_oi" id="goodsBatchFlowPane_oi"
                                                   value="goodsBatchFlowPane" onclick="boxChecked(this);"/>库存流向
                                            <input type="checkbox" name="configView_oi" id="shopSaleFlowPane_oi"
                                                   value="shopSaleFlowPane" onclick="boxChecked(this);"/>门店销售流向
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="scope" class="col-xs-1 control-label"/></label>
                                        <div class="col-xs-8 control-label text-left" id="d2_viewType_oi">
                                            <input type="checkbox" name="configView_oi" id="orderPane_oi"
                                                   value="orderPane" onclick="boxChecked(this);"/>销售订单推送
                                            <em>注意: </em><span style="color: #3342FF">(如想使用销售订单推送功能，先配置推送数据配置)</span><br/>
                                            <input type="checkbox" name="configView_oi" id="orderSnPane_oi"
                                                   value="orderSnPane" onclick="boxChecked(this);"/>销售单单号回写
                                            <em>注意: </em><span style="color: #3342FF">(如想使用销售单单号回写，先配置推送数据配置)</span><br/>
                                            <input type="checkbox" name="configView_oi" id="flowSaleNoPane_oi"
                                                   value="flowSaleNoPane" onclick="boxChecked(this);"/>流向销售单单号回写
                                            <em>注意: </em><span style="color: #3342FF">(如想使用流向销售单单号回写，先配置推送数据配置)</span><br/>
                                            <input type="checkbox" name="configView_oi" id="orderBillPane_oi"
                                                   value="orderBillPane" onclick="boxChecked(this);"/>开票申请推送
                                            <em>注意: </em><span style="color: #3342FF">(如想使用出库单推送功能，先配置推送数据配置)</span>><br/>
                                            <input type="checkbox" name="configView_oi" id="orderReturnPane_oi"
                                                   value="orderReturnPane" onclick="boxChecked(this);"/>退货单推送
                                            <em>注意: </em><span style="color: #3342FF">(如想使用出库单推送功能，先配置推送数据配置)</span>><br/>
                                            <input type="checkbox" name="configView_oi" id="orderAgreementPane_oi"
                                                   value="orderAgreementPane" onclick="boxChecked(this);"/>返利申请单推送
                                            <em>注意: </em><span style="color: #3342FF">(如想使用返利申请单推送功能，先配置推送数据配置)</span>><br/>
                                            <input type="checkbox" name="configView_oi" id="orderPurchasePane_oi"
                                                   value="orderPurchasePane" onclick="boxChecked(this);"/>采购订单推送
                                            <em>注意: </em><span style="color: #3342FF">(如想使用采购订单推送功能，先配置推送数据配置)</span>><br/>
                                            <input type="checkbox" name="configView_oi" id="flowPurchaseNoPane_oi"
                                                   value="flowPurchaseNoPane" onclick="boxChecked(this);"/>流向采购单单号回写
                                            <em>注意: </em><span style="color: #3342FF">(如想使用流向采购单单号回写功能，先配置推送数据配置)</span>><br/>
                                            <input type="checkbox" name="configView_oi" id="orderPurchaseSendPane_oi"
                                                   value="orderPurchaseSendPane" onclick="boxChecked(this);"/>采购发货单推送
                                            <em>注意: </em><span style="color: #3342FF">(如想使用采购发货单推送功能，先配置推送数据配置)</span>

                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="scope" class="col-xs-1 control-label"/></label>
                                        <div class="col-xs-8 control-label text-left">
                                            <button id="_oiInfoLock" type="button" class="btn btn-info"
                                                    onclick="lockButton('_oi');">解锁
                                            </button>
                                            <button id="_oiSaveInfo" type="button" class="btn btn-info"
                                                    onclick="alertModal('是否保存?',function(){saveConfig()});">保存
                                            </button>
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
</div>
</body>
</html>
<script src="<%=path%>/static/js/jquery-3.5.1.min.js" type="text/javascript"></script>
<script src="<%=path%>/static/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=path%>/static/js/b_common.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/rk/tabViewConfig.js"></script>
<script type="text/javascript">
    function loginOut() {
        var basePath = document.getElementById("basePath").value;
        window.document.location.href = basePath + "/login/logout.htm";
    }
</script>