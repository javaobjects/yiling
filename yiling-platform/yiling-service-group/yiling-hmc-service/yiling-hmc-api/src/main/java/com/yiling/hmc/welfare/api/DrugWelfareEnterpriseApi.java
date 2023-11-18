package com.yiling.hmc.welfare.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.welfare.dto.DrugWelfareEnterpriseDTO;
import com.yiling.hmc.welfare.dto.EnterpriseListDTO;
import com.yiling.hmc.welfare.dto.request.DeleteDrugWelfareEnterpriseRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareEnterprisePageRequest;
import com.yiling.hmc.welfare.dto.request.SaveDrugWelfareEnterpriseRequest;

/**
 * @author fan.shen
 * @date 2022-09-26
 */
public interface DrugWelfareEnterpriseApi {

    /**
     * 根据企业id查询参与的福利计划
     *
     * @param eId 查询条件
     * @return 福利计划
     */
    List<DrugWelfareEnterpriseDTO> getByEid(Long eId);

    /**
     * 根据企业id查询参与的福利计划
     *
     * @param welfareId 查询条件
     * @return 福利计划
     */
    List<DrugWelfareEnterpriseDTO> getByWelfareId(Long welfareId);

    /**
     * 查询参与过福利计划的企业
     *
     * @return
     */
    List<EnterpriseListDTO> getEnterpriseList();

    /**
     * 分页查询参与福利计划商家列表
     *
     * @param request
     * @return
     */
    Page<DrugWelfareEnterpriseDTO> pageList(DrugWelfareEnterprisePageRequest request);

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
}
