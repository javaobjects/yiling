package com.yiling.user.esb.service;

import java.util.List;

public interface EsbBackUpService {
    Boolean createBackupTable(String tableName, String backupTableName);

    /**
     * 添加字段
     *
     * @param backupTableName
     * @return
     */
    Boolean addColumn(String backupTableName);

    Boolean renameBackupTable(String tableName, String backupTableName);


    Long getBackupTableCount(String backupTableName);

    Boolean insertBackupTableData(String tableName, String backupTableName);

    List<String> getTableNames(String esb);
}
