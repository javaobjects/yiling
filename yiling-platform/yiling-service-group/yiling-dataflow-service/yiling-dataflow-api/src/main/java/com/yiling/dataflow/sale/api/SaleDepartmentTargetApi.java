package com.yiling.dataflow.sale.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sale.bo.SaleDepartmentTargetBO;
import com.yiling.dataflow.sale.dto.SaleDepartmentTargetDTO;
import com.yiling.dataflow.sale.dto.request.QuerySaleDepartmentTargetPageRequest;
import com.yiling.dataflow.sale.dto.request.UpdateConfigStatusRequest;
import com.yiling.dataflow.sale.enums.CrmSaleDepartmentTargetConfigStatusEnum;

import java.util.List;

/**
 * @author: gxl
 * @date: 2023/4/12
 */
public interface SaleDepartmentTargetApi {
    /**
     * 销售指标分解列表
     * @param request
     * @return
     */
    Page<SaleDepartmentTargetBO> querySaleDepartmentTargetPage(QuerySaleDepartmentTargetPageRequest request);

    /**
     * 销售指标部门配置数据根据
     * @param id
     * @return
     */
    List<SaleDepartmentTargetDTO> listBySaleTargetId(Long saleTargetId);

    /**
     * 销售部门数据
     * @param saleTargetId
     * @param departId
     * @return
     */
    SaleDepartmentTargetDTO listBySaleTargetIdAndDepartId(Long saleTargetId,Long departId);

    /**
     * 更新配置状态
     *
     * @param request
     */
    Boolean updateConfigStatus(UpdateConfigStatusRequest request);
}
