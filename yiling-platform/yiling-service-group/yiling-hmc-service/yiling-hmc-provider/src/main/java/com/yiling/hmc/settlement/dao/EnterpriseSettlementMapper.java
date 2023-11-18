package com.yiling.hmc.settlement.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.hmc.settlement.bo.EnterpriseSettlementPageBO;
import com.yiling.hmc.settlement.bo.EnterpriseSettlementPageResultBO;
import com.yiling.hmc.settlement.bo.SettlementEnterprisePageBO;
import com.yiling.hmc.settlement.dto.request.EnterpriseSettlementPageRequest;
import com.yiling.hmc.settlement.dto.request.SettlementEnterprisePageRequest;
import com.yiling.hmc.settlement.entity.EnterpriseSettlementDO;

/**
 * <p>
 * 商家结账表 Dao 接口
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-31
 */
@Repository
public interface EnterpriseSettlementMapper extends BaseMapper<EnterpriseSettlementDO> {

    /**
     * 以岭给商家药品对账
     *
     * @param request 查询条件
     * @return 对账信息
     */
    List<SettlementEnterprisePageBO> pageEnSettlement(@Param("request") SettlementEnterprisePageRequest request);

    /**
     * 以岭给商家药品对账
     *
     * @param request 查询条件
     * @return 对账信息
     */
    List<SettlementEnterprisePageBO> pageUnSettlement(@Param("request") SettlementEnterprisePageRequest request);

    /**
     * 统计未结算完成的订单数量
     *
     * @param request 查询条件
     * @return 未结算完成的订单数量
     */
    List<SettlementEnterprisePageBO> countUnSettlementOrder(@Param("request") SettlementEnterprisePageRequest request);

    /**
     * 统计已结算完成的订单数量
     *
     * @param request 查询条件
     * @return 已结算完成的订单数量
     */
    List<SettlementEnterprisePageBO> countEnSettlementOrder(@Param("request") SettlementEnterprisePageRequest request);

    /**
     * 以岭与商家结算，明细表分页查询
     *
     * @param page 分页信息
     * @param request 查询条件
     * @return 结算明细
     */
    Page<EnterpriseSettlementPageBO> pageList(Page<EnterpriseSettlementDO> page, @Param("request") EnterpriseSettlementPageRequest request);

    /**
     * 以岭与商家结算明细表分页,总数量和金额查询
     *
     * @param request 查询条件
     * @return 结算明细
     */
    EnterpriseSettlementPageResultBO pageResult(@Param("request") EnterpriseSettlementPageRequest request);
}
