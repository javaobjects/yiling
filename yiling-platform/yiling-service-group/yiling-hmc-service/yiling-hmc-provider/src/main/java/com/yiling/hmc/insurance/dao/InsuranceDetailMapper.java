package com.yiling.hmc.insurance.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.hmc.insurance.dto.request.InsuranceDetailListRequest;
import com.yiling.hmc.insurance.entity.InsuranceDetailDO;

/**
 * <p>
 * 保险商品明细表 Dao 接口
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-23
 */
@Repository
public interface InsuranceDetailMapper extends BaseMapper<InsuranceDetailDO> {

    /**
     * 查询所有启用的保险的保险明细
     *
     * @param page 分页查询
     * @param request 查询条件
     * @return 保险明细
     */
    Page<InsuranceDetailDO> pageByInsuranceNameAndStatus(Page<InsuranceDetailDO> page, @Param("request") InsuranceDetailListRequest request);

    /**
     * 查询所有符合管控商品，保险公司，启用状态的保险明细信息
     *
     * @param controlIdList 保险管控商品id
     * @param insuranceCompanyId 保险公司id
     * @param insuranceStatus 保险启用状态
     * @return 保险明细
     */
    List<InsuranceDetailDO> listByControlIdAndCompanyAndInsuranceStatus(@Param("controlIdList") List<Long> controlIdList, @Param("insuranceCompanyId") Long insuranceCompanyId, @Param("insuranceStatus") Integer insuranceStatus);
}
