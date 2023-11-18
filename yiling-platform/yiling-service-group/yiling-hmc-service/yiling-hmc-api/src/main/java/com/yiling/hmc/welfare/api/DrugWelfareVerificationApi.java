package com.yiling.hmc.welfare.api;

import java.util.List;

import com.yiling.hmc.welfare.dto.DrugWelfareVerificationDTO;
import com.yiling.hmc.welfare.dto.request.DrugWelfareGroupCouponSaveRequest;

/**
 * 药品福利核销
 *
 * @author: hongyang.zhang
 * @data: 2022/10/08
 */
public interface DrugWelfareVerificationApi {

    /**
     * 根据 入组福利券id集合查询核销记录
     *
     * @param groupCouponIds
     * @return
     */
    List<DrugWelfareVerificationDTO> getDrugWelfareVerificationByGroupCouponIds(List<Long> groupCouponIds);

    /**
     * 保存核销记录
     * @param request
     */
    void saveVerification(DrugWelfareGroupCouponSaveRequest request);

}
