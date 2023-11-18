package com.yiling.hmc.insurance.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.insurance.dto.InsuranceGoodsDTO;
import com.yiling.hmc.insurance.dto.InsurancePageDTO;
import com.yiling.hmc.insurance.dto.request.InsuranceListRequest;
import com.yiling.hmc.insurance.dto.request.InsurancePageRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceSaveRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceStatusRequest;
import com.yiling.hmc.insurance.entity.InsuranceDO;
import com.yiling.hmc.insurance.enums.InsuranceStatusEnum;

/**
 * <p>
 * C端保险保险表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-23
 */
public interface InsuranceService extends BaseService<InsuranceDO> {

    /**
     * 保险和明细新增和修改
     *
     * @param request 新增和修改请求参数
     * @return 成功/失败
     */
    boolean saveInsuranceAndDetail(InsuranceSaveRequest request);

    /**
     * 保险新增和修改
     *
     * @param request 新增和修改请求参数
     * @return 保险信息
     */
    InsuranceDO saveInsurance(InsuranceSaveRequest request);

    /**
     * 保险信息分页查询
     *
     * @param request 查询条件
     * @return 保险信息
     */
    Page<InsurancePageDTO> pageList(InsurancePageRequest request);

    /**
     * 保险信息查询
     *
     * @param request 查询条件
     * @return 保险信息
     */
    List<InsuranceDO> listByCondition(InsuranceListRequest request);

    /**
     * 根据保险公司和启用状态查询保险公司下的保险
     *
     * @param insuranceCompanyId 保险公司id
     * @param insuranceStatusEnum 启用/停用
     * @return 保险公司信息
     */
    List<InsuranceDO> listByCompanyIdAndStatus(Long insuranceCompanyId, InsuranceStatusEnum insuranceStatusEnum);

    /**
     * 修改保险状态
     *
     * @param request 保险状态修改请求参数
     * @return 成功/失败
     */
    boolean modifyStatus(InsuranceStatusRequest request);

    /**
     * 查询所有保险
     *
     * @return 保险信息
     */
    List<InsurancePageDTO> getAll();

    /**
     * 根据保险id集合查询保险信息
     *
     * @param insuranceIdList 保险id集合
     * @param insuranceCompanyId 保险公司id
     * @param insuranceStatusEnum 保险状态 1-启用 2-停用
     * @return 保险信息
     */
    List<InsuranceDO> listByIdListAndCompanyAndStatus(List<Long> insuranceIdList, Long insuranceCompanyId, InsuranceStatusEnum insuranceStatusEnum);

    /**
     * 查询保险关联商品
     *
     * @return
     */
    List<InsuranceGoodsDTO> queryGoods();

}
