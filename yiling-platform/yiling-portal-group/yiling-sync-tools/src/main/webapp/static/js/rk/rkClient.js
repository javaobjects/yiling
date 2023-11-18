var logText = [];
var dbTypeFlag = "";

$(function () {
    //初始化页面数据
    fnInitTable();
    getName();
});

function fnInitTable() {
    defaultShow();
    //获取系统配置信息
    getSystemConfig();
    //是否显示oraclee
    showOracleInfo();
    lockInfo('db');
    getGoods();
    lockInfo('_g');
    getGoodsBatch();
    lockInfo('_gb');
    getSendOrder();
    lockInfo('_os');
    getCustomerInfo();
    lockInfo('_cust');
    getOrderInfo();
    lockInfo('_o');
    getOrderBillInfo();
    lockInfo('_ob');
    getOrderReturnInfo();
    lockInfo('_or');
    getOrderAgreementInfo();
    lockInfo('_oa');
    getOrderPurchase();
    lockInfo('_op');
    getIpAndPort();
    lockInfo('_log');
    getOrderPurchaseSend();
    lockInfo('_ops');
    getOrderPurchaseFlow();
    lockInfo('_opf');
    getOrderSaleFlow();//**************************************
    lockInfo('_osf');
    getGoodsCustomerPrice();
    lockInfo('_gcp');
    getGoodsGroupPrice();
    lockInfo('_ggp');
    getGoodsBatchFlow();
    lockInfo('_opgf');
    getOrderPurchaseDelivery();
    lockInfo('_opd');
    getOrderSnInfo();
    lockInfo('_sn');
    getOrderFsnInfo();
    lockInfo('_fsn');
    getOrderFpnInfo();
    lockInfo('_fpn');
    getShopSaleFlow();
    lockInfo('_ossf');
    //初始化tab
    initTabView();
}


//初始化tab
function initTabView() {
    var requestUrl = ctx + "/erp/getTaskConfigInfo.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            $("#myTab").html("");
            var lilist = "";
            var li0 = '<li class="active"><a href="#dataSource" data-toggle="tab">系统信息</a></li>';
            lilist += li0;


            if (data.goodsPane == null || data.goodsPane == "" || data.goodsPane == "1") {
                lilist += '<li><a href="#drug" data-toggle="tab">药品信息同步</a></li>';
            }
            if (data.goodsBatchPane == null || data.goodsBatchPane == "" || data.goodsBatchPane == "1") {
                lilist += '<li><a href="#stock" data-toggle="tab">药品库存同步</a></li>';
            }
            if ((data.customerPane != "" || data.customerPane != null) && data.customerPane == "1") {
                lilist += '<li><a href="#customer" data-toggle="tab">终端客户同步</a></li>';
            }
            if ((data.customerPricePane != "" || data.customerPricePane != null) && data.customerPricePane == "1") {
                lilist += '<li><a href="#customerPrice" data-toggle="tab">客户定价同步</a></li>';
            }
            if ((data.groupPricePane != "" || data.groupPricePane != null) && data.groupPricePane == "1") {
                lilist += '<li><a href="#groupPrice" data-toggle="tab">客户分组定价同步</a></li>';
            }
            if ((data.orderSendPane != "" || data.orderSendPane != null) && data.orderSendPane == "1") {
                lilist += '<li><a href="#placeNo" data-toggle="tab">发货单同步</a></li>';
            }
            if ((data.orderPane != "" || data.orderPane != null) && data.orderPane == "1") {
                lilist += '<li><a href="#orderIssued" data-toggle="tab">订单推送</a></li>';
            }
            if ((data.orderSnPane != "" || data.orderSnPane != null) && data.orderSnPane == "1") {
                lilist += '<li><a href="#orderSn" data-toggle="tab">ERP订单单号回写</a></li>';
            }
            if ((data.flowSaleNoPane != "" || data.flowSaleNoPane != null) && data.flowSaleNoPane == "1") {
                lilist += '<li><a href="#flowSaleNo" data-toggle="tab">流向销售单单号回写</a></li>';
            }
            if ((data.orderBillPane != "" || data.orderBillPane != null) && data.orderBillPane == "1") {
                lilist += '<li><a href="#orderBill" data-toggle="tab">开票申请单推送</a></li>';
            }
            if ((data.orderReturnPane != "" || data.orderReturnPane != null) && data.orderReturnPane == "1") {
                lilist += '<li><a href="#orderReturn" data-toggle="tab">退货单推送</a></li>';
            }
            if ((data.orderAgreementPane != "" || data.orderAgreementPane != null) && data.orderAgreementPane == "1") {
                lilist += '<li><a href="#orderAgreement" data-toggle="tab">返利申请单推送</a></li>';
            }
            if ((data.orderPurchasePane != "" || data.orderPurchasePane != null) && data.orderPurchasePane == "1") {
                lilist += '<li><a href="#orderPurchase" data-toggle="tab">采购订单推送</a></li>';
            }
            if ((data.flowPurchaseNoPane != "" || data.flowPurchaseNoPane != null) && data.flowPurchaseNoPane == "1") {
                lilist += '<li><a href="#flowPurchaseNo" data-toggle="tab">流向采购单单号回写</a></li>';
            }
            if ((data.orderPurchaseSendPane != "" || data.orderPurchaseSendPane != null) && data.orderPurchaseSendPane == "1") {
                lilist += '<li><a href="#orderPurchaseSend" data-toggle="tab">采购订单发货推送</a></li>';
            }
            if ((data.orderPurchaseDeliveryPane != "" || data.orderPurchaseDeliveryPane != null) && data.orderPurchaseDeliveryPane == "1") {
                lilist += '<li><a href="#orderPurchaseDelivery" data-toggle="tab">采购入库单同步</a></li>';
            }
            if ((data.orderPurchaseFlowPane != "" || data.orderPurchaseFlowPane != null) && data.orderPurchaseFlowPane == "1") {
                lilist += '<li><a href="#orderPurchaseFlow" data-toggle="tab">采购订单流向</a></li>';
            }
            if ((data.orderSaleFlowPane != "" || data.orderSaleFlowPane != null) && data.orderSaleFlowPane == "1") {
                lilist += '<li><a href="#orderSaleFlow" data-toggle="tab">销售订单流向</a></li>';
            }
            if ((data.goodsBatchFlowPane != "" || data.goodsBatchFlowPane != null) && data.goodsBatchFlowPane == "1") {
                lilist += '<li><a href="#goodsBatchFlow" data-toggle="tab">库存流向</a></li>';
            }
            if ((data.shopSaleFlowPane != "" || data.shopSaleFlowPane != null) && data.shopSaleFlowPane == "1") {
                lilist += '<li><a href="#shopSaleFlow" data-toggle="tab">门店销售流向</a></li>';
            }
            lilist += '<li><a href="#log" data-toggle="tab">日志同步</a></li>';
            $("#myTab").html(lilist);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("获取选项卡展示配置信息失败！");
        }
    });
}

function defaultShow() {
    $(".form-control").each(function () {
        var t_id = $(this).attr("id");
        if (t_id.indexOf('frequency') >= 0) {
            $('#' + t_id + '').val("5");
        }
    });
    $("#frequency_opf").val("1440");
    $("#frequency_osf").val("1440");
    $("#frequency_ossf").val("1440");
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
    if (obj != 'db') {
        $("input:radio").each(function () {
            var t_id = $(this).attr("id");
            if (t_id.indexOf('' + obj + '') >= 0) {
                $('#' + t_id + '').attr("disabled", false);
            }
        });
    }
    if (obj == '_o' || obj == '_p') {
        $("input:checkbox").each(function () {
            var t_id = $(this).attr("id");
            if (t_id.indexOf('' + obj + '') >= 0) {
                $('#' + t_id + '').attr("disabled", false);
            }
        });
    }
    $(".form-control").each(function () {
        var t_id = $(this).attr("id");
        if (t_id.indexOf('' + obj + '') >= 0) {
            $('#' + t_id + '').attr("disabled", false);
        }
    });
}

function lockInfo(obj) {
    if (obj != 'db') {
        $("input:radio").each(function () {
            var t_id = $(this).attr("id");
            if (t_id.indexOf('' + obj + '') >= 0) {
                $('#' + t_id + '').attr("disabled", true);
            }
        });
    }
    if (obj == '_o' || obj == '_p') {
        $("input:checkbox").each(function () {
            var t_id = $(this).attr("id");
            if (t_id.indexOf('' + obj + '') >= 0) {
                $('#' + t_id + '').attr("disabled", true);
            }
        });
    }
    $(".form-control").each(function () {
        var t_id = $(this).attr("id");
        if (t_id.indexOf('' + obj + '') >= 0) {
            $('#' + t_id + '').attr("disabled", true);
        }
    });
}


function showOracleInfo() {
    var type = $("#dbType").val();
    dbTypeFlag = type;
    if (type == "Oracle") {
        $("#dbOracleTypeLabel").show();
        $("#dbOracleType").show();
        $("#dbOracleSidLabel").show();
        $("#dbOracleSid").show();
    } else {
        $("#dbOracleTypeLabel").hide();
        $("#dbOracleType").hide();
        $("#dbOracleSidLabel").hide();
        $("#dbOracleSid").hide();
        if ($("#dbOracleType").val() == '' || $("#dbOracleType").val() == null) {
            $("#dbOracleType").val('sidName');
        }
        if ($("#dbOracleSid").val() == '' || $("#dbOracleSid").val() == null) {
            $("#dbOracleSid").val('oracleSid');
        }
    }
}

function testButton() {
    var requestParam = {
        dbName: $("#dbName").val(),
        dbType: $("#dbType").val(),
        dbOracleType: $("#dbOracleType").val(),
        dbCharacter: $("#dbCharacter").val(),
        dbIP: $("#dbIP").val(),
        dbPath: $.trim($("#dbPath").val()),
        dbOracleSid: $("#dbOracleSid").val()
    };
    if (verify(requestParam)) {
        requestParam.dbLoginName = $("#dbLoginName").val();
        requestParam.dbLoginPW = $("#dbLoginPW").val();
        requestParam.dbPort = $("#dbPort").val();
        var requestUrl = ctx + "/system/testDB.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                if (data == "success") {
                    alertModalb("测试连接数据库成功！");
                }
                if (data == "db_false") {
                    alertModalb("测试连接数据库失败，请检查数据库配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    } else {
        alertModalb("有必输项未输入，请检查！");
    }
}

function getSystemConfig() {
    $('#dbSaveInfo').hide();
    var requestUrl = ctx + "/system/getSystemConfig.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data != null) {
                $("#dbName").val(data.dbName);
                $("#dbType option[value='" + data.dbType + "']").attr("selected", true);
                $("#dbOracleType option[value='" + data.oracleType + "']").attr("selected", true);
                $("#dbCharacter option[value='" + data.dbCharset + "']").attr("selected", true);
                $("#dbLoginName").val(data.dbLoginName);
                $("#dbLoginPW").val(data.dbLoginPW);
                $("#dbIP").val(data.dbIp);
                $("#dbPort").val(data.dbPort);
                $("#dbPath").val(data.urlPath == '' ? 'http://admin-gateway.test.zcyhtong.com/router/rest' : data.urlPath);
                $("#dbPharmacyName").val(data.name);
                $("#dbSecret").val(data.secret);
                $("#dbKey").val(data.key);
                $("#dbOracleSid").val(data.oracleSid);
                showOracleInfo();
            } else {
                alertModalb("请配置系统信息！");
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function verify(obj) {
    var res = true, type = "n";
    var reg = new RegExp("^[0-9]*$");
    $.each(obj, function (k, v) {
        if (k == "dbSecret" && (v == "" || v == null)) {
            res = false;
        } else if (k == "dbKey" && (v == "" || v == null)) {
            res = false;
        } else if (k == "dbLoginName" && (v == "" || v == null)) {
            res = false;
        }

        if (k.indexOf('Dock') >= 0 && (v == "" || v == null)) {
            res = true;
        } else if (dbTypeFlag != "Oracle" && k == "dbOracleType" && (v == "" || v == null || typeof (v) == "undefined" || v == "null")) {
            res = true;
        } else if (dbTypeFlag != "Oracle" && k == "dbOracleSid" && (v == "" || v == null || typeof (v) == "undefined" || v == "null")) {
            res = true;
        } else if (v == "" || v == null || typeof (v) == "undefined" || v == "null") {
            res = false;
        }

    });
    return res;
}


function saveDB() {
    $("#sysModal").modal("show");//显示“正在更新”字样的模态框

    var requestParam = {
        dbName: $("#dbName").val(),
        dbType: $("#dbType").val(),
        dbOracleType: $("#dbOracleType").val(),
        dbCharacter: $("#dbCharacter").val(),
        dbIP: $("#dbIP").val(),
        dbPath: $.trim($("#dbPath").val()),
        dbSecret: $("#dbSecret").val(),
        dbKey: $("#dbKey").val(),
        dbOracleSid: $("#dbOracleSid").val()
    };
    if (verify(requestParam)) {
        requestParam.dbLoginName = $("#dbLoginName").val();
        requestParam.dbLoginPW = $("#dbLoginPW").val();
        requestParam.dbPort = $("#dbPort").val();
        var requestUrl = ctx + "/system/saveDB.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                $('#sysModal').modal('hide');
                if (data == "success") {
                    lockButton('db');
                    alertModalb("连接数据库和服务器成功！保存成功！");
                    getName();
                }
                if (data == "db_false") {
                    alertModalb("测试连接数据库失败，请检查数据库配置信息！");
                }
                if (data == "_false") {
                    alertModalb("测试连接服务器失败，请检查数据库配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $('#sysModal').modal('hide');
                alertModalb("测试连接数据库和服务器失败，请检查数据库配置信息！");
            }
        });
    } else {
//		$("#dbInfoLock").html("锁定");
        $('#sysModal').modal('hide');
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}

function getGoods() {
    $('#_gSaveInfo').hide();
    var requestUrl = ctx + "/client/getGoods.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.taskStatus == "1") {
                $("#openDock_g").attr("checked", true);
            } else {
                $("#closeDock_g").attr("checked", true);
            }
            $("#frequency_g").val(data.taskInterval == "" ? "5" : data.taskInterval);
            $("#sqlContext_g").val(data.taskSQL);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

/**
 * sql去除制表符，换行等
 * @param sqlContext
 * @returns {*}
 */
function formatSql(sqlContext) {
    return sqlContext;
}

/**
 * json转为object
 * @param data json字符串
 * @returns {any}
 */
function jsonToObject(data) {
    return JSON.parse(data);
}

function saveGoods() {
    var requestParam = {
        openDock: $("input[name='openDock_g']:checked").val(),
        frequency: $("#frequency_g").val(),
        sqlContext: formatSql($("#sqlContext_g").val())
    };
    if (verify(requestParam)) {
        var requestUrl = ctx + "/client/saveGoods.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                var dataObj = typeof data == 'string' ? jsonToObject(data) : data;
                if (dataObj.code == "200") {
                    lockButton('_g');
                    alertModalb("保存成功！");
                } else if (dataObj.message != "" && dataObj.message != null) {
                    alertModalb(dataObj.message);
                } else {
                    alertModalb("保存失败，请检查推送配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModalb("保存失败，请检查配置信息！");
            }
        });
    } else {
        $("#_gInfoLock").html("锁定");
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}


function deleteGoods() {
    var requestUrl = ctx + "/client/deleteGoodsCache.htm";
    $.ajax({
        url: requestUrl,
        type: 'POST',
        dataType: "json",
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.code == 200) {
                lockButton('_g');
                alertModalb("清除缓存成功！");
            }
            if (data.code != 200) {
                alertModalb("清除缓存成功，" + data.message);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("清除缓存成功，请检查配置信息！");
        }
    });
}

function sqlGoods() {
    $.ajax({
        url: ctx + "/client/sqlExecute.htm",
        type: "post",
        dataType: "json",
        data: {
            sqlContext: formatSql($("#sqlContext_g").val()),  //dateFormat($("#calendar").val())
            type: "10000002"
        },
        success: function (res) {
            if (res.code != 200) {
                alertModalb("sql执行失败，" + res.rows);
                return;
            }

            $("#table-request-product").bootstrapTable('destroy');
            var $dataTableHot = $("#table-request-product").bootstrapTable({
                data: res.rows,
                pagination: true,
                columns: [{
                    field: "in_sn",
                    title: "商品内码"
                }, {
                    field: "sn",
                    title: "商品编码"
                }, {
                    field: "bar_code",
                    title: "条形码"
                }, {
                    field: "name",
                    title: "商品名称"
                }, {
                    field: "name_code",
                    title: "商品名缩写"
                }, {
                    field: "common_name",
                    title: "通用名"
                }, {
                    field: "alias_name",
                    title: "商品别名"
                }, {
                    field: "license_no",
                    title: "批准文号"
                }, {
                    field: "specifications",
                    title: "规格"
                }, {
                    field: "unit",
                    title: "单位"
                }, {
                    field: "middle_package",
                    title: "中包装"
                }, {
                    field: "big_package",
                    title: "大包装"
                }, {
                    field: "manufacturer",
                    title: "生产厂家"
                }, {
                    field: "manufacturer_code",
                    title: "厂家缩写"
                }, {
                    field: "price",
                    title: "挂网价"
                }, {
                    field: "number",
                    title: "数量"
                }, {
                    field: "can_split",
                    title: "是否拆包销售"
                }]
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("保存失败，请检查配置信息！");
        }
    });
}

function getGoodsCustomerPrice() {
    $('#_gcpSaveInfo').hide();
    var requestUrl = ctx + "/client/getGoodsCustomerPrice.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.taskStatus == "1") {
                $("#openDock_gcp").attr("checked", true);
            } else {
                $("#closeDock_gcp").attr("checked", true);
            }
            $("#frequency_gcp").val(data.taskInterval == "" ? "5" : data.taskInterval);
            $("#sqlContext_gcp").val(data.taskSQL);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function saveGoodsCustomerPrice() {
    var requestParam = {
        openDock: $("input[name='openDock_gcp']:checked").val(),
        frequency: $("#frequency_gcp").val(),
        sqlContext: formatSql($("#sqlContext_gcp").val())
    };
    if (verify(requestParam)) {
        var requestUrl = ctx + "/client/saveGoodsCustomerPrice.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                var dataObj = typeof data == 'string' ? jsonToObject(data) : data;
                if (dataObj.code == "200") {
                    lockButton('_gcp');
                    alertModalb("保存成功！");
                } else if (dataObj.message != "" && dataObj.message != null) {
                    alertModalb(dataObj.message);
                } else {
                    alertModalb("保存失败，请检查推送配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModalb("保存失败，请检查配置信息！");
            }
        });
    } else {
        $("#_gcpInfoLock").html("锁定");
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}

function deleteGoodsCustomerPrice() {
    var requestUrl = ctx + "/client/deleteGoodsCustomerPriceCache.htm";
    $.ajax({
        url: requestUrl,
        type: 'POST',
        dataType: "json",
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.code == 200) {
                lockButton('_gcp');
                alertModalb("清除缓存成功！");
            }
            if (data.code != 200) {
                alertModalb("清除缓存成功，" + data.message);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("清除缓存成功，请检查配置信息！");
        }
    });
}

function sqlGoodsCustomerPrice() {
    $.ajax({
        url: ctx + "/client/sqlExecute.htm",
        type: "post",
        dataType: "json",
        data: {
            sqlContext: formatSql($("#sqlContext_gcp").val()),  //dateFormat($("#calendar").val())
            type: "10000009"
        },
        success: function (res) {
            if (res.code != 200) {
                alertModalb("sql执行失败，" + res.rows);
                return;
            }

            $("#table-request-customerPrice").bootstrapTable('destroy');
            var $dataTableHot = $("#table-request-customerPrice").bootstrapTable({
                data: res.rows,
                pagination: true,
                columns: [{
                    field: "gcp_id_no",
                    title: "主键"
                }, {
                    field: "in_sn",
                    title: "商品内码"
                }, {
                    field: "inner_code",
                    title: "客户内码"
                }, {
                    field: "price",
                    title: "价格"
                }]
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("保存失败，请检查配置信息！");
        }
    });
}


function getGoodsGroupPrice() {
    $('#_ggpSaveInfo').hide();
    var requestUrl = ctx + "/client/getGoodsGroupPrice.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.taskStatus == "1") {
                $("#openDock_ggp").attr("checked", true);
            } else {
                $("#closeDock_ggp").attr("checked", true);
            }
            $("#frequency_ggp").val(data.taskInterval == "" ? "5" : data.taskInterval);
            $("#sqlContext_ggp").val(data.taskSQL);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function saveGoodsGroupPrice() {
    var requestParam = {
        openDock: $("input[name='openDock_ggp']:checked").val(),
        frequency: $("#frequency_ggp").val(),
        sqlContext: formatSql($("#sqlContext_ggp").val())
    };
    if (verify(requestParam)) {
        var requestUrl = ctx + "/client/saveGoodsGroupPrice.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                var dataObj = typeof data == 'string' ? jsonToObject(data) : data;
                if (dataObj.code == "200") {
                    lockButton('_ggp');
                    alertModalb("保存成功！");
                } else if (dataObj.message != "" && dataObj.message != null) {
                    alertModalb(dataObj.message);
                } else {
                    alertModalb("保存失败，请检查推送配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModalb("保存失败，请检查配置信息！");
            }
        });
    } else {
        $("#_ggpInfoLock").html("锁定");
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}

function deleteGoodsGroupPrice() {
    var requestUrl = ctx + "/client/deleteGoodsGroupPriceCache.htm";
    $.ajax({
        url: requestUrl,
        type: 'POST',
        dataType: "json",
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.code == 200) {
                lockButton('_ggp');
                alertModalb("清除缓存成功！");
            }
            if (data.code != 200) {
                alertModalb("清除缓存成功，" + data.message);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("清除缓存成功，请检查配置信息！");
        }
    });
}

function sqlGoodsGroupPrice() {
    $.ajax({
        url: ctx + "/client/sqlExecute.htm",
        type: "post",
        dataType: "json",
        data: {
            sqlContext: formatSql($("#sqlContext_ggp").val()),  //dateFormat($("#calendar").val())
            type: "10000010"
        },
        success: function (res) {
            if (res.code != 200) {
                alertModalb("sql执行失败，" + res.rows);
                return;
            }

            $("#table-request-groupPrice").bootstrapTable('destroy');
            var $dataTableHot = $("#table-request-groupPrice").bootstrapTable({
                data: res.rows,
                pagination: true,
                columns: [{
                    field: "ggp_id_no",
                    title: "主键"
                }, {
                    field: "in_sn",
                    title: "商品内码"
                }, {
                    field: "group_name",
                    title: "分组名称"
                }, {
                    field: "price",
                    title: "价格"
                }]
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("保存失败，请检查配置信息！");
        }
    });
}

function syncData(requestUrl, requestParam, buttonName) {
    $.ajax({
        url: requestUrl,
        data: requestParam,
        dataType: 'json',
        type: 'POST',
//		async : false,
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (result) {
            alertModalb(result.message, function () {
                //同步结束后，将按钮置亮
                $('#' + buttonName).attr("disabled", false);
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function isSyncStart(requestUrl, requestParam) {
    var result = false;
    $.ajax({
        url: requestUrl,
        data: requestParam,
        dataType: 'json',
        type: 'POST',
        async: false,
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data != null && data.data.isDoneQL == "false") {
                result = true;
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
    return result;
}

function getGoodsBatch() {
    $('#_gbSaveInfo').hide();
    var requestUrl = ctx + "/client/getGoodsBatch.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.taskStatus == "1") {
                $("#openDock_gb").attr("checked", true);
            } else {
                $("#closeDock_gb").attr("checked", true);
            }
            $("#frequency_gb").val(data.taskInterval == "" ? "5" : data.taskInterval);
            $("#sqlContext_gb").val(data.taskSQL);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function saveGoodsBatch() {
    var requestParam = {
        openDock: $("input[name='openDock_gb']:checked").val(),
        frequency: $("#frequency_gb").val(),
        sqlContext: formatSql($("#sqlContext_gb").val())
    };
    if (verify(requestParam)) {
        var requestUrl = ctx + "/client/saveGoodsBatch.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                var dataObj = typeof data == 'string' ? jsonToObject(data) : data;
                if (dataObj.code == "200") {
                    lockButton('_gb');
                    alertModalb("保存成功！");
                } else if (dataObj.message != "" && dataObj.message != null) {
                    alertModalb(dataObj.message);
                } else {
                    alertModalb("保存失败，请检查推送配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModalb("保存失败，请检查配置信息！");
            }
        });
    } else {
        $("#_gbInfoLock").html("锁定");
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}

function deleteGoodsBatchCache() {
    var requestUrl = ctx + "/client/deleteGoodsBatchCache.htm";
    $.ajax({
        url: requestUrl,
        type: 'POST',
        dataType: "json",
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.code == 200) {
                lockButton('_gb');
                alertModalb("清除缓存成功！");
            }
            if (data.code != 200) {
                alertModalb("清除缓存失败，" + data.message);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("清除缓存失败，请检查配置信息！");
        }
    });
}

function sqlGoodsBatch() {
    $.ajax({
        url: ctx + "/client/sqlExecute.htm",
        type: "post",
        dataType: "json",
        data: {
            sqlContext: formatSql($("#sqlContext_gb").val()),  //dateFormat($("#calendar").val())
            type: "10000003"
        },
        success: function (res) {
            if (res.code != 200) {
                alertModalb("sql执行失败，" + res.rows);
                return;
            }

            $("#table-request-stock").bootstrapTable('destroy');
            var $dataTableHot = $("#table-request-stock").bootstrapTable({
                data: res.rows,
                pagination: true,
                columns: [{
                    field: "gb_id_no",
                    title: "库存主键"
                }, {
                    field: "in_sn",
                    title: "商品内码"
                }, {
                    field: "gb_batch_no",
                    title: "批次信息"
                }, {
                    field: "gb_produce_time",
                    title: "生产日期"
                }, {
                    field: "gb_end_time",
                    title: "有效期效期"
                }, {
                    field: "gb_produce_address",
                    title: "生产地址"
                }, {
                    field: "gb_number",
                    title: "库存数量"
                }]
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("保存失败，请检查配置信息！");
        }
    });
}

function getSendOrder() {
    $('#_osSaveInfo').hide();
    var requestUrl = ctx + "/order/getSendOrder.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.taskStatus == "1") {
                $("#openDock_os").attr("checked", true);
            } else {
                $("#closeDock_os").attr("checked", true);
            }
            $("#frequency_os").val(data.taskInterval == "" ? "5" : data.taskInterval);
            $("#sqlContext_os").val(data.taskSQL);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function saveOrderSend() {
    var requestParam = {
        openDock: $("input[name='openDock_os']:checked").val(),
        frequency: $("#frequency_os").val(),
        sqlContext: formatSql($("#sqlContext_os").val())
    };
    if (verify(requestParam)) {
        var requestUrl = ctx + "/order/saveSendOrder.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            dataType: 'json',
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                var dataObj = typeof data == 'string' ? jsonToObject(data) : data;
                if (dataObj.code == "200") {
                    lockButton('_os');
                    alertModalb("保存成功！");
                } else if (dataObj.message != "" && dataObj.message != null) {
                    alertModalb(dataObj.message);
                } else {
                    alertModalb("保存失败，请检查推送配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModalb("保存失败，请检查配置信息！");
            }
        });
    } else {
        $("#_osInfoLock").html("锁定");
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}

function deleteOrderSendCache() {
    var requestUrl = ctx + "/order/deleteSendOrderCache.htm";
    $.ajax({
        url: requestUrl,
        type: 'POST',
        dataType: "json",
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.code == 200) {
                lockButton('_os');
                alertModalb("清除缓存成功！");
            }
            if (data.code != 200) {
                alertModalb("清除缓存失败，" + data.message);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("清除缓存失败，请检查配置信息！");
        }
    });
}

function sqlOrderSend() {
    $.ajax({
        url: ctx + "/client/sqlExecute.htm",
        type: "post",
        dataType: "json",
        data: {
            sqlContext: formatSql($("#sqlContext_os").val()),  //dateFormat($("#calendar").val())
            type: "10000004"
        },
        success: function (res) {
            if (res.code != 200) {
                alertModalb("sql执行失败，" + res.rows);
                return;
            }

            $("#table-request-placeNo").bootstrapTable('destroy');
            var $dataTableHot = $("#table-request-placeNo").bootstrapTable({
                data: res.rows,
                pagination: true,
                columns: [{
                    field: "osi_id",
                    title: "发货单主键Id"
                }, {
                    field: "order_id",
                    title: "订单Id"
                }, {
                    field: "order_detail_id",
                    title: "订单明细Id"
                },{
                    field: "delivery_number",
                    title: "出库单号"
                }, {
                    field: "eas_send_order_id",
                    title: "出库单主键"
                },{
                    field: "send_batch_no",
                    title: "批次号"
                }, {
                    field: "effective_time",
                    title: "有效期"
                }, {
                    field: "product_time",
                    title: "生产日期"
                }, {
                    field: "send_num",
                    title: "发货数量"
                }, {
                    field: "send_type",
                    title: "发货状态(1发货2关闭3作废)"
                }]
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("保存失败，请检查配置信息！");
        }
    });
}

function getCustomerInfo() {
    $('#_custSaveInfo').hide();
    var requestUrl = ctx + "/client/getCustomerInfo.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.taskStatus == "1") {
                $("#openDock_cust").attr("checked", true);
            } else {
                $("#closeDock_cust").attr("checked", true);
            }
            $("#frequency_cust").val(data.taskInterval == "" ? "5" : data.taskInterval);
            $("#sqlContext_cust").val(data.taskSQL);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function saveCustomerInfo() {
    var requestParam = {
        openDock: $("input[name='openDock_cust']:checked").val(),
        frequency: $("#frequency_cust").val(),
        sqlContext: formatSql($("#sqlContext_cust").val())
    };
    if (verify(requestParam)) {
        var requestUrl = ctx + "/client/saveCustomerInfo.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            dataType: 'json',
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                var dataObj = typeof data == 'string' ? jsonToObject(data) : data;
                if (dataObj.code == "200") {
                    lockButton('_cust');
                    alertModalb("保存成功！");
                } else if (dataObj.message != "" && dataObj.message != null) {
                    alertModalb(dataObj.message);
                } else {
                    alertModalb("保存失败，请检查推送配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModalb("保存失败，请检查配置信息！");
            }
        });
    } else {
        $("#_custInfoLock").html("锁定");
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}


function deleteCustomerInfoCache() {
    var requestUrl = ctx + "/client/deleteCustomerCache.htm";
    $.ajax({
        url: requestUrl,
        type: 'POST',
        dataType: "json",
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.code == 200) {
                lockButton('_cust');
                alertModalb("清除缓存成功！");
            }
            if (data.code != 200) {
                alertModalb("清除缓存失败，" + data.message);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("清除缓存失败，请检查配置信息！");
        }
    });
}


function sqlCustomerInfo() {
    $.ajax({
        url: ctx + "/client/sqlExecute.htm",
        type: "post",
        dataType: "json",
        data: {
            sqlContext: formatSql($("#sqlContext_cust").val()),  //dateFormat($("#calendar").val())
            type: "10000001"
        },
        success: function (res) {
            if (res.code != 200) {
                alertModalb("sql执行失败，" + res.rows);
                return;
            }

            $("#table-request-customer").bootstrapTable('destroy');
            var $dataTableHot = $("#table-request-customer").bootstrapTable({
                data: res.rows,
                pagination: true,
                columns: [{
                    field: "inner_code",
                    title: "客户内码"
                }, {
                    field: "sn",
                    title: "客户编码"
                }, {
                    field: "name",
                    title: "企业名称"
                }, {
                    field: "group_name",
                    title: "分组名称"
                }, {
                    field: "license_no",
                    title: "营业执照号/医疗机构许可证"
                }, {
                    field: "customer_type",
                    title: "终端类型"
                }, {
                    field: "contact",
                    title: "联系人"
                }, {
                    field: "phone",
                    title: "手机号码"
                }, {
                    field: "province",
                    title: "省"
                }, {
                    field: "city",
                    title: "市"
                }, {
                    field: "region",
                    title: "区"
                }, {
                    field: "address",
                    title: "详细地址"
                }]
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("保存失败，请检查配置信息！");
        }
    });
}

//选择Radio
function radioChecked(obj) {
    var name = $(obj).prop("name");
    var otherName;
    if (name.indexOf('open') >= 0) {
        otherName = "close" + name.substring(4, name.length);
        $('#' + otherName).prop('checked', false);
    }

    if (name.indexOf('close') >= 0) {
        otherName = "open" + name.substring(5, name.length);
        $('#' + otherName).prop('checked', false);
    }
    $(obj).attr("checked", "checked");

}

function getOrderInfo() {
    $('#_oSaveInfo').hide();
    var requestUrl = ctx + "/order/getOrderInfo.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.taskStatus == "1") {
                $("#openDock_o").attr("checked", true);
            } else {
                $("#closeDock_o").attr("checked", true);
            }
            $("#frequency_o").val(data.taskInterval == "" ? "5" : data.taskInterval);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function getOrderFsnInfo() {
    $('#_fsnSaveInfo').hide();
    var requestUrl = ctx + "/order/getOrderFsnInfo.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.taskStatus == "1") {
                $("#openDock_fsn").attr("checked", true);
            } else {
                $("#closeDock_fsn").attr("checked", true);
            }
            $("#frequency_fsn").val(data.taskInterval == "" ? "5" : data.taskInterval);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}


function getOrderFpnInfo() {
    $('#_fpnSaveInfo').hide();
    var requestUrl = ctx + "/order/getOrderFpnInfo.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.taskStatus == "1") {
                $("#openDock_fpn").attr("checked", true);
            } else {
                $("#closeDock_fpn").attr("checked", true);
            }
            $("#frequency_fpn").val(data.taskInterval == "" ? "5" : data.taskInterval);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function getOrderSnInfo() {
    $('#_oSaveInfo').hide();
    var requestUrl = ctx + "/order/getOrderSnInfo.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.taskStatus == "1") {
                $("#openDock_sn").attr("checked", true);
            } else {
                $("#closeDock_sn").attr("checked", true);
            }
            $("#frequency_sn").val(data.taskInterval == "" ? "5" : data.taskInterval);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function getOrderBillInfo() {
    $('#_obSaveInfo').hide();
    var requestUrl = ctx + "/order/getOrderBillInfo.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.taskStatus == "1") {
                $("#openDock_ob").attr("checked", true);
            } else {
                $("#closeDock_ob").attr("checked", true);
            }
            $("#frequency_ob").val(data.taskInterval == "" ? "5" : data.taskInterval);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function getOrderPurchase() {
    $('#_opSaveInfo').hide();
    var requestUrl = ctx + "/order/getOrderPurchaseInfo.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.taskStatus == "1") {
                $("#openDock_op").attr("checked", true);
            } else {
                $("#closeDock_op").attr("checked", true);
            }
            $("#frequency_op").val(data.taskInterval == "" ? "5" : data.taskInterval);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function saveOrder() {
    var requestParam = {
        openDock: $("input[name='openDock_o']:checked").val(),
        frequency: $("#frequency_o").val()
    };
    if (verify(requestParam)) {
        var requestUrl = ctx + "/order/saveOrder.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                if (data == "success") {
                    lockButton('_o');
                    alertModalb("保存成功！");
                }
                if (data == "false") {
                    alertModalb("保存失败，请检查推送配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModalb("保存失败，请检查推送配置信息！");
            }
        });
    } else {
        $("#_oInfoLock").html("锁定");
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}

function saveOrderSn() {
    var requestParam = {
        openDock: $("input[name='openDock_sn']:checked").val(),
        frequency: $("#frequency_sn").val()
    };
    if (verify(requestParam)) {
        var requestUrl = ctx + "/order/saveOrderSn.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                if (data == "success") {
                    lockButton('_sn');
                    alertModalb("保存成功！");
                }
                if (data == "false") {
                    alertModalb("保存失败，请检查推送配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModalb("保存失败，请检查推送配置信息！");
            }
        });
    } else {
        $("#_snInfoLock").html("锁定");
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}

function saveOrderFsn() {
    var requestParam = {
        openDock: $("input[name='openDock_fsn']:checked").val(),
        frequency: $("#frequency_fsn").val()
    };
    if (verify(requestParam)) {
        var requestUrl = ctx + "/order/saveOrderFsn.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                if (data == "success") {
                    lockButton('_fsn');
                    alertModalb("保存成功！");
                }
                if (data == "false") {
                    alertModalb("保存失败，请检查推送配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModalb("保存失败，请检查推送配置信息！");
            }
        });
    } else {
        $("#_fsnInfoLock").html("锁定");
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}


function saveOrderFpn() {
    var requestParam = {
        openDock: $("input[name='openDock_fpn']:checked").val(),
        frequency: $("#frequency_fpn").val()
    };
    if (verify(requestParam)) {
        var requestUrl = ctx + "/order/saveOrderFpn.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                if (data == "success") {
                    lockButton('_fpn');
                    alertModalb("保存成功！");
                }
                if (data == "false") {
                    alertModalb("保存失败，请检查推送配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModalb("保存失败，请检查推送配置信息！");
            }
        });
    } else {
        $("#_fpnInfoLock").html("锁定");
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}


function saveOrderBill() {
    var requestParam = {
        openDock: $("input[name='openDock_ob']:checked").val(),
        frequency: $("#frequency_ob").val()
    };
    if (verify(requestParam)) {
        var requestUrl = ctx + "/order/saveOrderBill.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                if (data == "success") {
                    lockButton('_ob');
                    alertModalb("保存成功！");
                }
                if (data == "false") {
                    alertModalb("保存失败，请检查推送配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModalb("保存失败，请检查推送配置信息！");
            }
        });
    } else {
        $("#_obInfoLock").html("锁定");
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}


function getOrderAgreementInfo() {
    $('#_oaSaveInfo').hide();
    var requestUrl = ctx + "/order/getOrderAgreementInfo.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.taskStatus == "1") {
                $("#openDock_oa").attr("checked", true);
            } else {
                $("#closeDock_oa").attr("checked", true);
            }
            $("#frequency_oa").val(data.taskInterval == "" ? "5" : data.taskInterval);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function saveOrderAgreement() {
    var requestParam = {
        openDock: $("input[name='openDock_oa']:checked").val(),
        frequency: $("#frequency_oa").val()
    };
    if (verify(requestParam)) {
        var requestUrl = ctx + "/order/saveOrderAgreement.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                if (data == "success") {
                    lockButton('_oa');
                    alertModalb("保存成功！");
                }
                if (data == "false") {
                    alertModalb("保存失败，请检查推送配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModalb("保存失败，请检查推送配置信息！");
            }
        });
    } else {
        $("#_oaInfoLock").html("锁定");
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}


function getOrderReturnInfo() {
    $('#_orSaveInfo').hide();
    var requestUrl = ctx + "/order/getOrderReturnInfo.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.taskStatus == "1") {
                $("#openDock_or").attr("checked", true);
            } else {
                $("#closeDock_or").attr("checked", true);
            }
            $("#frequency_or").val(data.taskInterval == "" ? "5" : data.taskInterval);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function saveOrderReturn() {
    var requestParam = {
        openDock: $("input[name='openDock_or']:checked").val(),
        frequency: $("#frequency_or").val()
    };
    if (verify(requestParam)) {
        var requestUrl = ctx + "/order/saveOrderReturn.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                if (data == "success") {
                    lockButton('_or');
                    alertModalb("保存成功！");
                }
                if (data == "false") {
                    alertModalb("保存失败，请检查推送配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModalb("保存失败，请检查推送配置信息！");
            }
        });
    } else {
        $("#_orInfoLock").html("锁定");
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}

function saveOrderPurchase() {
    var requestParam = {
        openDock: $("input[name='openDock_op']:checked").val(),
        frequency: $("#frequency_op").val()
    };
    if (verify(requestParam)) {
        var requestUrl = ctx + "/order/saveOrderPurchase.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                if (data == "success") {
                    lockButton('_op');
                    alertModalb("保存成功！");
                }
                if (data == "false") {
                    alertModalb("保存失败，请检查推送配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModalb("保存失败，请检查推送配置信息！");
            }
        });
    } else {
        $("#_opInfoLock").html("锁定");
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}


function getName() {
    $('#businessName').hide();
    var requestUrl = ctx + "/system/getSystemConfig.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data != null) {
                var htmlStr = "<p>欢迎 " + data.name + " 使用对接工具" + data.version + "</p>";
                $("#businessName").html(htmlStr);
                $('#businessName').show();
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function getIpAndPort() {
    // var requestUrl = ctx + "/log/getIpAndPort.htm";
    // $.ajax({
    //     url: requestUrl,
    //     dataType: 'json',
    //     type: 'POST',
    //     contentType: "application/x-www-form-urlencoded; charset=UTF-8",
    //     success: function (data) {
    //         ipAndPort = data.ipAndPort;
    //     },
    //     error: function (XMLHttpRequest, textStatus, errorThrown) {
    //     }
    // });
    // 获取IP、端口
    // var cur = window.document.location.href;
    // var pathname = window.document.location.pathname;
    // var pos = cur.indexOf( pathname );
    // ipAndPort = cur.substring( 0, pos );
    console.log(ipAndPort);
}

function getOrderPurchaseSend() {
    $('#_opsSaveInfo').hide();
    var requestUrl = ctx + "/order/getOrderPurchaseSendInfo.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.taskStatus == "1") {
                $("#openDock_ops").attr("checked", true);
            } else {
                $("#closeDock_ops").attr("checked", true);
            }
            $("#frequency_ops").val(data.taskInterval == "" ? "5" : data.taskInterval);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function saveOrderPurchaseSend() {
    var requestParam = {
        openDock: $("input[name='openDock_ops']:checked").val(),
        frequency: $("#frequency_ops").val()
    };
    if (verify(requestParam)) {
        var requestUrl = ctx + "/order/saveOrderPurchaseSend.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                if (data == "success") {
                    lockButton('_ops');
                    alertModalb("保存成功！");
                }
                if (data == "false") {
                    alertModalb("保存失败，请检查推送配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModalb("保存失败，请检查推送配置信息！");
            }
        });
    } else {
        $("#_opsInfoLock").html("锁定");
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}


function sqlOrderPurchaseFlowInfo() {
    $("#opfModal").modal("show");//显示“正在更新”字样的模态框
    $.ajax({
        url: ctx + "/client/sqlExecute.htm",
        type: "post",
        dataType: "json",
        data: {
            sqlContext: formatSql($("#sqlContext_opf").val()),  //dateFormat($("#calendar").val())
            type: "40000006",
            frequency: $("#frequency_opf").val(),
            flowDateCount: $("#flowDateCount_opf").val()
        },
        success: function (res) {
            $("#opfModal").modal("hide");//显示“正在更新”字样的模态框
            if (res.code != 200) {
                alertModalb("sql执行失败，" + res.rows);
                return;
            }

            $("#table-request-orderPurchaseFlow").bootstrapTable('destroy');
            var $dataTableHot = $("#table-request-orderPurchaseFlow").bootstrapTable({
                data: res.rows,
                pagination: true,
                columns: [{
                    field: "po_id",
                    title: "Erp采购订单主键"
                }, {
                    field: "po_no",
                    title: "Erp采购订单号"
                }, {
                    field: "po_time",
                    title: "采购日期"
                }, {
                    field: "order_time",
                    title: "下单日期"
                }, {
                    field: "enterprise_inner_code",
                    title: "客户内码"
                }, {
                    field: "enterprise_name",
                    title: "客户名称"
                }, {
                    field: "po_batch_no",
                    title: "批次号"
                }, {
                    field: "po_quantity",
                    title: "采购数量"
                }, {
                    field: "po_product_time",
                    title: "生产日期"
                }, {
                    field: "po_effective_time",
                    title: "效期"
                }, {
                    field: "po_price",
                    title: "价格"
                }, {
                    field: "goods_in_sn",
                    title: "商品内码"
                }, {
                    field: "goods_name",
                    title: "商品名称"
                }, {
                    field: "po_license",
                    title: "批准文号"
                }, {
                    field: "po_specifications",
                    title: "商品规格"
                }, {
                    field: "po_unit",
                    title: "商品单位"
                }, {
                    field: "po_manufacturer",
                    title: "商品生产厂家"
                }, {
                    field: "po_source",
                    title: "订单来源"
                }]
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            $("#opfModal").modal("hide");//显示“正在更新”字样的模态框
            alertModalb("保存失败，请检查配置信息！");
        }
    });
}

function deleteOrderPurchaseFlowInfoCache() {
    var requestUrl = ctx + "/flow/deleteOrderPurchaseFlowInfoCache.htm";
    $.ajax({
        url: requestUrl,
        type: 'POST',
        dataType: "json",
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.code == 200) {
                lockButton('_opf');
                alertModalb("清除缓存成功！");
            }
            if (data.code != 200) {
                alertModalb("清除缓存失败，" + data.message);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("清除缓存失败，请检查配置信息！");
        }
    });
}

function getOrderPurchaseFlow() {
    $('#_opfSaveInfo').hide();
    var requestUrl = ctx + "/flow/getOrderPurchaseFlowInfo.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.taskStatus == "1") {
                $("#openDock_opf").attr("checked", true);
            } else {
                $("#closeDock_opf").attr("checked", true);
            }
            $("#flowDateCount_opf").val(data.flowDateCount == "" ? "15" : data.flowDateCount);
            $("#frequency_opf").val(data.taskInterval == "" ? "1440" : data.taskInterval);
            $("#sqlContext_opf").val(data.taskSQL);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function saveOrderPurchaseFlow() {
    $("#opfModal").modal("show");//显示“正在更新”字样的模态框
    var requestParam = {
        openDock: $("input[name='openDock_opf']:checked").val(),
        frequency: $("#frequency_opf").val(),
        flowDateCount: $("#flowDateCount_opf").val(),
        sqlContext: formatSql($("#sqlContext_opf").val())
    };
    if (verify(requestParam)) {
        var requestUrl = ctx + "/flow/saveOrderPurchaseFlow.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                $('#opfModal').modal('hide');
                var dataObj = typeof data == 'string' ? jsonToObject(data) : data;
                if (dataObj.code == "200") {
                    lockButton('_opf');
                    alertModalb("保存成功！");
                } else if (dataObj.message != "" && dataObj.message != null) {
                    alertModalb(dataObj.message);
                } else {
                    alertModalb("保存失败，请检查推送配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $('#opfModal').modal('hide');
                alertModalb("保存失败，请检查推送配置信息！");
            }
        });
    } else {
        $('#opfModal').modal('hide');
        $("#_opfInfoLock").html("锁定");
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}

function sqlOrderSaleFlowInfo() {
    $("#osfModal").modal("show");//显示“正在更新”字样的模态框
    $.ajax({
        url: ctx + "/client/sqlExecute.htm",
        type: "post",
        dataType: "json",
        timeout:60000,
        data: {
            sqlContext: formatSql($("#sqlContext_osf").val()),  //dateFormat($("#calendar").val())
            type: "40000007",
            frequency: $("#frequency_osf").val(),
            flowDateCount: $("#flowDateCount_osf").val()
        },
        success: function (res) {
            $("#osfModal").modal("hide");//显示“正在更新”字样的模态框
            if (res.code != 200) {
                alertModalb("sql执行失败，" + res.rows);
                return;
            }

            $("#table-request-orderSaleFlow").bootstrapTable('destroy');
            var $dataTableHot = $("#table-request-orderSaleFlow").bootstrapTable({
                data: res.rows,
                pagination: true,
                columns: [{
                    field: "so_id",
                    title: "Erp销售订单主键"
                }, {
                    field: "so_no",
                    title: "Erp销售订单号"
                }, {
                    field: "so_time",
                    title: "销售日期"
                }, {
                    field: "order_time",
                    title: "下单日期"
                }, {
                    field: "enterprise_inner_code",
                    title: "客户内码"
                }, {
                    field: "enterprise_name",
                    title: "客户名称"
                }, {
                    field: "license_number",
                    title: "统一信用代码"
                }, {
                    field: "so_batch_no",
                    title: "批号"
                }, {
                    field: "so_quantity",
                    title: "销售数量"
                }, {
                    field: "so_product_time",
                    title: "生产日期"
                }, {
                    field: "so_effective_time",
                    title: "效期"
                }, {
                    field: "so_price",
                    title: "价格"
                }, {
                    field: "goods_in_sn",
                    title: "商品内码"
                }, {
                    field: "goods_name",
                    title: "商品名称"
                }, {
                    field: "so_license",
                    title: "批准文号"
                }, {
                    field: "so_specifications",
                    title: "商品规格"
                }, {
                    field: "so_unit",
                    title: "商品单位"
                }, {
                    field: "so_manufacturer",
                    title: "商品生产厂家"
                }, {
                    field: "so_source",
                    title: "订单来源"
                }]
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            $("#osfModal").modal("hide");//显示“正在更新”字样的模态框
            alertModalb("保存失败，请检查配置信息！");
        }
    });
}

function deleteOrderSaleFlowInfoCache() {
    var requestUrl = ctx + "/flow/deleteOrderSaleFlowInfoCache.htm";
    $.ajax({
        url: requestUrl,
        type: 'POST',
        dataType: "json",
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.code == 200) {
                lockButton('_osf');
                alertModalb("清除缓存成功！");
            }
            if (data.code != 200) {
                alertModalb("清除缓存失败，" + data.message);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("清除缓存失败，请检查配置信息！");
        }
    });
}

function getOrderSaleFlow() {
    $('#_osfSaveInfo').hide();
    var requestUrl = ctx + "/flow/getOrderSaleFlowInfo.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.taskStatus == "1") {
                $("#openDock_osf").attr("checked", true);
            } else {
                $("#closeDock_osf").attr("checked", true);
            }
            $("#frequency_osf").val(data.taskInterval == "" ? "1440" : data.taskInterval);
            $("#flowDateCount_osf").val(data.flowDateCount == "" ? "15" : data.flowDateCount);
            $("#sqlContext_osf").val(data.taskSQL);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function saveOrderSaleFlow() {
    $("#osfModal").modal("show");//显示“正在更新”字样的模态框
    var requestParam = {
        openDock: $("input[name='openDock_osf']:checked").val(),
        frequency: $("#frequency_osf").val(),
        flowDateCount: $("#flowDateCount_osf").val(),
        sqlContext: formatSql($("#sqlContext_osf").val())
    };
    if (verify(requestParam)) {
        var requestUrl = ctx + "/flow/saveOrderSaleFlow.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                $("#osfModal").modal("hide");//显示“正在更新”字样的模态框
                var dataObj = typeof data == 'string' ? jsonToObject(data) : data;
                if (dataObj.code == "200") {
                    lockButton('_osf');
                    alertModalb("保存成功！");
                } else if (dataObj.message != "" && dataObj.message != null) {
                    alertModalb(dataObj.message);
                } else {
                    alertModalb("保存失败，请检查推送配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $("#osfModal").modal("hide");//显示“正在更新”字样的模态框
                alertModalb("保存失败，请检查推送配置信息！");
            }
        });
    } else {
        $("#osfModal").modal("hide");//显示“正在更新”字样的模态框
        $("#_osfInfoLock").html("锁定");
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}

function getGoodsBatchFlow() {
    $('#_opgfSaveInfo').hide();
    var requestUrl = ctx + "/flow/getGoodsBatchFlowInfo.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.taskStatus == "1") {
                $("#openDock_opgf").attr("checked", true);
            } else {
                $("#closeDock_opgf").attr("checked", true);
            }
            $("#frequency_opgf").val(data.taskInterval == "" ? "5" : data.taskInterval);
            $("#sqlContext_opgf").val(data.taskSQL);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function saveGoodsBatchFlow() {
    $("#sysModal").modal("show");//显示“正在更新”字样的模态框
    var requestParam = {
        openDock: $("input[name='openDock_opgf']:checked").val(),
        frequency: $("#frequency_opgf").val(),
        sqlContext: formatSql($("#sqlContext_opgf").val())
    };
    if (verify(requestParam)) {
        var requestUrl = ctx + "/flow/saveGoodsBatchFlow.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                $("#sysModal").modal("hide");//显示“正在更新”字样的模态框
                var dataObj = typeof data == 'string' ? jsonToObject(data) : data;
                if (dataObj.code == "200") {
                    lockButton('_opgf');
                    alertModalb("保存成功！");
                } else if (dataObj.message != "" && dataObj.message != null) {
                    alertModalb(dataObj.message);
                } else {
                    alertModalb("保存失败，请检查推送配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModalb("保存失败，请检查推送配置信息！");
            }
        });
    } else {
        $("#sysModal").modal("hide");//显示“正在更新”字样的模态框
        $("#_opgfInfoLock").html("锁定");
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}

function sqlGoodsBatchFlowInfo() {
    $("#sysModal").modal("show");//显示“正在更新”字样的模态框
    $.ajax({
        url: ctx + "/client/sqlExecute.htm",
        type: "post",
        dataType: "json",
        data: {
            sqlContext: formatSql($("#sqlContext_opgf").val()),  //dateFormat($("#calendar").val())
            type: "40000008",
            flowDateCount: $("#frequency_opgf").val()
        },
        success: function (res) {
            $("#sysModal").modal("hide");//显示“正在更新”字样的模态框
            if (res.code != 200) {
                alertModalb("sql执行失败，" + res.rows);
                return;
            }

            $("#table-request-goodsBatchFlow").bootstrapTable('destroy');
            var $dataTableHot = $("#table-request-goodsBatchFlow").bootstrapTable({
                data: res.rows,
                pagination: true,
                columns: [{
                    field: "gb_id_no",
                    title: "库存流水ID"
                }, {
                    field: "gb_time",
                    title: "入库时间"
                }, {
                    field: "in_sn",
                    title: "药品内码"
                }, {
                    field: "gb_name",
                    title: "商品名称"
                }, {
                    field: "gb_batch_no",
                    title: "批次号"
                }, {
                    field: "gb_number",
                    title: "库存数量"
                }, {
                    field: "gb_unit",
                    title: "商品单位"
                }, {
                    field: "gb_produce_time",
                    title: "生产日期"
                }, {
                    field: "gb_end_time",
                    title: "效期"
                }, {
                    field: "gb_specifications",
                    title: "商品规格"
                }, {
                    field: "gb_license",
                    title: "批准文号"
                }, {
                    field: "gb_manufacturer",
                    title: "商品生产厂家"
                }, {
                    field: "gb_produce_address",
                    title: "生产地址"
                }]
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            $("#sysModal").modal("hide");//显示“正在更新”字样的模态框
            alertModalb("保存失败，请检查配置信息！");
        }
    });
}

function deleteGoodsBatchFlowInfoCache() {
    var requestUrl = ctx + "/flow/deleteGoodsBatchFlowInfoCache.htm";
    $.ajax({
        url: requestUrl,
        type: 'POST',
        dataType: "json",
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.code == 200) {
                lockButton('_opgf');
                alertModalb("清除缓存成功！");
            }
            if (data.code != 200) {
                alertModalb("清除缓存失败，" + data.message);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("清除缓存失败，请检查配置信息！");
        }
    });
}

function getOrderPurchaseDelivery() {
    $('#_opdSaveInfo').hide();
    var requestUrl = ctx + "/order/getOrderPurchaseDelivery.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.taskStatus == "1") {
                $("#openDock_opd").attr("checked", true);
            } else {
                $("#closeDock_opd").attr("checked", true);
            }
            $("#frequency_opd").val(data.taskInterval == "" ? "5" : data.taskInterval);
            $("#sqlContext_opd").val(data.taskSQL);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function saveOrderPurchaseDelivery() {
    var requestParam = {
        openDock: $("input[name='openDock_opd']:checked").val(),
        frequency: $("#frequency_opd").val(),
        sqlContext: formatSql($("#sqlContext_opd").val())
    };
    if (verify(requestParam)) {
        var requestUrl = ctx + "/order/saveOrderPurchaseDelivery.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                var dataObj = typeof data == 'string' ? jsonToObject(data) : data;
                if (dataObj.code == "200") {
                    lockButton('_opd');
                    alertModalb("保存成功！");
                } else if (dataObj.message != "" && dataObj.message != null) {
                    alertModalb(dataObj.message);
                } else {
                    alertModalb("保存失败，请检查抓取配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModalb("保存失败，请检查配置信息！");
            }
        });
    } else {
        $("#_opdInfoLock").html("锁定");
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}

function deleteOrderPurchaseDelivery() {
    var requestUrl = ctx + "/order/deleteOrderPurchaseDelivery.htm";
    $.ajax({
        url: requestUrl,
        type: 'POST',
        dataType: "json",
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.code == 200) {
                lockButton('_opd');
                alertModalb("清除缓存成功！");
            }
            if (data.code != 200) {
                alertModalb("清除缓存成功，" + data.message);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("清除缓存成功，请检查配置信息！");
        }
    });
}

function sqlOrderPurchaseDelivery() {
    $.ajax({
        url: ctx + "/client/sqlExecute.htm",
        type: "post",
        dataType: "json",
        data: {
            sqlContext: formatSql($("#sqlContext_opd").val()),
            type: "20000018"
        },
        success: function (res) {
            if (res.code != 200) {
                alertModalb("sql执行失败，" + res.rows);
                return;
            }

            $("#table-request-orderPurchaseDelivery").bootstrapTable('destroy');
            var $dataTableHot = $("#table-request-orderPurchaseDelivery").bootstrapTable({
                data: res.rows,
                pagination: true,
                columns: [{
                    field: "delivery_no",
                    title: "入库单号"
                }, {
                    field: "delivery_quantity",
                    title: "入库数量"
                }, {
                    field: "order_delivery_erp_id",
                    title: "POP订单ERP出库单ID"
                }]
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("保存失败，请检查配置信息！");
        }
    });
}

function sqlShopSaleFlowInfo() {
    $("#ossfModal").modal("show");//显示“正在更新”字样的模态框
    $.ajax({
        url: ctx + "/client/sqlExecute.htm",
        type: "post",
        dataType: "json",
        timeout:60000,
        data: {
            sqlContext: formatSql($("#sqlContext_ossf").val()),  //dateFormat($("#calendar").val())
            type: "40000012",
            frequency: $("#frequency_ossf").val(),
            flowDateCount: $("#flowDateCount_ossf").val()
        },
        success: function (res) {
            $("#ossfModal").modal("hide");//显示“正在更新”字样的模态框
            if (res.code != 200) {
                alertModalb("sql执行失败，" + res.rows);
                return;
            }

            $("#table-request-shopSaleFlow").bootstrapTable('destroy');
            var $dataTableHot = $("#table-request-shopSaleFlow").bootstrapTable({
                data: res.rows,
                pagination: true,
                columns: [{
                    field: "shop_no",
                    title: "门店编码"
                }, {
                    field: "shop_name",
                    title: "门店名称"
                }, {
                    field: "so_id",
                    title: "Erp销售订单主键"
                }, {
                    field: "so_no",
                    title: "Erp销售订单号"
                }, {
                    field: "so_time",
                    title: "销售日期"
                }, {
                    field: "enterprise_name",
                    title: "客户名称"
                }, {
                    field: "so_batch_no",
                    title: "批号"
                }, {
                    field: "so_quantity",
                    title: "销售数量"
                }, {
                    field: "so_product_time",
                    title: "生产日期"
                }, {
                    field: "so_effective_time",
                    title: "效期"
                }, {
                    field: "so_price",
                    title: "价格"
                }, {
                    field: "goods_in_sn",
                    title: "商品内码"
                }, {
                    field: "goods_name",
                    title: "商品名称"
                }, {
                    field: "so_license",
                    title: "批准文号"
                }, {
                    field: "so_specifications",
                    title: "商品规格"
                }, {
                    field: "so_unit",
                    title: "商品单位"
                }, {
                    field: "so_manufacturer",
                    title: "商品生产厂家"
                }]
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            $("#ossfModal").modal("hide");//显示“正在更新”字样的模态框
            alertModalb("保存失败，请检查配置信息！");
        }
    });
}

function deleteShopSaleFlowInfoCache() {
    var requestUrl = ctx + "/flow/deleteShopSaleFlowInfoCache.htm";
    $.ajax({
        url: requestUrl,
        type: 'POST',
        dataType: "json",
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.code == 200) {
                lockButton('_ossf');
                alertModalb("清除缓存成功！");
            }
            if (data.code != 200) {
                alertModalb("清除缓存失败，" + data.message);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("清除缓存失败，请检查配置信息！");
        }
    });
}

function getShopSaleFlow() {
    $('#_ossfSaveInfo').hide();
    var requestUrl = ctx + "/flow/getShopSaleFlowInfo.htm";
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.taskStatus == "1") {
                $("#openDock_ossf").attr("checked", true);
            } else {
                $("#closeDock_ossf").attr("checked", true);
            }
            $("#frequency_ossf").val(data.taskInterval == "" ? "1440" : data.taskInterval);
            $("#flowDateCount_ossf").val(data.flowDateCount == "" ? "1" : data.flowDateCount);
            $("#sqlContext_ossf").val(data.taskSQL);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function saveShopSaleFlow() {
    $("#ossfModal").modal("show");//显示“正在更新”字样的模态框
    var requestParam = {
        openDock: $("input[name='openDock_ossf']:checked").val(),
        frequency: $("#frequency_ossf").val(),
        flowDateCount: $("#flowDateCount_ossf").val(),
        sqlContext: formatSql($("#sqlContext_ossf").val())
    };
    if (verify(requestParam)) {
        var requestUrl = ctx + "/flow/saveShopSaleFlow.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            type: 'POST',
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                $("#ossfModal").modal("hide");//显示“正在更新”字样的模态框
                var dataObj = typeof data == 'string' ? jsonToObject(data) : data;
                if (dataObj.code == "200") {
                    lockButton('_ossf');
                    alertModalb("保存成功！");
                } else if (dataObj.message != "" && dataObj.message != null) {
                    alertModalb(dataObj.message);
                } else {
                    alertModalb("保存失败，请检查推送配置信息！");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $("#ossfModal").modal("hide");//显示“正在更新”字样的模态框
                alertModalb("保存失败，请检查推送配置信息！");
            }
        });
    } else {
        $("#ossfModal").modal("hide");//显示“正在更新”字样的模态框
        $("#_ossfInfoLock").html("锁定");
        alertModalb("有必输项未输入或输入错误，请检查！");
    }
}

