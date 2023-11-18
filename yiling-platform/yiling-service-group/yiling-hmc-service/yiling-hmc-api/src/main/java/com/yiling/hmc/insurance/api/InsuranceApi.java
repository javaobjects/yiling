package com.yiling.hmc.insurance.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.insurance.dto.InsuranceDTO;
import com.yiling.hmc.insurance.dto.InsuranceGoodsDTO;
import com.yiling.hmc.insurance.dto.InsurancePageDTO;
import com.yiling.hmc.insurance.dto.request.InsurancePageRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceSaveRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceStatusRequest;
import com.yiling.hmc.insurance.enums.InsuranceStatusEnum;

/**
 * 保险API
 *
 * @author: yong.zhang
 * @date: 2022/3/24
 */
public interface InsuranceApi {

    /**
     * 保险新增和修改
     *
     * @param request 新增和修改请求参数
     * @return 成功/失败
     */
    boolean saveInsurance(InsuranceSaveRequest request);

    /**
     * 保险信息分页查询
     *
     * @param request 查询条件
     * @return 保险信息
     */
    Page<InsurancePageDTO> pageList(InsurancePageRequest request);

    /**
     * 根据id查询保险信息
     *
     * @param id id
     * @return 保险信息
     */
    InsuranceDTO queryById(Long id);

    /**
     * 根据保险公司和启用状态查询保险公司下的保险
     *
     * @param insuranceCompanyId 保险公司id
     * @param insuranceStatusEnum 启用/停用
     * @return 保险公司信息
     */
    List<InsuranceDTO> listByCompanyIdAndStatus(Long insuranceCompanyId, InsuranceStatusEnum insuranceStatusEnum);

    /**
     * 根据id查询保险信息
     *
     * @param idList id集合
     * @return 保险信息
     */
    List<InsuranceDTO> queryByIdList(List<Long> idList);

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
     * 查询所有保险关联商品
     */
    List<InsuranceGoodsDTO> queryGoods();
}
