<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<body>
<div class="tab-pane fade" id="orderPurchaseFlow">
    <div class="form-horizontal padding-t-10">
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>对接选项:</label>
            <div class="col-xs-3 control-label text-left">
                <input type="radio" value="true" name="openDock_opf" id="openDock_opf" onclick="radioChecked(this);"/>开启对接
                &nbsp;&nbsp;-&nbsp;&nbsp;
                <input type="radio" value="false" name="closeDock_opf" id="closeDock_opf"
                       onclick="radioChecked(this);"/>关闭对接
            </div>
            <label for="scope" class="col-xs-1 control-label"><em>*</em>同步频率:</label>
            <div class="col-xs-3 control-label text-left">
                <input type="text" class="form-control width-70" id="frequency_opf" name="frequency_opf" value="1440"/>分&nbsp;&nbsp;<font color="red">同步频率1440分钟(24小时)以内</font>
            </div>
            <label for="scope" class="col-xs-1 control-label"><em>*</em>同步天数:</label>
            <div class="col-xs-3 control-label text-left">
                <input type="text" class="form-control width-70" id="flowDateCount_opf" name="flowDateCount_opf" value="15"/>天<br />
                <font color="red">查询天数默认为15即为从今天开始向前推算15天，共查询15天的数据</font>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>查询SQL:</label>
            <div class="col-xs-10 control-label text-left">
                <textarea rows="15" class="form-control" cols="50" id="sqlContext_opf" name="sqlContext_opf" wrap="soft"
                          placeholder="请输入SQL语句"></textarea>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"></label>
            <div class="col-xs-3 control-label text-left">
                <button id="_opfInfoLock" type="button" class="btn btn-info" onclick="lockButton('_opf');">解锁</button>
                <!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
                <button id="_opfSaveInfo" type="button" class="btn btn-info"
                        onclick="alertModal('是否保存?',function(){saveOrderPurchaseFlow()});">保存
                </button>
                <button id="_opfSql" type="button" class="btn btn-info" onclick="sqlOrderPurchaseFlowInfo();">执行sql
                </button>

                <button id="_opfDelete" type="button" class="btn btn-info" onclick="deleteOrderPurchaseFlowInfoCache();">
                    清除缓存数据
                </button>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>需要的字段:</label>
            <div class="col-xs-10 control-label text-left">
                    po_id:Erp采购订单主键,po_no:Erp采购订单号,po_time:采购日期,order_time:下单日期,enterprise_inner_code:客户内码,enterprise_name:客户名称,po_batch_no:批号,po_quantity:采购数量,po_product_time:生产日期,po_effective_time:效期,po_price:价格,goods_in_sn:商品内码,goods_name:商品名称,po_license:批准文号,po_specifications:商品规格,po_unit:商品单位,po_manufacturer:商品生产厂家
            </div>
        </div>
    </div>
    <div class="form-group">
        <div style="overflow:scroll;">
            <table class="table table-hover" id="table-request-orderPurchaseFlow"
                   style="min-width:1000px;font-size: 14px">
            </table>
        </div>
    </div>
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="opfModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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