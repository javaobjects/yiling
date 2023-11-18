package com.yiling.dataflow.wash.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.UnlockCustomerMatchingInfoDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerMatchingInfoPageRequest;

/**
 * @author fucheng.bai
 * @date 2023/5/29
 */
public interface UnlockCustomerMatchingInfoApi {

    List<UnlockCustomerMatchingInfoDTO> getListByCustomerName(String customerName);

    void matchingRateExecute(String name);


    void matchingRateBatchJobStart();
    Page<UnlockCustomerMatchingInfoDTO> getPageList(QueryUnlockCustomerMatchingInfoPageRequest request);


}
