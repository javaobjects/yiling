package com.yiling.hmc.welfare.api;

import java.util.List;

import com.yiling.hmc.welfare.dto.DrugWelfareCouponDTO;
import com.yiling.hmc.welfare.dto.request.DrugWelfareCouponUpdateRequest;

/**
 * 药品福利计划券包API
 * @author fan.shen
 * @date 2022-09-26
 */
public interface DrugWelfareCouponApi {

    /**
     * 根据idList查询药品福利计划
     *
     * @param id 福利id
     * @return 药品福利计划DTO
     */
    List<DrugWelfareCouponDTO> getByWelfareId(Long id);

    /**
     * 根据药品福利计划id查询药品福利券包
     *
     * @param drugWelfareId
     * @return 药品福利计划DTO
     */
    List<DrugWelfareCouponDTO> queryByDrugWelfareId(Long drugWelfareId);

    /**
     * 编辑或更新药品福利计划券包
     * @param requestList
     * @return
     */
    Boolean updateDrugWelfareCoupon(List<DrugWelfareCouponUpdateRequest> requestList);
}
