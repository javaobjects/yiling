package com.yiling.hmc.welfare.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.welfare.dto.DrugWelfareDTO;
import com.yiling.hmc.welfare.dto.request.DrugWelfarePageRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareUpdateRequest;

/**
 * 药品福利计划API
 * @author fan.shen
 * @date 2022-09-26
 */
public interface DrugWelfareApi {

    /**
     * 根据idList查询药品福利计划
     *
     * @param idList 查询条件
     * @return 药品福利计划DTO
     */
    List<DrugWelfareDTO> getByIdList(List<Long> idList);

    /**
     * 根据id查询药品福利计划
     *
     * @param id
     * @return 药品福利计划DTO
     */
    DrugWelfareDTO queryById(Long id);

    /**
     * 查询药品福利计划分页列表
     * @param request
     * @return
     */
    Page<DrugWelfareDTO> pageList(DrugWelfarePageRequest request);

    /**
     * 编辑或更新药品福利计划
     * @param request
     * @return
     */
    boolean updateDrugWelfare(DrugWelfareUpdateRequest request);

}
