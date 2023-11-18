package com.yiling.basic.contract.dao;

import org.springframework.stereotype.Repository;

import com.yiling.basic.contract.entity.CovenantDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * @author fucheng.bai
 * @date 2022/11/11
 */
@Repository
public interface CovenantMapper extends BaseMapper<CovenantDO> {

    CovenantDO getById(Long id);
}
