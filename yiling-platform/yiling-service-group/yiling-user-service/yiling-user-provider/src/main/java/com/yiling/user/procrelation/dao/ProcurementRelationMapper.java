package com.yiling.user.procrelation.dao;

import com.yiling.user.procrelation.entity.ProcurementRelationDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * pop采购关系表 Dao 接口
 * </p>
 *
 * @author dexi.yao
 * @date 2023-05-19
 */
@Repository
public interface ProcurementRelationMapper extends BaseMapper<ProcurementRelationDO> {

    /**
     * 更新采购关系并升高版本号
     *
     * @param request
     * @return
     */
    int updateVersion(@Param("request") ProcurementRelationDO request);

    /**
     * 更新采购关系并升高版本号
     *
     * @param request
     * @return
     */
    int updateInfoByVersion(@Param("request") ProcurementRelationDO request);

    Long queryAutoIncrement(@Param("dbName") String dbName);
}
