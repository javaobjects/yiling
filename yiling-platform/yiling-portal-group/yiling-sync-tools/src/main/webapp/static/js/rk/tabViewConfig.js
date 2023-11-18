$(function () {
    getTabViewConfigInfo();
    lockInfo('_oi');
});

//选择CheckBox
function boxChecked(obj) {
    if ($(obj).attr('checked')) {
        $(obj).attr('checked', false);
    } else {
        $(obj).attr('checked', true);
    }
}

function saveConfig() {
    var checkBoxValue, goodsPane = "0",
        goodsBatchPane = "0",
        customerPane = "0",
        customerPricePane = "0",
        groupPricePane = "0",
        orderSendPane = "0",
        orderPane = "0",
        orderSnPane = "0",
        flowSaleNoPane = "0",
        orderBillPane = "0",
        orderReturnPane = "0",
        orderAgreementPane = "0",
        orderPurchasePane = "0",
        flowPurchaseNoPane="0",
        orderPurchaseSendPane = "0",
        orderPurchaseFlowPane = "0",
        orderSaleFlowPane = "0",
        goodsBatchFlowPane = "0",
        orderPurchaseDeliveryPane = "0",
        shopSaleFlowPane = "0";

    // custDataPane="0";
    $("#clientViewConfig input").each(function () {
        if ($(this).attr("checked") == "checked") {
            checkBoxValue = $(this).val();
            if (checkBoxValue == "goodsPane") {
                goodsPane = "1";
            }
            if (checkBoxValue == "goodsBatchPane") {
                goodsBatchPane = "1";
            }
            if (checkBoxValue == "customerPane") {
                customerPane = "1";
            }
            if (checkBoxValue == "customerPricePane") {
                customerPricePane = "1";
            }
            if (checkBoxValue == "groupPricePane") {
                groupPricePane = "1";
            }
            if (checkBoxValue == "orderPane") {
                orderPane = "1";
            }
            if (checkBoxValue == "orderSnPane") {
                orderSnPane = "1";
            }
            if (checkBoxValue == "flowSaleNoPane") {
                flowSaleNoPane = "1";
            }
            if (checkBoxValue == "orderSendPane") {
                orderSendPane = "1";
            }
            if (checkBoxValue == "orderBillPane") {
                orderBillPane = "1";
            }
            if (checkBoxValue == "orderReturnPane") {
                orderReturnPane = "1";
            }
            if (checkBoxValue == "orderAgreementPane") {
                orderAgreementPane = "1";
            }
            if (checkBoxValue == "orderPurchasePane") {
                orderPurchasePane = "1";
            }
            if (checkBoxValue == "flowPurchaseNoPane") {
                flowPurchaseNoPane = "1";
            }
            if (checkBoxValue == "orderPurchaseSendPane") {
                orderPurchaseSendPane = "1";
            }
            if (checkBoxValue == "orderPurchaseFlowPane") {
                orderPurchaseFlowPane = "1";
            }
            if (checkBoxValue == "orderSaleFlowPane") {
                orderSaleFlowPane = "1";
            }
            if (checkBoxValue == "goodsBatchFlowPane") {
                goodsBatchFlowPane = "1";
            }
            if (checkBoxValue == "orderPurchaseDeliveryPane") {
                orderPurchaseDeliveryPane = "1";
            }
            if (checkBoxValue == "shopSaleFlowPane") {
                shopSaleFlowPane = "1";
            }
        }
    });
    var requestParam = {
        goodsPane: goodsPane,//商品信息
        goodsBatchPane: goodsBatchPane,//商品库存
        customerPane: customerPane,//客户信息
        customerPricePane: customerPricePane,//客户定价信息
        groupPricePane: groupPricePane,//客户分组定价信息
        orderSendPane: orderSendPane,//发货单
        orderPane: orderPane,//订单推送
        orderSnPane: orderSnPane,//ERP订单单号回写
        flowSaleNoPane:flowSaleNoPane,//流向销售单单号回写
        orderBillPane: orderBillPane,//出库单推送
        orderReturnPane: orderReturnPane,//出库单推送
        orderAgreementPane: orderAgreementPane,//协议返利申请但推送
        orderPurchasePane: orderPurchasePane,//采购订单推送
        flowPurchaseNoPane: flowPurchaseNoPane,//流向采购单单号回写
        orderPurchaseSendPane: orderPurchaseSendPane,//采购发货单推送
        orderPurchaseFlowPane: orderPurchaseFlowPane,//采购订单流向
        orderSaleFlowPane: orderSaleFlowPane,//销售订单流向
        goodsBatchFlowPane: goodsBatchFlowPane,//库存流向
        orderPurchaseDeliveryPane: orderPurchaseDeliveryPane,//采购入库单同步
        shopSaleFlowPane: shopSaleFlowPane//连锁门店销售单流向
    };

    var requestUrl = ctx + "/erp/savePane.htm";
    $.ajax({
        url: requestUrl,
        data: requestParam,
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data == "success") {
                lockButton('_oi');
                alertModalb("保存成功！");
            }
            if (data == "false") {
                alertModalb("保存失败，请检查配置信息！");
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("保存失败，请检查配置信息！");
        }
    });
}

function getTabViewConfigInfo() {
    $('#_oiSaveInfo').hide();
    var requestUrl = ctx + "/erp/getTaskConfigInfo.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.goodsPane == null || data.goodsPane == "" || data.goodsPane == "1") {
                $("#goodsPane_oi").attr("checked", true);
            }
            if (data.goodsBatchPane == null || data.goodsBatchPane == "" || data.goodsBatchPane == "1") {
                $("#goodsBatchPane_oi").attr("checked", true);
            }
            if ((data.customerPane != "" || data.customerPane != null) && data.customerPane == "1") {
                $("#customerPane_oi").attr("checked", true);
            }
            if ((data.customerPricePane != "" || data.customerPricePane != null) && data.customerPricePane == "1") {
                $("#customerPricePane_oi").attr("checked", true);
            }
            if ((data.groupPricePane != "" || data.groupPricePane != null) && data.groupPricePane == "1") {
                $("#groupPricePane_oi").attr("checked", true);
            }
            if ((data.orderPane != "" || data.orderPane != null) && data.orderPane == "1") {
                $("#orderPane_oi").attr("checked", true);
            }
            if ((data.orderSnPane != "" || data.orderSnPane != null) && data.orderSnPane == "1") {
                $("#orderSnPane_oi").attr("checked", true);
            }
            if ((data.flowSaleNoPane != "" || data.flowSaleNoPane != null) && data.flowSaleNoPane == "1") {
                $("#flowSaleNoPane_oi").attr("checked", true);
            }
            if ((data.orderSendPane != "" || data.orderSendPane != null) && data.orderSendPane == "1") {
                $("#orderSendPane_oi").attr("checked", true);
            }
            if ((data.orderBillPane != "" || data.orderBillPane != null) && data.orderBillPane == "1") {
                $("#orderBillPane_oi").attr("checked", true);
            }
            if ((data.orderReturnPane != "" || data.orderReturnPane != null) && data.orderReturnPane == "1") {
                $("#orderReturnPane_oi").attr("checked", true);
            }
            if ((data.orderAgreementPane != "" || data.orderAgreementPane != null) && data.orderAgreementPane == "1") {
                $("#orderAgreementPane_oi").attr("checked", true);
            }
            if ((data.orderPurchasePane != "" || data.orderPurchasePane != null) && data.orderPurchasePane == "1") {
                $("#orderPurchasePane_oi").attr("checked", true);
            }
            if ((data.flowPurchaseNoPane != "" || data.flowPurchaseNoPane != null) && data.flowPurchaseNoPane == "1") {
                $("#flowPurchaseNoPane_oi").attr("checked", true);
            }
            if ((data.orderPurchaseSendPane != "" || data.orderPurchaseSendPane != null) && data.orderPurchaseSendPane == "1") {
                $("#orderPurchaseSendPane_oi").attr("checked", true);
            }
            if ((data.orderPurchaseFlowPane != "" || data.orderPurchaseFlowPane != null) && data.orderPurchaseFlowPane == "1") {
                $("#orderPurchaseFlowPane_oi").attr("checked", true);
            }
            if ((data.orderSaleFlowPane != "" || data.orderSaleFlowPane != null) && data.orderSaleFlowPane == "1") {
                $("#orderSaleFlowPane_oi").attr("checked", true);
            }
            if ((data.goodsBatchFlowPane != "" || data.goodsBatchFlowPane != null) && data.goodsBatchFlowPane == "1") {
                $("#goodsBatchFlowPane_oi").attr("checked", true);
            }
            if ((data.orderPurchaseDeliveryPane != "" || data.orderPurchaseDeliveryPane != null) && data.orderPurchaseDeliveryPane == "1") {
                $("#orderPurchaseDeliveryPane_oi").attr("checked", true);
            }
            if ((data.shopSaleFlowPane != "" || data.shopSaleFlowPane != null) && data.shopSaleFlowPane == "1") {
                $("#shopSaleFlowPane_oi").attr("checked", true);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("获取选项卡展示配置信息失败！");
        }
    });
}


function lockButton(obj) {
    if ($('#' + obj + 'InfoLock').is(":hidden")) {
        lockInfo(obj);
    } else {
        unlockInfo(obj);
    }
    $('#' + obj + 'InfoLock').toggle();
    $('#' + obj + 'SaveInfo').toggle();
}

function unlockInfo(obj) {
    if (obj == '_oi') {
        $("input:checkbox").each(function () {
            var t_id = $(this).attr("id");
            if (t_id.indexOf('' + obj + '') >= 0) {
                $('#' + t_id + '').attr("disabled", false);
            }
        })
    }
}

function lockInfo(obj) {
    if (obj == '_oi') {
        $("input:checkbox").each(function () {
            var t_id = $(this).attr("id");
            if (t_id.indexOf('' + obj + '') >= 0) {
                $('#' + t_id + '').attr("disabled", true);
            }
        })
    }
}