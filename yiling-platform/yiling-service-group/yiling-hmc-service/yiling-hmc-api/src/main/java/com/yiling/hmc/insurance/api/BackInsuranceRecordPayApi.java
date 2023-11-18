package com.yiling.hmc.insurance.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.insurance.bo.InsuranceRecordPayBO;
import com.yiling.hmc.insurance.dto.request.QueryInsuranceRecordPayPageRequest;

/**
 * @author: gxl
 * @date: 2022/4/13
 */
public interface BackInsuranceRecordPayApi {

    /**
     * 保险缴费记录
     * @param request
     * @return
     */
     Page<InsuranceRecordPayBO> queryPage(QueryInsuranceRecordPayPageRequest request);
}