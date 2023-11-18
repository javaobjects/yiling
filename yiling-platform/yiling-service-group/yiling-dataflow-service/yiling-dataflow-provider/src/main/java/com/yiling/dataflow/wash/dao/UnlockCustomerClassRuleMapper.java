package com.yiling.dataflow.wash.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.dataflow.wash.entity.UnlockCustomerClassRuleDO;

/**
 * <p>
 * 非锁客户分类规则表 Mapper 接口
 * </p>
 *
 * @author baifc
 * @since 2023-04-26
 */
@Repository
public interface UnlockCustomerClassRuleMapper extends BaseMapper<UnlockCustomerClassRuleDO> {

    List<Long> getLastRuleId(@Param("todayStr") String todayStr);

}
