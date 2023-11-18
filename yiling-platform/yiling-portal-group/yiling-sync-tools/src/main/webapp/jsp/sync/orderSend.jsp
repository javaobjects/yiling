<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<div class="tab-pane fade" id="placeNo">
    <div class="form-horizontal padding-t-10">
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>对接选项:</label>
            <div class="col-xs-3 control-label text-left">
                <input type="radio" value="true" name="openDock_os" id="openDock_os"
                       onclick="radioChecked(this);"/>开启对接
                &nbsp;&nbsp;-&nbsp;&nbsp;
                <input type="radio" value="false" name="closeDock_os" id="closeDock_os"
                       onclick="radioChecked(this);"/>关闭对接
            </div>
            <label for="scope" class="col-xs-1 control-label"><em>*</em>同步频率:</label>
            <div class="col-xs-3 control-label text-left">
                <input type="text" class="form-control width-70" id="frequency_os" name="frequency_os"
                       value="5"/>分&nbsp;&nbsp;<font color="red">同步频率1440分钟(24小时)以内</font>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>查询SQL:</label>
            <div class="col-xs-10 control-label text-left">
                <textarea rows="15" class="form-control" cols="50" id="sqlContext_os" name="sqlContext_os"
                          wrap="soft" placeholder="请输入SQL语句"></textarea>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"></label>
            <div class="col-xs-3 control-label text-left">
                <button id="_osInfoLock" type="button" class="btn btn-info" onclick="lockButton('_os');">解锁
                </button>
                <button id="_osSaveInfo" type="button" class="btn btn-info"
                        onclick="saveOrderSend();">保存
                </button>
                <button id="_oSql" type="button" class="btn btn-info" onclick="sqlOrderSend();">执行sql</button>
                <button id="_osDelete" type="button" class="btn btn-info" onclick="deleteOrderSendCache();">
                    清除缓存数据
                </button>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>需要的字段:</label>
            <div class="col-xs-10 control-label text-left">
                    osi_id:发货单主键,order_id:订单Id,order_detail_id:订单明细Id,delivery_number:出库单号,send_batch_no:批次号,effective_time:有效期,product_time:生产日期,send_num:发货数量,send_type:发货单类型（1正常发货2订单关闭3作废）
            </div>
        </div>
        <div class="form-group">
            <div style="overflow:scroll;">
                <table class="table table-hover" id="table-request-placeNo"
                       style="min-width:1000px;font-size: 14px">
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>