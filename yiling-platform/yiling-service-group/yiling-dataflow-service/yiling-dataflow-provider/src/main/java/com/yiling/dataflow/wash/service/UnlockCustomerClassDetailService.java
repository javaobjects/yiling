package com.yiling.dataflow.wash.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerClassDetailCountRequest;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerClassDetailPageRequest;
import com.yiling.dataflow.wash.entity.UnlockCustomerClassDetailDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 非锁客户分类明细表 服务类
 * </p>
 *
 * @author baifc
 * @since 2023-04-26
 */
public interface UnlockCustomerClassDetailService extends BaseService<UnlockCustomerClassDetailDO> {

    Page<UnlockCustomerClassDetailDO> listPage(QueryUnlockCustomerClassDetailPageRequest request);

    Integer countByRequest(QueryUnlockCustomerClassDetailCountRequest request);

    /**
     * 非锁流向客户分类处理
     * @param ufwtId 非锁流向任务id
     */
    void unlockCustomerClassMappingHandle(Long ufwtId);

    /**
     * 根据经销商编码以及客户名称查询明细
     * @param crmId
     * @param customerName
     */
    UnlockCustomerClassDetailDO findByCrmIdAndCustomerName(Long crmId, String customerName);

    /**
     * 根据经销商编码以及客户名称删除明细
     * @param crmId
     * @param customerName
     */
    void deleteByCrmIdAndCustomerName(Long crmId, String customerName);
}
