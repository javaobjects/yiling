<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<div class="tab-pane fade" id="customerPrice">
    <div class="form-horizontal padding-t-10">
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>对接选项:</label>
            <div class="col-xs-3 control-label text-left">
                <input type="radio" value="true" name="openDock_gcp" id="openDock_gcp" onclick="radioChecked(this);"/>开启对接
                &nbsp;&nbsp;-&nbsp;&nbsp;
                <input type="radio" value="false" name="closeDock_gcp" id="closeDock_gcp" onclick="radioChecked(this);"/>关闭对接
            </div>
            <label for="scope" class="col-xs-1 control-label"><em>*</em>同步频率:</label>
            <div class="col-xs-3 control-label text-left">
                <input type="text" class="form-control width-70" id="frequency_gcp" name="frequency_gcp"
                       value="5"/>分&nbsp;&nbsp;<font color="red">同步频率1440分钟(24小时)以内</font>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>查询SQL:</label>
            <div class="col-xs-10 control-label text-left">
                <textarea rows="15" class="form-control" cols="50" id="sqlContext_gcp" name="sqlContext_gcp" wrap="soft"
                          placeholder="请输入SQL语句"></textarea>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"></label>
            <div class="col-xs-3 control-label text-left">
                <button id="_gcpInfoLock" type="button" class="btn btn-info" onclick="lockButton('_gcp');">解锁</button>
                <!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
                <button id="_gcpSaveInfo" type="button" class="btn btn-info" onclick="saveGoodsCustomerPrice();">保存</button>

                <button id="_gcpSql" type="button" class="btn btn-info" onclick="sqlGoodsCustomerPrice();">执行sql</button>

                <button id="_gcpDelete" type="button" class="btn btn-info" onclick="deleteGoodsCustomerPrice();">清除缓存数据</button>
            </div>
        </div>
        <div class="form-group"
        <label for="scope" class="col-xs-1 control-label"><em>*</em>需要的字段:</label>>
            <div class="col-xs-10 control-label text-left">
                    gcp_id_no:主键,in_sn:商品内码,inner_code:客户内码,price:价格
            </div>
        </div>
        <div class="form-group">
			<div style="overflow:scroll;">
				<table class="table table-hover" id="table-request-customerPrice"
					   style="min-width:1000px;font-size: 14px">
				</table>
			</div>
        </div>
    </div>
</div>
</body>
</html>