<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<body>
<div class="tab-pane fade" id="shopSaleFlow">
    <div class="form-horizontal padding-t-10">
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>对接选项:</label>
            <div class="col-xs-3 control-label text-left">
                <input type="radio" value="true" name="openDock_ossf" id="openDock_ossf" onclick="radioChecked(this);"/>开启对接
                &nbsp;&nbsp;-&nbsp;&nbsp;
                <input type="radio" value="false" name="closeDock_ossf" id="closeDock_ossf"
                       onclick="radioChecked(this);"/>关闭对接
            </div>
            <label for="scope" class="col-xs-1 control-label"><em>*</em>同步频率:</label>
            <div class="col-xs-3 control-label text-left">
                <input type="text" class="form-control width-70" id="frequency_ossf" name="frequency_ossf" value="1440"/>分&nbsp;&nbsp;<font color="red">同步频率1440分钟(24小时)以内</font>
            </div>
            <label for="scope" class="col-xs-1 control-label"><em>*</em>同步天数:</label>
            <div class="col-xs-3 control-label text-left">
                <input type="text" class="form-control width-70" id="flowDateCount_ossf" name="flowDateCount_ossf" value="2"/>天<br />
                <font color="red">查询天数默认为2即为从今天开始向前推算2天，共查询2天的数据</font>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>查询SQL:</label>
            <div class="col-xs-10 control-label text-left">
                <textarea rows="15" class="form-control" cols="50" id="sqlContext_ossf" name="sqlContext_ossf" wrap="soft"
                          placeholder="请输入SQL语句"></textarea>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"></label>
            <div class="col-xs-3 control-label text-left">
                <button id="_ossfInfoLock" type="button" class="btn btn-info" onclick="lockButton('_ossf');">解锁</button>
                <!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
                <button id="_ossfSaveInfo" type="button" class="btn btn-info"
                        onclick="alertModal('是否保存?',function(){saveShopSaleFlow()});">保存
                </button>
                <button id="_ossfSql" type="button" class="btn btn-info" onclick="sqlShopSaleFlowInfo();">执行sql</button>

                <button id="_ossfDelete" type="button" class="btn btn-info" onclick="deleteShopSaleFlowInfoCache();">
                    清除缓存数据
                </button>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>需要的字段:</label>>
            <div class="col-xs-10 control-label text-left">
                shop_no:门店编码,shop_name:门店名称,so_id:Erp销售订单主键,so_no:Erp销售订单号,so_time:销售日期,enterprise_name:客户名称,so_batch_no:批号,so_quantity:销售数量,so_product_time:生产日期,so_effective_time:效期,so_price:价格,goods_in_sn:商品内码,goods_name:商品名称,so_license:批准文号,so_specifications:商品规格,so_unit:商品单位,so_manufacturer:商品生产厂家
            </div>
        </div>
        <div class="form-group">
            <div style="overflow:scroll;">
                <table class="table table-hover" id="table-request-shopSaleFlow"
                       style="min-width:1000px;font-size: 14px">
                </table>
            </div>
        </div>
        <!-- 模态框（Modal） -->
        <div class="modal fade" id="ossfModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
</div>
</body>
</html>