package com.yiling.hmc.welfare.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.welfare.dto.request.DeleteDrugWelfareEnterpriseRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareEnterprisePageRequest;
import com.yiling.hmc.welfare.dto.request.SaveDrugWelfareEnterpriseRequest;
import com.yiling.hmc.welfare.entity.DrugWelfareEnterpriseDO;

/**
 * <p>
 * 药品福利参与商家表 服务类
 * </p>
 *
 * @author hongyang.zhang
 * @date 2022-09-26
 */
public interface DrugWelfareEnterpriseService extends BaseService<DrugWelfareEnterpriseDO> {

    /**
     * 查询企业下参与的药品福利计划
     *
     * @return
     */
    List<DrugWelfareEnterpriseDO> getByEid(Long eId);

    /**
     * 查询参与过福利计划的企业
     *
     * @return
     */
    List<DrugWelfareEnterpriseDO> getEnterpriseList();

    /**
     * 分页查询参与福利计划商家列表
     *
     * @param request
     * @return
     */
    Page<DrugWelfareEnterpriseDO> pageList(DrugWelfareEnterprisePageRequest request);

    /**
     * 保存福利计划与商家关系
     *
     * @param request
     * @return
     */
    Boolean saveDrugWelfareEnterprise(SaveDrugWelfareEnterpriseRequest request);

    /**
     * 删除福利计划与商家关系
     *
     * @param request
     * @return
     */
    Boolean deleteDrugWelfareEnterprise(DeleteDrugWelfareEnterpriseRequest request);

    /**
     * 根据企业id查询参与的福利计划
     *
     * @param welfareId 查询条件
     * @return 福利计划
     */
    List<DrugWelfareEnterpriseDO> getByWelfareId(Long welfareId);

}
