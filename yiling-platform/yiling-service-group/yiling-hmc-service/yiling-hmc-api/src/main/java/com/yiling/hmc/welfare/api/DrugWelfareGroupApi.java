package com.yiling.hmc.welfare.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.welfare.dto.DrugWelfareGroupDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareStatisticsPageDTO;
import com.yiling.hmc.welfare.dto.request.DrugWelfareStatisticsPageRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareGroupListRequest;
import com.yiling.hmc.welfare.dto.request.QueryGroupCouponRequest;
import com.yiling.hmc.welfare.dto.request.SaveGroupRequest;

/**
 * 药品福利计划API
 *
 * @author fan.shen
 * @date 2022-09-26
 */
public interface DrugWelfareGroupApi {


    /**
     * 根据用户id查询入组记录
     *
     * @param userId 用户id
     * @return 药品福利入组DTO
     */
    DrugWelfareGroupDTO getWelfareGroupByWelfareIdAndUserId(Long welfareId, Long userId);


    /**
     * 根据用户id查询入组记录
     *
     * @param userId 用户id
     * @return 药品福利入组DTO
     */
    List<DrugWelfareGroupDTO> getWelfareGroupByUserId(Long userId);

    /**
     * 入组
     * @param request
     * @return
     */
    Long joinGroup(SaveGroupRequest request);

    /**
     * 查询入组记录
     *
     * @param groupId
     * @return
     */
    DrugWelfareGroupDTO getById(Long groupId);

    /**
     * 药品福利统计
     *
     * @param request
     * @return
     */
    Page<DrugWelfareStatisticsPageDTO> statisticsPage(DrugWelfareStatisticsPageRequest request);

    /**
     * 获取有用户入组的店员id
     *
     * @return
     */
    List<Long> getSellerUserIds();

    /**
     * 根据request查询入组记录
     *
     * @param request
     * @return
     */
    List<DrugWelfareGroupDTO> listDrugWelfareGroup(DrugWelfareGroupListRequest request);

    /**
     * 查询核销状态
     * @param request
     * @return
     */
    Integer queryVerifyStatus(QueryGroupCouponRequest request);
}
