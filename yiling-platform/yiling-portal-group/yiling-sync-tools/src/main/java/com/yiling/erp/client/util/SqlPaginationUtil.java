package com.yiling.erp.client.util;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.yiling.erp.client.common.Constants;
import com.yiling.open.erp.enums.ErpTopicName;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * @author shuan
 */
@Slf4j
public class SqlPaginationUtil {

    public static String getSearchSql(String sql, String type) throws ErpException {
        if (StringUtils.isBlank(sql) || StringUtils.isBlank(type)) {
            throw new ErpException("数据库配置有误，请填写正确数据库配置");
        }

        String dataSql = "";
        if (type.trim().equals("Mysql")) {
            dataSql = "SELECT * FROM (" + sql + ") as lol";
        } else if (type.trim().equals("SQL Server") || type.trim().equals("SQL Server2000")) {
            dataSql = "SELECT * FROM (" + sql + ") lol";
        } else if (type.trim().equals("Oracle")) {
            dataSql = "SELECT * FROM(" + sql + ") lol";
        } else if (type.trim().equals("ODBC")) {
            dataSql = "SELECT * FROM (" + sql + ") lol";
        } else if (type.trim().equals("ODBC-DBF")) {
            dataSql = sql;
        } else {
            throw new ErpException("数据库类型有误，请选择正确的数据类型");
        }
        log.debug("执行sql：" + dataSql);
        return dataSql;
    }

    public static String getTopSql(String sql, String type,Integer limit) throws ErpException {
        if (StringUtils.isBlank(sql) || StringUtils.isBlank(type)) {
            throw new ErpException("数据库配置有误，请填写正确数据库配置");
        }

        String dataSql = "";
        if (type.trim().equals("Mysql")) {
            dataSql = "SELECT * FROM (" + sql + ")  lol limit " + limit;
        } else if (type.trim().equals("SQL Server") || type.trim().equals("SQL Server2000")) {
            dataSql = "SELECT TOP " + limit + " * FROM (" + sql + ") lol";
        } else if (type.trim().equals("Oracle")) {
            dataSql = "SELECT * FROM(" + sql + ") lol where rownum<"+limit;
        } else if (type.trim().equals("ODBC")) {
            dataSql = "SELECT TOP " + limit + " * FROM (" + sql + ") lol";
        } else if (type.trim().equals("ODBC-DBF")) {
            dataSql = sql;
        } else {
            throw new ErpException("数据库类型有误，请选择正确的数据类型");
        }
        log.debug("执行sql：" + dataSql);
        return dataSql;
    }

    public static String getFlowSearchSql(String sql, String type, String methodNo, Integer flowDateCount, Date date) throws ErpException {
        if (StringUtils.isBlank(sql) || StringUtils.isBlank(type) || flowDateCount==0) {
            throw new ErpException("数据库配置有误，请填写正确数据库配置");
        }

        // 流向数据默认查询范围15天
        Map<String,String> map = new HashMap<>();
        int basicDateOffset = 1;
        int dateOffset = 0;
        String startDate = "";
        String endDate = "";
        String timeField = "";
        if(flowDateCount!=0){
            dateOffset = basicDateOffset - flowDateCount;
        }

        endDate = DateUtil.format(date, Constants.FORMATE_DAY) + " 23:59:59";
        Date startDateTemp = DateUtil.offset(date, DateField.DAY_OF_MONTH, dateOffset);
        startDate = DateUtil.format(startDateTemp, Constants.FORMATE_DAY) + " 00:00:00";
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        if(ObjectUtil.equal(ErpTopicName.ErpPurchaseFlow.getMethod(), methodNo)){
            timeField = "po_time";
        }
        if(ObjectUtil.equal(ErpTopicName.ErpSaleFlow.getMethod(), methodNo) || ObjectUtil.equal(ErpTopicName.ErpShopSaleFlow.getMethod(), methodNo)){
            timeField = "so_time";
        }

        String dataSql = "";
        String whereCondition = " where lol." + timeField + " >= '" + startDate + "' and lol."+ timeField + "<= '"+ endDate + "'";
        if (type.trim().equals("Mysql")) {
            dataSql = "SELECT * FROM (" + sql + ") as lol" + whereCondition;
        } else if (type.trim().equals("SQL Server") || type.trim().equals("SQL Server2000")) {
            dataSql = "SELECT * FROM (" + sql + ") lol" + whereCondition;
        } else if (type.trim().equals("Oracle")) {
            whereCondition = " where " + timeField + " >= TO_DATE('" + startDate + "','yyyy-mm-dd hh24:mi:ss') and "+ timeField + "<= TO_DATE('" + endDate + "','yyyy-mm-dd hh24:mi:ss')";
            dataSql = "SELECT * FROM(" + sql + ") " + whereCondition;
        } else if (type.trim().equals("ODBC")) {
            dataSql = "SELECT * FROM (" + sql + ") lol" + whereCondition;
        } else if (type.trim().equals("ODBC-DBF")) {
            dataSql = sql;
        } else {
            throw new ErpException("数据库类型有误，请选择正确的数据类型");
        }
        log.debug("执行sql：" + dataSql);
        return dataSql;
    }

//    public static void main(String[] args) {
//        String flowDateCount = "2";
//        int basicDateOffset = 1;
//        int dateOffset = 0;
//        String startDate = "";
//        String endDate = "";
//        dateOffset = basicDateOffset - Integer.parseInt(flowDateCount);
//        Date date = new Date();
//        endDate = DateUtil.format(date, ErpConstants.FORMATE_DAY) + " 23:59:59";
//        Date startDateTemp = DateUtil.offset(date, DateField.DAY_OF_MONTH, dateOffset);
//        startDate = DateUtil.format(startDateTemp, ErpConstants.FORMATE_DAY) + " 00:00:00";
//
//        System.out.println(">>>>> flowDateCount:" + flowDateCount);
//        System.out.println(">>>>> startDate:" + startDate);
//        System.out.println(">>>>> endDate:" + endDate);
//    }

    public static String getSearchSql(String sql, String type, int start, int end, int pageSize, String sortFieldAlias) throws ErpException {
        String dataSql = "";
        if (StringUtil.isNotEmpty(sortFieldAlias)) {
            sql = addSortToSql(sql, sortFieldAlias, type);
        }
        if ((type == null) || (type.trim().equals(""))) {
            throw new ErpException("数据库配置有误，请填写正确数据库配置");
        }
        if (type.trim().equals("Mysql")) {
            dataSql = "SELECT * FROM (" + sql + ") as lol limit " + start + ", " + pageSize;
        } else if (type.trim().equals("SQL Server")) {
            dataSql = "SELECT * FROM (" + sql + ") lol where lol.RC > " + start + " and lol.RC <= " + end;
        } else if (type.trim().equals("SQL Server2000")) {
            start++;
            dataSql = "SELECT TOP " + pageSize + " * FROM (" + sql + ")t WHERE (PK >= (SELECT MAX(PK) FROM (SELECT TOP " + start + " PK FROM (" + sql + ")t ORDER BY PK) AS T)) ORDER BY PK";
        } else if (type.trim().equals("Oracle")) {
            dataSql = "SELECT * FROM (SELECT lol.*, ROWNUM RN FROM (" + sql + ") lol WHERE ROWNUM <= " + end + ") WHERE RN > " + start;
        } else if (type.trim().equals("DB2")) {
            dataSql = "SELECT * FROM (SELECT BOB.*, ROWNUMBER() OVER() AS RN FROM (" + sql + ") AS BOB) AS lol WHERE lol.RN > " + start + " AND lol.RN <= " + end;
        } else if (type.trim().equals("ODBC")) {
            dataSql = "SELECT * FROM (" + sql + ") lol where lol.RC > " + start + " and lol.RC <= " + end;
        } else if (type.trim().equals("ODBC-DBF")) {
            dataSql = sql;
        } else {
            throw new ErpException("数据库类型有误，请选择正确的数据类型");
        }
        log.debug("执行sql：" + dataSql);
        return dataSql;
    }

    public static String addSortToSql(String sql, String sortFieldAlias, String sqlType) {
        String sortField = findSortField(sql, sortFieldAlias, sqlType);
        if (sortField == null) {
            return sql;
        }
        if (("Mysql".equals(sqlType)) || ("Oracle".equals(sqlType)) || ("DB2".equals(sqlType))) {
            sql = sql + " order by " + sortField;
        }
        if ("SQL Server".equals(sqlType) || ("ODBC".equals(sqlType))) {
            int index = sql.toLowerCase().indexOf("select");
            sql = "select row_number()over(order by " + sortField + ") RC," + sql.substring(index + 6);
        }
        if ("SQL Server2000".equals(sqlType)) {
            int index = sql.toLowerCase().indexOf("select");
            sql = "SELECT TOP 100 PERCENT " + sortField + " AS PK," + sql.substring(index + 6);
        }
        if ("ODBC-DBF".equals(sqlType)) {
            sql = sql;
        }
        return sql;
    }

    private static String findSortField(String sql, String sortFieldAlias, String sqlType) {
        sql = sql.trim().toLowerCase().replaceAll("\\s{1,}", " ");
        Map sqlMainBody = parseQuerySqlMainBody(sql);
        if ((sqlMainBody == null) || (sqlMainBody.size() == 0)) {
            return null;
        }
        boolean isSort = checkSort(sqlMainBody, sqlType);
        if (isSort) {
            return null;
        }
        List<String> queryFields = parseSqlField((String) sqlMainBody.get("queryFields"), ',');
        String field = null;
        for (String queryField : queryFields) {
            field = findFieldByAlias(queryField, sortFieldAlias);
            if (field != null) {
                break;
            }
        }
        return field;
    }

    private static String findFieldByAlias(String fieldAndAlias, String sortFieldAlias) {
        List fields = parseSqlField(fieldAndAlias, ' ');
        int index = ((String) fields.get(fields.size() - 1)).trim().indexOf(sortFieldAlias);
        int length = ((String) fields.get(fields.size() - 1)).trim().length();
        if ((index == -1) || ((index != length - sortFieldAlias.length() - 1) && (index != length - sortFieldAlias.length()))) {
            return null;
        }
        String sortField = null;
        for (int i = 0; i < fields.size(); i++) {
            String field = ((String) fields.get(i)).trim();
            if ("as".equals(field)) {
                sortField = (String) fields.get(i - 1);
            } else if (field.endsWith(")as")) {
                sortField = field.substring(0, field.lastIndexOf("as"));
            }
        }
        if (sortField == null) {
            index = ((String) fields.get(fields.size() - 1)).indexOf(")");
            if (fields.size() > 1) {
                sortField = "distinct".equals(fields.get(fields.size() - 2)) ? (String) fields.get(fields.size() - 1) : (String) fields.get(fields.size() - 2);
            } else {
                sortField = (String) fields.get(fields.size() - 1);
            }
            if (index != -1) {
                sortField = sortField.substring(0, index + 1);
            }
        }
        return sortField.trim();
    }

    private static Map<String, String> parseQuerySqlMainBody(String sql) {
        if (!sql.startsWith("select")) {
            return null;
        }
        int bracesNumber = 0;
        int endIndex = sql.indexOf("from");
        Map sqlMainBody = new HashMap();
        for (int i = 6; i < sql.length(); i++) {
            char symbol = sql.charAt(i);
            if (symbol == '(') {
                bracesNumber++;
            }
            if (symbol == ')') {
                bracesNumber--;
            }
            if ((bracesNumber == 0) && (endIndex == i)) {
                String queryFields = sql.substring(6, i).trim();
                String afterFrom = sql.substring(i + 4).trim();
                sqlMainBody.put("queryFields", queryFields);
                sqlMainBody.put("afterFrom", afterFrom);
                break;
            }
            endIndex = sql.indexOf("from", i + 1);
        }

        return sqlMainBody;
    }

    private static boolean checkSort(Map<String, String> sqlMainBody, String sqlType) {
        boolean isSort = false;

        if (("Mysql".equals(sqlType)) || ("Oracle".equals(sqlType)) || ("DB2".equals(sqlType))  || ("ODBC-DBF".equals(sqlType))) {
            List fields = parseSqlField((String) sqlMainBody.get("afterFrom"), ' ');
            for (int i = 0; i < fields.size() - 1; i++) {
                if (("order".equals(((String) fields.get(i)).trim())) && ("by".equals(((String) fields.get(i + 1)).trim()))) {
                    isSort = true;
                    break;
                }
            }
        }
        if ("SQL Server".equals(sqlType) || ("ODBC".equals(sqlType))) {
            List<String> separatedFields = parseSqlField((String) sqlMainBody.get("queryFields"), ',');
            for (String separatedField : separatedFields) {
                if (separatedField.endsWith("rc")) {
                    List fields = parseSqlField(separatedField.trim(), ' ');
                    String field = ((String) fields.get(fields.size() - 1)).trim();
                    if (("rc".equals(field)) || (field.endsWith(")rc")) || (field.endsWith(".rc"))) {
                        isSort = true;
                        break;
                    }
                }
            }
        }
        if ("SQL Server2000".equals(sqlType)) {
            List<String> separatedFields = parseSqlField((String) sqlMainBody.get("queryFields"), ',');
            for (String separatedField : separatedFields) {
                if (separatedField.endsWith("pk")) {
                    List fields = parseSqlField(separatedField.trim(), ' ');
                    String field = ((String) fields.get(fields.size() - 1)).trim();
                    if (("pk".equals(field)) || (field.endsWith(")pk")) || (field.endsWith(".pk"))) {
                        isSort = true;
                        break;
                    }
                }
            }
        }
        return isSort;
    }

    private static List<String> parseSqlField(String sql, char separator) {
        int index = 0;
        List sqlField = new ArrayList();
        int associateQueryCount = 0;
        for (int i = 0; i < sql.length(); i++) {
            char symbol = sql.charAt(i);
            if (symbol == '(') {
                associateQueryCount++;
            }
            if (symbol == ')') {
                associateQueryCount--;
            }
            if ((associateQueryCount == 0) && (symbol == separator)) {
                String field = sql.substring(index, i);
                sqlField.add(field.trim());
                index = i + 1;
            }
        }

        sqlField.add(sql.substring(index).trim());
        return sqlField;
    }
}
