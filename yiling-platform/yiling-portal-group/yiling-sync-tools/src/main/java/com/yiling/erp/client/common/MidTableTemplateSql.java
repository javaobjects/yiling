package com.yiling.erp.client.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="MidTableTemplateSql")
public class MidTableTemplateSql
        implements Serializable
{
    private static final long serialVersionUID = -9032865682284302545L;
    private String oracleOrder;
    private String oracleOrderDetail;
    private String mysqlOrder;
    private String mysqlOrderDetail;
    private String sqlserverOrder;
    private String sqlserverOrderDetail;

    public Map<String, String> getByType(String type)
    {
        type = type.replaceAll(" ", "");
        Map sql = new HashMap();
        if (type.equalsIgnoreCase("oracle")) {
            sql.put("orderSql", this.oracleOrder);
            sql.put("orderDetailSql", this.oracleOrderDetail);
            sql.put("createResult", "false");
        }

        if ((type.equalsIgnoreCase("sqlserver")) || (type.equalsIgnoreCase("sqlserver2000"))) {
            sql.put("orderSql", this.sqlserverOrder);
            sql.put("orderDetailSql", this.sqlserverOrderDetail);
            sql.put("createResult", "false");
        }
        if (type.equalsIgnoreCase("mysql")) {
            sql.put("orderSql", this.mysqlOrder);
            sql.put("orderDetailSql", this.mysqlOrderDetail);
            sql.put("createResult", "false");
        }
        sql.put("sqlType", type);
        return sql;
    }

    public String getOracleOrder()
    {
        return this.oracleOrder;
    }

    public void setOracleOrder(String oracleOrder)
    {
        this.oracleOrder = oracleOrder;
    }

    public String getOracleOrderDetail()
    {
        return this.oracleOrderDetail;
    }

    public void setOracleOrderDetail(String oracleOrderDetail)
    {
        this.oracleOrderDetail = oracleOrderDetail;
    }

    public String getMysqlOrder()
    {
        return this.mysqlOrder;
    }

    public void setMysqlOrder(String mysqlOrder)
    {
        this.mysqlOrder = mysqlOrder;
    }

    public String getMysqlOrderDetail()
    {
        return this.mysqlOrderDetail;
    }

    public void setMysqlOrderDetail(String mysqlOrderDetail)
    {
        this.mysqlOrderDetail = mysqlOrderDetail;
    }

    public String getSqlserverOrder()
    {
        return this.sqlserverOrder;
    }

    public void setSqlserverOrder(String sqlserverOrder)
    {
        this.sqlserverOrder = sqlserverOrder;
    }

    public String getSqlserverOrderDetail()
    {
        return this.sqlserverOrderDetail;
    }

    public void setSqlserverOrderDetail(String sqlserverOrderDetail)
    {
        this.sqlserverOrderDetail = sqlserverOrderDetail;
    }
}