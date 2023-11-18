package com.yiling.hmc.welfare.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.welfare.dto.DrugWelfareGroupCouponDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareGroupCouponStatisticsPageDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareGroupCouponVerificationDTO;
import com.yiling.hmc.welfare.dto.request.DrugWelfareGroupCouponListRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareGroupCouponVerificationRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareStatisticsPageRequest;

/**
 * 入组福利券包API
 *
 * @author fan.shen
 * @date 2022-09-27
 */
public interface DrugWelfareGroupCouponApi {


    /**
     * 根据入组id查询券包
     *
     * @param groupId 入组id
     * @return 药品福利入组DTO
     */
    List<DrugWelfareGroupCouponDTO> getWelfareGroupCouponByGroupId(Long groupId);

    /**
     * 根据入组id查询券包
     *
     * @param groupIdList 入组id
     * @return 药品福利入组DTO
     */
    List<DrugWelfareGroupCouponDTO> getWelfareGroupCouponByGroupIdList(List<Long> groupIdList);

    /**
     * 福利券使用统计导出
     * @param request
     * @return
     */
    Page<DrugWelfareGroupCouponStatisticsPageDTO> exportStatistics(DrugWelfareStatisticsPageRequest request);


    /**
     * 根据request查询券包
     *
     * @param request
     * @return 药品福利入组DTO
     */
    Page<DrugWelfareGroupCouponDTO> listDrugWelfareGroupCoupon(DrugWelfareGroupCouponListRequest request);

    /**
     * 确定核销
     * @param request
     * @return
     */
    DrugWelfareGroupCouponVerificationDTO verificationDrugWelfareGroupCoupon(DrugWelfareGroupCouponVerificationRequest request);

}
