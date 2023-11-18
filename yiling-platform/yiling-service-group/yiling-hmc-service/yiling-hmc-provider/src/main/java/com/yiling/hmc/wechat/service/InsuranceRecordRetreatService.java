package com.yiling.hmc.wechat.service;

import com.yiling.hmc.insurance.bo.InsuranceRecordRetreatBO;
import com.yiling.hmc.wechat.dto.InsuranceRecordRetreatDTO;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceRetreatRequest;
import com.yiling.hmc.wechat.entity.InsuranceRecordRetreatDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 退保记录表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-04-06
 */
public interface InsuranceRecordRetreatService extends BaseService<InsuranceRecordRetreatDO> {

    /**
     * 保存退保记录
     * @param request
     * @return
     */
    Long saveInsuranceRecordRetreat(SaveInsuranceRetreatRequest request);

    /**
     * 根据参保记录查询退保记录
     * @param insuranceRecordId
     * @return
     */
    InsuranceRecordRetreatDTO getByInsuranceRecordId(Long insuranceRecordId);

    /**
     * 后台查询退保详情
     * @param insuranceRecordId
     * @return
     */
    InsuranceRecordRetreatBO getRetreatDetail(Long insuranceRecordId);
}
