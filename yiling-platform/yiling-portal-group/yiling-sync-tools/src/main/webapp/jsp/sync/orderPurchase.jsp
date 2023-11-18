<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<body>
<div class="tab-pane fade" id="orderPurchase">
	<div class="form-horizontal padding-t-10">
		<div class="form-group">
			<label for="scope" class="col-xs-1 control-label"><em>*</em>对接选项:</label>
			<div class="col-xs-3 control-label text-left">
				<input type="radio" value="true" name="openDock_op" id="openDock_op" onclick="radioChecked(this);" />开启对接
				&nbsp;&nbsp;-&nbsp;&nbsp;
				<input type="radio" value="false" name="closeDock_op" id="closeDock_op" onclick="radioChecked(this);" />关闭对接
			</div>
			<label for="scope" class="col-xs-1 control-label"><em>*</em>同步频率:</label>
			<div class="col-xs-3 control-label text-left">
				<input type="text" class="form-control width-70" id="frequency_op" name="frequency_op" value="5" />分  <font color="red">同步频率1440分钟(24小时)以内</font>
			</div>
		</div>
		<div class="form-group">
			<label for="scope" class="col-xs-1 control-label"></label>
			<div class="col-xs-3 control-label text-left">
				<button id="_opInfoLock" type="button" class="btn btn-info" onclick="lockButton('_op');">解锁</button>
				<!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
				<button id="_opSaveInfo" type="button" class="btn btn-info" onclick="alertModal('是否保存?',function(){saveOrderPurchase()});">保存</button>
			</div>
		</div>
	</div>
</div>
</body>
</html>