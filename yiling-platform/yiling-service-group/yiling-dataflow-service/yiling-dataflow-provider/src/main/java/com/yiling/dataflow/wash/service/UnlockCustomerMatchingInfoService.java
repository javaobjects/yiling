package com.yiling.dataflow.wash.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerMatchingInfoPageRequest;
import com.yiling.dataflow.wash.entity.UnlockCustomerMatchingInfoDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 非锁客户匹配度表 服务类
 * </p>
 *
 * @author baifc
 * @since 2023-04-26
 */
public interface UnlockCustomerMatchingInfoService extends BaseService<UnlockCustomerMatchingInfoDO> {

    void matchingRateExecute(String name);

    void matchingRateBatchExecute();

    List<UnlockCustomerMatchingInfoDO> getListByCustomerName(String customerName);

    Page<UnlockCustomerMatchingInfoDO> getPageList(QueryUnlockCustomerMatchingInfoPageRequest request);

}
