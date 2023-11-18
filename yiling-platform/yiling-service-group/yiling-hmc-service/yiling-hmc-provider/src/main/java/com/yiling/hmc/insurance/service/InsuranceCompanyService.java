package com.yiling.hmc.insurance.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.insurance.dto.InsuranceCompanyDTO;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanyDeleteRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanyListRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanyPageRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanySaveRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanyStatusRequest;
import com.yiling.hmc.insurance.entity.InsuranceCompanyDO;

/**
 * <p>
 * 保险公司表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-23
 */
public interface InsuranceCompanyService extends BaseService<InsuranceCompanyDO> {

    /**
     * 保险公司信息查询
     *
     * @param request 查询条件
     * @return 保险公司信息
     */
    List<InsuranceCompanyDO> listByCondition(InsuranceCompanyListRequest request);

    /**
     * 保险服务商新增
     *
     * @param request 新增请求参数
     * @return 成功/失败
     */
    boolean saveInsuranceCompany(InsuranceCompanySaveRequest request);

    /**
     * 保险服务商列表查询
     *
     * @param request 查询条件请求参数
     * @return 保险服务商信息
     */
    Page<InsuranceCompanyDO> pageList(InsuranceCompanyPageRequest request);

    /**
     * 保险服务商删除
     *
     * @param request 删除条件
     * @return 成功/失败
     */
    boolean deleteInsuranceCompany(InsuranceCompanyDeleteRequest request);

    /**
     * 保险服务商启用和停用
     *
     * @param request 请求参数
     * @return 成功/失败
     */
    boolean modifyStatus(InsuranceCompanyStatusRequest request);

    /**
     * 批量获取
     * @param insuranceCompanyIdList
     * @return
     */
    List<InsuranceCompanyDTO> listByIdList(List<Long> insuranceCompanyIdList);
}
