package com.yiling.hmc.welfare.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.welfare.dto.request.DrugWelfareCouponUpdateRequest;
import com.yiling.hmc.welfare.entity.DrugWelfareCouponDO;

/**
 * <p>
 * 药品福利券包表 服务类
 * </p>
 *
 * @author hongyang.zhang
 * @date 2022-09-26
 */
public interface DrugWelfareCouponService extends BaseService<DrugWelfareCouponDO> {

    /**
     * 获取福利券包
     * @param id
     * @return
     */
    List<DrugWelfareCouponDO> getByWelfareId(Long id);
    /**
     * 根据药品福利计划id查询药品福利券包
     *
     * @param drugWelfareId
     * @return 药品福利计划DTO
     */
    List<DrugWelfareCouponDO> queryByDrugWelfareId(Long drugWelfareId);

    /**
     * 编辑或更新药品福利计划
     * @param requestList
     * @return
     */
    Boolean updateDrugWelfareCoupon(List<DrugWelfareCouponUpdateRequest> requestList);

}
