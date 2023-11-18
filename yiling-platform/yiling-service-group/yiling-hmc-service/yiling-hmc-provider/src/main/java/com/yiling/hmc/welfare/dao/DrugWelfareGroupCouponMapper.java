package com.yiling.hmc.welfare.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.hmc.welfare.dto.DrugWelfareGroupCouponStatisticsPageDTO;
import com.yiling.hmc.welfare.dto.request.DrugWelfareStatisticsPageRequest;
import com.yiling.hmc.welfare.entity.DrugWelfareGroupCouponDO;

/**
 * <p>
 * 入组福利券表 Dao 接口
 * </p>
 *
 * @author hongyang.zhang
 * @date 2022-09-26
 */
@Repository
public interface DrugWelfareGroupCouponMapper extends BaseMapper<DrugWelfareGroupCouponDO> {
    /**
     * @param page
     * @param request
     * @return
     */
    Page<DrugWelfareGroupCouponStatisticsPageDTO> exportStatistics(Page<DrugWelfareGroupCouponStatisticsPageDTO> page, @Param("request") DrugWelfareStatisticsPageRequest request);
}
