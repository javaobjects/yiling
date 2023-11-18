package com.yiling.erp.client.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.erp.client.util.DataBaseConnection;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.TaskConfig;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OperTypeEnum;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Slf4j
@Service
public class TaskConfigDao {

    @Autowired
    private SQLiteHelper sqliteHelper;

    public List<TaskConfig> executeQueryTaskConfigList(String dbPath, String database, String sql) throws Exception {
        List<TaskConfig> rsList = new ArrayList<TaskConfig>();
        Statement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement prsts = null;
        try {
            connection = sqliteHelper.getConnection(dbPath, database);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            prsts = connection.prepareStatement(sql);
            // 判断列名是否存在
            boolean flowDateCountEexistFlag = DataBaseConnection.getInstance().columnNameIsEexist("flowDateCount", prsts);

            while (resultSet.next()) {
                TaskConfig taskConfig = new TaskConfig();
                taskConfig.setTaskNo(resultSet.getString("taskNo"));
                taskConfig.setTaskName(resultSet.getString("taskName"));
                taskConfig.setTaskInterval(resultSet.getString("taskInterval"));
                String taskSQL = resultSet.getString("taskSQL");
                taskConfig.setTaskSQL(taskSQL);
                taskConfig.setTaskStatus(resultSet.getString("taskStatus"));
                taskConfig.setTaskKey(resultSet.getString("taskKey"));
                taskConfig.setSpringId(resultSet.getString("springId"));
                taskConfig.setMethodName(resultSet.getString("methodName"));
                if (flowDateCountEexistFlag) {
                    String flowDateCount = resultSet.getString("flowDateCount");
                    if (StringUtils.isNotBlank(StrUtil.nullToEmpty(flowDateCount)) && !ObjectUtil.equal("null", flowDateCount)) {
                        taskConfig.setFlowDateCount(flowDateCount);
                    }
                }
                taskConfig.setUpdateTime(resultSet.getString("updateTime"));
                taskConfig.setCreateTime(resultSet.getString("createTime"));
                rsList.add(taskConfig);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return rsList;
    }

    /**
     * 新增或更新 TaskConfig
     *
     * @param dbPath
     * @param database
     * @param taskConfig
     * @return
     * @throws Exception
     */
    public boolean saveTaskConfig(String dbPath, String database, TaskConfig taskConfig) throws Exception {
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            // 用于拼sql
            StringBuilder sb = new StringBuilder();
            Map<String, Object> map = new LinkedHashMap<>();
            List<String> key = new LinkedList<>();
            // 拿到字段对应的值，并且放入map中
            map.put("taskNo", taskConfig.getTaskNo());
            map.put("taskName", taskConfig.getTaskName());
            map.put("taskInterval", taskConfig.getTaskInterval());
            map.put("taskSQL", taskConfig.getTaskSQL());
            map.put("taskStatus", taskConfig.getTaskStatus());
            map.put("taskKey", taskConfig.getTaskKey());
            map.put("springId", taskConfig.getSpringId());
            map.put("methodName", taskConfig.getMethodName());
            map.put("syncStatus", 0);
            map.put("flowDateCount", StrUtil.nullToEmpty(taskConfig.getFlowDateCount()));
            map.put("updateTime", taskConfig.getUpdateTime());
            map.put("createTime", taskConfig.getCreateTime());

            int index = 0;
            for (Map.Entry<String, Object> m : map.entrySet()) {
                key.add(index, m.getKey());
                index++;
            }

            List<TaskConfig> lists = executeQueryTaskConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from task_config where taskNo=" + taskConfig.getTaskNo());
            boolean insertFlag = false;
            if (CollectionUtil.isEmpty(lists)) {
                // insert
                insertFlag = true;
                // 拼接sql
                sb.append("INSERT INTO main.task_config(");
                for (int i = 0; i < key.size() - 1; i++) {
                    sb.append(key.get(i) + ",");
                }
                sb.append(key.get(key.size() - 1) + ") VALUES (");
                for (int i = 0; i < key.size() - 1; i++) {
                    sb.append("? , ");
                }
                sb.append("? )");
            } else {
                // update
                // 拼接sql
                sb.append("UPDATE main.task_config SET ");
                for (int i = 1; i < key.size() - 1; i++) {
                    sb.append(key.get(i) + "=?, ");
                }
                sb.append(key.get(key.size() - 1) + "=? WHERE taskNo=?");
            }

            connection = sqliteHelper.getConnection(dbPath, database);
            ps = connection.prepareStatement(sb.toString());
            // insert ....values(?,?,?) / UPDATE SET =?,=? .... 给站位符 ? 赋值
            if (insertFlag) {
                for (int i = 0; i < key.size(); i++) {
                    ps.setObject(i + 1, map.get(key.get(i)) == null ? "" : map.get(key.get(i)));
                }
            } else {
                for (int i = 1; i < key.size(); i++) {
                    ps.setObject(i, map.get(key.get(i)) == null ? "" : map.get(key.get(i)));
                }
                ps.setObject(key.size(), map.get(key.get(0)) == null ? "" : map.get(key.get(0)));
            }

            int result = ps.executeUpdate();
            if (result > 0) {
                return true;
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    public Map<String, String> findMapAll(String dbPath, String database, String methodName) throws Exception {
        Statement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        Map<String, String> map = new HashMap<>();
        try {
            connection = sqliteHelper.getConnection(dbPath, database);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from " + methodName);
            while (resultSet.next()) {
                map.put(resultSet.getString("key"), resultSet.getString("md5"));
            }

        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return map;
    }

    public void sqliteDataAll(String dbPath, String database, String methodName, List<BaseErpEntity> keys) throws Exception {
        List<BaseErpEntity> addList = new ArrayList<>();
        List<BaseErpEntity> updateList = new ArrayList<>();
        for (BaseErpEntity baseErpEntity : keys) {
            if (baseErpEntity.getOperType().equals(OperTypeEnum.ADD.getCode())) {
                addList.add(baseErpEntity);
            } else if (baseErpEntity.getOperType().equals(OperTypeEnum.UPDATE.getCode())) {
                updateList.add(baseErpEntity);
            }
        }

        Statement statement = null;
        PreparedStatement psts = null;
        PreparedStatement psts1 = null;
        Connection connection = null;
        try {
            connection = sqliteHelper.getConnection(dbPath, database);
            statement = connection.createStatement();
            connection.setAutoCommit(false); // 设置手动提交
            if(CollectionUtil.isNotEmpty(addList)) {
                psts = connection.prepareStatement("insert into " + methodName + " (key,md5)values(?,?)");
                for (BaseErpEntity baseErpEntity : addList) {
                    if (ObjectUtil.equal(ErpTopicName.ErpPurchaseFlow.getTopicName(), methodName)
                            || ObjectUtil.equal(ErpTopicName.ErpSaleFlow.getTopicName(), methodName)) {
                        psts.setString(1, baseErpEntity.getFlowKey());
                        psts.setString(2, baseErpEntity.getFlowCacheFileMd5());
                    } else {
                        psts.setString(1, baseErpEntity.getErpPrimaryKey());
                        psts.setString(2, baseErpEntity.sign());
                    }
                    psts.addBatch();
                }
                psts.executeBatch(); // 执行批量处理
            }
            if(CollectionUtil.isNotEmpty(updateList)) {
                String sb = "delete from " + methodName + " where key in (";
                for (BaseErpEntity baseErpEntity : updateList) {
                    if (ObjectUtil.equal(ErpTopicName.ErpPurchaseFlow.getTopicName(), methodName)
                            || ObjectUtil.equal(ErpTopicName.ErpSaleFlow.getTopicName(), methodName)) {
                        sb = sb + "'" + baseErpEntity.getFlowKey() + "',";
                    } else {
                        sb = sb + "'" + baseErpEntity.getErpPrimaryKey() + "',";
                    }
                }
                sb = sb.substring(0, sb.length() - 1) + ")";
                statement.executeUpdate(sb);

                StringBuffer insert = new StringBuffer("insert into " + methodName + " (key,md5)values(?,?)");
                psts1 = connection.prepareStatement(insert.toString());
                for (BaseErpEntity baseErpEntity : updateList) {
                    if (ObjectUtil.equal(ErpTopicName.ErpPurchaseFlow.getTopicName(), methodName)
                            || ObjectUtil.equal(ErpTopicName.ErpSaleFlow.getTopicName(), methodName)) {
                        psts1.setString(1, baseErpEntity.getFlowKey());
                        psts1.setString(2, baseErpEntity.getFlowCacheFileMd5());
                    } else {
                        psts1.setString(1, baseErpEntity.getErpPrimaryKey());
                        psts1.setString(2, baseErpEntity.sign());
                    }
                    psts1.addBatch();
                }
                psts1.executeBatch(); // 执行批量处理
            }
            connection.commit();  // 提交
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (psts != null) {
                psts.close();
            }
            if (psts1 != null) {
                psts1.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void deleteSqliteDataAll(String dbPath, String database, String methodName, List<BaseErpEntity> keys) throws Exception {
        Statement statement = null;
        Connection connection = null;
        try {
            connection = sqliteHelper.getConnection(dbPath, database);
            statement = connection.createStatement();
            String sb = "delete from " + methodName + " where key in (";
            for (BaseErpEntity baseErpEntity : keys) {
                sb = sb + "'" + baseErpEntity.getErpPrimaryKey() + "',";
            }
            sb = sb.substring(0, sb.length() - 1) + ")";
            statement.executeUpdate(sb);
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void deleteSqliteDataAll(String dbPath, String database, String methodName) throws Exception {
        Statement statement = null;
        Connection connection = null;
        try {
            connection = sqliteHelper.getConnection(dbPath, database);
            statement = connection.createStatement();
            String sb = "delete from " + methodName;
            statement.executeUpdate(sb);
            log.info("[" + methodName + "] 清除客户端缓存数据");
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * 删除数据并且添加数据
     *
     * @param dbPath
     * @param database
     * @param methodName
     * @throws Exception
     */
    public void deleteSqliteDataAndAddData(String dbPath, String database, String methodName, Map<String, String> mapData) throws Exception {
        Statement statement = null;
        Connection connection = null;
        PreparedStatement psts = null;
        try {
            connection = sqliteHelper.getConnection(dbPath, database);
            connection.setAutoCommit(false); // 设置手动提交
            statement = connection.createStatement();
            String sb = "delete from " + methodName;
            statement.executeUpdate(sb);

            psts = connection.prepareStatement("insert into " + methodName + " (key,md5)values(?,?)");
            for (Map.Entry<String, String> baseErpEntity : mapData.entrySet()) {
                psts.setString(1, baseErpEntity.getKey());
                psts.setString(2, baseErpEntity.getValue());
                psts.addBatch();
            }
            psts.executeBatch(); // 执行批量处理
            connection.commit();  // 提交
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (psts != null) {
                psts.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void addSqliteDataAll(String dbPath, String database, String methodName, List<BaseErpEntity> keys) throws Exception {
        PreparedStatement psts = null;
        Connection connection = null;
        try {
            connection = sqliteHelper.getConnection(dbPath, database);
            connection.setAutoCommit(false); // 设置手动提交
            psts = connection.prepareStatement("insert into " + methodName + " (key,md5)values(?,?)");
            for (BaseErpEntity baseErpEntity : keys) {
                if (ObjectUtil.equal(ErpTopicName.ErpPurchaseFlow.getTopicName(), methodName)
                        || ObjectUtil.equal(ErpTopicName.ErpSaleFlow.getTopicName(), methodName)) {
                    psts.setString(1, baseErpEntity.getFlowKey());
                    psts.setString(2, baseErpEntity.getFlowCacheFileMd5());
                } else {
                    psts.setString(1, baseErpEntity.getErpPrimaryKey());
                    psts.setString(2, baseErpEntity.sign());
                }
                psts.addBatch();
            }
            psts.executeBatch(); // 执行批量处理
            connection.commit();  // 提交
        } finally {
            if (psts != null) {
                psts.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }


    public void updateSqliteDataAll(String dbPath, String database, String methodName, List<BaseErpEntity> keys) throws Exception {
        Statement statement = null;
        Connection connection = null;
        PreparedStatement psts = null;
        try {
            connection = sqliteHelper.getConnection(dbPath, database);
            statement = connection.createStatement();
            connection.setAutoCommit(false); // 设置手动提交
            String sb = "delete from " + methodName + " where key in (";
            for (BaseErpEntity baseErpEntity : keys) {
                if (ObjectUtil.equal(ErpTopicName.ErpPurchaseFlow.getTopicName(), methodName)
                        || ObjectUtil.equal(ErpTopicName.ErpSaleFlow.getTopicName(), methodName)) {
                    sb = sb + "'" + baseErpEntity.getFlowKey() + "',";
                } else {
                    sb = sb + "'" + baseErpEntity.getErpPrimaryKey() + "',";
                }
            }
            sb = sb.substring(0, sb.length() - 1) + ")";
            statement.executeUpdate(sb);

            StringBuffer insert = new StringBuffer("insert into " + methodName + " (key,md5)values(?,?)");
            psts = connection.prepareStatement(insert.toString());
            for (BaseErpEntity baseErpEntity : keys) {
                if (ObjectUtil.equal(ErpTopicName.ErpPurchaseFlow.getTopicName(), methodName)
                        || ObjectUtil.equal(ErpTopicName.ErpSaleFlow.getTopicName(), methodName)) {
                    psts.setString(1, baseErpEntity.getFlowKey());
                    psts.setString(2, baseErpEntity.getFlowCacheFileMd5());
                } else {
                    psts.setString(1, baseErpEntity.getErpPrimaryKey());
                    psts.setString(2, baseErpEntity.sign());
                }
                psts.addBatch();
            }
            psts.executeBatch(); // 执行批量处理
            connection.commit();  // 提交
        } finally {
            if (psts != null) {
                psts.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public boolean updateTaskConfigSyncStatus(String dbPath, String database, String taskNo, String syncStatus) throws Exception {
        Statement statement = null;
        Connection connection = null;

        StringBuffer buf = new StringBuffer();
        buf.append("update task_config set syncStatus='").append(syncStatus).append("'").append(" where taskNo='").append(taskNo + "'");
        try {
            connection = sqliteHelper.getConnection(dbPath, database);
            statement = connection.createStatement();
            Integer i = statement.executeUpdate(buf.toString());
            if (i > 0) {
                return true;
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

}
