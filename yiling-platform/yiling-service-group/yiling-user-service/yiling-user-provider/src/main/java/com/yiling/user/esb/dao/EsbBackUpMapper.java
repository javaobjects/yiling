package com.yiling.user.esb.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EsbBackUpMapper {
    @InterceptorIgnore(tenantLine = "true")
    Boolean createBackupTable(@Param("tableName") String tableName, @Param("backupTableName") String backupTableName);

    /**
     * 添加字段
     *
     * @param backupTableName
     * @return
     */
    Boolean addColumn(@Param("backupTableName") String backupTableName);

    Boolean renameBackupTable(@Param("newTableName") String tableName, @Param("backupTableName") String backupTableName);


    Long getBackupTableCount(@Param("backupTableName") String backupTableName);

    Boolean insertBackupTableData(@Param("tableName") String tableName, @Param("backupTableName") String backupTableName);

    List<String> getTableNames(@Param("esb") String esb);
}
