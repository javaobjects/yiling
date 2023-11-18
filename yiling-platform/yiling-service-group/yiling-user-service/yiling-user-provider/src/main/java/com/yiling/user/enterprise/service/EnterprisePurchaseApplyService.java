package com.yiling.user.enterprise.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.bo.EnterprisePurchaseApplyBO;
import com.yiling.user.enterprise.dto.request.AddPurchaseApplyRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePurchaseApplyPageRequest;
import com.yiling.user.enterprise.dto.request.UpdatePurchaseApplyStatusRequest;
import com.yiling.user.enterprise.entity.EnterprisePurchaseApplyDO;

/**
 * <p>
 * 企业采购申请表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-01-17
 */
public interface EnterprisePurchaseApplyService extends BaseService<EnterprisePurchaseApplyDO> {

    /**
     * 获取采购商的 企业关系申请信息
     * @param customerEid
     * @param eid
     * @return
     */
    EnterprisePurchaseApplyBO getByCustomerEid(Long customerEid, Long eid);

    /**
     * 获取供应商的 企业关系申请信息
     * @param customerEid
     * @param eid
     * @return
     */
    EnterprisePurchaseApplyBO getByEid(Long customerEid, Long eid);

    /**
     * 查询企业采购关系分页列表
     * @param request
     * @return
     */
    Page<EnterprisePurchaseApplyBO> pageList(QueryEnterprisePurchaseApplyPageRequest request);

    /**
     * 添加采购关系申请
     * @param request
     * @return
     */
    boolean addPurchaseApply(AddPurchaseApplyRequest request);

    /**
     * 更新审核状态
     * @param request
     * @return
     */
    boolean updateAuthStatus(UpdatePurchaseApplyStatusRequest request);

    /**
     * 根据采购商和供应商获取采购状态
     * @param eidList 供应商企业ID集合
     * @param customerEid 采购商企业ID
     * @return 状态标识：1.去建采 2.审核中 3.已建采
     */
    Map<Long,Integer> getPurchaseApplyStatus(List<Long> eidList, Long customerEid);
}
