package com.yiling.hmc.wechat.dao;

import com.yiling.hmc.wechat.dto.InsuranceFetchPlanGroupDTO;
import com.yiling.hmc.wechat.entity.InsuranceFetchPlanDetailDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * C端拿药计划明细表 Dao 接口
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-31
 */
@Repository
public interface InsuranceFetchPlanDetailMapper extends BaseMapper<InsuranceFetchPlanDetailDO> {

    /**
     * 获取每个拿药计划下商品数量
     * @param insuranceRecordIdList
     * @return
     */
    List<InsuranceFetchPlanGroupDTO> groupByInsuranceRecordId(@Param("insuranceRecordIdList") List<Long> insuranceRecordIdList);
}
