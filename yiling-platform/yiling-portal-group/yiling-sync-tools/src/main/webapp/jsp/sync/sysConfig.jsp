<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<div class="tab-pane fade in active" id="dataSource">
	<div class="form-horizontal padding-t-10">
		<div class="form-group">
			<label for="scope" class="col-xs-1 control-label"><em>*</em>数据库名称:</label>
			<div class="col-xs-3">
				<input type="text" class="form-control" id="dbName" name="dbName" placeholder="数据库名称">
			</div>
			<label for="scope" class="col-xs-1 control-label"><em>*</em>数据库类型:</label>
			<div class="col-xs-3">
				<select class="form-control" id="dbType" name="dbType" onclick="showOracleInfo();">
					<option value="Mysql">Mysql</option>
					<option value="Oracle">Oracle</option>
					<option value="SQL Server">SQL Server</option>
					<option value="SQL Server2000">SQL Server2000</option>
					<option value="ODBC">ODBC</option>
					<option value="ODBC-DBF">ODBC-DBF</option>
					<option value="DB2">DB2</option>
				</select>
			</div>
		</div>
		<div class="form-group">
			<label for="scope" class="col-xs-1 control-label"><div id="dbOracleTypeLabel"><em>*</em>Oracle类型:</div></label>
			<div class="col-xs-3">
				<select class="form-control" id="dbOracleType" name="dbOracleType">
					<option value="sidName">SIDName</option>
					<option value="serviceName">ServiceName</option>
				</select>
			</div>
            <label for="scope" class="col-xs-1 control-label">
                <div id="dbOracleSidLabel"><em>*</em>Oracle实例(SIDName):</div>
            </label>
            <div class="col-xs-3">
                <input type="text" class="form-control" id="dbOracleSid" name="dbOracleSid" placeholder="Oracle实例名称">
            </div>
		</div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"><em>*</em>字符集:</label>
            <div class="col-xs-3">
                <select class="form-control" id="dbCharacter" name="dbCharacter">
                    <option value="UTF-8">UTF-8</option>
                    <option value="GBK">GBK</option>
                    <option value="gb2312">gb2312</option>
                </select>
            </div>
        </div>
		<div class="form-group">
			<label for="scope" class="col-xs-1 control-label">登录名:</label>
			<div class="col-xs-3">
				<input type="text" class="form-control" id="dbLoginName" name="dbLoginName" placeholder="登录名">
			</div>
			<label for="scope" class="col-xs-1 control-label">密码:</label>
			<div class="col-xs-3">
				<input type="password" class="form-control" id="dbLoginPW" name="dbLoginPW" placeholder="密码">
			</div>
		</div>
		<div class="form-group">
			<label for="scope" class="col-xs-1 control-label"><em>*</em>数据库IP:</label>
			<div class="col-xs-3">
				<input type="text" class="form-control" id="dbIP" name="dbIP" placeholder="数据库IP">
			</div>
			<label for="scope" class="col-xs-1 control-label">数据库端口:</label>
			<div class="col-xs-3">
				<input type="text" class="form-control" id="dbPort" name="dbPort" placeholder="数据库端口">
			</div>
		</div>
		<div class="form-group">
			<label for="scope" class="col-xs-1 control-label"><em>*</em>企业Key:</label>
			<div class="col-xs-3 control-label text-left">
				<input type="text" class="form-control" id="dbKey" name="dbKey" placeholder="企业Key">
			</div>
			<label for="scope" class="col-xs-1 control-label"><em>*</em>企业密钥:</label>
			<div class="col-xs-3">
				<input type="text" class="form-control" id="dbSecret" name="dbSecret" placeholder="企业密钥">
			</div>
		</div>
		<div class="form-group">
			<label for="scope" class="col-xs-1 control-label"><em>*</em>接口前缀地址:</label>
			<div class="col-xs-3">
				<input type="text" class="form-control" id="dbPath" name="dbPath" value="http://39.103.129.98:9982/router/rest">
			</div>
		</div>
		<div class="form-group">
			<label for="scope" class="col-xs-1 control-label"></label>
			<div class="col-xs-3">
				<button type="button" class="btn btn-info" onclick="testButton();">测试数据库连接</button>
			</div>
			<label for="scope" class="col-xs-1 control-label"></label>
			<div class="col-xs-3">
				<button id="dbInfoLock" type="button" class="btn btn-info" onclick="lockButton('db');">解锁</button>
				<!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
				<button id="dbSaveInfo" type="button" class="btn btn-info" onclick="alertModal('是否保存?',function(){saveDB()});">保存</button>
			</div>
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
</body>
</html>