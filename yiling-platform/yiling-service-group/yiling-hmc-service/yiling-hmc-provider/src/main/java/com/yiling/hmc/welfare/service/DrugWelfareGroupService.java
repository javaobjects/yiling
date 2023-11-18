package com.yiling.hmc.welfare.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.welfare.dto.request.DrugWelfareGroupListRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareStatisticsPageRequest;
import com.yiling.hmc.welfare.dto.request.QueryGroupCouponRequest;
import com.yiling.hmc.welfare.dto.request.SaveGroupRequest;
import com.yiling.hmc.welfare.entity.DrugWelfareGroupDO;

/**
 * <p>
 * 药品福利入组表 服务类
 * </p>
 *
 * @author hongyang.zhang
 * @date 2022-09-26
 */
public interface DrugWelfareGroupService extends BaseService<DrugWelfareGroupDO> {

    /**
     * 根据用户id查询有效的入组计划
     *
     *
     * @param welfareId
     * @param userId 用户id
     * @return 药品福利计划DTO
     */
    DrugWelfareGroupDO getWelfareGroupByWelfareIdAndUserId(Long welfareId, Long userId);

    /**
     * 根据用户id查询有效的入组计划
     *
     * @param userId 用户id
     * @return 药品福利计划DTO
     */
    List<DrugWelfareGroupDO> getValidWelfareByUserId(Long userId);

    /**
     * 根据企业id和福利计划id查询有效入组计划
     *
     * @param eid
     * @param drugWelfareId
     * @return
     */
    DrugWelfareGroupDO getValidWelfareByEidAndDrugWelfareId(Long eid, Long drugWelfareId);

    /**
     * 入组
     *
     * @param request
     * @return
     */
    Long joinGroup(SaveGroupRequest request);

    /**
     * 药品福利统计
     *
     * @param request
     * @return
     */
    Page<DrugWelfareGroupDO> statisticsPage(DrugWelfareStatisticsPageRequest request);

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
    List<DrugWelfareGroupDO> listDrugWelfareGroup(DrugWelfareGroupListRequest request);

    /**
     * 查询核销状态
     * @param request
     * @return
     */
    Integer queryVerifyStatus(QueryGroupCouponRequest request);
}
