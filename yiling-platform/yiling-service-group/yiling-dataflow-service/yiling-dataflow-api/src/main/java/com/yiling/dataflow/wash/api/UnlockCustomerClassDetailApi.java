package com.yiling.dataflow.wash.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.UnlockCustomerClassDetailDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerClassDetailCountRequest;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerClassDetailPageRequest;
import com.yiling.dataflow.wash.dto.request.UpdateCustomerClassificationRequest;

/**
 * @author fucheng.bai
 * @date 2023/5/5
 */
public interface UnlockCustomerClassDetailApi {

    Page<UnlockCustomerClassDetailDTO> listPage(QueryUnlockCustomerClassDetailPageRequest request);

    void resetCustomerClassification(UpdateCustomerClassificationRequest request);

    Integer count(QueryUnlockCustomerClassDetailCountRequest request);
}
