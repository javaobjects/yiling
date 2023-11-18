package com.yiling.hmc.welfare.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.welfare.dto.request.DrugWelfareGroupCouponSaveRequest;
import com.yiling.hmc.welfare.entity.DrugWelfareVerificationDO;

/**
 * <p>
 * 药品福利核销表 服务类
 * </p>
 *
 * @author hongyang.zhang
 * @date 2022-09-26
 */
public interface DrugWelfareVerificationService extends BaseService<DrugWelfareVerificationDO> {

    /**
     * 根据 入组福利券id集合查询核销记录
     *
     * @param groupCouponIds
     * @return
     */
    List<DrugWelfareVerificationDO> getDrugWelfareVerificationByGroupCouponIds(List<Long> groupCouponIds);

    /**
     * 保存核销记录
     * @param request
     */
    void saveVerification(DrugWelfareGroupCouponSaveRequest request);
}
