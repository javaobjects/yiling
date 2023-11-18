package com.yiling.dataflow.agency.dao;

import java.util.List;

import com.yiling.dataflow.agency.entity.CrmDepartmentAreaRelationDO;
import com.yiling.dataflow.agency.entity.CrmDepartmentProductRelationDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 省区与业务省区对应关系表 Dao 接口
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-16
 */
@Repository
public interface CrmDepartmentAreaRelationMapper extends BaseMapper<CrmDepartmentAreaRelationDO> {

    List<Long> getAvailableProductGroupIds(@Param("ids") List<Long> productGroupIds);
}
