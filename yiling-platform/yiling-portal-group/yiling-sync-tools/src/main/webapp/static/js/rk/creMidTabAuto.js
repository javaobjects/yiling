$(function () {
    getTemplateInfo();
    lockInfo('_oi');
});


function saveConfig() {
    var requestParam = {
        status: $("#status_oi").val(),
        dbName: $("#dbName_oi").val(),
        dbType: $("#dbType_oi").val(),
        oracleType: $("#dbOracleType_oi").val(),
        oracleSid: $("#dbOracleSid_oi").val(),
        dbCharacter: $("#dbCharacter_oi").val(),
        dbLoginName: $("#dbLoginName_oi").val(),
        dbLoginPW: $("#dbLoginPW_oi").val(),
        dbIp: $("#dbIP_oi").val(),
        dbPort: $("#dbPort_oi").val(),
        orderSql: $("#sqlContext_Order_oi").val(),
        orderDetailSql: $("#sqlContext_OrderDetail_oi").val()
    };

    // if (requestParam.orderSql === "" || requestParam.orderSql == null) {
    //     alertModalb("订单建表sql信息错误！");
    //     return;
    // }
    //
    // if (requestParam.orderDetailSql === "" || requestParam.orderDetailSql == null) {
    //     alertModalb("订单明细建表sql信息错误！");
    //     return;
    // } else {
        var requestUrl = ctx + "/erp/saveandcreate.htm";
        $.ajax({
            url: requestUrl,
            data: requestParam,
            dataType: 'json',
            type: 'POST',
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (data) {
                if (data.flag == "1") {
                    lockButton('_oi');
                    getTemplateInfo();
                    alertModalb("保存并创建成功！");
                }
                if (data.flag == "0") {
                    alertModalb("创建失败:" + data.result);
                    lockButton('_oi');
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModalb("保存失败，请检查sql信息！");
            }
        });
    // }
}

function saveBaseTable() {
    var requestUrl = ctx + "/erp/saveBaseTable.htm";

    $.ajax({
        url: requestUrl,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if (data.flag == "1") {
                lockButton('_oi');
                alertModalb("保存并创建成功！");
            }
            if (data.flag == "0") {
                alertModalb("创建失败:" + data.result);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModalb("保存失败，请检查sql信息！");
        }
    });
}

function showOracleInfo() {
    var type = $("#dbType_oi").val();

    if (type == "Oracle") {
        $("#dbOracleTypeLabel_oi").show();
        $("#dbOracleType_oi").show();
        $("#dbOracleSidLabel_oi").show();
        $("#dbOracleSid_oi").show();
    } else {
        $("#dbOracleTypeLabel_oi").hide();
        $("#dbOracleType_oi").hide();
        $("#dbOracleSidLabel_oi").hide();
        $("#dbOracleSid_oi").hide();
        if($("#dbOracleType_oi").val() == '' || $("#dbOracleType_oi").val() == null){
            $("#dbOracleType_oi").val('sidName');
        }
        if($("#dbOracleSid_oi").val() == '' || $("#dbOracleSid_oi").val() == null){
            $("#dbOracleSid_oi").val('oracleSid');
        }
    }

    // var requestParam = {
    //     dbType: type
    // };
    //
    // var requestUrl = ctx + "/erp/getSqlByType.htm";
    // $.ajax({
    //     url: requestUrl,
    //     data: requestParam,
    //     type: 'POST',
    //     dataType: 'json',
    //     contentType: "application/x-www-form-urlencoded; charset=UTF-8",
    //     success: function (data) {
    //         if ((data.orderSql != "" && data.orderSql != null)) {
    //             $("#sqlContext_Order_oi").val(data.orderSql);
    //         }
    //         if ((data.orderDetailSql != "" && data.orderDetailSql != null)) {
    //             $("#sqlContext_OrderDetail_oi").val(data.orderDetailSql);
    //         }
    //     },
    //     error: function (XMLHttpRequest, textStatus, errorThrown) {
    //         //			alertModalb("获取数据库配置信息失败！");
    //     }
    // });
}

function testButton() {
    var requestParam = {
        dbName: $("#dbName_oi").val(),
        dbType: $("#dbType_oi").val(),
        dbOracleType: $("#dbOracleType_oi").val(),
        dbCharacter: $("#dbCharacter_oi").val(),
        dbLoginName: $("#dbLoginName_oi").val(),
        dbLoginPW: $("#dbLoginPW_oi").val(),
        dbIP: $("#dbIP_oi").val(),
        dbPort: $("#dbPort_oi").val(),
        dbOracleSid: $("#dbOracleSid_oi").val()
    };

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
            //			alertModalb("获取数据库配置信息失败！");
        }
    });

}

function getTemplateInfo() {
    $('#_oiSaveInfo').hide();
    var requestParam = {
        sqlType: 'oracle'
    };
    var requestUrl = ctx + "/erp/getSql.htm";
    $.ajax({
        url: requestUrl,
        data: requestParam,
        dataType: 'json',
        type: 'POST',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (data) {
            if ((data.orderSql != "" && data.orderSql != null)) {
                $("#sqlContext_Order_oi").val(data.orderSql);
            }
            if ((data.orderDetailSql != "" && data.orderDetailSql != null)) {
                $("#sqlContext_OrderDetail_oi").val(data.orderDetailSql);
            }
            if ((data.dbName != "" && data.dbName != null)) {
                $("#dbName_oi").val(data.dbName);
            }
            if ((data.dbType != "" && data.dbType != null)) {
                $("#dbType_oi").val(data.dbType);
            }
            if ((data.oracleType != "" && data.oracleType != null)) {
                $("#oracleType_oi").val(data.oracleType);
            }
            if ((data.oracleSid != "" && data.oracleSid != null)) {
                $("#dbOracleSid_oi").val(data.oracleSid);
            }
            if ((data.dbCharset != "" && data.dbCharset != null)) {
                $("#dbCharset_oi").val(data.dbCharset);
            }
            if ((data.dbLoginName != "" && data.dbLoginName != null)) {
                $("#dbLoginName_oi").val(data.dbLoginName);
            }
            if ((data.dbLoginPW != "" && data.dbLoginPW != null)) {
                $("#dbLoginPW_oi").val(data.dbLoginPW);
            }
            if ((data.dbIp != "" && data.dbIp != null)) {
                $("#dbIP_oi").val(data.dbIp);
            }
            if ((data.dbPort != "" && data.dbPort != null)) {
                $("#dbPort_oi").val(data.dbPort);
            }
            if (data.status != "" && data.status != null && data.status === '1') {
                $("#createResult").html("订单表已创建");
            } else {
                $("#createResult").html("订单表未创建");
            }
            showOracleInfo();
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
    $(".form-control").each(function () {
        var t_id = $(this).attr("id");
        if (t_id.indexOf('' + obj + '') >= 0) {
            $('#' + t_id + '').attr("disabled", false);
        }
    })
}

function lockInfo(obj) {
    $(".form-control").each(function () {
        var t_id = $(this).attr("id");
        if (t_id.indexOf('' + obj + '') >= 0) {
            $('#' + t_id + '').attr("disabled", true);
        }
    })
}