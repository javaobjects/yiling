package com.yiling.hmc.insurance.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.insurance.dto.InsuranceDetailDTO;
import com.yiling.hmc.insurance.dto.request.InsuranceDetailListRequest;

/**
 * 保险明细API
 *
 * @author: yong.zhang
 * @date: 2022/3/24
 */
public interface InsuranceDetailApi {

    /**
     * 根据保险id查询明细信息
     *
     * @param insuranceId 保险id
     * @return 明细信息
     */
    List<InsuranceDetailDTO> listByInsuranceId(Long insuranceId);

    /**
     * 根据管控id查询保险明细信息
     *
     * @param controlIdList 管控id集合
     * @return 明细信息
     */
    List<InsuranceDetailDTO> listByControlId(List<Long> controlIdList);

    /**
     * 查询所有启用的保险的保险明细
     *
     * @param request 查询条件
     * @return 保险明细
     */
    Page<InsuranceDetailDTO> pageByInsuranceNameAndStatus(InsuranceDetailListRequest request);

    /**
     * 查询所有符合管控商品，保险公司，启用状态的保险明细信息
     *
     * @param controlIdList 保险管控商品id
     * @param insuranceCompanyId 保险公司id
     * @param insuranceStatus 保险启用状态
     * @return 保险明细
     */
    List<InsuranceDetailDTO> listByControlIdAndCompanyAndInsuranceStatus(List<Long> controlIdList, Long insuranceCompanyId, Integer insuranceStatus);
}
