package com.yiling.hmc.insurance.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.insurance.bo.InsuranceRecordBO;
import com.yiling.hmc.insurance.dto.request.QueryBackInsuranceRecordPageRequest;

/**
 * @author: gxl
 * @date: 2022/4/11
 */
public interface BackInsuranceRecordApi {
    /**
     * 后台-查询保险记录
     * @param recordPageRequest
     * @return
     */
    Page<InsuranceRecordBO> queryPage(QueryBackInsuranceRecordPageRequest recordPageRequest);
}
