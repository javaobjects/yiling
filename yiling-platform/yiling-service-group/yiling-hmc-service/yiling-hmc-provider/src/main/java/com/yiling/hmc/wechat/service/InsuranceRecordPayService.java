package com.yiling.hmc.wechat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.insurance.bo.InsuranceRecordPayBO;
import com.yiling.hmc.insurance.dto.request.QueryInsuranceRecordPayPageRequest;
import com.yiling.hmc.wechat.dto.InsuranceRecordPayDTO;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceRecordPayRequest;
import com.yiling.hmc.wechat.entity.InsuranceRecordPayDO;

import java.util.List;

/**
 * <p>
 * C端参保记录支付流水表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-28
 */
public interface InsuranceRecordPayService extends BaseService<InsuranceRecordPayDO> {

    /**
     * 保存支付记录
     * @param request
     * @return
     */
    Long saveInsuranceRecordPay(SaveInsuranceRecordPayRequest request);

    /**
     * 后台分页查询记录
     * @param request
     * @return
     */
    Page<InsuranceRecordPayBO> queryPage(QueryInsuranceRecordPayPageRequest request);

    /**
     * 查询保单交费记录
     * @param insuranceRecordId
     * @return
     */
    List<InsuranceRecordPayDTO> queryByInsuranceRecordId(Long insuranceRecordId);

    /**
     * 查询交费记录
     * @param recordPayId
     * @return
     */
    InsuranceRecordPayDTO queryById(Long recordPayId);
}
