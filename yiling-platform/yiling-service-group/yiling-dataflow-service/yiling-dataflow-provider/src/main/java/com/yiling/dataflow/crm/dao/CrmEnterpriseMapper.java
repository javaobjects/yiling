package com.yiling.dataflow.crm.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterprisePageRequest;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yiling.dataflow.crm.dto.request.UpdateAgencyEnterpriseRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-06-21
 */
@Repository
public interface CrmEnterpriseMapper extends BaseMapper<CrmEnterpriseDO> {

    /**
     * 机构信息修改
     *
     * @param request 修改条件
     * @return 条数
     */
    int updateCrmEnterpriseSimple(@Param("request") UpdateAgencyEnterpriseRequest request);
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
    Page<CrmEnterpriseDO> page(Page<CrmEnterpriseDO> page, @Param("request") QueryCrmEnterprisePageRequest request);

    List<CrmEnterpriseDO> listByIdsAndName(@Param("ids") List<Long> ids, @Param("name") String name);

    Long getBackupTableCount(@Param("backupTableName") String backupTableName);

    Boolean insertBackupTableData(@Param("tableName") String tableName, @Param("backupTableName") String backupTableName);

}
