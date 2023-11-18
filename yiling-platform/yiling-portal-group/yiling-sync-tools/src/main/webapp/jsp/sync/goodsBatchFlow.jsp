<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<body>
<div class="tab-pane fade" id="goodsBatchFlow">
    <div class="form-horizontal padding-t-10">
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>对接选项:</label>
            <div class="col-xs-3 control-label text-left">
                <input type="radio" value="true" name="openDock_opgf" id="openDock_opgf" onclick="radioChecked(this);"/>开启对接
                &nbsp;&nbsp;-&nbsp;&nbsp;
                <input type="radio" value="false" name="closeDock_opgf" id="closeDock_opgf"
                       onclick="radioChecked(this);"/>关闭对接
            </div>
            <label for="scope" class="col-xs-1 control-label"><em>*</em>同步频率:</label>
            <div class="col-xs-3 control-label text-left">
                <input type="text" class="form-control width-70" id="frequency_opgf" name="frequency_opgf" value="5"/>分&nbsp;&nbsp;<font color="red">同步频率1440分钟(24小时)以内</font>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>查询SQL:</label>
            <div class="col-xs-10 control-label text-left">
                <textarea rows="15" class="form-control" cols="50" id="sqlContext_opgf" name="sqlContext_opgf" wrap="soft"
                          placeholder="请输入SQL语句"></textarea>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"></label>
            <div class="col-xs-3 control-label text-left">
                <button id="_opgfInfoLock" type="button" class="btn btn-info" onclick="lockButton('_opgf');">解锁</button>
                <!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
                <button id="_opgfSaveInfo" type="button" class="btn btn-info" onclick="saveGoodsBatchFlow();">保存</button>
                <button id="_opgfSql" type="button" class="btn btn-info" onclick="sqlGoodsBatchFlowInfo();">执行sql
                </button>

                <button id="_opgfDelete" type="button" class="btn btn-info" onclick="deleteGoodsBatchFlowInfoCache();">
                    清除缓存数据
                </button>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>需要的字段:</label>
            <div class="col-xs-10 control-label text-left">
                    gb_id_no:Erp库存流水ID,in_sn:药品内码,gb_batch_no:批次号,gb_produce_time:生产时间,gb_end_time:有效期,gb_produce_address:生产地址,gb_number:库存数量,gb_name:商品名称,gb_license:商品注册证号,gb_specifications:商品规格,gb_unit:商品单位,gb_manufacturer:商品生产厂家
            </div>
        </div>

        <div class="form-group">
            <div style="overflow:scroll;">
                <table class="table table-hover" id="table-request-goodsBatchFlow"
                       style="min-width:1000px;font-size: 14px">
                </table>
            </div>
        </div>

        <!-- 模态框（Modal） -->
        <div class="modal fade" id="sysModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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