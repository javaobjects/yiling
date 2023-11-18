<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<div class="tab-pane fade" id="drug">
    <div class="form-horizontal padding-t-10">
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>对接选项:</label>
            <div class="col-xs-3 control-label text-left">
                <input type="radio" value="true" name="openDock_g" id="openDock_g" onclick="radioChecked(this);"/>开启对接
                &nbsp;&nbsp;-&nbsp;&nbsp;
                <input type="radio" value="false" name="closeDock_g" id="closeDock_g" onclick="radioChecked(this);"/>关闭对接
            </div>
            <label for="scope" class="col-xs-1 control-label"><em>*</em>同步频率:</label>
            <div class="col-xs-3 control-label text-left">
                <input type="text" class="form-control width-70" id="frequency_g" name="frequency_g"
                       value="5"/>分&nbsp;&nbsp;<font color="red">同步频率1440分钟(24小时)以内</font>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>查询SQL:</label>
            <div class="col-xs-10 control-label text-left">
                <textarea rows="15" class="form-control" cols="50" id="sqlContext_g" name="sqlContext_g" wrap="soft"
                          placeholder="请输入SQL语句"></textarea>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"></label>
            <div class="col-xs-3 control-label text-left">
                <button id="_gInfoLock" type="button" class="btn btn-info" onclick="lockButton('_g');">解锁</button>
                <!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
                <button id="_gSaveInfo" type="button" class="btn btn-info" onclick="saveGoods();">保存</button>

                <button id="_gSql" type="button" class="btn btn-info" onclick="sqlGoods();">执行sql</button>

                <button id="_gDelete" type="button" class="btn btn-info" onclick="deleteGoods();">清除缓存数据</button>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>需要的字段:</label>
            <div class="col-xs-10 control-label text-left">
                    in_sn:商品内码,sn:商品编码,bar_code:条形码,name:商品名称,name_code:商品名缩写,common_name:通用名,alias_name:商品别名,license_no:批准文号,specifications:规格,unit:单位,middle_package:中包装,big_package:大包装,manufacturer:生产厂家,manufacturer_code:厂家缩写,price:价格,number:数量,can_split:是否拆包销售
            </div>
        </div>
        <div class="form-group">
			<div style="overflow:scroll;">
				<table class="table table-hover" id="table-request-product"
					   style="min-width:1000px;font-size: 14px">
				</table>
			</div>
        </div>
    </div>
</div>
</body>
</html>