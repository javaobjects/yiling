<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<body>
<div class="tab-pane fade" id="orderPurchaseDelivery">
    <div class="form-horizontal padding-t-26">
        <div class="form-group">
            <label for="scope" class="col-xs-2 control-label"><em>*</em>对接选项:</label>
            <div class="col-xs-3 control-label text-left">
                <input type="radio" value="true" name="openDock_opd" id="openDock_opd"
                       onclick="radioChecked(this);"/>开启对接
                &nbsp;&nbsp;-&nbsp;&nbsp;
                <input type="radio" value="false" name="closeDock_opd" id="closeDock_opd"
                       onclick="radioChecked(this);"/>关闭对接
            </div>
            <label for="scope" class="col-xs-2 control-label"><em>*</em>同步频率:</label>
            <div class="col-xs-3 control-label text-left">
                <input type="text" class="form-control width-70" id="frequency_opd" name="frequency_opd"
                       value="5"/>分&nbsp;&nbsp;<font color="red">同步频率1440分钟(24小时)以内</font>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-2 control-label"><em>*</em>查询SQL:</label>
            <div class="col-xs-3 control-label text-left">
                <textarea rows="10" class="form-control" cols="50" id="sqlContext_opd" name="sqlContext_opd"
                          wrap="soft" placeholder="请输入SQL语句"></textarea>
            </div>
            <label for="scope" class="col-xs-2 control-label"></label>
            <div class="col-xs-3 control-label text-left">
                <button id="_opdInfoLock" type="button" class="btn btn-info" onclick="lockButton('_opd');">解锁</button>

                <button id="_opdSaveInfo" type="button" class="btn btn-info" onclick="saveOrderPurchaseDelivery();">保存</button>

                <button id="_opdSql" type="button" class="btn btn-info" onclick="sqlOrderPurchaseDelivery();">执行sql</button>

                <button id="_opdDelete" type="button" class="btn btn-info" onclick="deleteOrderPurchaseDelivery();">清除缓存数据</button>
                <div style="margin-top:25px;">
                    <font color="red">需要同步的字段：</font>
                    <p>
                        delivery_no:入库单号,delivery_quantity:入库数量,order_delivery_erp_id:POP订单ERP出库单ID</p>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div style="overflow:scroll;">
                <table class="table table-hover" id="table-request-orderPurchaseDelivery"
                       style="min-width:1000px;font-size: 14px">
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>