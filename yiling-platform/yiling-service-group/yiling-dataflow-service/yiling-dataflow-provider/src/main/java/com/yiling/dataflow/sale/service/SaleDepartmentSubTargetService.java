package com.yiling.dataflow.sale.service;

import java.util.List;

import com.yiling.dataflow.sale.dto.SaleDepartmentSubTargetDTO;
import com.yiling.dataflow.sale.dto.SaleDepartmentTargetDTO;
import com.yiling.dataflow.sale.dto.request.QuerySaleDepartmentSubTargetRequest;
import com.yiling.dataflow.sale.dto.request.SaveBathSaleDepartmentSubTargetRequest;
import com.yiling.dataflow.sale.entity.SaleDepartmentSubTargetDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 部门销售指标子项配置 服务类
 * </p>
 *
 * @author gxl
 * @date 2023-04-12
 */
public interface SaleDepartmentSubTargetService extends BaseService<SaleDepartmentSubTargetDO> {

    /**
     * 根据指标id查询子参数
     *
     * @param targetId
     * @param targetId
     * @return
     */
    List<SaleDepartmentSubTargetDTO> queryListByTargetIdAndDeptId(Long targetId,Long deptId);

    List<SaleDepartmentSubTargetDTO> listByParam(QuerySaleDepartmentSubTargetRequest request);

    int removeBySaleTargetAndDepartId(SaveBathSaleDepartmentSubTargetRequest request);
}
