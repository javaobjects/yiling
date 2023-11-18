package com.yiling.erp.client.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据源类型枚举类
 *
 * @author: houjie.sun
 * @date: 2022/4/2
 */
@Getter
@AllArgsConstructor
public enum DbTypeEnum {

    MYSQL("Mysql"),
    SQL_SERVER("SQL Server"),
    SQL_SERVER_2000("SQL Server2000"),
    ORACLE("Oracle"),
    DB2("DB2"),
    ODBC("ODBC"),
    ODBC_DBF("ODBC-DBF"),
    ;

    private String code;

    public static DbTypeEnum getByCode(Integer code) {
        for(DbTypeEnum e: DbTypeEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
