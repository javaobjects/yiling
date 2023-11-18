<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<div class="tab-pane fade" id="stock">
	<div class="form-horizontal padding-t-10">
		<div class="form-group">
			<label for="scope" class="col-xs-1 control-label"><em>*</em>对接选项:</label>
			<div class="col-xs-3 control-label text-left">
				<input type="radio" value="true" name="openDock_gb" id="openDock_gb" onclick="radioChecked(this);" />开启对接
				&nbsp;&nbsp;-&nbsp;&nbsp;
				<input type="radio" value="false" name="closeDock_gb" id="closeDock_gb" onclick="radioChecked(this);" />关闭对接
			</div>
			<label for="scope" class="col-xs-1 control-label"><em>*</em>同步频率:</label>
			<div class="col-xs-3 control-label text-left">
				<input type="text" class="form-control width-70" id="frequency_gb" name="frequency_gb" value="5" />分&nbsp;&nbsp;<font color="red">同步频率1440分钟(24小时)以内</font>
			</div>
		</div>
		<div class="form-group">
			<label for="scope" class="col-xs-1 control-label"><em>*</em>查询SQL:</label>
			<div class="col-xs-10 control-label text-left">
				<textarea rows="15" class="form-control" cols="50" id="sqlContext_gb" name="sqlContext_gb" wrap="soft" placeholder="请输入SQL语句"></textarea>
			</div>
        </div>
        <div class="form-group">
			<label for="scope" class="col-xs-1 control-label"></label>
			<div class="col-xs-3 control-label text-left">
				<button id="_gbInfoLock" type="button" class="btn btn-info" onclick="lockButton('_gb');">解锁</button>
				<!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
				<button id="_gbSaveInfo" type="button" class="btn btn-info" onclick="saveGoodsBatch();">保存</button>

				<button id="_gbSql" type="button" class="btn btn-info" onclick="sqlGoodsBatch();">执行sql</button>

				<button id="_gbDelet" type="button" class="btn btn-info" onclick="deleteGoodsBatchCache();">清除缓存数据</button>
			</div>
		</div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>需要的字段:</label>
            <div class="col-xs-10 control-label text-left">
                    gb_id_no:库存主键,in_sn:商品内码,gb_batch_no:批次号,gb_produce_time:生产时间,gb_end_time:有效期,gb_produce_address:生产地址,gb_number:可销售库存
            </div>
        </div>
		<div class="form-group">
			<div style="overflow:scroll;">
				<table class="table table-hover" id="table-request-stock"
					   style="min-width:1000px; font-size: 14px">
				</table>
			</div>
		</div>
	</div>
</div>
</body>
</html>