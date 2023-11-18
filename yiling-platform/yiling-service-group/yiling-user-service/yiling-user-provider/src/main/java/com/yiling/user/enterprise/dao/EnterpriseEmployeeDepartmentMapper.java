package com.yiling.user.enterprise.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.enterprise.entity.EnterpriseEmployeeDepartmentDO;

/**
 * <p>
 * 企业员工所属部门信息 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-11-05
 */
@Repository
public interface EnterpriseEmployeeDepartmentMapper extends BaseMapper<EnterpriseEmployeeDepartmentDO> {

    int bindEmployeeDepartments(@Param("list") List<EnterpriseEmployeeDepartmentDO> list);
}
