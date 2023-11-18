package com.yiling.hmc.welfare.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.welfare.dto.DrugWelfareGroupCouponStatisticsPageDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareGroupCouponVerificationDTO;
import com.yiling.hmc.welfare.dto.request.DrugWelfareGroupCouponListRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareGroupCouponVerificationRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareStatisticsPageRequest;
import com.yiling.hmc.welfare.entity.DrugWelfareGroupCouponDO;

/**
 * <p>
 * 入组福利券表 服务类
 * </p>
 *
 * @author hongyang.zhang
 * @date 2022-09-26
 */
public interface DrugWelfareGroupCouponService extends BaseService<DrugWelfareGroupCouponDO> {

    /**
     * 根据入组id查询券包
     * @param groupId
     * @return
     */
    List<DrugWelfareGroupCouponDO> getWelfareGroupCouponByGroupId(Long groupId);

    /**
     * 根据入组id查询券包
     * @param groupIdList
     * @return
     */
    List<DrugWelfareGroupCouponDO> getWelfareGroupCouponByGroupIdList(List<Long> groupIdList);

    /**
     * 根据request查询券包
     * @param request
     * @return
     */
    Page<DrugWelfareGroupCouponDO> listDrugWelfareGroupCoupon(DrugWelfareGroupCouponListRequest request);


    /**
     * 根据券码查询记录
     * @param code
     * @return
     */
    DrugWelfareGroupCouponDO getGroupCouponByCode(String code);


    /**
     * 福利券使用统计导出
     * @param request
     * @return
     */
    Page<DrugWelfareGroupCouponStatisticsPageDTO> exportStatistics(DrugWelfareStatisticsPageRequest request);

    /**
     * 确定核销
     * @param request
     * @return
     */
    DrugWelfareGroupCouponVerificationDTO verificationDrugWelfareGroupCoupon(DrugWelfareGroupCouponVerificationRequest request);

}
