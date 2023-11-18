<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<div class="tab-pane fade" id="customer">
    <div class="form-horizontal padding-t-10">
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>对接选项:</label>
            <div class="col-xs-3 control-label text-left">
                <input type="radio" value="true" name="openDock_cust" id="openDock_cust" onclick="radioChecked(this);">开启对接</input>
                &nbsp;&nbsp;-&nbsp;&nbsp;
                <input type="radio" value="false" name="closeDock_cust" id="closeDock_cust" onclick="radioChecked(this);">关闭对接</input>
            </div>
            <label for="scope" class="col-xs-1 control-label"><em>*</em>同步频率:</label>
            <div class="col-xs-3 control-label text-left">
                <input type="text" class="form-control width-70" id="frequency_cust" name="frequency_cust" value="5"/>分 <font color="red">同步频率1440分钟(24小时)以内</font></input>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>查询SQL:</label>
            <div class="col-xs-10 control-label text-left">
                <textarea rows="15" class="form-control" cols="50" id="sqlContext_cust" name="sqlContext_cust" wrap="soft"
                          placeholder="请输入SQL语句"></textarea>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"></label>
            <div class="col-xs-3 control-label text-left">
                <button id="_custInfoLock" type="button" class="btn btn-info" onclick="lockButton('_cust');">解锁</button>
                <button id="_custSaveInfo" type="button" class="btn btn-info" onclick="saveCustomerInfo();">保存</button>
                <button id="_dSql" type="button" class="btn btn-info" onclick="sqlCustomerInfo();">执行sql</button>

                <button id="_dDelete" type="button" class="btn btn-info" onclick="deleteCustomerInfoCache();">清除缓存数据</button>
            </div>
        </div>
        <div class="form-group">
            <div class="col-xs-10 control-label text-left">
                <label for="scope" class="col-xs-1 control-label"><em>*</em>需要的字段:</label>
                    inner_code:客户内码,sn:客户编码,name:企业名称,group_name:分组名称,license_no:营业执照号/医疗机构许可证,customer_type:终端类型,contact:联系人,phone:手机号码,province:省,city:市,region:区,address:详细地址
            </div>
        </div>
        <div class="form-group">
            <div style="overflow:scroll;">
                <table class="table table-hover" id="table-request-customer"
                       style="min-width:1000px;font-size: 14px">
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>