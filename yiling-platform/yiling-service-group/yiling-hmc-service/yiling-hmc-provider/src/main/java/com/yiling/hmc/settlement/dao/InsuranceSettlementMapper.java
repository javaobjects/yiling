package com.yiling.hmc.settlement.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.hmc.settlement.bo.InsuranceSettlementPageBO;
import com.yiling.hmc.settlement.bo.InsuranceSettlementPageResultBO;
import com.yiling.hmc.settlement.dto.request.InsuranceSettlementPageRequest;
import com.yiling.hmc.settlement.entity.InsuranceSettlementDO;

/**
 * <p>
 * 保司结账表 Dao 接口
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-31
 */
@Repository
public interface InsuranceSettlementMapper extends BaseMapper<InsuranceSettlementDO> {

    /**
     * 保司结账明细列表
     *
     * @param page 分页信息
     * @param request 查询条件
     * @return 结算明细
     */
    Page<InsuranceSettlementPageBO> pageList(Page<InsuranceSettlementDO> page, @Param("request") InsuranceSettlementPageRequest request);

    /**
     * 保司结账明细列表，总数量和金额查询
     *
     * @param request 查询条件
     * @return 结算明细
     */
    InsuranceSettlementPageResultBO pageResult(@Param("request") InsuranceSettlementPageRequest request);
}
