package com.yiling.hmc.insurance.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.insurance.dto.request.InsuranceDetailListRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceSaveRequest;
import com.yiling.hmc.insurance.entity.InsuranceDetailDO;

/**
 * <p>
 * 保险商品明细表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-23
 */
public interface InsuranceDetailService extends BaseService<InsuranceDetailDO> {

    /**
     * 保险明细新增时的保存
     *
     * @param request 请求参数
     * @return 成功/失败
     */
    boolean saveInsuranceDetail(InsuranceSaveRequest request);

    /**
     * 保险明细修改时的保存
     *
     * @param request 请求参数
     * @return 成功/失败
     */
    boolean saveEditInsuranceDetail(InsuranceSaveRequest request);

    /**
     * 根据保险id查询明细信息
     *
     * @param insuranceId 保险id
     * @return 明细信息
     */
    List<InsuranceDetailDO> listByInsuranceId(Long insuranceId);

    /**
     * 根据管控id查询保险明细信息
     *
     * @param controlIdList 管控id集合
     * @return 明细信息
     */
    List<InsuranceDetailDO> listByControlId(List<Long> controlIdList);

    /**
     * 查询所有启用的保险的保险明细
     *
     * @param request 查询条件
     * @return 保险明细
     */
    Page<InsuranceDetailDO> pageByInsuranceNameAndStatus(InsuranceDetailListRequest request);

    /**
     * 查询所有符合管控商品，保险公司，启用状态的保险明细信息
     *
     * @param controlIdList 保险管控商品id
     * @param insuranceCompanyId 保险公司id
     * @param insuranceStatus 保险启用状态
     * @return 保险明细
     */
    List<InsuranceDetailDO> listByControlIdAndCompanyAndInsuranceStatus(List<Long> controlIdList, Long insuranceCompanyId, Integer insuranceStatus);
}
