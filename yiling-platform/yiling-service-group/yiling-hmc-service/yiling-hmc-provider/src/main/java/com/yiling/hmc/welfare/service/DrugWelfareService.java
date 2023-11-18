package com.yiling.hmc.welfare.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.welfare.dto.request.DrugWelfarePageRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareUpdateRequest;
import com.yiling.hmc.welfare.entity.DrugWelfareDO;

/**
 * <p>
 * 药品福利表 服务类
 * </p>
 *
 * @author hongyang.zhang
 * @date 2022-09-26
 */
public interface DrugWelfareService extends BaseService<DrugWelfareDO> {

    /**
     * 批量获取药品福利计划
     * @param idList
     * @return
     */
    List<DrugWelfareDO> getByIdList(List<Long> idList);

    /**
     * 药品福利计划分页查询
     *
     * @param request 查询条件
     * @return
     */
    Page<DrugWelfareDO> pageList(DrugWelfarePageRequest request);

    /**
     * 编辑或更新药品福利计划
     * @param request
     * @return
     */
    boolean updateDrugWelfare(DrugWelfareUpdateRequest request);

}
